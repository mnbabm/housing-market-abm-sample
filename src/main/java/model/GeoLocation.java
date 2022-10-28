package model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import util.OwnFunctions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class GeoLocation implements HasID {

    private static int nextId;
    final int id;

    //state variables
    List<Neighbourhood> neighbourhoods = new ArrayList<Neighbourhood>();
    public double[] histPriceIndex;
    public double[] histPriceIndexToBeginning;
    public double basePriceIndexToAverageWageIncome;
    public int[] histNNewlyBuiltFlats; //underConstruction or ready but not sold, excluding flats the construction of which has just started and so are not for sale
    public int[] histNNewlyBuiltFlatsSold; //just underConstruction since what has been sold will not count as newlyBuilt from then on
    public double[] histRenovationUnitCost;
    public double[] histConstructionUnitCost;
    public double[] histRenovationQuantity;

    //derived variables
    double priceIndex;
    double priceIndexToBeginning;
    double cyclicalAdjuster;
    double housePriceToIncome;
    double averageWageIncome;
    double priceIndexToAverageWageIncome;
    double constructionMarkup;
    double areaUnderConstruction;
    double plannedAdditionalAreaUnderConstruction;
    double nFlatsToBuildAdjuster;
    double highQualityUnitCost;
    double renovationUnitCost;
    double constructionUnitCost;
    double predictedRenovationUnitCost;
    int nFullTimeWorkers;
    double sumFullTimeWage;
    double averageWage;
    double renovationQuantity;


    public GeoLocation() {
        id = GeoLocation.nextId;
        GeoLocation.nextId++;
        makeHistoryVariables();
    }

    public GeoLocation(int id) {
        this.id = id;
        if (GeoLocation.nextId < id + 1) GeoLocation.nextId = id + 1;
        makeHistoryVariables();
    }

    void makeHistoryVariables() {
        histPriceIndex = new double[Model.nHistoryPeriods + Model.maxNPeriods];
        histPriceIndexToBeginning = new double[Model.nHistoryPeriods + Model.maxNPeriods];
        histNNewlyBuiltFlats = new int[Model.nHistoryPeriods + Model.maxNPeriods];
        histNNewlyBuiltFlatsSold = new int[Model.nHistoryPeriods + Model.maxNPeriods];
        histRenovationUnitCost = new double[Model.nHistoryPeriods + Model.maxNPeriods];
        histConstructionUnitCost = new double[Model.nHistoryPeriods + Model.maxNPeriods];
        histRenovationQuantity = new double[Model.nHistoryPeriods + Model.maxNPeriods];
    }


    public void calculateHousePriceToIncome() {
        double sumHousePrice = 0;
        double sumIncome = 0;
        for (Household household : Model.households.values()) {
            if (household.home != null) {
                sumHousePrice += household.home.getEstimatedMarketPrice();
                sumIncome += household.wageIncome;
            }
        }


        housePriceToIncome = sumHousePrice / sumIncome;

    }

    void calculateAndSetPriceIndex() {

        int baseMaxPeriod = Model.period - 3;

        double quarterlyPriceIndex = 0;
        int nFlats = 0;
        for (Neighbourhood neighbourhood : neighbourhoods) {
            nFlats += Model.useTransactionWeightsForPriceIndex ? neighbourhood.nTransactions : neighbourhood.nFlats;
            quarterlyPriceIndex += neighbourhood.quarterlyPriceIndex * (Model.useTransactionWeightsForPriceIndex ? neighbourhood.nTransactions : neighbourhood.nFlats);
        }
        quarterlyPriceIndex /= nFlats;

        priceIndex = histPriceIndex[Model.nHistoryPeriods + baseMaxPeriod] * quarterlyPriceIndex;
        if (Model.period == 3) {
            priceIndex = quarterlyPriceIndex;
            for (int i = 0; i < Model.period; i++) {
                histPriceIndex[Model.nHistoryPeriods + i] = priceIndex;
            }
        }
    }

    void calculateAndSetPriceIndexToBeginning() {

        priceIndexToBeginning = 0;
        int nFlats = 0;
        for (Neighbourhood neighbourhood : neighbourhoods) {
            nFlats += Model.useTransactionWeightsForPriceIndex ? neighbourhood.nTransactions : neighbourhood.nFlats;
            priceIndexToBeginning += neighbourhood.priceIndexToBeginning * (Model.useTransactionWeightsForPriceIndex ? neighbourhood.nTransactions : neighbourhood.nFlats);
        }
        priceIndexToBeginning /= nFlats;

    }

    void calculateAndSetAverageWageIncome() {
        double sumWage = 0;
        int nWorkers = 0;
        for (Household household : Model.households.values()) {
            if ((household.home != null && household.home.bucket.neighbourhood.geoLocation == this)
                    || (household.rentHome != null && household.rentHome.bucket.neighbourhood.geoLocation == this)) {
                sumWage += household.wageIncome;
                nWorkers += household.members.size();
            }
        }
        averageWageIncome = sumWage / nWorkers;
    }

    void calculateAndSetPriceIndexToAverageWageIncome() {
        priceIndexToAverageWageIncome = histPriceIndex[Model.nHistoryPeriods + Model.period - 1] / averageWageIncome;
        if (basePriceIndexToAverageWageIncome == 0) {
            basePriceIndexToAverageWageIncome = priceIndexToAverageWageIncome;
        }
    }

    void calculateAndSetConstructionMarkup() {
        double soldRatio = histNNewlyBuiltFlatsSold[Model.nHistoryPeriods + Model.period - 1] / (double) histNNewlyBuiltFlats[Model.nHistoryPeriods + Model.period - 1];
        if (Double.isNaN(soldRatio)) soldRatio = 0;
        constructionMarkup = Model.constructionMarkupRatio1Level + (soldRatio - Model.constructionMarkupRatio1) * (Model.constructionMarkupRatio2Level - Model.constructionMarkupRatio1Level) / (Model.constructionMarkupRatio2 - Model.constructionMarkupRatio1);

    }

    public void calculateAndSetCyclicalAdjuster() {
        cyclicalAdjuster = 1;
        if (Model.period > Model.cyclicalAdjusterBasePeriod + 1) {
            cyclicalAdjuster = 1 + (histPriceIndexToBeginning[Model.nHistoryPeriods + Model.period - 1] / Model.histNominalGDPLevel[Model.nHistoryPeriods + Model.period - 1] / (histPriceIndexToBeginning[Model.nHistoryPeriods + Model.cyclicalAdjusterBasePeriod] / Model.histNominalGDPLevel[Model.nHistoryPeriods + Model.cyclicalAdjusterBasePeriod]) - 1) * Model.cyclicalAdjusterPriceIndexCoeff;
        }
    }


    public void calculateAndSetRenovationUnitCost() {
        renovationUnitCost = calculateRenovationUnitCostBase();
    }

    public double calculateRenovationUnitCostBase() {
        double renovationUnitCostBase = Model.priceLevel * Model.realGDPLevel * Model.renovationUnitCostBaseRegional;
        if (this == Model.capital) {
            renovationUnitCostBase *= (1 + Model.constructionAndRenovationUnitCostBaseCapitalMarkup);
        } else if (this == Model.agglomeration) {
            renovationUnitCostBase *= (1 + Model.constructionAndRenovationUnitCostBaseAgglomerationMarkup);
        }

        return renovationUnitCostBase + Model.renovationUnitCostAverageWageCoeff * averageWage;

    }

    public void calculateAndSetConstructionUnitCost() {
        constructionUnitCost = calculateConstructionUnitCostBase();
    }

    public double calculateConstructionUnitCostBase() {
        double constructionUnitCostBase = Model.priceLevel * Model.realGDPLevel * Model.constructionUnitCostBaseRegional;
        if (this == Model.capital) {
            constructionUnitCostBase *= (1 + Model.constructionAndRenovationUnitCostBaseCapitalMarkup);
        } else if (this == Model.agglomeration) {
            constructionUnitCostBase *= (1 + Model.constructionAndRenovationUnitCostBaseAgglomerationMarkup);
        }

        return constructionUnitCostBase + Model.constructionUnitCostAverageWageCoeff * averageWage;
    }

}
