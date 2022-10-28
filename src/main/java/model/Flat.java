package model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@Getter
@Setter
public class Flat implements HasID {

    public static int nextId;
    public final int id;

    //state variables
    public double size;
    public double state;
    public boolean isNewlyBuilt = false;
    public Bucket bucket;

    LoanContract loanContract;
    public Household ownerHousehold;
    Constructor ownerConstructor;
    Investor ownerInvestor;

    public Household renter;
    public boolean rentedByExternal = false;

    public boolean isForSale = false;
    public boolean isForcedSale = false;
    public boolean isForRent = false;
    boolean willBeForRent = false;
    public double lastMarketPrice;
    public double forSalePrice;
    public int nForSalePeriods;
    public int nPeriodsLeftForConstruction;
    int nPeriodsLeftForRent;

    public double rent;
    public boolean unSoldWithZeroBids = false;

    boolean isFictiveFlatForSale = false;
    double lastNaturallyUpdatedMarketPriceOfFictiveFlat = 0;
    int periodOfLastNaturalMarketPriceUpdateOfFictiveFlat = 0;


    //derived variables
    boolean boughtNow = false;
    double investmentStateIncrease;
    public double marketPrice;
    public double estimatedMarketPrice;
    ArrayList<FlatSaleRecord> similarFlatSaleRecords = new ArrayList<>();
    ArrayList<ForSaleRecord> similarForSaleRecords = new ArrayList<>();
    boolean marketPriceCalculated;
    boolean estimatedMarketPriceCalculated;
    double reservationPrice;
    double soldRatioOfSimilarFlats;

    ArrayList<Bid> bids = new ArrayList<>();


    public Flat() {
        id = Flat.nextId;
        Flat.nextId++;
    }

    public Flat(int id) {
        this.id = id;
        if (Flat.nextId < id + 1) Flat.nextId = id + 1;
    }

    public Flat(Flat flat) {
        this.id = -1;
        this.size = flat.size;
        this.state = flat.state;
        this.bucket = flat.bucket;
        this.forSalePrice = flat.forSalePrice;
        this.isNewlyBuilt = flat.isNewlyBuilt;
        this.nPeriodsLeftForConstruction = flat.nPeriodsLeftForConstruction;
        this.isForcedSale = flat.isForcedSale;
    }

    public void deleteFlat() {

        Model.flats.remove(id);

        if (bucket != null) bucket.nFlats--;
        Model.flatsForParallelComputing.get(id % Model.nThreads).remove(id);

    }

    public void setBucket(Bucket bucket) {
        if (this.bucket != null) this.bucket.nFlats--;
        bucket.nFlats++;
        this.bucket = bucket;
    }

    public void setBucket(Neighbourhood neighbourhood) {

        if (bucket != null) {
            bucket.nFlats--;
        }

        this.bucket = findBucket(neighbourhood);
        bucket.nFlats++;

    }

    public Bucket findBucket(Neighbourhood neighbourhood) {
        for (Bucket bucket : neighbourhood.getBuckets()) {

            if (size > bucket.sizeMin && size <= bucket.sizeMax && state > bucket.stateMin && state <= bucket.stateMax) {
                return bucket;
            }
        }
        return null;
    }

    public void setState(double state) {
        this.state = state;
        if (bucket != null) {
            setBucket(bucket.neighbourhood);
        }
    }

    public void forSaleCalculations() {

        if (isForRent && nPeriodsLeftForRent == 0) {
            if (ownerInvestor != null && Model.threadNextDouble(this) < bucket.rentSaleProbabilityCentralInvestor) {
                setForSale(true);
            }
            if (ownerHousehold != null && Model.threadNextDouble(this) < bucket.rentSaleProbabilityHouseholds) {
                setForSale(true);
            }
        }

        if (ownerHousehold != null && ownerHousehold.isMoving && this == ownerHousehold.home) {
            setForSale(true);
            if (nForSalePeriods >= Model.nMonthsToWithdrawHomeFromMarket && isForcedSale == false) {
                setForSale(false);
                setNForSalePeriods(0);
            }
        }

        if (ownerConstructor != null) {
            isForSale = true;
        }

        if (isForSale || isForcedSale) {

            if (nPeriodsLeftForConstruction == 0) nForSalePeriods++;

            calculateAndSetForSalePrice();
            if (ownerConstructor != null) {
                reservationPrice = forSalePrice;
            } else {
                double sellerReservationPriceDecrease = Model.sellerReservationPriceDecrease;
                if (bucket.histNSold[Model.nHistoryPeriods + Model.period - 1] > 0) {
                    double soldRatio = bucket.histNSold[Model.nHistoryPeriods + Model.period - 1] / (double) bucket.histNForSale[Model.nHistoryPeriods + Model.period - 1];
                    sellerReservationPriceDecrease *= 1 - soldRatio;
                }
                reservationPrice = forSalePrice * (1 + Model.reservationMarkup) * Math.pow(1 - sellerReservationPriceDecrease, nForSalePeriods - 1);
                reservationPrice = Math.max(reservationPrice, Model.minForSalePrice);

            }

        }
    }

    public void calculateAndSetForSalePrice() {

        if (ownerConstructor != null) {
            double constructionForSalePrice = (size * (bucket.neighbourhood.landPrice + getGeoLocation().constructionUnitCost)) * (1 + Math.max(0.0, getGeoLocation().constructionMarkup));
            if (constructionForSalePrice > getMarketPrice() * Model.maxConstructionPriceToMarketPriceWithProperConstructionCostCoverage && getMarketPrice() / size > getGeoLocation().constructionUnitCost * Model.constructionCostCoverageNeed)
                constructionForSalePrice = getMarketPrice() * Model.maxConstructionPriceToMarketPriceWithProperConstructionCostCoverage;

            double adjustedConstructionForSalePrice = constructionForSalePrice;
            if (adjustedConstructionForSalePrice > getMarketPrice() && nForSalePeriods >= Model.nForSalePeriodsToStartAdjustingConstructionForSalePrice) {
                adjustedConstructionForSalePrice -= (adjustedConstructionForSalePrice - getMarketPrice()) * (1 - Math.pow(Model.parameterForConstructionForSalePriceAdjustment, nForSalePeriods - Model.nForSalePeriodsToStartAdjustingConstructionForSalePrice));
            }

            forSalePrice = Math.max(adjustedConstructionForSalePrice, getMarketPrice());

            forSalePrice = Math.max(forSalePrice, Model.minForSalePrice);
            return;
        }

        forSalePrice = getMarketPrice();

        if (isForcedSale) {
            forSalePrice *= Model.forcedSaleDiscount;
            if (Model.realGDPLevelShockPath[Model.period] <= Model.realGDPLevelShockTriggerValueForAdditionalForcedSaleDiscount)
                forSalePrice *= (1 - Model.additionalForcedSaleDiscount);
        }

        if (nForSalePeriods > 1) {
            forSalePrice *= Math.pow(1 - Model.monthlyForSalePriceDecrease, nForSalePeriods - 1);
        }

        forSalePrice = Math.max(forSalePrice, Model.minForSalePrice);

    }


    public void calculateAndSetRent() {
        rent = calculateMarketRent();
    }

    public double calculateMarketRent() {
        return getMarketPrice() * Model.rentToPrice * (1 + bucket.getNeighbourhood().getRentMarkup());
    }

    public double calculateMarketPrice() {

        if (Model.phase == Model.Phase.SETUP || Model.marketPriceOnRegression) {
            return getEstimatedMarketPrice();
        }

        refreshSimilarFlatSaleRecordsAndForSaleRecords();

        boolean marketPriceBasedOnTwoClosestNeighbours = true;
        if (marketPriceBasedOnTwoClosestNeighbours) {
            double calculatedMarketPriceBasedOnTwoClosestNeighbours = calculateMarketPriceBasedOnTwoClosestNeighbours();

            if (isFictiveFlatForSale) {
                if (calculatedMarketPriceBasedOnTwoClosestNeighbours != lastNaturallyUpdatedMarketPriceOfFictiveFlat) {
                    lastNaturallyUpdatedMarketPriceOfFictiveFlat = calculatedMarketPriceBasedOnTwoClosestNeighbours;
                    periodOfLastNaturalMarketPriceUpdateOfFictiveFlat = Model.period;
                    return lastNaturallyUpdatedMarketPriceOfFictiveFlat;
                } else {
                    return lastNaturallyUpdatedMarketPriceOfFictiveFlat * Model.priceLevel * Model.realGDPLevel / (Model.priceLevelPath[periodOfLastNaturalMarketPriceUpdateOfFictiveFlat] * Model.realGDPLevelPath[periodOfLastNaturalMarketPriceUpdateOfFictiveFlat]);
                }
            }


            if (calculatedMarketPriceBasedOnTwoClosestNeighbours > Model.minForSalePriceForRegression) {
                lastMarketPrice = calculatedMarketPriceBasedOnTwoClosestNeighbours;
            }

            if (Double.isNaN(lastMarketPrice)) {
                calculateMarketPriceBasedOnTwoClosestNeighbours();
            }

            if (lastMarketPrice == 0) {
                if (isNewlyBuilt) {
                    for (Flat flat : Model.fictiveNewlyBuiltFlatsForSale) {
                        if (flat.bucket == bucket) lastMarketPrice = flat.lastMarketPrice / flat.size * size;
                    }
                }
            }

            return lastMarketPrice;
        }


        if (similarFlatSaleRecords.size() == 0) return getEstimatedMarketPrice();

        double sumForSalePrice = 0;
        double sumSize = 0;
        double sumStateDeviation = 0;

        for (FlatSaleRecord flatSaleRecord : similarFlatSaleRecords) {

            sumForSalePrice += flatSaleRecord.price;
            sumSize += flatSaleRecord.size;
            sumStateDeviation += Math.abs(flatSaleRecord.state - state);

        }
        double marketPrice = sumForSalePrice / sumSize * size;

        if (Double.isNaN(marketPrice)) {
            return getEstimatedMarketPrice();
        } else {
            return marketPrice;
        }

    }

    public double calculateMarketPriceBasedOnTwoClosestNeighbours() {
        FlatSaleRecord closestFlatSaleRecord = null;
        FlatSaleRecord secondClosestFlatSaleRecord = null;
        double closestDistance = 1000000;
        double secondClosestDistance = 1000000;
        for (FlatSaleRecord flatSaleRecord : bucket.getNeighbourhood().flatSaleRecordsForPrice) {
            double sizeDistance = Math.abs(flatSaleRecord.size - size) / size;
            double stateDistance = Math.abs(flatSaleRecord.state - state) / state;
            if (sizeDistance > Model.sizeDistanceRatioThresholdForClosestNeighbourCalculation || stateDistance > Model.stateDistanceRatioThresholdForClosestNeighbourCalculation)
                continue;
            double distance = Model.sizeWeightInClosestNeighbourCalculation * sizeDistance + Model.stateWeightInClosestNeighbourCalculation * stateDistance;

            if (distance < closestDistance) {
                secondClosestFlatSaleRecord = closestFlatSaleRecord;
                secondClosestDistance = closestDistance;
                closestFlatSaleRecord = flatSaleRecord;
                closestDistance = distance;

            } else if (distance < secondClosestDistance) {
                secondClosestFlatSaleRecord = flatSaleRecord;
                secondClosestDistance = distance;
            }
        }


        if (secondClosestFlatSaleRecord == null) {
            return lastMarketPrice;
        } else {
            double unitCostClosest = closestFlatSaleRecord.price / closestFlatSaleRecord.size;
            double unitCostSecondClosest = secondClosestFlatSaleRecord.price / secondClosestFlatSaleRecord.size;

            double moreExpensiveUnitCost = unitCostClosest;
            double moreExpensiveState = closestFlatSaleRecord.state;
            double lessExpensiveUnitCost = unitCostSecondClosest;
            double lessExpensiveState = secondClosestFlatSaleRecord.state;
            if (unitCostClosest < unitCostSecondClosest) {
                moreExpensiveUnitCost = unitCostSecondClosest;
                moreExpensiveState = secondClosestFlatSaleRecord.state;
                lessExpensiveUnitCost = unitCostClosest;
                lessExpensiveState = closestFlatSaleRecord.state;
            }

            if (moreExpensiveState <= lessExpensiveState) {
                return (moreExpensiveUnitCost + lessExpensiveUnitCost) / 2 * size;
            } else {
                double averageState = (moreExpensiveState + lessExpensiveState) / 2;
                double stateUnitCost = (moreExpensiveUnitCost - lessExpensiveUnitCost) / (moreExpensiveState - lessExpensiveState);
                double unitCost = (moreExpensiveUnitCost + lessExpensiveUnitCost) / 2 + (state - averageState) * stateUnitCost;
                unitCost = Math.max(lessExpensiveUnitCost, Math.min(unitCost, moreExpensiveUnitCost));
                return unitCost * size;
            }

        }
    }

    public void calculateAndSetMarketPrice() {
        synchronized (this) {
            marketPrice = calculateMarketPrice();
            marketPriceCalculated = true;
        }
    }

    public double getMarketPrice() {
        if (marketPriceCalculated == false) {
            calculateAndSetMarketPrice();
        }
        return marketPrice;
    }

    public void calculateAndSetEstimatedMarketPrice() {
        estimatedMarketPrice = calculateEstimatedMarketPrice();
        estimatedMarketPriceCalculated = true;
    }

    public double getEstimatedMarketPrice() {
        if (estimatedMarketPriceCalculated == false) {
            calculateAndSetEstimatedMarketPrice();
        }
        return estimatedMarketPrice;
    }


    public double calculateEstimatedMarketPrice() {
        if (Model.phase == Model.Phase.SETUP || Model.marketPriceOnRegression)
            return bucket.neighbourhood.priceRegressionFunctionLinear.flatPrice(this);
        if (getMarketPrice() > 0) return getMarketPrice();
        return bucket.neighbourhood.priceRegressionFunctionLinear.flatPrice(this);
    }

    public static Comparator<Flat> comparatorForSalePrice = new Comparator<Flat>() {

        public int compare(Flat o1, Flat o2) {
            return Double.compare(o1.getForSalePrice(), o2.getForSalePrice());
        }
    };

    public static Comparator<Flat> comparatorForSaleUnitPrice = new Comparator<Flat>() {

        public int compare(Flat o1, Flat o2) {
            return Double.compare(o1.getForSalePrice() / o1.size, o2.getForSalePrice() / o2.size);
        }
    };

    public static Comparator<Flat> comparatorRent = new Comparator<Flat>() {

        public int compare(Flat o1, Flat o2) {
            return Double.compare(o1.getRent(), o2.getRent());
        }
    };

    public void nullMiscDerivedVariables() {
        boughtNow = false;
        investmentStateIncrease = 0;
        marketPriceCalculated = false;
        estimatedMarketPriceCalculated = false;
        reservationPrice = 0;
        bids.clear();
        soldRatioOfSimilarFlats = 0;

    }

    public void stateDepreciation() {


        if (ownerConstructor != null || nPeriodsLeftForConstruction > 0) return;
        if (bucket.isHighQuality == false) {
            if (bucket.stateIndex == 1 && state * (1 - Model.stateDepreciation) < bucket.stateMin) return;
            state *= (1 - Model.stateDepreciation);
        } else {
            state -= (bucket.stateMax - bucket.stateMin) / Model.nPeriodsInHighQualityBucket;
        }

        if (state < bucket.stateMin) {
            setBucket(bucket.neighbourhood);
        }

    }

    public void decreasePeriodsLeftForRent() {
        if (nPeriodsLeftForRent > 0) {


            nPeriodsLeftForRent--;

            if (nPeriodsLeftForRent == 0) {

                if (renter != null) {
                    renter.rentHome = null;
                    renter = null;
                } else if (rentedByExternal == true) rentedByExternal = false;

            }

        }

    }

    public boolean isAvailableForRent() {
        return (isForRent && nPeriodsLeftForRent == 0 && isForSale == false && isForcedSale == false);
    }

    public Neighbourhood getNeighbourhood() {
        return bucket.getNeighbourhood();
    }

    public double getQuality() {
        return bucket.getNeighbourhood().getQuality();
    }

    public GeoLocation getGeoLocation() {
        return bucket.getNeighbourhood().getGeoLocation();
    }

    public void giveHomeUnderConstructionBackToConstructor() {
        ownerHousehold = null;
        ownerConstructor = Model.constructors.get(0);
        getOwnerConstructor().flatsUnderConstruction.add(this);
        if (loanContract != null) {
            loanContract.endLoanContract();
        }
    }


    public void refreshSimilarFlatSaleRecordsAndForSaleRecords() {

        similarFlatSaleRecords.clear();
        similarForSaleRecords.clear();

        for (ForSaleRecord forSaleRecord : bucket.getNeighbourhood().forSaleRecordsForPrice) {
            if (forSaleRecord.isSimilarRecordForPrice(this)) similarForSaleRecords.add(forSaleRecord);
        }

        for (FlatSaleRecord flatSaleRecord : bucket.getNeighbourhood().flatSaleRecordsForPrice) {
            if (flatSaleRecord.isSimilarRecordForPrice(this)) {
                similarFlatSaleRecords.add(flatSaleRecord);
            }
        }


        if (similarFlatSaleRecords.size() > 0) {
            soldRatioOfSimilarFlats = similarFlatSaleRecords.size() / (double) similarForSaleRecords.size();
        } else {
            soldRatioOfSimilarFlats = -0.00001;
        }

    }

    public void flatAccounting() {
        bids.clear();
    }

    public double getPredictedRenovationCost(double stateIncrease) {
        return size * stateIncrease * getGeoLocation().predictedRenovationUnitCost * (1 + Model.renovationCostBuffer);
    }

    public void accountBidOnFlat(Bid bid) {
        synchronized (this) {
            bids.add(bid);
        }
    }

    public void sortBidsDecreasing() {
        Collections.sort(bids, Bid.comparatorReservationPriceDecrease);
    }

}
