package model;

import util.OwnFunctions;

public class ModelRunnableFunctions {

    public static void selectWithFictiveSupplyIncrementDemand(Household household) {

        if (household.isMoving && household.rentHome == null && household.tooYoungToBuyOrRent() == false) {


            Flat flatToBuy = household.selectWithFictiveSupply();
            Flat fictiveFlatToBuy = null;


            if (flatToBuy != null) {
                fictiveFlatToBuy = new Flat(flatToBuy);
                household.fictiveFlatToBuy = fictiveFlatToBuy;
                if (household.mayRenovateWhenBuying)
                    household.replaceFlatStateAndForSalePriceAccordingToOptimalRenovation(fictiveFlatToBuy);
                household.fictiveSurplus = household.utilityFunctionCES.calculateAbsoluteReservationPriceForFlat(fictiveFlatToBuy) - fictiveFlatToBuy.forSalePrice;
            }

            if (fictiveFlatToBuy != null && household.canBuyFlat(fictiveFlatToBuy)) {

                if (fictiveFlatToBuy.isNewlyBuilt && household.canBeAskedForFictiveDemandForNewlyBuiltFlats) {
                    flatToBuy.bucket.incrementNewlyBuiltDemand();
                }
                household.setCanBeAskedForFictiveDemandForNewlyBuiltFlats(false);

            } else {
                household.setFlatTooExpensiveToBuy(true);
            }

        }

    }

    public static void selectFictiveRentAndIncrementDemand(Household household) {
        if (household.mayTryToRent() || household.rentHome != null) {
            Flat flatToRent = household.selectFictiveRent();
            if (flatToRent != null) flatToRent.bucket.incrementNFictiveRentDemand();
        }
    }

    public static void placeBidsOnFlats(Household household) {
        household.bids.clear();
        if (household.maySelectHome()) {
            household.placeBidsOnFlats();
        }
    }

    public static void flatAccounting(Flat flat) {
        flat.flatAccounting();
    }

    public static void flatBidSorting(Flat flat) {
        flat.sortBidsDecreasing();
    }


}
