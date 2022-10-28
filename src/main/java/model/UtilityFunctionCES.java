package model;

import lombok.Getter;
import lombok.Setter;
import util.OwnFunctions;

@Setter
@Getter
public class UtilityFunctionCES implements HasID, UtilityFunction {

    private static int nextId;
    final int id;

    //state variables
    public Household household;

    public double absExponentSize;
    public double absExponentState;
    public double absCoeffSize;
    public double absCoeffState;
    public double absSigmoid1;
    public double absSigmoid2;


    public UtilityFunctionCES() {
        id = UtilityFunctionCES.nextId;
        UtilityFunctionCES.nextId++;

        Model.utilityFunctionsCES.put(id, this);
    }

    public void cloneParametersTo(UtilityFunctionCES utilityFunctionCES) {

        utilityFunctionCES.absCoeffSize = absCoeffSize;
        utilityFunctionCES.absCoeffState = absCoeffState;
        utilityFunctionCES.absExponentSize = absExponentSize;
        utilityFunctionCES.absExponentState = absExponentState;
        utilityFunctionCES.absSigmoid1 = absSigmoid1;
        utilityFunctionCES.absSigmoid2 = absSigmoid2;

    }

    public double calculateUtility(Flat flat) {

        double price = flat.forSalePrice;
        if (price == 0) price = flat.getMarketPrice();
        if (Model.phase == Model.Phase.FICTIVEDEMANDFORRENT || Model.phase == Model.Phase.RENTALMARKET) {
            price = flat.rent / Model.rentToPrice;
        }

        return calculateAbsoluteReservationPriceForFlat(flat) - price;

    }


    public double calculateAbsoluteReservationPriceForFlat(Flat flat) {

        if (household.canChangeGeoLocation == false && flat.getGeoLocation() != household.preferredGeoLocation)
            return 0;


        double absoluteReservationPrice = (absCoeffSize * Math.pow(flat.size, absExponentSize) * (1 + absCoeffState * Math.pow(flat.state, absExponentState)) + absSigmoid1 / Math.pow(1 + Math.exp(-flat.bucket.neighbourhood.quality), 1 / absSigmoid2)) * household.lifeTimeIncome * Model.priceLevel * flat.getGeoLocation().cyclicalAdjuster;

        if (flat.isNewlyBuilt) {
            absoluteReservationPrice += (Model.newlyBuiltUtilityAdjusterCoeff1 + Model.newlyBuiltUtilityAdjusterCoeff2 * flat.getQuality()) * absCoeffSize * Math.pow(flat.size, absExponentSize) * household.lifeTimeIncome;
            if (absoluteReservationPrice < 0) absoluteReservationPrice = 0;
        }

        return absoluteReservationPrice;
    }

    public void deleteUtilityFunctionCES() {
        Model.utilityFunctionsCES.remove(id);
    }

}
