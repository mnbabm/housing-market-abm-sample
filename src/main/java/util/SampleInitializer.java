package util;

import model.*;
import parallel.ParallelComputer;

import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class SampleInitializer {

    static Properties prop;
    public static String configFileName;

    static int nIndividuals;
    static double marriageRatio;
    static double[] typeShare;
    static int maxAgeInYearsToBeAChild;

    static int minAgeDifferenceBetweenChildAndParent;
    static int maxAgeDifferenceBetweenChildAndParent;

    static double[] firstWage_mean;
    static double[] firstWage_std;
    static double[] firstWage_min;
    static double[] firstWage_max;

    static double utilityFunction_absCoeffSize_mean;
    static double utilityFunction_absCoeffSize_std;
    static double utilityFunction_absCoeffSize_min;
    static double utilityFunction_absCoeffSize_max;
    static double utilityFunction_absCoeffState_mean;
    static double utilityFunction_absCoeffState_std;
    static double utilityFunction_absCoeffState_min;
    static double utilityFunction_absCoeffState_max;
    static double utilityFunction_absExpSize_mean;
    static double utilityFunction_absExpSize_std;
    static double utilityFunction_absExpSize_min;
    static double utilityFunction_absExpSize_max;
    static double utilityFunction_absExpState_mean;
    static double utilityFunction_absExpState_std;
    static double utilityFunction_absExpState_min;
    static double utilityFunction_absExpState_max;
    static double utilityFunction_sigmoid1_mean;
    static double utilityFunction_sigmoid1_std;
    static double utilityFunction_sigmoid1_min;
    static double utilityFunction_sigmoid1_max;
    static double utilityFunction_sigmoid2_mean;
    static double utilityFunction_sigmoid2_std;
    static double utilityFunction_sigmoid2_min;
    static double utilityFunction_sigmoid2_max;

    static double minSurplusRatioForBestFlat;
    static double absCoeffSizeIncreaseInIteration;

    static double minDepositToPotentialWageIncome;
    static double mayRenovateWhenBuyingRatio;

    static double[] neighbourhoodQualities;
    static double[] bucketSizeIntervals;
    static double[] bucketStateIntervals;
    static double priceRegressionFunction_constant;
    static double priceRegressionFunction_coeffSize0;
    static double priceRegressionFunction_coeffSizeQualityCoeff;
    static double priceRegressionFunction_coeffSize2;
    static double priceRegressionFunction_coeffSizeStateMax;

    public static ArrayList<Flat> flatsForSale = new ArrayList<>();

    static int minAgeForRentalProbability;
    static double rentalProbabilityForMinAge;
    static int maxAgeForRentalProbability;
    static double rentalProbabilityForMaxAge;

    static double investorShare;
    static double minFirstWageForProperty;
    static ArrayList<Household> potentialInvestorHouseholds = new ArrayList<>();

    static double bank_yearlyInterestRateRegressionConstant;
    static double bank_yearlyInterestRateRegressionCoeffLnWageIncome;
    static double bank_yearlyInterestRateRegressionCoeffLTV;
    static double bank_yearlyInterestRateRegressionCoeffAgeCategory1;
    static double bank_yearlyInterestRateRegressionCoeffAgeCategory2;
    static double bank_bridgeLoanToValue;

    static double rentalMarketBuffer;
    static double initialYearlySoldRatio;
    static double histBucketMonthlyForSaleToSoldRatio;
    static double newlyBuiltRatio;
    static double neighbourhoodAreaToLastNewlyBuiltDemandRatio;
    static double soldRatioOfNewlyBuiltFlats;
    static double maxAreaUnderConstructionRatio;

    static double depositToLifeTimeIncomeRatioConstant;
    static double depositToLifeTimeIncomeRatioCoeff;
    static double depositDecreaseRatioForHomeValue;
    static double maxDepositToHomeValueForRenters;

    static double monthlyInvestmentRatio;
    static double yearlyGrowthRateForHistPriceIndices;

    static double normalYearlyRenovationRatio;

    static boolean channelConstructionSectorOn;
    static boolean channelRenovationOn;
    static boolean channelRentalMarketOn;
    static boolean channelBtlInvestmentOn;
    static boolean channelCreditMarketOn;
    static boolean channelGrowthOn;
    static boolean channelSimulationWithCapitalOnly;

    static Map<Bucket, Integer> nFlatsInBuckets = new HashMap<>();
    static Map<Bucket, Integer> nFlatsRentedInBuckets = new HashMap<>();

    static double realGDPMonthlyGrowthRate;

    public static void loadConfigurationParameters() {

        Model.randomSeed = Long.parseLong(prop.getProperty("randomSeed"));
        Model.nPeriods = Integer.parseInt(prop.getProperty("nPeriods"));
        Model.maxNPeriods = Integer.parseInt(prop.getProperty("maxNPeriods"));
        Model.nHistoryPeriods = Integer.parseInt(prop.getProperty("nHistoryPeriods"));
        Model.simulationWithShock = Boolean.parseBoolean(prop.getProperty("simulationWithShock"));
        Model.nTypes = Integer.parseInt(prop.getProperty("nTypes"));
        Model.lifespan = Integer.parseInt(prop.getProperty("lifespan"));
        Model.retirementAgeInPeriods = Integer.parseInt(prop.getProperty("retirementAgeInPeriods"));
        Model.pensionReplacementRate = PropertyToDoubleArray("pensionReplacementRate");
        Model.minUnemploymentPeriods = PropertyToIntArray("minUnemploymentPeriods");
        Model.maxNChildren = Integer.parseInt(prop.getProperty("maxNChildren"));
        Model.minAgeInPeriodsToBuyOrRentAFlatAsSingleHousehold = Integer.parseInt(prop.getProperty("minAgeInPeriodsToBuyOrRentAFlatAsSingleHousehold"));
        Model.nPeriodsToLookAheadToCalculateLifeTimeIncome = Integer.parseInt(prop.getProperty("nPeriodsToLookAheadToCalculateLifeTimeIncome"));

        Model.savingsRateConstant = Double.parseDouble(prop.getProperty("savingsRateConstant"));
        Model.savingsRateCoeff = Double.parseDouble(prop.getProperty("savingsRateCoeff"));
        Model.minConsumptionRate = Double.parseDouble(prop.getProperty("minConsumptionRate"));
        Model.minConsumptionPerCapitaLower = Double.parseDouble(prop.getProperty("minConsumptionPerCapitaLower"));
        Model.minConsumptionPerCapitaUpper = Double.parseDouble(prop.getProperty("minConsumptionPerCapitaUpper"));
        Model.minConsumptionPerCapitaLowerThreshold = Double.parseDouble(prop.getProperty("minConsumptionPerCapitaLowerThreshold"));
        Model.minConsumptionPerCapitaUpperThreshold = Double.parseDouble(prop.getProperty("minConsumptionPerCapitaUpperThreshold"));
        Model.weightOfAdditionalAdultsInMinConsumption = Double.parseDouble(prop.getProperty("weightOfAdditionalAdultsInMinConsumption"));
        Model.weightOfChildrenInMinConsumption = Double.parseDouble(prop.getProperty("weightOfChildrenInMinConsumption"));
        Model.minAgeInPeriodsToCountAsAdditionalAdultInMinConsumnption = Integer.parseInt(prop.getProperty("minAgeInPeriodsToCountAsAdditionalAdultInMinConsumnption"));

        Model.marketPriceOnRegression = Boolean.parseBoolean(prop.getProperty("marketPriceOnRegression"));
        Model.forcedSaleDiscount = Double.parseDouble(prop.getProperty("forcedSaleDiscount"));
        Model.realGDPLevelShockTriggerValueForAdditionalForcedSaleDiscount = Double.parseDouble(prop.getProperty("realGDPLevelShockTriggerValueForAdditionalForcedSaleDiscount"));
        Model.additionalForcedSaleDiscount = Double.parseDouble(prop.getProperty("additionalForcedSaleDiscount"));
        Model.monthlyForSalePriceDecrease = Double.parseDouble(prop.getProperty("monthlyForSalePriceDecrease"));
        Model.minForSalePriceForRegression = Double.parseDouble(prop.getProperty("minForSalePriceForRegression"));
        Model.minForSalePrice = Double.parseDouble(prop.getProperty("minForSalePrice"));
        Model.maxSizeValueForSize2 = Double.parseDouble(prop.getProperty("maxSizeValueForSize2"));
        Model.minStateValueForSizeState = Double.parseDouble(prop.getProperty("minStateValueForSizeState"));
        Model.priceRatioMinRatio = Double.parseDouble(prop.getProperty("priceRatioMinRatio"));
        Model.priceRatioMaxRatio = Double.parseDouble(prop.getProperty("priceRatioMaxRatio"));
        Model.nFlatsToLookAt = Integer.parseInt(prop.getProperty("nFlatsToLookAt"));
        Model.minNFlatsToLookAtToShiftFlatIndices = Integer.parseInt(prop.getProperty("minNFlatsToLookAtToShiftFlatIndices"));
        Model.maxShiftInFlatIndicesInChooseBestFlat = Integer.parseInt(prop.getProperty("maxShiftInFlatIndicesInChooseBestFlat"));
        Model.maxNChecksFromBucketOfNewlyBuiltBestFlat = Integer.parseInt(prop.getProperty("maxNChecksFromBucketOfNewlyBuiltBestFlat"));
        Model.renovationStateIncreaseWhenBuying = Double.parseDouble(prop.getProperty("renovationStateIncreaseWhenBuying"));
        Model.maxNIterationsForHouseholdPurchases = Integer.parseInt(prop.getProperty("maxNIterationsForHouseholdPurchases"));
        Model.canChangeGeoLocationProbability = PropertyToDoubleArray("canChangeGeoLocationProbability");
        Model.nFictiveFlatsForSalePerBucket = Integer.parseInt(prop.getProperty("nFictiveFlatsForSalePerBucket"));

        Model.sizeDistanceRatioThresholdForClosestNeighbourCalculation = Double.parseDouble(prop.getProperty("sizeDistanceRatioThresholdForClosestNeighbourCalculation"));
        Model.stateDistanceRatioThresholdForClosestNeighbourCalculation = Double.parseDouble(prop.getProperty("stateDistanceRatioThresholdForClosestNeighbourCalculation"));
        Model.sizeWeightInClosestNeighbourCalculation = Double.parseDouble(prop.getProperty("sizeWeightInClosestNeighbourCalculation"));
        Model.stateWeightInClosestNeighbourCalculation = Double.parseDouble(prop.getProperty("stateWeightInClosestNeighbourCalculation"));
        Model.maxSizeDifferenceRatio = Double.parseDouble(prop.getProperty("maxSizeDifferenceRatio"));
        Model.maxStateDifferenceRatio = Double.parseDouble(prop.getProperty("maxStateDifferenceRatio"));

        Model.ageInYearsForMandatoryMoving = Integer.parseInt(prop.getProperty("ageInYearsForMandatoryMoving"));
        Model.probabilityOfMandatoryMoving = Double.parseDouble(prop.getProperty("probabilityOfMandatoryMoving"));
        Model.probabilityOfAssessingPotentialNewHomes = Double.parseDouble(prop.getProperty("probabilityOfAssessingPotentialNewHomes"));
        Model.baseAgeInYearsForProbabilityOfAssessingPotentialNewHomes = Double.parseDouble(prop.getProperty("baseAgeInYearsForProbabilityOfAssessingPotentialNewHomes"));
        Model.yearlyDiscountForProbabilityOfAssessingPotentialNewHomes = Double.parseDouble(prop.getProperty("yearlyDiscountForProbabilityOfAssessingPotentialNewHomes"));
        Model.thresholdRatioForMoving = Double.parseDouble(prop.getProperty("thresholdRatioForMoving"));
        Model.thresholdPriceDifferenceForMoving = Double.parseDouble(prop.getProperty("thresholdPriceDifferenceForMoving"));
        Model.nMonthsToWithdrawHomeFromMarket = Integer.parseInt(prop.getProperty("nMonthsToWithdrawHomeFromMarket"));
        Model.minNMonthsOfOwnHomeOnMarketToBuyFlat = Integer.parseInt(prop.getProperty("minNMonthsOfOwnHomeOnMarketToBuyFlat"));
        Model.nPeriodsForAverageNForSaleToNSold = Integer.parseInt(prop.getProperty("nPeriodsForAverageNForSaleToNSold"));
        Model.targetNForSaleToNSold = Double.parseDouble(prop.getProperty("targetNForSaleToNSold"));
        Model.coeffProbabilityNForSaleToNSoldAdjustment = Double.parseDouble(prop.getProperty("coeffProbabilityNForSaleToNSoldAdjustment"));
        Model.minForSaleToNSoldProbabilityAdjustment = Double.parseDouble(prop.getProperty("minForSaleToNSoldProbabilityAdjustment"));
        Model.maxForSaleToNSoldProbabilityAdjustment = Double.parseDouble(prop.getProperty("maxForSaleToNSoldProbabilityAdjustment"));

        Model.cyclicalAdjusterPriceIndexCoeff = Double.parseDouble(prop.getProperty("cyclicalAdjusterPriceIndexCoeff"));
        Model.cyclicalAdjusterBasePeriod = Integer.parseInt(prop.getProperty("cyclicalAdjusterBasePeriod"));
        Model.coeffInReservationPriceAdjusterAccordingToNPeriodsWaitingForFlatToBuy = Double.parseDouble(prop.getProperty("coeffInReservationPriceAdjusterAccordingToNPeriodsWaitingForFlatToBuy"));
        Model.reservationMarkup = Double.parseDouble(prop.getProperty("reservationMarkup"));
        Model.sellerReservationPriceDecrease = Double.parseDouble(prop.getProperty("sellerReservationPriceDecrease"));
        Model.adjusterProbabilityOfPlacingBid = Double.parseDouble(prop.getProperty("adjusterProbabilityOfPlacingBid"));
        Model.probabilityOfPlacingBidCoeff = Double.parseDouble(prop.getProperty("probabilityOfPlacingBidCoeff"));
        Model.newlyBuiltUtilityAdjusterCoeff1 = Double.parseDouble(prop.getProperty("newlyBuiltUtilityAdjusterCoeff1"));
        Model.newlyBuiltUtilityAdjusterCoeff2 = Double.parseDouble(prop.getProperty("newlyBuiltUtilityAdjusterCoeff2"));
        Model.reservationPriceShare = Double.parseDouble(prop.getProperty("reservationPriceShare"));
        Model.maxForSaleMultiplier = Double.parseDouble(prop.getProperty("maxForSaleMultiplier"));
        Model.maxIncreaseInReservationPriceAsARatioOfTheSurplusDifferenceToTheFictiveFlat = Double.parseDouble(prop.getProperty("maxIncreaseInReservationPriceAsARatioOfTheSurplusDifferenceToTheFictiveFlat"));
        Model.powerInAdjustmentInReservationPrice = Double.parseDouble(prop.getProperty("powerInAdjustmentInReservationPrice"));
        Model.maxNBidsPlacedPerHousehold = Integer.parseInt(prop.getProperty("maxNBidsPlacedPerHousehold"));

        Model.minYearlyInterestRate = Double.parseDouble(prop.getProperty("minYearlyInterestRate"));
        Model.maxYearlyInterestRate = Double.parseDouble(prop.getProperty("maxYearlyInterestRate"));
        Model.minMarketPriceForBridgeLoan = Double.parseDouble(prop.getProperty("minMarketPriceForBridgeLoan"));
        Model.bridgeLoanDuration = Integer.parseInt(prop.getProperty("bridgeLoanDuration"));
        Model.renovationLoanDuration = Integer.parseInt(prop.getProperty("renovationLoanDuration"));
        Model.durationIncreaseInIncreaseLoanForRenovation = Double.parseDouble(prop.getProperty("durationIncreaseInIncreaseLoanForRenovation"));
        Model.nNonPerformingPeriodsForRestructuring = Integer.parseInt(prop.getProperty("nNonPerformingPeriodsForRestructuring"));
        Model.nNonPerformingPeriodsForForcedSale = Integer.parseInt(prop.getProperty("nNonPerformingPeriodsForForcedSale"));
        Model.minDurationForRestructuring = Integer.parseInt(prop.getProperty("minDurationForRestructuring"));
        Model.durationIncreaseInRestructuring = Integer.parseInt(prop.getProperty("durationIncreaseInRestructuring"));
        Model.incomeRatioForBankFromFlatForSalePrice = Double.parseDouble(prop.getProperty("incomeRatioForBankFromFlatForSalePrice"));
        Model.nPeriodsInNegativeKHR = Integer.parseInt(prop.getProperty("nPeriodsInNegativeKHR"));
        Model.additionalPTI = Double.parseDouble(prop.getProperty("additionalPTI"));
        Model.firstBuyerAdditionalLTV = Double.parseDouble(prop.getProperty("firstBuyerAdditionalLTV"));

        Model.nPeriodsForConstruction = Integer.parseInt(prop.getProperty("nPeriodsForConstruction"));
        Model.sizeMaxOfConstructionFlatsToSizeMinInLargestSizeBucket = Double.parseDouble(prop.getProperty("sizeMaxOfConstructionFlatsToSizeMinInLargestSizeBucket"));
        Model.sizeMaxOfFictiveFlatsToSizeMinInLargestSizeBucket = Double.parseDouble(prop.getProperty("sizeMaxOfFictiveFlatsToSizeMinInLargestSizeBucket"));
        Model.constructionUnitCostBaseRegional = Double.parseDouble(prop.getProperty("constructionUnitCostBaseRegional"));
        Model.constructionAndRenovationUnitCostBaseCapitalMarkup = Double.parseDouble(prop.getProperty("constructionAndRenovationUnitCostBaseCapitalMarkup"));
        Model.constructionAndRenovationUnitCostBaseAgglomerationMarkup = Double.parseDouble(prop.getProperty("constructionAndRenovationUnitCostBaseAgglomerationMarkup"));
        Model.constructionUnitCostAverageWageCoeff = Double.parseDouble(prop.getProperty("constructionUnitCostAverageWageCoeff"));
        Model.constructionMarkupRatio1 = Double.parseDouble(prop.getProperty("constructionMarkupRatio1"));
        Model.constructionMarkupRatio1Level = Double.parseDouble(prop.getProperty("constructionMarkupRatio1Level"));
        Model.constructionMarkupRatio2 = Double.parseDouble(prop.getProperty("constructionMarkupRatio2"));
        Model.constructionMarkupRatio2Level = Double.parseDouble(prop.getProperty("constructionMarkupRatio2Level"));
        Model.maxConstructionPriceToMarketPriceWithProperConstructionCostCoverage = Double.parseDouble(prop.getProperty("maxConstructionPriceToMarketPriceWithProperConstructionCostCoverage"));
        Model.constructionCostCoverageNeed = Double.parseDouble(prop.getProperty("constructionCostCoverageNeed"));
        Model.nForSalePeriodsToStartAdjustingConstructionForSalePrice = Integer.parseInt(prop.getProperty("nForSalePeriodsToStartAdjustingConstructionForSalePrice"));
        Model.parameterForConstructionForSalePriceAdjustment = Double.parseDouble(prop.getProperty("parameterForConstructionForSalePriceAdjustment"));
        Model.maxFlatStateToLandPriceStateForConstructionPurchase = Double.parseDouble(prop.getProperty("maxFlatStateToLandPriceStateForConstructionPurchase"));
        Model.nPeriodsForAverageNewlyBuiltDemand = Integer.parseInt(prop.getProperty("nPeriodsForAverageNewlyBuiltDemand"));
        Model.targetNewlyBuiltBuffer = Double.parseDouble(prop.getProperty("targetNewlyBuiltBuffer"));
        Model.maxNFlatsToBuildInBucketToAverageNewlyBuiltDemandRatio = Double.parseDouble(prop.getProperty("maxNFlatsToBuildInBucketToAverageNewlyBuiltDemandRatio"));
        Model.maxFlatSizeForLandPrice = Double.parseDouble(prop.getProperty("maxFlatSizeForLandPrice"));
        Model.maxFlatStateForLandPrice = Double.parseDouble(prop.getProperty("maxFlatStateForLandPrice"));
        Model.constructionAreaNeedRatio = Double.parseDouble(prop.getProperty("constructionAreaNeedRatio"));
        Model.monthlyConstructionAreaLimit = Boolean.parseBoolean(prop.getProperty("monthlyConstructionAreaLimit"));

        Model.renovationProbability = Double.parseDouble(prop.getProperty("renovationProbability"));
        Model.renovationUnitCostBaseRegional = Double.parseDouble(prop.getProperty("renovationUnitCostBaseRegional"));
        Model.renovationUnitCostAverageWageCoeff = Double.parseDouble(prop.getProperty("renovationUnitCostAverageWageCoeff"));
        Model.stateDepreciation = Double.parseDouble(prop.getProperty("stateDepreciation"));
        Model.nPeriodsInHighQualityBucket = Integer.parseInt(prop.getProperty("nPeriodsInHighQualityBucket"));
        Model.renovationCostBuffer = Double.parseDouble(prop.getProperty("renovationCostBuffer"));

        Model.capitalId = Integer.parseInt(prop.getProperty("capitalId"));
        Model.agglomerationId = Integer.parseInt(prop.getProperty("agglomerationId"));

        Model.nPeriodsForFlatSaleRecords = Integer.parseInt(prop.getProperty("nPeriodsForFlatSaleRecords"));
        Model.nPeriodsForFlatSaleRecordsToUseInPriceRegression = Integer.parseInt(prop.getProperty("nPeriodsForFlatSaleRecordsToUseInPriceRegression"));
        Model.nFlatsForPriceRegression = Integer.parseInt(prop.getProperty("nFlatsForPriceRegression"));
        Model.nPeriodsUntilCopyingPriceRegressionCoeffients = Integer.parseInt(prop.getProperty("nPeriodsUntilCopyingPriceRegressionCoeffients"));
        Model.maxChangeInRegressionParameters = Double.parseDouble(prop.getProperty("maxChangeInRegressionParameters"));
        Model.minNObservationForPriceIndex = Integer.parseInt(prop.getProperty("minNObservationForPriceIndex"));
        Model.baseMinPeriodForPriceIndexToBeginning = Integer.parseInt(prop.getProperty("baseMinPeriodForPriceIndexToBeginning"));
        Model.baseMaxPeriodForPriceIndexToBeginning = Integer.parseInt(prop.getProperty("baseMaxPeriodForPriceIndexToBeginning"));
        Model.size2inPriceIndexRegression = Boolean.parseBoolean(prop.getProperty("size2inPriceIndexRegression"));
        Model.useTransactionWeightsForPriceIndex = Boolean.parseBoolean(prop.getProperty("useTransactionWeightsForPriceIndex"));
        Model.useForcedSaleForPriceIndex = Boolean.parseBoolean(prop.getProperty("useForcedSaleForPriceIndex"));

        Model.minRentRatio = Double.parseDouble(prop.getProperty("minRentRatio"));
        Model.maxRentRatio = Double.parseDouble(prop.getProperty("maxRentRatio"));
        Model.ageInYearsToConsiderSomebodyOldEnoughToRentForSure = Integer.parseInt(prop.getProperty("ageInYearsToConsiderSomebodyOldEnoughToRentForSure"));
        Model.coeffOfFirstWageRegardingRentalProbability = Double.parseDouble(prop.getProperty("coeffOfFirstWageRegardingRentalProbability"));
        Model.rentToPrice = Double.parseDouble(prop.getProperty("rentToPrice"));
        Model.nMaxPeriodsForRent = Integer.parseInt(prop.getProperty("nMaxPeriodsForRent"));
        Model.rentMarkupPower = Double.parseDouble(prop.getProperty("rentMarkupPower"));
        Model.rentMarkupCoeff = Double.parseDouble(prop.getProperty("rentMarkupCoeff"));
        Model.rentMarkupUtilizationRatioCap = Double.parseDouble(prop.getProperty("rentMarkupUtilizationRatioCap"));
        Model.nPeriodsForAverageNFictiveRentDemand = Integer.parseInt(prop.getProperty("nPeriodsForAverageNFictiveRentDemand"));
        Model.nPeriodsForAverageReturn = Integer.parseInt(prop.getProperty("nPeriodsForAverageReturn"));
        Model.expectedReturnCapitalGainCoeff = Double.parseDouble(prop.getProperty("expectedReturnCapitalGainCoeff"));
        Model.nPeriodsForUtilizationRatio = Integer.parseInt(prop.getProperty("nPeriodsForUtilizationRatio"));
        Model.targetUtilizationRatio = Double.parseDouble(prop.getProperty("targetUtilizationRatio"));
        Model.minUtilizationRatioForTargetUtilizationRatioToUtilizationRatioInRentSaleProbability = Double.parseDouble(prop.getProperty("minUtilizationRatioForTargetUtilizationRatioToUtilizationRatioInRentSaleProbability"));
        Model.minTargetRatioForInvestmentProbability = Double.parseDouble(prop.getProperty("minTargetRatioForInvestmentProbability"));
        Model.stepInSearchingTargetRatioForInvestmentProbability = Double.parseDouble(prop.getProperty("stepInSearchingTargetRatioForInvestmentProbability"));
        Model.monthlyInvestmentRatioConstantCentralInvestor = Double.parseDouble(prop.getProperty("monthlyInvestmentRatioConstantCentralInvestor"));
        Model.monthlyInvestmentRatioCoeffExpectedReturnCentralInvestor = Double.parseDouble(prop.getProperty("monthlyInvestmentRatioCoeffExpectedReturnCentralInvestor"));
        Model.minDepositToInvest = Double.parseDouble(prop.getProperty("minDepositToInvest"));
        Model.yearlyRentSaleProbabilityAtZeroExpectedReturnSpread = Double.parseDouble(prop.getProperty("yearlyRentSaleProbabilityAtZeroExpectedReturnSpread"));
        Model.minYearlyExpectedReturnSpreadForZeroRentSaleProbability = Double.parseDouble(prop.getProperty("minYearlyExpectedReturnSpreadForZeroRentSaleProbability"));
        Model.householdInvestmentProbabilityPower = Double.parseDouble(prop.getProperty("householdInvestmentProbabilityPower"));
        Model.householdInvestmentProbabilityCoeff = Double.parseDouble(prop.getProperty("householdInvestmentProbabilityCoeff"));
        Model.maxHouseholdInvestmentProbability = Double.parseDouble(prop.getProperty("maxHouseholdInvestmentProbability"));
        Model.maxPlannedInvestmentValueToAggregateMarketValue = Double.parseDouble(prop.getProperty("maxPlannedInvestmentValueToAggregateMarketValue"));

        Model.ageInPeriodsForFirstWorkExperience = PropertyToIntArray("ageInPeriodsForFirstWorkExperience");
        Model.inheritorDistanceTypeCoeff = Double.parseDouble(prop.getProperty("inheritorDistanceTypeCoeff"));
        Model.inheritorDistanceFirstWageRatioCoeff = Double.parseDouble(prop.getProperty("inheritorDistanceFirstWageRatioCoeff"));
        Model.inheritorDistancePreferredGeoLocationCoeff = Double.parseDouble(prop.getProperty("inheritorDistancePreferredGeoLocationCoeff"));

        Model.familyBenefit = PropertyToDoubleArray("familyBenefit");
        Model.depositInheritanceRatio = Double.parseDouble(prop.getProperty("depositInheritanceRatio"));

    }

    public static void calculateModelParametersFromConfigurationParameters() {

        Model.wageRatio = deriveMonthlyWageRatio();
        Model.unemploymentRatesPath = deriveStaticPathsForTypes(PropertyToDoubleArray("unemploymentRatesBase"), Model.maxNPeriods);
        Model.unemploymentProbabilitiesPath = deriveStaticPathsForTypes(PropertyToDoubleArray("unemploymentProbabilitiesBase"), Model.maxNPeriods);

        double realGDPInitialValue = Double.parseDouble(prop.getProperty("realGDPInitialValue"));
        realGDPMonthlyGrowthRate = Double.parseDouble(prop.getProperty("realGDPMonthlyGrowthRate"));
        if (channelGrowthOn == false) realGDPMonthlyGrowthRate = 0;
        Model.realGDPLevelPath = derivePathWithFixedGrowthRate(realGDPInitialValue, realGDPMonthlyGrowthRate, Model.maxNPeriods);
        double priceLevelInitialValue = Double.parseDouble(prop.getProperty("priceLevelInitialValue"));
        double priceLevelMonthlyGrowthRate = Double.parseDouble(prop.getProperty("priceLevelMonthlyGrowthRate"));
        if (channelGrowthOn == false) priceLevelMonthlyGrowthRate = 0;
        Model.priceLevelPath = derivePathWithFixedGrowthRate(priceLevelInitialValue, priceLevelMonthlyGrowthRate, Model.nPeriods);

        Model.yearlyBaseRatePath = derivePathWithFixedGrowthRate(Double.parseDouble(prop.getProperty("yearlyBaseRateBase")), 0, Model.nPeriods);
        Model.LTVPath = derivePathWithFixedGrowthRate(Double.parseDouble(prop.getProperty("LTVBase")), 0, Model.nPeriods);
        Model.PTIPath = derivePathWithFixedGrowthRate(Double.parseDouble(prop.getProperty("PTIBase")), 0, Model.nPeriods);
        Model.maxDurationPath = derivePathWithFixedGrowthRate(Integer.parseInt(prop.getProperty("maxDurationBase")), 0, Model.nPeriods);
        Model.taxRatePath = derivePathWithFixedGrowthRate(Double.parseDouble(prop.getProperty("taxRateBase")), 0, Model.nPeriods);



        Model.marriageProbability = new double[Model.nTypes][Model.lifespan];
        double[] marriageProbabilityPoints = PropertyToDoubleArray("marriageProbabilityPoints");
        for (int i = 0; i < marriageProbabilityPoints.length; i += 3) {
            int typeIndex = (int) marriageProbabilityPoints[i];
            int ageInPeriods = (int) marriageProbabilityPoints[i + 1] * 12;
            double probability = marriageProbabilityPoints[i + 2];
            Model.marriageProbability[typeIndex][ageInPeriods] = probability;
        }

        Model.birthProbability = new double[Model.nTypes][Model.lifespan][Model.maxNChildren];
        double[] birthProbabilityPoints = PropertyToDoubleArray("birthProbabilityPoints");
        for (int i = 0; i < birthProbabilityPoints.length; i += 4) {
            int typeIndex = (int) birthProbabilityPoints[i];
            int ageInPeriods = (int) birthProbabilityPoints[i + 1] * 12;
            int childNum = (int) birthProbabilityPoints[i + 2];
            double probability = birthProbabilityPoints[i + 3];
            Model.birthProbability[typeIndex][ageInPeriods][childNum] = probability;
        }

        Model.deathProbability = new double[2][Model.nTypes][Model.lifespan];
        double[] deathProbabilityPoints = PropertyToDoubleArray("deathProbabilityPoints");
        for (int i = 0; i < deathProbabilityPoints.length; i += 4) {
            int genderIndex = (int) deathProbabilityPoints[i];
            int typeIndex = (int) deathProbabilityPoints[i + 1];
            int ageInPeriods = (int) deathProbabilityPoints[i + 2] * 12;
            double probability = deathProbabilityPoints[i + 3];
            Model.deathProbability[genderIndex][typeIndex][ageInPeriods] = probability;
        }

        //SHOCK

        Model.realGDPLevelShockPath = deriveShockPath(Integer.parseInt(prop.getProperty("shockStartPeriod")), Integer.parseInt(prop.getProperty("shockEndPeriod")), PropertyToDoubleArray("realGDPShockPoints"));
        Model.priceLevelShockPath = deriveShockPath(Integer.parseInt(prop.getProperty("shockStartPeriod")), Integer.parseInt(prop.getProperty("shockEndPeriod")), PropertyToDoubleArray("priceLevelShockPoints"));
        Model.yearlyBaseRateShockPath = deriveShockPath(Integer.parseInt(prop.getProperty("shockStartPeriod")), Integer.parseInt(prop.getProperty("shockEndPeriod")), PropertyToDoubleArray("yearlyBaseRateShockPoints"));

        double[] unemploymentRate0ShockPath = deriveShockPath(Integer.parseInt(prop.getProperty("shockStartPeriod")), Integer.parseInt(prop.getProperty("shockEndPeriod")), PropertyToDoubleArray("unemploymentRate0ShockPoints"));
        double[] unemploymentRate1ShockPath = deriveShockPath(Integer.parseInt(prop.getProperty("shockStartPeriod")), Integer.parseInt(prop.getProperty("shockEndPeriod")), PropertyToDoubleArray("unemploymentRate1ShockPoints"));
        double[] unemploymentRate2ShockPath = deriveShockPath(Integer.parseInt(prop.getProperty("shockStartPeriod")), Integer.parseInt(prop.getProperty("shockEndPeriod")), PropertyToDoubleArray("unemploymentRate2ShockPoints"));
        double[] unemploymentProbability0ShockPath = deriveShockPath(Integer.parseInt(prop.getProperty("shockStartPeriod")), Integer.parseInt(prop.getProperty("shockEndPeriod")), PropertyToDoubleArray("unemploymentProbability0ShockPoints"));
        double[] unemploymentProbability1ShockPath = deriveShockPath(Integer.parseInt(prop.getProperty("shockStartPeriod")), Integer.parseInt(prop.getProperty("shockEndPeriod")), PropertyToDoubleArray("unemploymentProbability1ShockPoints"));
        double[] unemploymentProbability2ShockPath = deriveShockPath(Integer.parseInt(prop.getProperty("shockStartPeriod")), Integer.parseInt(prop.getProperty("shockEndPeriod")), PropertyToDoubleArray("unemploymentProbability2ShockPoints"));

        Model.unemploymentRatesShockPath = new double[Model.nPeriods][];
        Model.unemploymentProbabilitiesShockPath = new int[Model.nPeriods][];
        for (int i = 0; i < Model.nPeriods; i++) {
            Model.unemploymentRatesShockPath[i] = new double[Model.nTypes];
            Model.unemploymentProbabilitiesShockPath[i] = new int[Model.nTypes];

            Model.unemploymentRatesShockPath[i][0] = unemploymentRate0ShockPath[i];
            Model.unemploymentRatesShockPath[i][1] = unemploymentRate1ShockPath[i];
            Model.unemploymentRatesShockPath[i][2] = unemploymentRate2ShockPath[i];
            Model.unemploymentProbabilitiesShockPath[i][0] = (int) Math.ceil(unemploymentProbability0ShockPath[i]);
            Model.unemploymentProbabilitiesShockPath[i][1] = (int) Math.ceil(unemploymentProbability1ShockPath[i]);
            Model.unemploymentProbabilitiesShockPath[i][2] = (int) Math.ceil(unemploymentProbability2ShockPath[i]);

        }


    }

    public static void loadConfigurationParametersForInputGeneration() {

        nIndividuals = Integer.parseInt(prop.getProperty("nIndividuals"));
        marriageRatio = Double.parseDouble(prop.getProperty("marriageRatio"));
        typeShare = PropertyToDoubleArray("typeShare");
        maxAgeInYearsToBeAChild = Integer.parseInt(prop.getProperty("maxAgeInYearsToBeAChild"));
        minAgeDifferenceBetweenChildAndParent = Integer.parseInt(prop.getProperty("minAgeDifferenceBetweenChildAndParent"));
        maxAgeDifferenceBetweenChildAndParent = Integer.parseInt(prop.getProperty("maxAgeDifferenceBetweenChildAndParent"));
        firstWage_mean = PropertyToDoubleArray("firstWage_mean");
        firstWage_std = PropertyToDoubleArray("firstWage_std");
        firstWage_min = PropertyToDoubleArray("firstWage_min");
        firstWage_max = PropertyToDoubleArray("firstWage_max");

        utilityFunction_absCoeffSize_mean = Double.parseDouble(prop.getProperty("utilityFunction_absCoeffSize_mean"));
        utilityFunction_absCoeffSize_std = Double.parseDouble(prop.getProperty("utilityFunction_absCoeffSize_std"));
        utilityFunction_absCoeffSize_min = Double.parseDouble(prop.getProperty("utilityFunction_absCoeffSize_min"));
        utilityFunction_absCoeffSize_max = Double.parseDouble(prop.getProperty("utilityFunction_absCoeffSize_max"));
        utilityFunction_absCoeffState_mean = Double.parseDouble(prop.getProperty("utilityFunction_absCoeffState_mean"));
        utilityFunction_absCoeffState_std = Double.parseDouble(prop.getProperty("utilityFunction_absCoeffState_std"));
        utilityFunction_absCoeffState_min = Double.parseDouble(prop.getProperty("utilityFunction_absCoeffState_min"));
        utilityFunction_absCoeffState_max = Double.parseDouble(prop.getProperty("utilityFunction_absCoeffState_max"));
        utilityFunction_absExpSize_mean = Double.parseDouble(prop.getProperty("utilityFunction_absExpSize_mean"));
        utilityFunction_absExpSize_std = Double.parseDouble(prop.getProperty("utilityFunction_absExpSize_std"));
        utilityFunction_absExpSize_min = Double.parseDouble(prop.getProperty("utilityFunction_absExpSize_min"));
        utilityFunction_absExpSize_max = Double.parseDouble(prop.getProperty("utilityFunction_absExpSize_max"));
        utilityFunction_absExpState_mean = Double.parseDouble(prop.getProperty("utilityFunction_absExpState_mean"));
        utilityFunction_absExpState_std = Double.parseDouble(prop.getProperty("utilityFunction_absExpState_std"));
        utilityFunction_absExpState_min = Double.parseDouble(prop.getProperty("utilityFunction_absExpState_min"));
        utilityFunction_absExpState_max = Double.parseDouble(prop.getProperty("utilityFunction_absExpState_max"));
        utilityFunction_sigmoid1_mean = Double.parseDouble(prop.getProperty("utilityFunction_sigmoid1_mean"));
        utilityFunction_sigmoid1_std = Double.parseDouble(prop.getProperty("utilityFunction_sigmoid1_std"));
        utilityFunction_sigmoid1_min = Double.parseDouble(prop.getProperty("utilityFunction_sigmoid1_min"));
        utilityFunction_sigmoid1_max = Double.parseDouble(prop.getProperty("utilityFunction_sigmoid1_max"));
        utilityFunction_sigmoid2_mean = Double.parseDouble(prop.getProperty("utilityFunction_sigmoid2_mean"));
        utilityFunction_sigmoid2_std = Double.parseDouble(prop.getProperty("utilityFunction_sigmoid2_std"));
        utilityFunction_sigmoid2_min = Double.parseDouble(prop.getProperty("utilityFunction_sigmoid2_min"));
        utilityFunction_sigmoid2_max = Double.parseDouble(prop.getProperty("utilityFunction_sigmoid2_max"));

        minSurplusRatioForBestFlat = Double.parseDouble(prop.getProperty("minSurplusRatioForBestFlat"));
        absCoeffSizeIncreaseInIteration = Double.parseDouble(prop.getProperty("absCoeffSizeIncreaseInIteration"));

        minDepositToPotentialWageIncome = Double.parseDouble(prop.getProperty("minDepositToPotentialWageIncome"));
        mayRenovateWhenBuyingRatio = Double.parseDouble(prop.getProperty("mayRenovateWhenBuyingRatio"));

        bucketSizeIntervals = PropertyToDoubleArray("bucketSizeIntervals");
        bucketStateIntervals = PropertyToDoubleArray("bucketStateIntervals");
        neighbourhoodQualities = PropertyToDoubleArray("neighbourhoodQualities");

        priceRegressionFunction_constant = Double.parseDouble(prop.getProperty("priceRegressionFunction_constant"));
        priceRegressionFunction_coeffSize0 = Double.parseDouble(prop.getProperty("priceRegressionFunction_coeffSize0"));
        priceRegressionFunction_coeffSizeQualityCoeff = Double.parseDouble(prop.getProperty("priceRegressionFunction_coeffSizeQualityCoeff"));
        priceRegressionFunction_coeffSize2 = Double.parseDouble(prop.getProperty("priceRegressionFunction_coeffSize2"));
        priceRegressionFunction_coeffSizeStateMax = Double.parseDouble(prop.getProperty("priceRegressionFunction_coeffSizeStateMax"));

        minAgeForRentalProbability = Integer.parseInt(prop.getProperty("minAgeForRentalProbability"));
        rentalProbabilityForMinAge = Double.parseDouble(prop.getProperty("rentalProbabilityForMinAge"));
        maxAgeForRentalProbability = Integer.parseInt(prop.getProperty("maxAgeForRentalProbability"));
        rentalProbabilityForMaxAge = Double.parseDouble(prop.getProperty("rentalProbabilityForMaxAge"));
        investorShare = Double.parseDouble(prop.getProperty("investorShare"));
        minFirstWageForProperty = Double.parseDouble(prop.getProperty("minFirstWageForProperty"));

        bank_yearlyInterestRateRegressionConstant = Double.parseDouble(prop.getProperty("bank_yearlyInterestRateRegressionConstant"));
        bank_yearlyInterestRateRegressionCoeffLnWageIncome = Double.parseDouble(prop.getProperty("bank_yearlyInterestRateRegressionCoeffLnWageIncome"));
        bank_yearlyInterestRateRegressionCoeffLTV = Double.parseDouble(prop.getProperty("bank_yearlyInterestRateRegressionCoeffLTV"));
        bank_yearlyInterestRateRegressionCoeffAgeCategory1 = Double.parseDouble(prop.getProperty("bank_yearlyInterestRateRegressionCoeffAgeCategory1"));
        bank_yearlyInterestRateRegressionCoeffAgeCategory2 = Double.parseDouble(prop.getProperty("bank_yearlyInterestRateRegressionCoeffAgeCategory2"));
        bank_bridgeLoanToValue = Double.parseDouble(prop.getProperty("bank_bridgeLoanToValue"));

        rentalMarketBuffer = Double.parseDouble(prop.getProperty("rentalMarketBuffer"));
        initialYearlySoldRatio = Double.parseDouble(prop.getProperty("initialYearlySoldRatio"));
        histBucketMonthlyForSaleToSoldRatio = Double.parseDouble(prop.getProperty("histBucketMonthlyForSaleToSoldRatio"));
        newlyBuiltRatio = Double.parseDouble(prop.getProperty("newlyBuiltRatio"));
        neighbourhoodAreaToLastNewlyBuiltDemandRatio = Double.parseDouble(prop.getProperty("neighbourhoodAreaToLastNewlyBuiltDemandRatio"));
        soldRatioOfNewlyBuiltFlats = Double.parseDouble(prop.getProperty("soldRatioOfNewlyBuiltFlats"));
        maxAreaUnderConstructionRatio = Double.parseDouble(prop.getProperty("maxAreaUnderConstructionRatio"));

        depositToLifeTimeIncomeRatioConstant = Double.parseDouble(prop.getProperty("depositToLifeTimeIncomeRatioConstant"));
        depositToLifeTimeIncomeRatioCoeff = Double.parseDouble(prop.getProperty("depositToLifeTimeIncomeRatioCoeff"));
        depositDecreaseRatioForHomeValue = Double.parseDouble(prop.getProperty("depositDecreaseRatioForHomeValue"));
        maxDepositToHomeValueForRenters = Double.parseDouble(prop.getProperty("maxDepositToHomeValueForRenters"));

        monthlyInvestmentRatio = Double.parseDouble(prop.getProperty("monthlyInvestmentRatio"));
        yearlyGrowthRateForHistPriceIndices = Double.parseDouble(prop.getProperty("yearlyGrowthRateForHistPriceIndices"));
        normalYearlyRenovationRatio = Double.parseDouble(prop.getProperty("normalYearlyRenovationRatio"));

        Model.highQualityStateMin = bucketStateIntervals[bucketStateIntervals.length - 2];
        Model.highQualityStateMax = bucketStateIntervals[bucketStateIntervals.length - 1];

    }

    public static void loadChannelBooleans() {
        String fileName = "src/main/java/resources/sampleConfiguration.properties";

        channelConstructionSectorOn = Boolean.parseBoolean(prop.getProperty("channelConstructionSectorOn"));
        channelRenovationOn = Boolean.parseBoolean(prop.getProperty("channelRenovationOn"));
        channelRentalMarketOn = Boolean.parseBoolean(prop.getProperty("channelRentalMarketOn"));
        channelBtlInvestmentOn = Boolean.parseBoolean(prop.getProperty("channelBtlInvestmentOn"));
        channelCreditMarketOn = Boolean.parseBoolean(prop.getProperty("channelCreditMarketOn"));
        channelGrowthOn = Boolean.parseBoolean(prop.getProperty("channelGrowthOn"));
        channelSimulationWithCapitalOnly = Boolean.parseBoolean(prop.getProperty("channelSimulationWithCapitalOnly"));
    }

    public static void deactivateChannels() {


        if (channelConstructionSectorOn == false) {
            Model.constructionAreaNeedRatio = 0.00000002;
            Model.maxNFlatsToBuildInBucketToAverageNewlyBuiltDemandRatio = 0;
            Model.newlyBuiltUtilityAdjusterCoeff1 = -1000000;
            Model.newlyBuiltUtilityAdjusterCoeff2 = 0;
        }

        if (channelRenovationOn == false) {
            mayRenovateWhenBuyingRatio = 0.0;
            Model.stateDepreciation = 0.00000000000004;
            Model.nPeriodsInHighQualityBucket = 240000000;
            Model.renovationProbability = 0.0;
        }

        if (channelRentalMarketOn == false) {
            rentalProbabilityForMinAge = 0.0;
            rentalProbabilityForMaxAge = 0.0;

            Model.maxHouseholdInvestmentProbability = 0.0;
            Model.monthlyInvestmentRatioConstantCentralInvestor = 0.0;
            Model.monthlyInvestmentRatioCoeffExpectedReturnCentralInvestor = 0.0;
            Model.yearlyRentSaleProbabilityAtZeroExpectedReturnSpread = 0;
        }

        if (channelRentalMarketOn == false && channelBtlInvestmentOn == true) {
            OwnFunctions.pauseHere("You cannot have the rental market turned off and btlInvestment turned on at the same time.");
        }

        if (channelBtlInvestmentOn == false) {
            Model.maxHouseholdInvestmentProbability = 0.0;
            Model.monthlyInvestmentRatioConstantCentralInvestor = 0.0;
            Model.monthlyInvestmentRatioCoeffExpectedReturnCentralInvestor = 0.0;
            Model.yearlyRentSaleProbabilityAtZeroExpectedReturnSpread = 0;
        }

        if (channelCreditMarketOn == false) {
            depositToLifeTimeIncomeRatioConstant = 100000000000000.0;
        }

        if (channelGrowthOn == false) {
            double realGDPInitialValue = Double.parseDouble(prop.getProperty("realGDPInitialValue"));
            realGDPMonthlyGrowthRate = 0;
            Model.realGDPLevelPath = derivePathWithFixedGrowthRate(realGDPInitialValue, realGDPMonthlyGrowthRate, Model.maxNPeriods);
            double priceLevelInitialValue = Double.parseDouble(prop.getProperty("priceLevelInitialValue"));
            double priceLevelMonthlyGrowthRate = 0;
            Model.priceLevelPath = derivePathWithFixedGrowthRate(priceLevelInitialValue, priceLevelMonthlyGrowthRate, Model.nPeriods);
        }

        if (channelSimulationWithCapitalOnly) {
            Model.singleGeoLocationId = Model.capitalId;
        }

    }

    public static void setup() {
        try {
            FileReader fileReader = new FileReader(configFileName);
            prop = new Properties();
            prop.load(fileReader);
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadChannelBooleans();
        loadConfigurationParameters();
        if (Model.parametersToOverride != null) Model.parametersToOverride.overrideParameters();
        calculateModelParametersFromConfigurationParameters();
        if (Model.parametersToOverride != null) Model.parametersToOverride.overrideParameters();
        loadConfigurationParametersForInputGeneration();
        if (Model.parametersToOverride != null) Model.parametersToOverride.overrideParameters();
        deactivateChannels();
        Model.setSeeds();
        Model.setPeriodicalValues();

        createBuckets();
        Model.createInvestor();
        Model.createConstructor();
        Model.createBank();

        createIndividuals();
        assignTypeAndFirstWageToIndividuals();
        matchPairsAndMakeHouseholdsForFemaleAdults();
        assignHouseholdToNonMarriedIndividuals();
        setPreferredGeoLocationForHouseholds();

        assignWorkExperienceAndLifeTimeIncomeEarnedToIndividuals();
        assignFlatsToHouseholds();
        createHomeFlats();

        switchHomesToRent();
        generateOtherStateVariablesForIndividuals();
        generateOtherStateVariablesForHouseholds();

        setNFlatsInBuckets();
        createFlatsForRentalMarketBuffer();
        setExternalDemand();
        createFlatsForExternalDemand();
        setBucketHistVariables();
        setNeighbourhoodRentMarkups();
        setRents();
        setNeighbourhoodHistReturn();

        createFlatsForSale();
        setHouseholdDeposits();

        setBankParameters();

        initializeContructionSector();

        calculateHistInvestmentValues();
        calculateHistPriceIndices();
        calculateLastMarketPrices();
        //createForSaleAndFlatSaleRecords();
        setHistRenovationQuantities();

    }

    public static void createBuckets() {

        if (channelSimulationWithCapitalOnly) {
            ArrayList<Double> neighbourhoodQualitiesSingleGeolocationArrayList = new ArrayList<>();
            for (int i = 0; i < neighbourhoodQualities.length / 2; i++) {
                if (neighbourhoodQualities[i] == Model.capitalId) {
                    neighbourhoodQualitiesSingleGeolocationArrayList.add(neighbourhoodQualities[i]);
                    neighbourhoodQualitiesSingleGeolocationArrayList.add(neighbourhoodQualities[i + 1]);
                }
            }
            double[] neighbourhoodQualitiesSingleGeolocation = new double[neighbourhoodQualitiesSingleGeolocationArrayList.size()];
            for (int i = 0; i < neighbourhoodQualitiesSingleGeolocationArrayList.size(); i++) {
                neighbourhoodQualitiesSingleGeolocation[i] = neighbourhoodQualitiesSingleGeolocationArrayList.get(i);
            }
            neighbourhoodQualities = neighbourhoodQualitiesSingleGeolocation;
        }

        for (int i = 0; i < neighbourhoodQualities.length; i += 2) {
            int geoLocationId = (int) neighbourhoodQualities[i];
            double neighbourhoodQuality = (int) neighbourhoodQualities[i + 1];

            GeoLocation geoLocation = Model.geoLocations.get(geoLocationId);
            if (geoLocation == null) {
                geoLocation = Model.createGeoLocation(geoLocationId);
                geoLocation.calculateAndSetCyclicalAdjuster();
            }

            Neighbourhood neighbourhood = Model.createNeighbourhood();
            geoLocation.getNeighbourhoods().add(neighbourhood);
            neighbourhood.setGeoLocation(geoLocation);
            neighbourhood.setQuality(neighbourhoodQuality);

            PriceRegressionFunctionLinear priceRegressionFunction = Model.createPriceRegressionFunctionLinear();
            priceRegressionFunction.constant = priceRegressionFunction_constant;
            priceRegressionFunction.coeffSize = priceRegressionFunction_coeffSize0 + priceRegressionFunction_coeffSizeQualityCoeff * neighbourhood.quality;
            priceRegressionFunction.coeffSize2 = priceRegressionFunction_coeffSize2;
            priceRegressionFunction.coeffSizeState = priceRegressionFunction_coeffSizeStateMax / bucketStateIntervals[bucketStateIntervals.length - 1];
            neighbourhood.priceRegressionFunctionLinear = priceRegressionFunction;

            for (int k = 0; k < bucketSizeIntervals.length - 1; k++) {
                for (int l = 0; l < bucketStateIntervals.length - 1; l++) {
                    Bucket bucket = Model.createBucket();
                    neighbourhood.getBuckets().add(bucket);
                    bucket.setNeighbourhood(neighbourhood);
                    bucket.setSizeMin(bucketSizeIntervals[k]);
                    bucket.setSizeMax(bucketSizeIntervals[k + 1]);
                    bucket.setStateMin(bucketStateIntervals[l]);
                    bucket.setStateMax(bucketStateIntervals[l + 1]);
                    bucket.setSizeIndex(k + 1);
                    bucket.setStateIndex(l + 1);

                    if (l == bucketStateIntervals.length - 2) {
                        bucket.setHighQuality(true);
                    }

                }
            }

        }
        if (Model.geoLocations.size() == 1) Model.simulationWithSingleGeoLocation = true;
    }

    public static void createIndividuals() {

        for (int i = 0; i < nIndividuals; i++) {
            Individual individual = Model.createIndividual();
            individual.ageInPeriods = Model.rnd.nextInt(Model.lifespan);
            individual.setMale(Model.rnd.nextDouble() < 0.5);
        }

    }

    public static void assignTypeAndFirstWageToIndividuals() {
        MappingWithWeights<Integer> typeProbabilities = new MappingWithWeights<>();
        for (int i = 0; i < typeShare.length; i++) {
            typeProbabilities.put(typeShare[i], i);
        }
        for (Individual individual : Model.individuals.values()) {
            int typeIndex = typeProbabilities.selectObjectAccordingToCumulativeProbability(Model.rnd.nextDouble());
            individual.setTypeIndex(typeIndex);
            individual.firstWage = OwnFunctions.randomNumberFromTruncatedGaussian(firstWage_mean[typeIndex], firstWage_std[typeIndex], firstWage_min[typeIndex], firstWage_max[typeIndex]);
        }
    }

    public static void assignWorkExperienceAndLifeTimeIncomeEarnedToIndividuals() {

        for (Individual individual : Model.individuals.values()) {
            if (individual.getHousehold() == null) continue;
            double lifeTimeIncomeEarned = 0;
            int workExperience = 0;

            for (int i = Model.ageInPeriodsForFirstWorkExperience[individual.getTypeIndex()]; i < Math.min(individual.ageInPeriods, Model.retirementAgeInPeriods); i++) {
                lifeTimeIncomeEarned += Model.wageRatio[individual.getTypeIndex()][workExperience] * individual.getFirstWage() / Math.pow(1 + realGDPMonthlyGrowthRate, Math.min(individual.ageInPeriods, Model.retirementAgeInPeriods) - i - 1);
                workExperience++;
            }

            individual.setWorkExperience(workExperience);
            individual.setLifeTimeIncomeEarned(lifeTimeIncomeEarned);
        }
    }

    public static void matchPairsAndMakeHouseholdsForFemaleAdults() {
        Map<Integer, ArrayList<Individual>> males = new HashMap();
        for (int i = 0; i < Model.lifespan / 12; i++) {
            males.put(i, new ArrayList<>());
        }
        for (Individual individual : Model.individuals.values()) {
            if (individual.isMale()) males.get(individual.calculateAgeInYears()).add(individual);
        }

        for (Individual individual : Model.individuals.values()) {
            if (!individual.isMale() && individual.calculateAgeInYears() > maxAgeInYearsToBeAChild && Model.rnd.nextDouble() < marriageRatio) {

                ArrayList<Individual> sameAgeArrayList = males.get(individual.calculateAgeInYears());
                if (sameAgeArrayList.size() == 0) {
                    continue;
                }

                Individual husband = sameAgeArrayList.get(sameAgeArrayList.size() - 1);
                sameAgeArrayList.remove(sameAgeArrayList.size() - 1);

                Household household = Model.createHousehold();
                individual.setHousehold(household);
                household.members.add(individual);
                husband.setHousehold(household);
                household.members.add(husband);

            }
        }
    }

    public static void assignHouseholdToNonMarriedIndividuals() {

        Map<Integer, ArrayList<Household>> potentialParentHouseholds = new HashMap();
        for (int i = 0; i < Model.lifespan / 12; i++) {
            potentialParentHouseholds.put(i, new ArrayList<>());
        }
        for (Household household : Model.households.values()) {
            if (household.members.size() == 2) {
                potentialParentHouseholds.get(household.calculateAgeInYearsOfOldestMember()).add(household);
            }
        }

        for (Individual individual : Model.individuals.values()) {
            if (individual.getHousehold() != null) continue;
            if (individual.calculateAgeInYears() > maxAgeInYearsToBeAChild) {
                Household household = Model.createHousehold();
                individual.setHousehold(household);
                household.members.add(individual);
            } else {
                int ageDifference = minAgeDifferenceBetweenChildAndParent + Model.rnd.nextInt(maxAgeDifferenceBetweenChildAndParent - minAgeDifferenceBetweenChildAndParent);
                ArrayList<Household> parentHouseholdArrayList = potentialParentHouseholds.get(individual.calculateAgeInYears() + ageDifference + 1);

                Household parentHousehold = parentHouseholdArrayList.get(Model.rnd.nextInt(parentHouseholdArrayList.size()));
                while (parentHousehold.getChildren().size() > Model.maxNChildren) {
                    parentHousehold = parentHouseholdArrayList.get(Model.rnd.nextInt(parentHouseholdArrayList.size()));
                }
                individual.setParentHousehold(parentHousehold);
                for (Individual sibling : parentHousehold.children) {
                    if (sibling != individual) {
                        individual.siblings.add(sibling);
                        sibling.siblings.add(individual);
                    }
                }
                parentHousehold.getChildren().add(individual);
            }
        }

    }

    public static void setPreferredGeoLocationForHouseholds() {
        for (Household household : Model.households.values()) {
            household.setCanChangeGeoLocation(false);
            double randomNumber = Model.rnd.nextDouble();
            int geoLocationId = (int) Math.floor(randomNumber * Model.geoLocations.size());
            household.setPreferredGeoLocation(Model.geoLocations.get(geoLocationId));
            if (channelSimulationWithCapitalOnly)
                household.setPreferredGeoLocation(Model.geoLocations.get(Model.capitalId));
        }
    }

    public static void assignFlatsToHouseholds() {
        calculateLifeTimeIncomeForHouseholds();
        assignUtilityParametersToHouseholds();
        generateFlatsForSaleInArray();
        associationOfHomes();
    }

    public static void calculateLifeTimeIncomeForHouseholds() {
        for (Individual individual : Model.individuals.values()) {
            individual.refreshLabourMarketStatus();
            individual.calculateLifeTimeIncomeEarnedAndLifeTimeIncome();
        }
        for (Household household : Model.households.values()) {
            household.refreshWageIncomeAndPotentialWageIncomeAndPermanentIncomeAndLifeTimeIncome();
            household.calculateMinMaxPrice();
        }
    }

    public static void assignUtilityParametersToHouseholds() {
        for (Household household : Model.households.values()) {
            assignUtilityParametersToHousehold(household);
        }
    }

    public static void assignUtilityParametersToHousehold(Household household) {
        household.utilityFunctionCES = new UtilityFunctionCES();

        household.utilityFunctionCES.household = household;
        double absCoeffSize = OwnFunctions.threadRandomNumberFromTruncatedGaussian(household, utilityFunction_absCoeffSize_mean, utilityFunction_absCoeffSize_std, utilityFunction_absCoeffSize_min, utilityFunction_absCoeffSize_max);
        household.utilityFunctionCES.absCoeffSize = absCoeffSize;
        double absCoeffState = OwnFunctions.threadRandomNumberFromTruncatedGaussian(household, utilityFunction_absCoeffState_mean, utilityFunction_absCoeffState_std, utilityFunction_absCoeffState_min, utilityFunction_absCoeffState_max);
        household.utilityFunctionCES.absCoeffState = absCoeffState;
        double absExpSize = OwnFunctions.threadRandomNumberFromTruncatedGaussian(household, utilityFunction_absExpSize_mean, utilityFunction_absExpSize_std, utilityFunction_absExpSize_min, utilityFunction_absExpSize_max);
        household.utilityFunctionCES.absExponentSize = absExpSize;
        double absExpState = OwnFunctions.threadRandomNumberFromTruncatedGaussian(household, utilityFunction_absExpState_mean, utilityFunction_absExpState_std, utilityFunction_absExpState_min, utilityFunction_absExpState_max);
        household.utilityFunctionCES.absExponentState = absExpState;
        double absSigmoid1 = OwnFunctions.threadRandomNumberFromTruncatedGaussian(household, utilityFunction_sigmoid1_mean, utilityFunction_sigmoid1_std, utilityFunction_sigmoid1_min, utilityFunction_sigmoid1_max);
        household.utilityFunctionCES.absSigmoid1 = absSigmoid1;
        double absSigmoid2 = OwnFunctions.threadRandomNumberFromTruncatedGaussian(household, utilityFunction_sigmoid2_mean, utilityFunction_sigmoid2_std, utilityFunction_sigmoid2_min, utilityFunction_sigmoid2_max);
        household.utilityFunctionCES.absSigmoid2 = absSigmoid2;
    }

    public static void generateFlatsForSaleInArray() {

        int nFlatsInSizeRange = 5;
        int nFlatsInStateRange = 5;
        for (Bucket bucket : Model.buckets.values()) {
            for (int i = 0; i < nFlatsInSizeRange; i++) {
                for (int j = 0; j < nFlatsInStateRange; j++) {
                    Flat flat = new Flat();
                    flat.size = bucket.sizeMin + (bucket.sizeMax - bucket.sizeMin) / nFlatsInSizeRange * i;
                    if (i == 0) flat.size *= 1.0001;
                    flat.state = bucket.stateMin + (bucket.stateMax - bucket.stateMin) / nFlatsInStateRange * j;
                    if (j == 0) flat.state *= 1.0001;
                    flat.bucket = bucket;
                    flat.isForSale = true;
                    flat.forSalePrice = flat.getNeighbourhood().priceRegressionFunctionLinear.flatPrice(flat);
                    flatsForSale.add(flat);
                }
            }
        }

        Collections.sort(flatsForSale, Flat.comparatorForSalePrice);

    }

    public static void associationOfHomes() {

        List<Runnable> tasks = Model.householdsForParallelComputing.stream().map(a -> {
            Runnable task = () -> {
                for (Household household : a.values()) {
                    chooseInitialHomeForHousehold(household);
                }
            };
            return task;
        }).collect(Collectors.toList());
        ParallelComputer.compute(tasks);


    }

    public static void chooseInitialHomeForHousehold(Household household) {

        Flat bestFlat = household.chooseBestFlat(flatsForSale, flatsForSale.size());

        int iter = 0;
        while (bestFlat == null || household.utilityFunctionCES.calculateAbsoluteReservationPriceForFlat(bestFlat) < bestFlat.forSalePrice * minSurplusRatioForBestFlat) {

            iter++;
            assignUtilityParametersToHousehold(household);
            household.utilityFunctionCES.absCoeffSize *= (1 + absCoeffSizeIncreaseInIteration * iter);
            bestFlat = household.chooseBestFlat(flatsForSale, flatsForSale.size());

        }
        household.home = bestFlat;
    }

    public static void createHomeFlats() {
        for (Household household : Model.households.values()) {
            Flat homeFlat = household.home;
            if (homeFlat != null) {
                Flat flat = Model.createFlat();
                flat.size = Math.max(bucketSizeIntervals[0] * 1.0000001, homeFlat.size * (1 - 0 * 0.00003 * (Model.rnd.nextDouble() - 0.5)));
                flat.state = Math.max(bucketStateIntervals[0] * 1.0000001, homeFlat.state * (1 + 0 * 0.001 * (Model.rnd.nextDouble() - 0.5)));
                flat.bucket = homeFlat.bucket;

                flat.ownerHousehold = household;
                household.home = flat;

                flat.lastMarketPrice = flat.getNeighbourhood().priceRegressionFunctionLinear.flatPrice(flat);


            }
        }

        Map<Bucket, Integer> nFlatsInBuckets = new HashMap<>();
        for (Bucket bucket : Model.buckets.values()) {
            nFlatsInBuckets.put(bucket, 0);
        }
        for (Flat flat : Model.flats.values()) {
            OwnFunctions.increaseIntegerMapValue(nFlatsInBuckets, flat.bucket, 1);
        }

    }

    public static void switchHomesToRent() {

        for (Bucket bucket : Model.buckets.values()) {
            nFlatsRentedInBuckets.put(bucket, 0);
        }

        for (Household household : Model.households.values()) {
            if (household.getMembers().get(0).getFirstWage() > minFirstWageForProperty) {
                potentialInvestorHouseholds.add(household);
            }
        }

        Map<Integer, Double> probabilityOfRentAccordingToAge = new HashMap<>();

        for (int i = 0; i < Model.lifespan / 12; i++) {
            if (i < minAgeForRentalProbability || i > maxAgeForRentalProbability) {
                probabilityOfRentAccordingToAge.put(i, 0.0);
            } else {
                double probabilityOfRent = rentalProbabilityForMinAge - (i - minAgeForRentalProbability) * (rentalProbabilityForMinAge - rentalProbabilityForMaxAge) / (double) (maxAgeForRentalProbability - minAgeForRentalProbability);
                probabilityOfRentAccordingToAge.put(i, probabilityOfRent);
            }
        }

        for (Household household : Model.households.values()) {
            if (Model.rnd.nextDouble() < probabilityOfRentAccordingToAge.get((int) Math.floor(household.calculateAgeOfOldestMember() / 12))) {
                Flat flat = household.home;
                flat.setNPeriodsLeftForRent(1 + Model.rnd.nextInt(Model.nMaxPeriodsForRent));

                household.setHome(null);
                household.setRentHome(flat);
                flat.setRenter(household);
                flat.setOwnerHousehold(null);
                flat.setForRent(true);


                findOwnerForFlat(flat);

                OwnFunctions.increaseIntegerMapValue(nFlatsRentedInBuckets, flat.bucket, 1);

            }
        }

    }

    public static void findOwnerForFlat(Flat flat) {
        if (Model.rnd.nextDouble() < investorShare && flat.getLoanContract() == null) {
            flat.setOwnerInvestor(Model.investors.get(0));
            Model.investors.get(0).getProperties().add(flat);
        } else {
            Household household = findHouseholdOwnerForFlat(flat);

            flat.setOwnerHousehold(household);
            household.getProperties().add(flat);

        }

    }

    public static Household findHouseholdOwnerForFlat(Flat flat) {
        return potentialInvestorHouseholds.get(Model.rnd.nextInt(potentialInvestorHouseholds.size()));
    }

    public static void generateOtherStateVariablesForIndividuals() {
        for (Individual individual : Model.individuals.values()) {
            if (individual.getParentHousehold() != null) {
                for (Individual sibling : individual.getParentHousehold().getChildren()) {
                    if (sibling != individual) individual.getSiblings().add(sibling);
                }
            }
            int typeIndex = individual.getTypeIndex();
            if (Model.rnd.nextDouble() < Model.unemploymentRates[typeIndex]) {
                individual.setNPeriodsInUnemployment(1 + Model.rnd.nextInt((int) Math.round(Model.unemploymentRates[typeIndex] / Model.unemploymentProbabilities[typeIndex])));
            }
        }
    }

    public static void generateOtherStateVariablesForHouseholds() {
        for (Household household : Model.households.values()) {

            household.setMinDepositToPotentialWageIncome(minDepositToPotentialWageIncome);
            Flat home = household.home != null ? household.home : household.getRentHome();
            household.setPreferredGeoLocation(home.getGeoLocation());
            household.setMayRenovateWhenBuying(Model.rnd.nextDouble() < mayRenovateWhenBuyingRatio);
            household.setNBirths(household.getChildren().size());
            household.canChangeLocationAccordingToRegionalProbability();

            if (household.home != null) {
                household.setShouldNotRent(true);
                household.setFirstBuyer(false);
                household.setCanChangeGeoLocation(false);
            }

        }
    }

    public static double[] PropertyToDoubleArray(String propertyName) {
        String string = prop.getProperty(propertyName);
        if (string.equals("{}")) return new double[0];
        string = string.replace("{", "");
        string = string.replace("}", "");
        string = string.replace(", ", ",");
        String[] values = string.split(",");
        double[] result = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = Double.parseDouble(values[i]);
        }

        return result;

    }

    public static int[] PropertyToIntArray(String propertyName) {

        String string = prop.getProperty(propertyName);
        if (string.equals("{}")) return new int[0];
        string = string.replace("{", "");
        string = string.replace("}", "");
        string = string.replace(", ", ",");
        String[] values = string.split(",");

        int[] result = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = Integer.parseInt(values[i]);
        }

        return result;

    }

    public static double[][] deriveMonthlyWageRatio() {

        double[][] wageRatio = new double[Model.nTypes][];

        wageRatio[0] = PropertyToDoubleArray("wageRatio0");
        wageRatio[1] = PropertyToDoubleArray("wageRatio1");
        wageRatio[2] = PropertyToDoubleArray("wageRatio2");

        double[][] calculatedWageRatio = new double[Model.nTypes][Model.lifespan];

        for (int i = 0; i < Model.nTypes; i++) {
            for (int j = 0; j < Model.lifespan; j++) {
                if (j < wageRatio[i].length * 12) {
                    calculatedWageRatio[i][j] = wageRatio[i][(int) Math.floor(j / 12.0)];
                } else {
                    calculatedWageRatio[i][j] = calculatedWageRatio[i][j - 1];
                }
            }
        }

        return calculatedWageRatio;

    }

    public static void setNFlatsInBuckets() {
        for (Bucket bucket : Model.buckets.values()) {
            nFlatsInBuckets.put(bucket, 0);
        }
        for (Household household : Model.households.values()) {
            Bucket bucket = null;
            if (household.home != null) {
                bucket = household.home.bucket;
            } else bucket = household.getRentHome().bucket;
            OwnFunctions.increaseIntegerMapValue(nFlatsInBuckets, bucket, 1);
        }
    }

    public static void createFlatsForRentalMarketBuffer() {

        for (Bucket bucket : Model.buckets.values()) {
            int nFlatsRentedInBucket = nFlatsRentedInBuckets.get(bucket);
            double nFlatsToCreate = Math.ceil(nFlatsRentedInBucket * rentalMarketBuffer);
            for (int i = 0; i < nFlatsToCreate; i++) {
                Flat flat = Model.createFlat();
                flat.setBucket(bucket);
                flat.setSize((bucket.getSizeMin() + bucket.getSizeMax()) / 2);
                flat.setState((bucket.getStateMin() + bucket.getStateMax()) / 2);
                flat.setForRent(true);

                findOwnerForFlat(flat);

            }
        }

    }

    public static void setExternalDemand() {
        Map<Double, Neighbourhood> neighbourhoodQualities = new HashMap<>();
        for (Neighbourhood neighbourhood : Model.neighbourhoods.values()) {
            neighbourhoodQualities.put(neighbourhood.quality, neighbourhood);
        }

        double[] externalDemandData = PropertyToDoubleArray("externalDemandData");

        Neighbourhood neighbourhood = neighbourhoodQualities.get(externalDemandData[0]);

        double[] externalDemandShare = createStaticExternalDemandShare(externalDemandData[1]);
        int endPeriod = 0;
        int index = 0;


        for (int i = 0; i < externalDemandData.length; i++) {
            if (externalDemandData[i] == -1) {
                setExternalDemandInBuckets(neighbourhood, externalDemandShare, nFlatsInBuckets);
                index = 0;
            } else {
                index++;
                if (index == 1) {
                    neighbourhood = neighbourhoodQualities.get(externalDemandData[i]);
                } else if (index == 2) {
                    externalDemandShare = createStaticExternalDemandShare(externalDemandData[i]);
                    endPeriod = 0;
                } else if (index % 2 == 0) {
                    double startShare = externalDemandShare[endPeriod];
                    int startPeriod = endPeriod + 1;
                    double endShare = externalDemandData[i];
                    endPeriod = (int) externalDemandData[i - 1];

                    externalDemandShare = updateExternalDemandShare(externalDemandShare, startShare, endShare, startPeriod, endPeriod);

                }

            }
        }
        setExternalDemandInBuckets(neighbourhood, externalDemandShare, nFlatsInBuckets);

        for (Bucket bucket : Model.buckets.values()) {
            int[] externalDemand = new int[Model.nPeriods];
            Model.externalDemand.putIfAbsent(bucket, externalDemand);
        }

    }

    public static void createFlatsForExternalDemand() {


        for (Bucket bucket : Model.buckets.values()) {
            int[] externalDemand = Model.externalDemand.get(bucket);
            double nFlatsToCreate = Math.ceil(externalDemand[0] * (1 + rentalMarketBuffer));
            for (int i = 0; i < nFlatsToCreate; i++) {
                Flat flat = Model.createFlat();
                flat.setBucket(bucket);
                flat.setSize((bucket.getSizeMin() + bucket.getSizeMax()) / 2);
                flat.setState((bucket.getStateMin() + bucket.getStateMax()) / 2);
                flat.setForRent(true);

                findOwnerForFlat(flat);

                if (i < externalDemand[0]) {
                    flat.setRentedByExternal(true);
                    flat.setNPeriodsLeftForRent(1);
                    OwnFunctions.increaseIntegerMapValue(nFlatsRentedInBuckets, flat.bucket, 1);
                }


            }
        }
    }

    static void setBankParameters() {
        for (Bank bank : Model.banks.values()) {
            bank.yearlyInterestRateRegressionConstant = bank_yearlyInterestRateRegressionConstant;
            bank.yearlyInterestRateRegressionCoeffLnWageIncome = bank_yearlyInterestRateRegressionCoeffLnWageIncome;
            bank.yearlyInterestRateRegressionCoeffLTV = bank_yearlyInterestRateRegressionCoeffLTV;
            bank.yearlyInterestRateRegressionCoeffAgeCategory1 = bank_yearlyInterestRateRegressionCoeffAgeCategory1;
            bank.yearlyInterestRateRegressionCoeffAgeCategory2 = bank_yearlyInterestRateRegressionCoeffAgeCategory2;
            bank.bridgeLoanToValue = bank_bridgeLoanToValue;
        }
    }

    static void setBucketHistVariables() {
        for (Bucket bucket : Model.buckets.values()) {
            for (int i = 0; i < Model.nHistoryPeriods; i++) {
                bucket.histNFlatsForRent[i] = (int) (nFlatsRentedInBuckets.get(bucket) * (1 + rentalMarketBuffer));
                bucket.histNFlatsRented[i] = nFlatsRentedInBuckets.get(bucket);
                bucket.histNFictiveRentDemand[i] = nFlatsRentedInBuckets.get(bucket);
                bucket.histNSold[i] = OwnFunctions.doubleToIntegerWithProperProbability(nFlatsInBuckets.get(bucket) * initialYearlySoldRatio / 12);
                bucket.histNForSale[i] = (int) Math.ceil(bucket.histNSold[i] * histBucketMonthlyForSaleToSoldRatio);
                bucket.histNewlyBuiltDemand[i] = OwnFunctions.doubleToIntegerWithProperProbability(bucket.histNSold[i] * newlyBuiltRatio);
            }
        }
    }

    public static void setNeighbourhoodRentMarkups() {
        for (Neighbourhood neighbourhood : Model.neighbourhoods.values()) {
            neighbourhood.calculateAndSetAggregateFlatInfo();
            neighbourhood.calculateAndSetRentMarkup();
        }
    }

    public static void setRents() {
        for (Flat flat : Model.flats.values()) {
            if (flat.isForRent()) flat.calculateAndSetRent();
        }
    }

    public static void setNeighbourhoodHistReturn() {
        for (Neighbourhood neighbourhood : Model.neighbourhoods.values()) {
            neighbourhood.rentIncome = 0;
            neighbourhood.forRentValue = 0;
        }

        for (Flat flat : Model.flats.values()) {
            if (flat.isForRent) {
                flat.bucket.neighbourhood.forRentValue += flat.getEstimatedMarketPrice();
                if (flat.renter != null || flat.rentedByExternal) flat.bucket.neighbourhood.rentIncome += flat.rent;
            }
        }

        for (Neighbourhood neighbourhood : Model.neighbourhoods.values()) {
            for (int i = 0; i < Model.nHistoryPeriods; i++) {
                neighbourhood.histReturn[i] = neighbourhood.rentIncome / neighbourhood.forRentValue;
                if (neighbourhood.forRentValue == 0) neighbourhood.histReturn[i] = 0;
                neighbourhood.histForRentValue[i] = neighbourhood.forRentValue;
            }
        }
    }

    public static void createFlatsForSale() {
        //ALL THESE FLATS ARE INHERITED

        Map<Bucket, Integer> nInheritedFlatsToSellInBucket = new HashMap<>();

        Map<Neighbourhood, ArrayList<Household>> householdsInNeighbourhood = new HashMap<>();
        for (Neighbourhood neighbourhood : Model.neighbourhoods.values()) {
            householdsInNeighbourhood.put(neighbourhood, new ArrayList<>());
        }
        for (Household household : Model.households.values()) {
            if (household.getHome() != null) {
                householdsInNeighbourhood.get(household.getHome().getNeighbourhood()).add(household);
            }
        }

        for (Bucket bucket : Model.buckets.values()) {
            nInheritedFlatsToSellInBucket.put(bucket, (int) (bucket.histNSold[Model.nHistoryPeriods] * bucket.getNFlats() * (histBucketMonthlyForSaleToSoldRatio - 1)));
        }

        for (Bucket bucket : Model.buckets.values()) {
            int nInheritedFlatsToSell = (int) (bucket.histNSold[Model.nHistoryPeriods] * bucket.getNFlats() * (histBucketMonthlyForSaleToSoldRatio - 1));
            for (int i = 0; i < nInheritedFlatsToSell; i++) {

                Flat flat = Model.createFlat();
                flat.size = bucket.sizeMin + Model.rnd.nextDouble() * (bucket.sizeMax - bucket.sizeMin);
                flat.state = bucket.stateMin + Model.rnd.nextDouble() * (bucket.stateMax - bucket.stateMin);
                flat.setBucket(bucket);

                flat.setNForSalePeriods(1 + OwnFunctions.doubleToIntegerWithProperProbability(histBucketMonthlyForSaleToSoldRatio - 1));
                flat.setForSalePrice(flat.getEstimatedMarketPrice());
                flat.setForSale(true);

                ArrayList<Household> householdsToDrawFrom = householdsInNeighbourhood.get(flat.getNeighbourhood());
                Household ownerHousehold = householdsToDrawFrom.get(Model.rnd.nextInt(householdsToDrawFrom.size()));

                flat.setOwnerHousehold(ownerHousehold);
                ownerHousehold.getProperties().add(flat);

            }
        }

    }

    static void setHouseholdDeposits() {
        for (Household household : Model.households.values()) {
            double lifeTimeIncomeEarned = 0;
            double firstWage = 0;
            for (Individual member : household.members) {
                lifeTimeIncomeEarned += member.getLifeTimeIncomeEarned();
                firstWage += member.firstWage;
            }
            double firstWagePerCapita = firstWage / household.members.size();
            double deposit = lifeTimeIncomeEarned * (depositToLifeTimeIncomeRatioConstant + depositToLifeTimeIncomeRatioCoeff * firstWagePerCapita / firstWage_mean[0]);
            if (household.home != null) {
                deposit -= household.home.getEstimatedMarketPrice() * depositDecreaseRatioForHomeValue;
                deposit = Math.max(deposit, firstWage); //homeOwners have at least a one-month-saving
            } else {
                deposit = Math.min(deposit, household.getRentHome().getEstimatedMarketPrice() * maxDepositToHomeValueForRenters);
            }

            household.setDeposit(deposit);
        }
    }

    static void initializeContructionSector() {

        calculateAndSetNeighbourhoodAreas();
        calculateLandPrices();
        setupFlatsReadyAndFlatsUnderConstruction();
        calculateRegionalConstructionAndRenovationCosts();
    }

    public static void calculateAndSetNeighbourhoodAreas() {

        for (Neighbourhood neighbourhood : Model.neighbourhoods.values()) {
            Model.constructors.get(0).getNeighbourhoodArea().put(neighbourhood, 0.0);
        }

        for (Bucket bucket : Model.buckets.values()) {
            int[] histNewlyBuiltDemand = bucket.getHistNewlyBuiltDemand();
            double additionalNeighbourhoodArea = histNewlyBuiltDemand[Model.nHistoryPeriods - 1] * (bucket.getSizeMin() + bucket.getSizeMax()) / 2;
            additionalNeighbourhoodArea *= neighbourhoodAreaToLastNewlyBuiltDemandRatio;

            OwnFunctions.increaseDoubleMapValue(Model.constructors.get(0).getNeighbourhoodArea(), bucket.neighbourhood, additionalNeighbourhoodArea);
        }
    }

    public static void calculateLandPrices() {
        for (Neighbourhood neighbourhood : Model.neighbourhoods.values()) {
            Flat flat = new Flat();
            Bucket bucket = new Bucket();
            for (Bucket bucket1 : neighbourhood.getBuckets()) {
                if (bucket1.sizeMax == bucketSizeIntervals[bucketSizeIntervals.length - 1] && bucket1.stateMin == bucketStateIntervals[0]) {
                    bucket = bucket1;
                }
            }
            flat.bucket = bucket;
            flat.size = bucket.sizeMin;
            flat.state = bucket.stateMax;
            double price = flat.getEstimatedMarketPrice();
            neighbourhood.setLandPrice(price / flat.size);
        }

    }

    public static void setupFlatsReadyAndFlatsUnderConstruction() {
        for (Bucket bucket : Model.buckets.values()) {

            double averageNewlyBuiltDemand = OwnFunctions.average(bucket.histNewlyBuiltDemand,
                    Model.nHistoryPeriods + Model.period - Model.nPeriodsForAverageNewlyBuiltDemand,
                    Model.nHistoryPeriods + Model.period - 1);
            double nFlatsReady = averageNewlyBuiltDemand * Model.nPeriodsForConstruction * Model.targetNewlyBuiltBuffer;
            double nFlatsUnderConstruction = averageNewlyBuiltDemand * (Model.nPeriodsForConstruction - 1);

            if (channelConstructionSectorOn == false) {
                nFlatsReady = 0;
                nFlatsUnderConstruction = 0;
            }

            for (int i = 0; i < Model.nHistoryPeriods; i++) {
                bucket.getNeighbourhood().getGeoLocation().histNNewlyBuiltFlats[i] += nFlatsReady + nFlatsUnderConstruction;
                bucket.getNeighbourhood().getGeoLocation().histNNewlyBuiltFlatsSold[i] += (int) Math.ceil((nFlatsReady + nFlatsUnderConstruction) * soldRatioOfNewlyBuiltFlats);
            }

            for (int i = 0; i <= Math.ceil(nFlatsReady + nFlatsUnderConstruction); i++) {
                Flat flat = Model.createFlat();
                flat.setBucket(bucket);
                flat.setSize(bucket.getSizeMaxForConstructionFlats() - Model.rnd.nextDouble() * (bucket.getSizeMaxForConstructionFlats() - bucket.sizeMin));
                flat.setState(Model.highQualityStateMax);
                flat.setForSale(true);
                flat.setNewlyBuilt(true);
                flat.setOwnerConstructor(Model.constructors.get(0));
                flat.calculateAndSetForSalePrice();

                if (i <= nFlatsUnderConstruction) {
                    flat.setNPeriodsLeftForConstruction(1 + Model.rnd.nextInt(Model.nPeriodsForConstruction));
                    Model.constructors.get(0).getFlatsUnderConstruction().add(flat);
                    OwnFunctions.increaseDoubleMapValue(Model.maxAreaUnderConstructionInGeoLocations, flat.getGeoLocation(), flat.size * maxAreaUnderConstructionRatio);
                } else {
                    flat.setNForSalePeriods(1);
                    Model.constructors.get(0).getFlatsReady().add(flat);
                }

            }

        }
    }

    public static void calculateRegionalConstructionAndRenovationCosts() {
        for (Individual individual : Model.individuals.values()) {
            individual.refreshLabourMarketStatus();
        }
        Model.calculateAverageWageInGeoLocations();

        for (GeoLocation geoLocation : Model.geoLocations.values()) {
            geoLocation.calculateRenovationUnitCostBase();
            geoLocation.calculateConstructionUnitCostBase();
        }

        for (GeoLocation geoLocation : Model.geoLocations.values()) {
            geoLocation.calculateAndSetRenovationUnitCost();
            geoLocation.calculateAndSetConstructionUnitCost();
        }

        for (GeoLocation geoLocation : Model.geoLocations.values()) {
            for (int i = 0; i < Model.nHistoryPeriods; i++) {
                geoLocation.histRenovationUnitCost[i] = geoLocation.getRenovationUnitCost();
                geoLocation.histConstructionUnitCost[i] = geoLocation.getConstructionUnitCost();
            }
        }

    }

    public static void calculateHistInvestmentValues() {
        Map<Neighbourhood, Double> investmentValue = new HashMap<>();
        for (Neighbourhood neighbourhood : Model.neighbourhoods.values()) {
            investmentValue.put(neighbourhood, 0.0);
        }
        for (Flat flat : Model.flats.values()) {
            if (flat.isForRent()) {
                Neighbourhood neighbourhood = flat.getBucket().getNeighbourhood();
                double additionalInvestmentValue = flat.getEstimatedMarketPrice() * monthlyInvestmentRatio;
                OwnFunctions.increaseDoubleMapValue(investmentValue, neighbourhood, additionalInvestmentValue);
            }
        }

        for (Neighbourhood neighbourhood : Model.neighbourhoods.values()) {
            double centralInvestmentValue = investmentValue.get(neighbourhood) * investorShare;
            double householdInvestmentValue = investmentValue.get(neighbourhood) * (1 - investorShare);
            double[] histCentralInvestmentValue = neighbourhood.getHistCentralInvestmentValue();
            double[] histHouseholdInvestmentValue = neighbourhood.getHistHouseholdInvestmentValue();
            for (int i = 0; i < Model.nHistoryPeriods; i++) {
                histCentralInvestmentValue[i] = centralInvestmentValue;
                histHouseholdInvestmentValue[i] = householdInvestmentValue;
            }
        }
    }

    static void calculateHistPriceIndices() {
        double[] histPriceIndex = new double[Model.nHistoryPeriods];
        histPriceIndex[Model.nHistoryPeriods - 1] = 1;
        for (int i = Model.nHistoryPeriods - 2; i >= 0; i--) {
            histPriceIndex[i] = histPriceIndex[i + 1] / Math.pow(1 + yearlyGrowthRateForHistPriceIndices, 1.0 / 12.0);
        }

        for (Neighbourhood neighbourhood : Model.neighbourhoods.values()) {
            for (int i = Model.nHistoryPeriods - 1; i >= 0; i--) {
                neighbourhood.histPriceIndex[i] = histPriceIndex[i];
                neighbourhood.histPriceIndexToBeginning[i] = histPriceIndex[i];
            }
        }
        for (GeoLocation geoLocation : Model.geoLocations.values()) {
            for (int i = Model.nHistoryPeriods - 1; i >= 0; i--) {
                geoLocation.histPriceIndex[i] = histPriceIndex[i];
                geoLocation.histPriceIndexToBeginning[i] = histPriceIndex[i];
            }
        }
    }

    public static void calculateLastMarketPrices() {
        for (Flat flat : Model.flats.values()) {
            flat.lastMarketPrice = flat.getEstimatedMarketPrice();
        }
    }


    static void setHistRenovationQuantities() {
        Map<GeoLocation, Double> renovationQuantity = new HashMap<>();
        for (GeoLocation geoLocation : Model.geoLocations.values()) {
            renovationQuantity.put(geoLocation, 0.0);
        }
        for (Flat flat : Model.flats.values()) {
            if (flat.ownerHousehold != null || flat.getOwnerInvestor() != null) {
                OwnFunctions.increaseDoubleMapValue(renovationQuantity, flat.getGeoLocation(), flat.size * Model.highQualityStateMax * normalYearlyRenovationRatio / 12);
            }
        }

        for (GeoLocation geoLocation : Model.geoLocations.values()) {
            for (int i = 0; i < Model.nHistoryPeriods; i++) {
                geoLocation.histRenovationQuantity[i] = renovationQuantity.get(geoLocation);
            }
        }

    }

    static double[] createStaticExternalDemandShare(double share) {
        double[] externalDemandShare = new double[Model.nPeriods];
        for (int i = 0; i < externalDemandShare.length; i++) {
            externalDemandShare[i] = share;
        }
        return externalDemandShare;
    }

    static double[] updateExternalDemandShare(double[] externalDemandShare, double startShare, double endShare, int startPeriod, int endPeriod) {

        for (int i = startPeriod; i < externalDemandShare.length; i++) {
            if (i <= endPeriod) {
                externalDemandShare[i] = startShare + (endShare - startShare) / (endPeriod - startPeriod + 1) * (i - startPeriod + 1);
            } else externalDemandShare[i] = endShare;
        }
        return externalDemandShare;
    }

    public static double[] derivePathWithFixedGrowthRate(double initialValue, double growthRate, int nPeriods) {

        double[] path = new double[nPeriods];
        path[0] = initialValue;
        for (int i = 1; i < nPeriods; i++) {
            path[i] = path[i - 1] * (1 + growthRate);
        }

        return path;
    }

    public static int[] derivePathWithFixedGrowthRate(int initialValue, double growthRate, int nPeriods) {

        int[] path = new int[nPeriods];
        path[0] = initialValue;
        for (int i = 1; i < nPeriods; i++) {
            path[i] = (int) (path[i - 1] * (1 + growthRate));
        }

        return path;
    }

    public static double[] derivePathWithPoints(double[] points, int nPeriods) {

        double[] path = new double[nPeriods];
        for (int i = 0; i < path.length; i++) {
            path[i] = points[1];
        }
        double[] pointsFull = new double[points.length + 2];
        for (int i = 0; i < points.length; i++) {
            pointsFull[i] = points[i];
        }
        pointsFull[pointsFull.length - 2] = nPeriods - 1;
        pointsFull[pointsFull.length - 1] = points[points.length - 1];

        for (int i = 0; i < pointsFull.length / 2; i++) {
            int exclusiveStartDate = 0;
            double exclusiveStartValue = pointsFull[1];
            if (i > 0) {
                exclusiveStartDate = (int) pointsFull[(i - 1) * 2];
                exclusiveStartValue = pointsFull[(i - 1) * 2 + 1];
            }

            int inclusiveEndDate = (int) pointsFull[i * 2];
            double inclusiveEndValue = pointsFull[i * 2 + 1];

            for (int j = exclusiveStartDate + 1; j <= inclusiveEndDate; j++) {
                path[j] = exclusiveStartValue + (inclusiveEndValue - exclusiveStartValue) / (inclusiveEndDate - exclusiveStartDate) * (j - exclusiveStartDate);
            }
        }

        return path;
    }

    public static double[] deriveShockPath(int shockStartPeriod, int shockEndPeriod, double[] shockPoints) {

        double[] path = new double[Model.maxNPeriods];
        for (int i = 0; i < path.length; i++) {
            path[i] = 1.0;
        }
        double[] shockPointsFull = new double[shockPoints.length + 4];
        shockPointsFull[0] = shockStartPeriod;
        shockPointsFull[1] = 1;
        shockPointsFull[shockPointsFull.length - 2] = shockEndPeriod;
        shockPointsFull[shockPointsFull.length - 1] = 1;
        for (int i = 2; i < shockPointsFull.length - 2; i++) {
            shockPointsFull[i] = shockPoints[i - 2];
        }

        for (int i = 1; i < shockPointsFull.length / 2; i++) {
            int exclusiveStartDate = (int) shockPointsFull[(i - 1) * 2];
            double exclusiveStartValue = shockPointsFull[(i - 1) * 2 + 1];
            int inclusiveEndDate = (int) shockPointsFull[i * 2];
            double inclusiveEndValue = shockPointsFull[i * 2 + 1];

            for (int j = exclusiveStartDate + 1; j <= inclusiveEndDate; j++) {
                path[j] = exclusiveStartValue + (inclusiveEndValue - exclusiveStartValue) / (inclusiveEndDate - exclusiveStartDate) * (j - exclusiveStartDate);
            }
        }

        return path;
    }

    public static void setExternalDemandInBuckets(Neighbourhood neighbourhood, double[] externalDemandShare, Map<Bucket, Integer> nFlatsInBuckets) {

        for (Bucket bucket : neighbourhood.getBuckets()) {
            int[] bucketExternalDemand = new int[externalDemandShare.length];
            int nFlatsInBucket = nFlatsInBuckets.get(bucket);
            for (int j = 0; j < Model.nPeriods; j++) {
                bucketExternalDemand[j] = (int) (externalDemandShare[j] * nFlatsInBucket);
            }
            Model.externalDemand.put(bucket, bucketExternalDemand);
        }
    }

    public static double[][] deriveStaticPathsForTypes(double[] array, int nPeriods) {

        double[][] paths = new double[nPeriods][array.length];
        for (int i = 0; i < nPeriods; i++) {
            for (int j = 0; j < array.length; j++) {
                paths[i][j] = array[j];
            }
        }

        return paths;
    }

}
