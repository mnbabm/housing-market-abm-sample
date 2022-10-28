package model;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parallel.ParallelComputer;
import util.*;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class Model {

    public static final Logger logger = LoggerFactory.getLogger(Model.class);
    public static ParametersToOverride parametersToOverride;
    public static Random rnd = new Random();
    public static Random[] rndArray;
    public static int numberOfRun = 0;
    public static int nThreads = 8;

    public static Map<Integer, Household> miscHH = new HashMap<>();

    public enum Phase {
        SETUP,
        BEGINNINGOFPERIOD,
        INVESTMENTDECISIONS,
        HOUSINGMARKETSUPPLY,
        FICTIVEDEMANDFORRENT,
        FICTIVECHOICEOFHOUSEHOLDS,
        FICTIVEDEMANDFORNEWLYBUILTFLATS_AFTERHOUSEHOLDPURCHASES,
        CONSTRUCTIONPURCHASES,
        INVESTMENTPURCHASES,
        HOUSEHOLDPURCHASES,
        CONSTRUCTFLATS,
        RENOVATEFLATS,
        RENTALMARKET,
        CONSUMEANDSAVE,
        ENDOFPERIOD
    }

    public static Phase phase = Phase.SETUP;
    public static int nIterations;
    public static boolean sampleRun = true;


    //I. Parameters from configuration file

    public static long randomSeed;
    public static int nPeriods;
    public static int maxNPeriods;
    public static int nHistoryPeriods;
    public static boolean simulationWithShock;

    public static boolean simulationWithSingleGeoLocation; //set endogenously
    public static int singleGeoLocationId; //set endogenousnly to capitalId (if needed)

    public static int nTypes;
    public static int lifespan;
    public static int retirementAgeInPeriods;
    public static double[] pensionReplacementRate;
    public static int[] minUnemploymentPeriods;
    public static int maxNChildren;
    public static int minAgeInPeriodsToBuyOrRentAFlatAsSingleHousehold;
    public static int nPeriodsToLookAheadToCalculateLifeTimeIncome;

    public static double savingsRateConstant;
    public static double savingsRateCoeff;
    public static double minConsumptionRate;
    public static double minConsumptionPerCapitaLower;
    public static double minConsumptionPerCapitaUpper;
    public static double minConsumptionPerCapitaLowerThreshold;
    public static double minConsumptionPerCapitaUpperThreshold;
    public static double weightOfAdditionalAdultsInMinConsumption;
    public static double weightOfChildrenInMinConsumption;
    public static int minAgeInPeriodsToCountAsAdditionalAdultInMinConsumnption;

    public static boolean marketPriceOnRegression;

    public static double forcedSaleDiscount;
    public static double realGDPLevelShockTriggerValueForAdditionalForcedSaleDiscount;
    public static double additionalForcedSaleDiscount;
    public static double monthlyForSalePriceDecrease;
    public static double minForSalePriceForRegression;
    public static double minForSalePrice;
    public static double maxSizeValueForSize2;
    public static double minStateValueForSizeState;
    public static double priceRatioMinRatio;
    public static double priceRatioMaxRatio;
    public static int nFlatsToLookAt;
    public static int minNFlatsToLookAtToShiftFlatIndices;
    public static int maxShiftInFlatIndicesInChooseBestFlat;
    public static int maxNChecksFromBucketOfNewlyBuiltBestFlat;
    public static double renovationStateIncreaseWhenBuying;
    public static int maxNIterationsForHouseholdPurchases;
    public static double[] canChangeGeoLocationProbability;
    public static int nFictiveFlatsForSalePerBucket;

    public static double sizeDistanceRatioThresholdForClosestNeighbourCalculation;
    public static double stateDistanceRatioThresholdForClosestNeighbourCalculation;
    public static double sizeWeightInClosestNeighbourCalculation;
    public static double stateWeightInClosestNeighbourCalculation;
    public static double maxSizeDifferenceRatio;
    public static double maxStateDifferenceRatio;

    public static int ageInYearsForMandatoryMoving;
    public static double probabilityOfMandatoryMoving;
    public static double probabilityOfAssessingPotentialNewHomes;
    public static double baseAgeInYearsForProbabilityOfAssessingPotentialNewHomes;
    public static double yearlyDiscountForProbabilityOfAssessingPotentialNewHomes;
    public static double thresholdRatioForMoving;
    public static double thresholdPriceDifferenceForMoving;

    public static int nMonthsToWithdrawHomeFromMarket;
    public static int minNMonthsOfOwnHomeOnMarketToBuyFlat;
    public static int nPeriodsForAverageNForSaleToNSold;
    public static double targetNForSaleToNSold;
    public static double coeffProbabilityNForSaleToNSoldAdjustment;
    public static double minForSaleToNSoldProbabilityAdjustment;
    public static double maxForSaleToNSoldProbabilityAdjustment;

    public static double cyclicalAdjusterPriceIndexCoeff;
    public static int cyclicalAdjusterBasePeriod;
    public static double coeffInReservationPriceAdjusterAccordingToNPeriodsWaitingForFlatToBuy;

    public static double reservationMarkup;
    public static double sellerReservationPriceDecrease;
    public static double adjusterProbabilityOfPlacingBid;
    public static double probabilityOfPlacingBidCoeff;
    public static double newlyBuiltUtilityAdjusterCoeff1;
    public static double newlyBuiltUtilityAdjusterCoeff2;
    public static double reservationPriceShare;
    public static double maxForSaleMultiplier;
    public static double maxIncreaseInReservationPriceAsARatioOfTheSurplusDifferenceToTheFictiveFlat;
    public static double powerInAdjustmentInReservationPrice;
    public static int maxNBidsPlacedPerHousehold;

    public static double minYearlyInterestRate;
    public static double maxYearlyInterestRate;
    public static double minMarketPriceForBridgeLoan;
    public static int bridgeLoanDuration;
    public static int renovationLoanDuration;
    public static double durationIncreaseInIncreaseLoanForRenovation;
    public static int nNonPerformingPeriodsForRestructuring;
    public static int nNonPerformingPeriodsForForcedSale;
    public static int minDurationForRestructuring;
    public static int durationIncreaseInRestructuring;
    public static double incomeRatioForBankFromFlatForSalePrice;
    public static int nPeriodsInNegativeKHR;
    public static double additionalPTI;
    public static double firstBuyerAdditionalLTV;

    public static int nPeriodsForConstruction;
    public static double highQualityStateMin; //derived
    public static double highQualityStateMax; //derived
    public static double sizeMaxOfConstructionFlatsToSizeMinInLargestSizeBucket;
    public static double sizeMaxOfFictiveFlatsToSizeMinInLargestSizeBucket;
    public static double constructionUnitCostBaseRegional;
    public static double constructionAndRenovationUnitCostBaseCapitalMarkup;
    public static double constructionAndRenovationUnitCostBaseAgglomerationMarkup;
    public static double constructionUnitCostAverageWageCoeff;
    public static double constructionMarkupRatio1;
    public static double constructionMarkupRatio1Level;
    public static double constructionMarkupRatio2;
    public static double constructionMarkupRatio2Level;
    public static double maxConstructionPriceToMarketPriceWithProperConstructionCostCoverage;
    public static double constructionCostCoverageNeed;
    public static int nForSalePeriodsToStartAdjustingConstructionForSalePrice;
    public static double parameterForConstructionForSalePriceAdjustment;
    public static double maxFlatStateToLandPriceStateForConstructionPurchase;

    public static int nPeriodsForAverageNewlyBuiltDemand;
    public static double targetNewlyBuiltBuffer;
    public static double maxNFlatsToBuildInBucketToAverageNewlyBuiltDemandRatio;
    public static double maxFlatSizeForLandPrice;
    public static double maxFlatStateForLandPrice;
    public static double constructionAreaNeedRatio;
    public static boolean monthlyConstructionAreaLimit;

    public static double renovationProbability;
    public static double renovationUnitCostBaseRegional;
    public static double renovationUnitCostAverageWageCoeff;
    public static double stateDepreciation;
    public static int nPeriodsInHighQualityBucket;
    public static double renovationCostBuffer;

    public static int capitalId;
    public static int agglomerationId;

    //Price and price index regression
    public static int nPeriodsForFlatSaleRecords;
    public static int nPeriodsForFlatSaleRecordsToUseInPriceRegression;
    public static int nFlatsForPriceRegression;
    public static int nPeriodsUntilCopyingPriceRegressionCoeffients;
    public static double maxChangeInRegressionParameters;
    public static int minNObservationForPriceIndex;
    public static int baseMinPeriodForPriceIndexToBeginning;
    public static int baseMaxPeriodForPriceIndexToBeginning;
    public static boolean size2inPriceIndexRegression;
    public static boolean useTransactionWeightsForPriceIndex;
    public static boolean useForcedSaleForPriceIndex;

    //Rent parameters
    public static double minRentRatio;
    public static double maxRentRatio;
    public static int ageInYearsToConsiderSomebodyOldEnoughToRentForSure;
    public static double coeffOfFirstWageRegardingRentalProbability;
    public static double rentToPrice;
    public static int nMaxPeriodsForRent;
    public static double rentMarkupPower;
    public static double rentMarkupCoeff;
    public static double rentMarkupUtilizationRatioCap;
    public static int nPeriodsForAverageNFictiveRentDemand;
    public static int nPeriodsForAverageReturn;
    public static double expectedReturnCapitalGainCoeff;
    public static int nPeriodsForUtilizationRatio;
    public static double targetUtilizationRatio;
    public static double minUtilizationRatioForTargetUtilizationRatioToUtilizationRatioInRentSaleProbability;
    public static double minTargetRatioForInvestmentProbability;
    public static double stepInSearchingTargetRatioForInvestmentProbability;
    public static double monthlyInvestmentRatioConstantCentralInvestor;
    public static double monthlyInvestmentRatioCoeffExpectedReturnCentralInvestor;
    public static double minDepositToInvest;
    public static double yearlyRentSaleProbabilityAtZeroExpectedReturnSpread;
    public static double minYearlyExpectedReturnSpreadForZeroRentSaleProbability;
    public static double householdInvestmentProbabilityPower;
    public static double householdInvestmentProbabilityCoeff;
    public static double maxHouseholdInvestmentProbability;
    public static double maxPlannedInvestmentValueToAggregateMarketValue;

    //Demographic parameters
    public static int[] ageInPeriodsForFirstWorkExperience;
    public static double inheritorDistanceTypeCoeff;
    public static double inheritorDistanceFirstWageRatioCoeff;
    public static double inheritorDistancePreferredGeoLocationCoeff;

    public static double[] familyBenefit;
    public static double depositInheritanceRatio;

    public static double[][][] birthProbability; //type X lifespan X childrenNum
    public static double[][] marriageProbability; //type X lifespan
    public static double[][][] deathProbability; //gender X type X lifespan


    //II. Derived parameters from configuration file

    public static double[][] wageRatio; //type X lifespan
    public static double[][] unemploymentRatesPath; //nPeriod X nTypes
    public static double[][] unemploymentProbabilitiesPath;  //nPeriod X nTypes

    public static double[] realGDPLevelPath;
    public static double[] priceLevelPath;
    public static double[] yearlyBaseRatePath;
    public static double[] LTVPath;
    public static double[] PTIPath;
    public static int[] maxDurationPath;
    public static double[] taxRatePath;


    public static double[][] unemploymentRatesShockPath; //nPeriod X nTypes
    public static int[][] unemploymentProbabilitiesShockPath;  //nPeriod X nTypes
    public static double[] realGDPLevelShockPath;
    public static double[] priceLevelShockPath;
    public static double[] yearlyBaseRateShockPath;

    //Parameters to set in setup
    public static GeoLocation capital;
    public static GeoLocation agglomeration;
    public static GeoLocation singleGeoLocation;
    public static double maxBucketSize;

    //III. Periodical values
    public static int period = 0;
    public static boolean iterateHouseholdPurchases = true;

    public static double[] unemploymentRates;
    public static double[] unemploymentProbabilities;
    public static double[] unemploymentEndingProbabilities;

    public static double realGDPLevel;
    public static double priceLevel;
    public static double yearlyBaseRate;
    public static double LTV;
    public static double PTI;
    public static int maxDuration;
    public static double taxRate;


    public static double renovationDemand;
    public static double renovationQuantity;

    public static double sumOwnHomePrice;
    public static double sumOwnHomePurchaserFirstWage;


    public static MappingWithWeights<Neighbourhood> neighbourhoodInvestmentProbabilities = new MappingWithWeights<>();


    //IV. State variables (outside of configuration)
    public static Map<Bucket, int[]> externalDemand = new HashMap<>();
    public static double[] histAverageRealPriceToFirstWage;
    public static double[] histNominalGDPLevel;


    //V. Maps for objects with an ID, other maps and lists
    public static Map<Integer, Household> households = new HashMap<Integer, Household>();
    public static Map<Integer, Flat> flats = new HashMap<Integer, Flat>();
    public static Map<Integer, LoanContract> loanContracts = new HashMap<Integer, LoanContract>();
    public static Map<Integer, Individual> individuals = new HashMap<Integer, Individual>();
    public static Map<Integer, GeoLocation> geoLocations = new HashMap<Integer, GeoLocation>();
    public static Map<Integer, Neighbourhood> neighbourhoods = new HashMap<Integer, Neighbourhood>();
    public static Map<Integer, Bucket> buckets = new HashMap<Integer, Bucket>();
    public static Map<Integer, Bank> banks = new HashMap<Integer, Bank>();
    public static Map<Integer, UtilityFunctionCES> utilityFunctionsCES = new HashMap<Integer, UtilityFunctionCES>();
    public static Map<Integer, PriceRegressionFunctionLinear> priceRegressionFunctionsLinear = new HashMap<Integer, PriceRegressionFunctionLinear>();
    public static Map<Integer, Constructor> constructors = new HashMap<Integer, Constructor>();
    public static Map<Integer, FlatSaleRecord> flatSaleRecords = new HashMap<Integer, FlatSaleRecord>();
    public static List<FlatSaleRecord> flatSaleRecordsForPriceIndexToBeginning = new ArrayList<>();
    public static Map<Integer, Investor> investors = new HashMap<Integer, Investor>();

    public static Map<GeoLocation, ArrayList<Flat>> flatsForSaleInGeoLocations = new HashMap<>();
    public static Map<Bucket, ArrayList<Flat>> nonNewlyBuiltFlatsForSaleInBuckets = new HashMap<>();
    public static Map<Neighbourhood, ArrayList<Flat>> flatsForSaleInNeighbourhoodsForConstructionPurchases = new HashMap<>();
    public static Map<Bucket, ArrayList<Flat>> flatsForSaleForInvestment = new HashMap<>();
    public static ArrayList<Flat> fictiveFlatsForSale = new ArrayList<>();
    public static ArrayList<Flat> fictiveNewlyBuiltFlatsForSale = new ArrayList<>();
    public static ArrayList<ForSaleRecord> forSaleRecords = new ArrayList<>();
    public static Map<GeoLocation, ArrayList<Flat>> fictiveFlatsForRentInGeoLocations = new HashMap<>();
    public static Map<GeoLocation, ArrayList<Flat>> flatsForRentInGeoLocations = new HashMap<>();


    public static ArrayList<Map<Integer, Household>> householdsForParallelComputing = new ArrayList<>();
    public static ArrayList<Map<Integer, Flat>> flatsForParallelComputing = new ArrayList<>();
    public static ArrayList<Map<Integer, Individual>> individualsForParallelComputing = new ArrayList<>();
    public static ArrayList<Map<Integer, GeoLocation>> geoLocationsForParallelComputing = new ArrayList<>();
    public static ArrayList<Map<Integer, LoanContract>> loanContractsForParallelComputing = new ArrayList<>();

    public static Map<GeoLocation, ArrayList<Flat>> sampleFlatsForSaleInGeoLocationsForAssessingPotentialNewHomes = new HashMap<>();

    public static Map<Integer, Individual> individualsToRemoveFromMap = new HashMap<>();
    public static ArrayList<Individual> individualsToAddtoMap = new ArrayList<>();
    public static ArrayList<Individual> individualsInArrayListAtBeginningOfPeriod = new ArrayList<>();

    public static ArrayList<Household> householdsInRandomOrder = new ArrayList<>();
    public static ArrayList<Flat> flatsInRandomOrder = new ArrayList<>();

    public static Map<GeoLocation, Double> maxAreaUnderConstructionInGeoLocations = new HashMap<>();

    //VI. Results

    public static double[] nTransactions;
    public static double[] averagePrice;
    public static double[] nForSale;
    public static double[] nNewLoanContracts;
    public static double[] newLoanVolume;
    public static ResultsGUI resultsGUI;


    private Model() {

    }

    public static void sampleSetup() {
        System.out.println();

        for (int i = 0; i < nThreads; i++) {
            householdsForParallelComputing.add(new HashMap<Integer, Household>());
            flatsForParallelComputing.add(new HashMap<Integer, Flat>());
            individualsForParallelComputing.add(new HashMap<Integer, Individual>());
            geoLocationsForParallelComputing.add(new HashMap<Integer, GeoLocation>());
            loanContractsForParallelComputing.add(new HashMap<Integer, LoanContract>());
        }

        SampleInitializer.setup();

        if (unemploymentRates == null) unemploymentRates = new double[nTypes];
        if (unemploymentProbabilities == null) unemploymentProbabilities = new double[nTypes];
        if (unemploymentEndingProbabilities == null) unemploymentEndingProbabilities = new double[nTypes];

        histAverageRealPriceToFirstWage = new double[nHistoryPeriods + nPeriods];
        for (int i = 0; i < nHistoryPeriods; i++) {
            histAverageRealPriceToFirstWage[i] = 1;
        }
        histNominalGDPLevel = new double[nHistoryPeriods + nPeriods];
        for (int i = 0; i < nHistoryPeriods; i++) {
            histNominalGDPLevel[i] = 1;
        }
        setPeriodicalValues();

        generateFictiveFlatsForSale();

        capital = geoLocations.get(capitalId);
        agglomeration = geoLocations.get(agglomerationId);
        singleGeoLocation = geoLocations.get(singleGeoLocationId);
        maxBucketSize = getMaxBucketSize();

        for (Household household : households.values()) {
            household.refreshSize();
        }

        for (GeoLocation geoLocation : geoLocations.values()) {
            flatsForSaleInGeoLocations.put(geoLocation, new ArrayList<Flat>());
            sampleFlatsForSaleInGeoLocationsForAssessingPotentialNewHomes.put(geoLocation, new ArrayList<>());
            flatsForRentInGeoLocations.put(geoLocation, new ArrayList<Flat>());
            fictiveFlatsForRentInGeoLocations.put(geoLocation, new ArrayList<Flat>());
        }
        for (Neighbourhood neighbourhood : Model.neighbourhoods.values()) {
            flatsForSaleInNeighbourhoodsForConstructionPurchases.put(neighbourhood, new ArrayList<Flat>());
        }
        for (Bucket bucket : Model.buckets.values()) {
            nonNewlyBuiltFlatsForSaleInBuckets.put(bucket, new ArrayList<>());
            bucket.createSampleFlat();
        }

        nTransactions = new double[nPeriods];
        averagePrice = new double[nPeriods];
        nForSale = new double[nPeriods];
        nNewLoanContracts = new double[nPeriods];
        newLoanVolume = new double[nPeriods];

        resultsGUI = new ResultsGUI();

    }


    public static void runPeriods() {
        for (int i = Model.period; i < nPeriods; i++) {
            Model.logger.info("PERIOD " + period);
            runPeriod();
            period++;
        }

    }

    public static void runPeriod() {

        beginningOfPeriod();
        investmentDecisions();
        housingMarketSupply();
        fictiveChoiceOfHouseholds();
        constructionPurchases();
        investmentPurchases();
        householdPurchases();
        constructFlats();
        renovateFlats();
        fictiveDemandForRent();
        rentalMarket();
        consumeAndSave();
        endOfPeriod();

    }


    public static void beginningOfPeriod() {

        phase = Phase.BEGINNINGOFPERIOD;

        setPeriodicalValues();
        nullMiscDerivedVariables();

        individualsInArrayListAtBeginningOfPeriod.clear();
        for (Individual individual : Model.individuals.values()) {
            individualsInArrayListAtBeginningOfPeriod.add(individual);
        }


        for (Bank bank : banks.values()) {
            bank.setBankStrategyParameters();
        }

        individualsToRemoveFromMap.clear();

        for (Individual individual : individuals.values()) {
            individual.aging();
        }
        for (Individual individual : individualsToRemoveFromMap.values()) {
            individual.deleteIndividual();
        }

        for (Individual individual : Model.individuals.values()) {
            individual.refreshLabourMarketStatus();
        }


        List<Runnable> tasksToComputeLifeTimeIncomeAndEarned = individualsForParallelComputing.stream().map(a -> {
            Runnable task = () -> {

                for (Individual individual : a.values()) {
                    calculateLifeTimeIncomeEarnedAndLifeTimeIncomeRunnable(individual);
                }

            };
            return task;
        }).collect(Collectors.toList());

        ParallelComputer.compute(tasksToComputeLifeTimeIncomeAndEarned);

        //marriage


        Map<Integer, ArrayList<Individual>> potentialHusbands = new HashMap<>();
        for (int i = 0; i < nTypes; i++) {
            potentialHusbands.put(i, new ArrayList<>());
        }

        for (Individual individual : Model.individuals.values()) {
            if (individual.isMale) {
                if (individual.household != null && individual.household.members.size() > 1) continue;
                int typeIndex = individual.typeIndex;
                double probability = Model.marriageProbability[typeIndex][individual.ageInPeriods];
                if (Model.rnd.nextDouble() < probability || individual.iWantToMarry) {
                    individual.iWantToMarry = true;
                    potentialHusbands.get(typeIndex).add(individual);
                }
            }
        }


        for (Individual individual : Model.individuals.values()) {
            if (!individual.isMale) {
                if (individual.household != null && individual.household.members.size() > 1) continue;
                int typeIndex = individual.typeIndex;
                double probability = Model.marriageProbability[typeIndex][individual.ageInPeriods];

                if (Model.rnd.nextDouble() < probability || individual.iWantToMarry) {
                    if (individual.household == null) individual.formOwnHousehold();
                    individual.iWantToMarry = true;
                    int indexOfHusband = potentialHusbands.get(typeIndex).size() - 1;
                    if (indexOfHusband == -1) continue;
                    Individual husband = potentialHusbands.get(typeIndex).get(indexOfHusband);
                    potentialHusbands.get(typeIndex).remove(indexOfHusband);
                    individual.uniteHouseholds(husband);

                }
            }
        }


        //birth

        individualsToAddtoMap.clear();
        for (Individual individual : individuals.values()) {
            individual.tryToGiveBirth();
        }
        for (Individual individual : individualsToAddtoMap) {
            addIndividualToMap(individual);
        }


        //

        for (GeoLocation geoLocation : Model.geoLocations.values()) {
            geoLocation.calculateAndSetCyclicalAdjuster();
        }

        refreshPriceRegressions();

        for (Neighbourhood neighbourhood : Model.neighbourhoods.values()) {
            neighbourhood.flatSaleRecordsForPrice.clear();
            neighbourhood.forSaleRecordsForPrice.clear();
        }


        for (FlatSaleRecord flatSaleRecord : Model.flatSaleRecords.values()) {
            if (flatSaleRecord.recordEligibleForPrice()) {
                flatSaleRecord.bucket.getNeighbourhood().flatSaleRecordsForPrice.add(flatSaleRecord);
            }
        }
        for (ForSaleRecord forSaleRecord : Model.forSaleRecords) {
            if (forSaleRecord.recordEligibleForPrice()) {
                forSaleRecord.bucket.getNeighbourhood().forSaleRecordsForPrice.add(forSaleRecord);
            }
        }

        for (Flat flat : flats.values()) {
            flat.nullMiscDerivedVariables();
            if (flat.isForSale == false && flat.isForcedSale == false) flat.forSalePrice = 0;

        }


        for (Flat flat : fictiveFlatsForSale) {
            flat.marketPriceCalculated = false;
        }
        for (Flat flat : fictiveNewlyBuiltFlatsForSale) {
            flat.marketPriceCalculated = false;
        }


        List<Runnable> tasksForMarketPrice = flatsForParallelComputing.stream().map(a -> {
            Runnable task = () -> {

                for (Flat flat : a.values()) {
                    calculateAndSetMarketPriceRunnable(flat);
                }

            };
            return task;
        }).collect(Collectors.toList());

        ParallelComputer.compute(tasksForMarketPrice);


        for (Bucket bucket : buckets.values()) {
            bucket.calculateAndSetAverageNForSaleToNSold();
            bucket.calculateAndSetNForSaleToNSoldProbabilityAdjustment();
        }
        generateSampleFlatsForSaleInGeoLocationsForAssessingPotentialNewHomes();

        List<Runnable> tasksToNullAndRefreshSizeAndIncomeLikeThings = householdsForParallelComputing.stream().map(a -> {
            Runnable task = () -> {

                for (Household household : a.values()) {
                    nullMiscVariablesAndRefreshSizeAndIncomeLikeThingsRunnable(household);
                }

            };
            return task;
        }).collect(Collectors.toList());

        ParallelComputer.compute(tasksToNullAndRefreshSizeAndIncomeLikeThings);

        List<Runnable> tasksPriceIncomeToAverageWageIncomeCalculations = geoLocationsForParallelComputing.stream().map(a -> {
            Runnable task = () -> {

                for (GeoLocation geoLocation : a.values()) {
                    priceIncomeToAverageWageIncomeCalculationsRunnable(geoLocation);
                }

            };
            return task;
        }).collect(Collectors.toList());

        ParallelComputer.compute(tasksPriceIncomeToAverageWageIncomeCalculations);


        refreshFictiveFlatsForSale();
        generateFlatsForSaleInGeoLocations();

        List<Runnable> tasksForSomeHouseholdVariablesAndDecisionOnMoving = householdsForParallelComputing.stream().map(a -> {
            Runnable task = () -> {

                ArrayList<Household> parallelHouseholdsInRandomOrder = new ArrayList<>();
                for (Household household : a.values()) {
                    parallelHouseholdsInRandomOrder.add(household);
                }
                Collections.shuffle(parallelHouseholdsInRandomOrder, Model.rndArray[parallelHouseholdsInRandomOrder.get(0).getId() % nThreads]);

                for (Household household : parallelHouseholdsInRandomOrder) {
                    calculateAndSetSomeVariablesAndDecideOnMovingRunnable(household);
                }

            };
            return task;
        }).collect(Collectors.toList());

        ParallelComputer.compute(tasksForSomeHouseholdVariablesAndDecisionOnMoving);

        calculateAverageWageInGeoLocations();
        for (GeoLocation geoLocation : Model.geoLocations.values()) {
            if (geoLocation.histRenovationUnitCost[nHistoryPeriods + period - 1] == 0) {
                for (int i = 0; i < Model.nHistoryPeriods; i++) {
                    geoLocation.histRenovationUnitCost[i] = geoLocation.calculateRenovationUnitCostBase();
                    geoLocation.histConstructionUnitCost[i] = geoLocation.calculateConstructionUnitCostBase();
                }
            }
        }


        for (LoanContract loanContract : loanContracts.values()) {
            loanContract.setIssuedInThisPeriod(false);
        }


        for (Flat flat : flats.values()) {
            flat.stateDepreciation();
            flat.decreasePeriodsLeftForRent();
        }

        for (Neighbourhood neighbourhood : neighbourhoods.values()) {
            neighbourhood.nullMiscDerivedVariables();
            neighbourhood.sortSimilarNeighbourhoods();
            neighbourhood.calculateAndSetAggregateFlatInfo();
            neighbourhood.calculateAndSetExpectedReturnAndSpread();
            neighbourhood.calculateAndSetRentMarkup();
        }

        for (Bucket bucket : buckets.values()) {
            bucket.nullMiscDerivedVariables();
        }
        for (Flat flat : flats.values()) {
            flat.bucket.nFlats++;
        }

        List<Runnable> tasks = flatsForParallelComputing.stream().map(a -> {
            Runnable task = () -> {

                for (Flat flat : a.values()) {
                    calculateAndSetEstimatedMarketPriceRunnable(flat);
                }

            };
            return task;
        }).collect(Collectors.toList());

        ParallelComputer.compute(tasks);

        List<Runnable> tasksForGeoLocations = geoLocations.values().stream().map(a -> {
            Runnable task = () -> {

                a.calculateHousePriceToIncome();

            };
            return task;
        }).collect(Collectors.toList());
        ParallelComputer.compute(tasksForGeoLocations);


        for (GeoLocation geoLocation : Model.geoLocations.values()) {
            geoLocation.calculateAndSetConstructionMarkup();
        }

        householdsInRandomOrder.clear();
        for (Household household : Model.households.values()) {
            householdsInRandomOrder.add(household);
        }
        Collections.shuffle(householdsInRandomOrder, rnd);


    }

    public static void calculateAndSetEstimatedMarketPriceRunnable(Flat flat) {
        flat.calculateAndSetEstimatedMarketPrice();
    }

    public static void calculateAndSetMarketPriceRunnable(Flat flat) {
        flat.calculateAndSetMarketPrice();
    }


    public static void forSaleCalculationsRunnable(Flat flat) {
        flat.forSaleCalculations();
    }


    public static void calculateAndSetSomeVariablesAndDecideOnMovingRunnable(Household household) {
        household.calculateAndSetHasNonPerformingLoan();
        household.calculateVariablesForPurchase();
        household.calculateVariablesForRent();
        household.setMinConsumptionLevel(household.calculateMinConsumptionLevel());
        household.calculateAndSetCanGetBridgeLoanInPeriod();
        household.updateLetThisYoungOverTooYoungAgeRent();
        household.decidesOnMoving();
    }

    public static void nullMiscVariablesAndRefreshSizeAndIncomeLikeThingsRunnable(Household household) {
        household.nullMiscDerivedVariables();
        household.refreshSize();
        household.refreshWageIncomeAndPotentialWageIncomeAndPermanentIncomeAndLifeTimeIncome();
    }

    public static void calculateLifeTimeIncomeEarnedAndLifeTimeIncomeRunnable(Individual individual) {
        individual.calculateLifeTimeIncomeEarnedAndLifeTimeIncome();
    }

    public static void priceIncomeToAverageWageIncomeCalculationsRunnable(GeoLocation geoLocation) {
        geoLocation.calculateAndSetAverageWageIncome();
        geoLocation.calculateAndSetPriceIndexToAverageWageIncome();
    }


    public static void refreshPriceRegressions() {

        for (Bucket bucket : buckets.values()) {
            bucket.flatSaleRecords.clear();
        }

        for (FlatSaleRecord flatSaleRecord : flatSaleRecords.values()) {
            if (flatSaleRecord.isNewlyBuilt == false && flatSaleRecord.getPeriodOfRecord() >= Model.period - Model.nPeriodsForFlatSaleRecordsToUseInPriceRegression) {
                if (flatSaleRecord.size > Model.maxSizeValueForSize2 || flatSaleRecord.state < Model.minStateValueForSizeState)
                    continue;
                flatSaleRecord.getBucket().flatSaleRecords.add(flatSaleRecord);
            }

        }

        for (Neighbourhood neighbourhood : neighbourhoods.values()) {
            neighbourhood.refreshPriceRegression();
        }
        for (Neighbourhood neighbourhood : neighbourhoods.values()) {
            if (period >= Model.nPeriodsUntilCopyingPriceRegressionCoeffients && neighbourhood.priceRegressionRefreshed == false) {
                neighbourhood.getPriceRegressionCoeffOfSimilarNeighbourhoods();
            }
        }

        calculateAndSetAggregateMarketValueForNeighbourhoods();

        buckets.values().forEach(Bucket::calculateSampleFlatPrice);

    }

    private static void investmentDecisions() {
        phase = Phase.INVESTMENTDECISIONS;

        for (Neighbourhood neighbourhood : neighbourhoods.values()) {
            neighbourhood.calculateAndSetPlannedInvestmentValue();
            neighbourhood.calculateAndSetInvestmentProbabilities();
        }

    }

    public static void housingMarketSupply() {
        phase = Phase.HOUSINGMARKETSUPPLY;

        for (Bucket bucket : buckets.values()) {
            bucket.calculateAndSetUtilizationRatio();
            bucket.calculateAndSetRentSaleProbabilities();
        }

        List<Runnable> tasksForSaleCalculations = flatsForParallelComputing.stream().map(a -> {
            Runnable task = () -> {

                for (Flat flat : a.values()) {
                    forSaleCalculationsRunnable(flat);
                }

            };
            return task;
        }).collect(Collectors.toList());

        ParallelComputer.compute(tasksForSaleCalculations);


        forSaleRecords.clear();
        for (Flat flat : flats.values()) {
            if (flat.isForSale || flat.isForcedSale) {

                ForSaleRecord forSaleRecord = new ForSaleRecord();
                forSaleRecord.periodOfRecord = period;
                forSaleRecord.bucket = flat.bucket;
                forSaleRecord.isNewlyBuilt = flat.isNewlyBuilt;
                forSaleRecord.size = flat.size;
                forSaleRecord.state = flat.state;
                forSaleRecord.neighbourhoodQuality = flat.getQuality();
                forSaleRecord.isForcedSale = flat.isForcedSale;
                forSaleRecords.add(forSaleRecord);

                flat.bucket.nForSale++;

            }
        }

    }


    public static void fictiveChoiceOfHouseholds() {


        phase = Phase.FICTIVECHOICEOFHOUSEHOLDS;

        buckets.values().forEach(a -> a.setNewlyBuiltDemand(0));

        refreshFictiveFlatsForSale();
        generateFlatsForSaleInGeoLocations();


        List<Runnable> tasks = householdsForParallelComputing.stream().map(a -> {
            Runnable task = () -> {

                for (Household household : a.values()) {
                    ModelRunnableFunctions.selectWithFictiveSupplyIncrementDemand(household);
                }

            };
            return task;
        }).collect(Collectors.toList());

        ParallelComputer.compute(tasks);


        phase = Phase.FICTIVEDEMANDFORNEWLYBUILTFLATS_AFTERHOUSEHOLDPURCHASES;
    }

    public static void generateFictiveFlatsForSale() {
        fictiveFlatsForSale.clear();
        fictiveNewlyBuiltFlatsForSale.clear();

        for (Bucket bucket : Model.buckets.values()) {


            for (int i = 0; i < Model.nFictiveFlatsForSalePerBucket; i++) {
                Flat flat = new Flat();
                flat.bucket = bucket; //not using setBucket, because that would change nFlats in the given bucket
                flat.setSize(bucket.sizeMin + Model.rnd.nextDouble() * (Math.min(bucket.sizeMax, bucket.getSizeMaxForFictiveFlats()) - bucket.sizeMin));
                flat.setState(bucket.stateMin + Model.rnd.nextDouble() * (bucket.stateMax - bucket.stateMin));
                flat.setNewlyBuilt(false);
                flat.setOwnerInvestor(investors.get(0));
                flat.setForSale(true);
                flat.setNForSalePeriods(1);
                flat.calculateAndSetForSalePrice();
                flat.lastMarketPrice = flat.forSalePrice;
                flat.isFictiveFlatForSale = true;
                flat.lastNaturallyUpdatedMarketPriceOfFictiveFlat = flat.forSalePrice;
                flat.periodOfLastNaturalMarketPriceUpdateOfFictiveFlat = 0;
                fictiveFlatsForSale.add(flat);
            }

            if (bucket.isHighQuality) {

                Flat flat = new Flat();
                flat.bucket = bucket; //not using setBucket, because that would change nFlats in the given bucket
                flat.setSize((bucket.sizeMin + bucket.getSizeMaxForConstructionFlats()) / 2);
                flat.setState(Model.highQualityStateMax);
                flat.setNewlyBuilt(true);
                flat.setOwnerConstructor(constructors.get(0));
                flat.setForSale(true);
                flat.setNForSalePeriods(1);
                flat.calculateAndSetForSalePrice();
                flat.lastMarketPrice = flat.forSalePrice;
                flat.isFictiveFlatForSale = true;
                flat.lastNaturallyUpdatedMarketPriceOfFictiveFlat = flat.forSalePrice;
                flat.periodOfLastNaturalMarketPriceUpdateOfFictiveFlat = 0;
                fictiveFlatsForSale.add(flat);
                fictiveNewlyBuiltFlatsForSale.add(flat);
            }
        }

        Collections.sort(fictiveFlatsForSale, Flat.comparatorForSalePrice);
        Collections.sort(fictiveNewlyBuiltFlatsForSale, Flat.comparatorForSalePrice);

    }

    public static void refreshFictiveFlatsForSale() {

        for (Flat flat : fictiveFlatsForSale) {
            flat.calculateAndSetForSalePrice();
        }
        for (Flat flat : fictiveNewlyBuiltFlatsForSale) {
            flat.calculateAndSetForSalePrice();
        }
        Collections.sort(fictiveFlatsForSale, Flat.comparatorForSalePrice);
        Collections.sort(fictiveNewlyBuiltFlatsForSale, Flat.comparatorForSalePrice);
    }

    public static void constructionPurchases() {

        phase = Phase.CONSTRUCTIONPURCHASES;

        double areaUnderConstruction = 0;
        for (GeoLocation geoLocation : Model.geoLocations.values()) {
            geoLocation.areaUnderConstruction = 0;
            geoLocation.plannedAdditionalAreaUnderConstruction = 0;
        }

        for (Flat flat : constructors.get(0).flatsReady) {
            flat.bucket.nNewlyBuiltFlatsNotYetSold++;
        }
        for (Flat flat : constructors.get(0).flatsUnderConstruction) {
            if (flat.ownerConstructor != null) {
                flat.bucket.nNewlyBuiltFlatsNotYetSold++;
            }

            areaUnderConstruction += flat.size;
            flat.getGeoLocation().areaUnderConstruction += flat.size;
        }


        for (Neighbourhood neighbourhood : flatsForSaleInNeighbourhoodsForConstructionPurchases.keySet()) {
            flatsForSaleInNeighbourhoodsForConstructionPurchases.get(neighbourhood).clear();
        }

        for (Flat flat : Model.flats.values()) {
            if (flat.ownerConstructor == null && flat.nPeriodsLeftForConstruction == 0 && (flat.isForSale || flat.isForcedSale) && flat.state < Model.maxFlatStateToLandPriceStateForConstructionPurchase * Model.maxFlatStateForLandPrice) {
                flatsForSaleInNeighbourhoodsForConstructionPurchases.get(flat.bucket.neighbourhood).add(flat);
            }
        }

        for (Neighbourhood neighbourhood : flatsForSaleInNeighbourhoodsForConstructionPurchases.keySet()) {
            Collections.sort(flatsForSaleInNeighbourhoodsForConstructionPurchases.get(neighbourhood), Flat.comparatorForSaleUnitPrice);
        }

        for (Bucket bucket : Model.buckets.values()) {
            int nFlatsToBuildInNeighbourhoodBucket = constructors.get(0).calculateNFlatsToBuildInBucket(bucket);
            bucket.getGeoLocation().plannedAdditionalAreaUnderConstruction += (bucket.sizeMin + bucket.getSizeMaxForConstructionFlats()) / 2 * nFlatsToBuildInNeighbourhoodBucket;
        }

        for (GeoLocation geoLocation : Model.geoLocations.values()) {
            geoLocation.nFlatsToBuildAdjuster = 1;
            if (monthlyConstructionAreaLimit) {
                if (geoLocation.plannedAdditionalAreaUnderConstruction > maxAreaUnderConstructionInGeoLocations.get(geoLocation) / nPeriodsForConstruction) {
                    double additionalAreaUnderConstructionWhichCanBeUsed = maxAreaUnderConstructionInGeoLocations.get(geoLocation) / nPeriodsForConstruction;
                    geoLocation.nFlatsToBuildAdjuster = additionalAreaUnderConstructionWhichCanBeUsed / geoLocation.plannedAdditionalAreaUnderConstruction;

                }
            } else if (geoLocation.plannedAdditionalAreaUnderConstruction + geoLocation.areaUnderConstruction > maxAreaUnderConstructionInGeoLocations.get(geoLocation)) {
                double additionalAreaUnderConstructionWhichCanBeUsed = maxAreaUnderConstructionInGeoLocations.get(geoLocation) - geoLocation.areaUnderConstruction;
                geoLocation.nFlatsToBuildAdjuster = additionalAreaUnderConstructionWhichCanBeUsed / geoLocation.plannedAdditionalAreaUnderConstruction;
            }
        }


        for (Neighbourhood neighbourhood : Model.neighbourhoods.values()) {

            ArrayList<Flat> flatsForSaleInNeighbourhood = flatsForSaleInNeighbourhoodsForConstructionPurchases.get(neighbourhood);

            Map<Bucket, Integer> nFlatsToBuildInNeighbourhoodBuckets = new HashMap<>();
            for (Bucket bucket : neighbourhood.getBuckets()) {
                int nFlatsToBuildInNeighbourhoodBucket = (int) Math.ceil(neighbourhood.getGeoLocation().nFlatsToBuildAdjuster * constructors.get(0).calculateNFlatsToBuildInBucket(bucket));
                nFlatsToBuildInNeighbourhoodBuckets.put(bucket, nFlatsToBuildInNeighbourhoodBucket);
            }


            while (Collections.max(nFlatsToBuildInNeighbourhoodBuckets.values()) > 0) {

                int maxValue = Collections.max(nFlatsToBuildInNeighbourhoodBuckets.values());
                Bucket bucketToBuildIn = null;
                for (Bucket bucket : nFlatsToBuildInNeighbourhoodBuckets.keySet()) {
                    if (nFlatsToBuildInNeighbourhoodBuckets.get(bucket) == maxValue) {
                        bucketToBuildIn = bucket;
                        break;
                    }
                }
                double newSize = bucketToBuildIn.getSizeMaxForConstructionFlats() - rnd.nextDouble() * (bucketToBuildIn.getSizeMaxForConstructionFlats() - bucketToBuildIn.sizeMin);


                if (flatsForSaleInNeighbourhood.size() == 0 && constructors.get(0).neighbourhoodArea.get(neighbourhood) < newSize)
                    break;

                if (constructors.get(0).neighbourhoodArea.get(neighbourhood) < newSize && flatsForSaleInNeighbourhood.size() > 0) {


                    Flat flatToBuy = flatsForSaleInNeighbourhood.get(0);
                    buyFlat(flatToBuy, constructors.get(0));

                    double newNeighbourhoodArea = constructors.get(0).neighbourhoodArea.get(neighbourhood) + flatToBuy.size;
                    constructors.get(0).neighbourhoodArea.replace(neighbourhood, newNeighbourhoodArea);

                    flatToBuy.deleteFlat();

                }


                if (constructors.get(0).neighbourhoodArea.get(neighbourhood) >= newSize * constructionAreaNeedRatio) {

                    Flat flat = Model.createFlat();
                    flat.size = newSize;
                    areaUnderConstruction += newSize;
                    flat.state = bucketToBuildIn.stateMax;
                    flat.setNewlyBuilt(true);
                    flat.setOwnerConstructor(Model.constructors.get(0));
                    flat.setBucket(bucketToBuildIn);
                    flat.setNPeriodsLeftForConstruction(Model.nPeriodsForConstruction);
                    constructors.get(0).flatsUnderConstruction.add(flat);

                    int newValue = nFlatsToBuildInNeighbourhoodBuckets.get(bucketToBuildIn) - 1;
                    nFlatsToBuildInNeighbourhoodBuckets.replace(bucketToBuildIn, newValue);

                    double newNeighbourhoodArea = constructors.get(0).neighbourhoodArea.get(neighbourhood) - newSize * constructionAreaNeedRatio;
                    constructors.get(0).neighbourhoodArea.replace(neighbourhood, newNeighbourhoodArea);
                }


            }

        }

        Model.investors.get(0).removeSoldFlatsFromProperties();

    }

    public static void investmentPurchases() {
        phase = Phase.INVESTMENTPURCHASES;

        investmentPurchasesScheme();

        Model.investors.get(0).removeSoldFlatsFromProperties();

    }

    public static void householdPurchases() {

        phase = Phase.HOUSEHOLDPURCHASES;

        generateFlatsForSaleInGeoLocations();

        iterateHouseholdPurchases = true;
        nIterations = 0;
        while (iterateHouseholdPurchases && nIterations < maxNIterationsForHouseholdPurchases) {
            nIterations++;

            List<Runnable> tasksFlatAccounting = flatsForParallelComputing.stream().map(a -> {
                Runnable task = () -> {
                    for (Flat flat : a.values()) {
                        ModelRunnableFunctions.flatAccounting(flat);
                    }
                };
                return task;
            }).collect(Collectors.toList());
            ParallelComputer.compute(tasksFlatAccounting);

            //placeBids

            List<Runnable> tasksPlaceBidsOnFlats = householdsForParallelComputing.stream().map(a -> {
                Runnable task = () -> {
                    for (Household household : a.values()) {
                        ModelRunnableFunctions.placeBidsOnFlats(household);
                    }
                };
                return task;
            }).collect(Collectors.toList());

            ParallelComputer.compute(tasksPlaceBidsOnFlats);


            List<Runnable> tasksBidSorting = flatsForParallelComputing.stream().map(a -> {
                Runnable task = () -> {
                    for (Flat flat : a.values()) {
                        ModelRunnableFunctions.flatBidSorting(flat);
                    }
                };
                return task;
            }).collect(Collectors.toList());
            ParallelComputer.compute(tasksBidSorting);


            flatsInRandomOrder.clear();
            for (Flat flat : Model.flats.values()) {
                if (flat.isForSale || flat.isForcedSale) {
                    flatsInRandomOrder.add(flat);
                }
            }
            Collections.shuffle(flatsInRandomOrder, rnd);


            for (Flat flat : flatsInRandomOrder) {
                double greatestBid = 0;
                Household greatestBidder = null;
                double secondGreatestBid = 0;
                if (flat.bids.size() > 0) {
                    double baseForSalePrice = flat.forSalePrice;
                    for (Bid bid : flat.bids) {
                        if (bid.household.isMoving) {
                            if (greatestBid == 0) {
                                greatestBid = bid.reservationPrice;
                                greatestBidder = bid.household;
                            } else {
                                secondGreatestBid = bid.reservationPrice;
                                break;
                            }
                        }

                    }

                    if (greatestBid > 0) {

                        if (secondGreatestBid > 0) {
                            flat.forSalePrice = secondGreatestBid;
                            if (Double.isNaN(flat.forSalePrice)) flat.forSalePrice = 0;
                        } else {
                            if (Double.isNaN(flat.forSalePrice)) flat.forSalePrice = 0;
                        }

                        if (Double.isNaN(flat.forSalePrice)) {
                            flat.forSalePrice = 0;
                        }

                        buyFlat(flat, greatestBidder);

                    }

                }
            }


            regenerateFlatsForSaleInGeoLocations();


            iterateHouseholdPurchases = false;
            for (Household household : Model.households.values()) {
                if (household.isMoving) iterateHouseholdPurchases = true;
            }


        }


        for (Household household : Model.households.values()) {
            if (household.isMoving && (household.flatTooExpensiveToBuy == false || household.firstBuyer))
                household.nPeriodsWaitingForFlatToBuy++;
        }


        for (Household household : Model.households.values()) {
            household.bids.clear();
            household.potentialBids.clear();
        }


        Model.investors.get(0).removeSoldFlatsFromProperties();

    }


    public static void generateFlatsForSaleInGeoLocations() {
        for (GeoLocation geoLocation : flatsForSaleInGeoLocations.keySet()) {
            flatsForSaleInGeoLocations.get(geoLocation).clear();
        }

        for (Bucket bucket : nonNewlyBuiltFlatsForSaleInBuckets.keySet()) {
            nonNewlyBuiltFlatsForSaleInBuckets.get(bucket).clear();
        }

        Collection<Flat> flatsToGoThrough;
        if (phase == Phase.FICTIVECHOICEOFHOUSEHOLDS || phase == Phase.BEGINNINGOFPERIOD) {
            flatsToGoThrough = fictiveFlatsForSale;
        } else {
            flatsToGoThrough = Model.flats.values();
        }

        for (Flat flat : flatsToGoThrough) {
            if (flat.isForSale || flat.isForcedSale) {
                GeoLocation geoLocation = flat.getBucket().getNeighbourhood().getGeoLocation();
                flatsForSaleInGeoLocations.get(geoLocation).add(flat);
                if (flat.isNewlyBuilt == false) {
                    Bucket bucket = flat.getBucket();
                    nonNewlyBuiltFlatsForSaleInBuckets.get(bucket).add(flat);
                }
            }
        }

        for (GeoLocation geoLocation : flatsForSaleInGeoLocations.keySet()) {
            Collections.sort(flatsForSaleInGeoLocations.get(geoLocation), Flat.comparatorForSalePrice);
        }
        for (Bucket bucket : nonNewlyBuiltFlatsForSaleInBuckets.keySet()) {
            Collections.sort(nonNewlyBuiltFlatsForSaleInBuckets.get(bucket), Flat.comparatorForSalePrice);
        }


    }

    public static void regenerateFlatsForSaleInGeoLocations() {

        for (GeoLocation geoLocation : flatsForSaleInGeoLocations.keySet()) {
            ArrayList<Flat> formerFlatsForSale = flatsForSaleInGeoLocations.get(geoLocation);
            ArrayList<Flat> newFlatsForSale = new ArrayList<>();
            for (Flat flat : formerFlatsForSale) {
                if (flat.isForSale || flat.isForcedSale) {
                    newFlatsForSale.add(flat);
                }
            }
            flatsForSaleInGeoLocations.replace(geoLocation, newFlatsForSale);
        }
    }

    public static void generateSampleFlatsForSaleInGeoLocationsForAssessingPotentialNewHomes() {
        for (GeoLocation geoLocation : sampleFlatsForSaleInGeoLocationsForAssessingPotentialNewHomes.keySet()) {
            sampleFlatsForSaleInGeoLocationsForAssessingPotentialNewHomes.get(geoLocation).clear();
        }

        int nFlatsInBucket = 1;
        if (phase == Phase.SETUP) nFlatsInBucket = 1;
        for (Bucket bucket : Model.buckets.values()) {
            for (int i = 0; i < nFlatsInBucket; i++) {
                Flat flat = new Flat();
                flat.bucket = bucket; //not using setBucket, because that would change nFlats in the given bucket
                flat.setSize((bucket.sizeMin + bucket.getSizeMaxForConstructionFlats()) / 2);
                flat.setState((bucket.stateMin + bucket.stateMax) / 2);
                if (i > 0) {
                    flat.setSize(bucket.sizeMin + rnd.nextDouble() * (bucket.sizeMax - bucket.sizeMin));
                    flat.setState(bucket.stateMin + rnd.nextDouble() * (bucket.stateMax - bucket.stateMin));
                }
                flat.setForSalePrice(flat.calculateMarketPrice());
                sampleFlatsForSaleInGeoLocationsForAssessingPotentialNewHomes.get(flat.bucket.neighbourhood.geoLocation).add(flat);
            }


            if (bucket.isHighQuality && phase != Phase.SETUP) {
                Flat newlyBuiltFlat = new Flat();
                newlyBuiltFlat.bucket = bucket; //not using setBucket, because that would change nFlats in the given bucket
                newlyBuiltFlat.setSize((bucket.sizeMin + bucket.getSizeMaxForConstructionFlats()) / 2);
                newlyBuiltFlat.setState(Model.highQualityStateMax);
                newlyBuiltFlat.ownerConstructor = Model.constructors.get(0);
                newlyBuiltFlat.setNewlyBuilt(true);
                newlyBuiltFlat.calculateAndSetForSalePrice();
                sampleFlatsForSaleInGeoLocationsForAssessingPotentialNewHomes.get(bucket.neighbourhood.geoLocation).add(newlyBuiltFlat);
            }
        }


        for (GeoLocation geoLocation : sampleFlatsForSaleInGeoLocationsForAssessingPotentialNewHomes.keySet()) {
            Collections.sort(sampleFlatsForSaleInGeoLocationsForAssessingPotentialNewHomes.get(geoLocation), Flat.comparatorForSalePrice);
        }
    }


    public static void constructFlats() {
        phase = Phase.CONSTRUCTFLATS;

        List<Flat> newFlatsUnderConstruction = new ArrayList<>();

        for (Flat flat : constructors.get(0).flatsUnderConstruction) {

            flat.nPeriodsLeftForConstruction--;
            if (flat.nPeriodsLeftForConstruction == 0) {
                if (flat.ownerConstructor != null)
                    constructors.get(0).flatsReady.add(flat);
                if (flat.ownerHousehold != null) {
                    Household household = flat.ownerHousehold;

                    if (flat == household.homeUnderConstruction) {
                        if (household.home != null) {
                            household.properties.add(household.home);
                            household.home.setForSale(true);
                        }
                        household.homeUnderConstruction = null;
                        household.home = flat;
                        household.setShouldNotRent(true);
                    }

                    if (flat.willBeForRent) {
                        flat.willBeForRent = false;
                        flat.isForRent = true;
                    }

                }
                if (flat.ownerInvestor != null) {
                    flat.willBeForRent = false;
                    flat.isForRent = true;
                }
            } else {
                newFlatsUnderConstruction.add(flat);
            }
        }

        constructors.get(0).flatsUnderConstruction.clear();
        for (Flat flat : newFlatsUnderConstruction) {
            constructors.get(0).flatsUnderConstruction.add(flat);
        }

    }

    public static void renovateFlats() {
        phase = Phase.RENOVATEFLATS;

        for (GeoLocation geoLocation : Model.geoLocations.values()) {
            geoLocation.renovationQuantity = 0;
            geoLocation.renovationUnitCost = geoLocation.histRenovationUnitCost[nHistoryPeriods + period - 1];
        }

        for (Household household : households.values()) {

            household.renovationDemand();

        }


        for (Flat flat : flats.values()) {

            if (flat.isForRent) {
                if (flat.bucket.isHighQuality) continue;
                if (flat.investmentStateIncrease == 0) flat.investmentStateIncrease = stateDepreciation;
                if (flat.state + flat.investmentStateIncrease > flat.bucket.stateMax)
                    flat.investmentStateIncrease = flat.bucket.stateMax - flat.state;
                renovationDemand += flat.investmentStateIncrease * flat.size;
            }
        }

        for (GeoLocation geoLocation : Model.geoLocations.values()) {
            geoLocation.calculateAndSetRenovationUnitCost();
            geoLocation.calculateAndSetConstructionUnitCost();
        }

        for (Household household : households.values()) {
            if (household.homeOptimalRenovation > 0) {
                household.renovateFlat(household.home);
            }
        }

        for (Flat flat : flats.values()) {
            if (flat.investmentStateIncrease > 0) {
                if (flat.ownerHousehold != null) flat.ownerHousehold.renovateFlat(flat);
                if (flat.ownerInvestor != null) {
                    flat.ownerInvestor.renovateFlat(flat);
                }
            }
        }

    }

    public static void fictiveDemandForRent() {
        phase = Phase.FICTIVEDEMANDFORRENT;

        for (Bucket bucket : buckets.values()) {
            int[] externalDemandForBucket = externalDemand.get(bucket);
            bucket.nFictiveRentDemand = externalDemandForBucket[period];
        }

        for (GeoLocation geoLocation : fictiveFlatsForRentInGeoLocations.keySet()) {
            fictiveFlatsForRentInGeoLocations.get(geoLocation).clear();
        }

        for (Bucket bucket : Model.buckets.values()) {
            Flat flat = new Flat();
            flat.bucket = bucket; //not using setBucket, because that would change nFlats in the given bucket
            flat.setSize(bucket.sizeMin + (bucket.getSizeMaxForConstructionFlats() - bucket.sizeMin) / 2);
            flat.setState(bucket.stateMin + (bucket.stateMax - bucket.stateMin) / 2);
            if (bucket.isHighQuality) {
                flat.setNewlyBuilt(true);
                flat.setState(bucket.stateMax);
            }

            flat.calculateAndSetRent();

            GeoLocation geoLocation = flat.getBucket().getNeighbourhood().getGeoLocation();
            fictiveFlatsForRentInGeoLocations.get(geoLocation).add(flat);
        }

        for (GeoLocation geoLocation : geoLocations.values()) {
            Collections.sort(fictiveFlatsForRentInGeoLocations.get(geoLocation), Flat.comparatorRent);
        }


        List<Runnable> tasks = householdsForParallelComputing.stream().map(a -> {
            Runnable task = () -> {

                for (Household household : a.values()) {
                    ModelRunnableFunctions.selectFictiveRentAndIncrementDemand(household);
                }

            };
            return task;
        }).collect(Collectors.toList());

        ParallelComputer.compute(tasks);


    }

    public static void rentalMarket() {
        phase = Phase.RENTALMARKET;

        for (Flat flat : flats.values()) {
            if (flat.isAvailableForRent()) {
                flat.calculateAndSetRent();
            }
        }

        //external demand
        Map<Bucket, ArrayList<Flat>> flatsForExternalRentInBuckets = new HashMap<>();
        for (Bucket bucket : buckets.values()) {
            flatsForExternalRentInBuckets.put(bucket, new ArrayList<Flat>());
        }
        for (Flat flat : flats.values()) {
            if (flat.isAvailableForRent()) {
                flatsForExternalRentInBuckets.get(flat.bucket).add(flat);
            }
        }


        for (Bucket bucket : buckets.values()) {
            int[] externalDemandInBucket = externalDemand.get(bucket);
            ArrayList<Flat> flatsInBucket = flatsForExternalRentInBuckets.get(bucket);
            for (int i = 0; i < externalDemandInBucket[period]; i++) {
                if (flatsInBucket.size() == 0) break;
                int indexOfFlatInList = flatsInBucket.size() - 1;
                Flat flat = flatsInBucket.get(indexOfFlatInList);
                flatsInBucket.remove(indexOfFlatInList);
                flat.nPeriodsLeftForRent = 1;
                flat.rentedByExternal = true;
            }
        }

        //household rents
        for (GeoLocation geoLocation : flatsForRentInGeoLocations.keySet()) {
            flatsForRentInGeoLocations.get(geoLocation).clear();
        }

        for (Flat flat : flats.values()) {
            if (flat.isAvailableForRent()) {
                flat.calculateAndSetRent();
                GeoLocation geoLocation = flat.bucket.neighbourhood.geoLocation;
                flatsForRentInGeoLocations.get(geoLocation).add(flat);
            }
        }

        for (ArrayList<Flat> listOfFlatsInGeoLocation : flatsForRentInGeoLocations.values()) {
            Collections.sort(listOfFlatsInGeoLocation, Flat.comparatorRent);
        }

        for (Household household : householdsInRandomOrder) {

            if (household.mayTryToRent()) {
                Flat flatToRent = household.selectRent();
                if (flatToRent != null) {
                    rentFlat(flatToRent, household);
                }
            }
        }

    }

    public static void consumeAndSave() {
        phase = Phase.CONSUMEANDSAVE;

        for (LoanContract loanContract : Model.loanContracts.values()) {
            if (loanContract.payment > loanContract.principal * (1 + loanContract.monthlyInterestRate)) {
                loanContract.payment = loanContract.principal * (1 + loanContract.monthlyInterestRate);
                loanContract.duration = 1;
            } else if (loanContract.duration == 1 && loanContract.payment < loanContract.principal * (1 + loanContract.monthlyInterestRate)) {
                loanContract.duration++;
            }
        }

        for (Household household : households.values()) {
            household.getPaid();
        }

        for (Household household : households.values()) {
            household.payRent();
        }

        for (Flat flat : flats.values()) {
            if (flat.rentedByExternal && flat.ownerHousehold != null) flat.ownerHousehold.creditDeposit(flat.rent);
        }

        for (Household household : households.values()) {
            household.consumeAndSave();
        }

    }


    public static void endOfPeriod() {
        phase = Phase.ENDOFPERIOD;

        calculateLandPrices();

        //History

        for (Bucket bucket : buckets.values()) {
            bucket.nFlatsForRent = 0;
            bucket.nFlatsRented = 0;
        }

        for (Flat flat : flats.values()) {
            if (flat.isForRent) flat.bucket.nFlatsForRent++;
            if (flat.renter != null || flat.rentedByExternal) flat.bucket.nFlatsRented++;
            if (flat.ownerConstructor != null || flat.nPeriodsLeftForConstruction > 0 && flat.nPeriodsLeftForConstruction < Model.nPeriodsForConstruction - 1) {
                flat.getGeoLocation().histNNewlyBuiltFlats[nHistoryPeriods + period]++;
            }
            if (flat.nPeriodsLeftForConstruction > 0 && flat.ownerConstructor == null) {
                flat.getGeoLocation().histNNewlyBuiltFlatsSold[nHistoryPeriods + period]++;
            }
        }

        for (Bucket bucket : buckets.values()) {
            bucket.histNewlyBuiltDemand[nHistoryPeriods + period] = bucket.newlyBuiltDemand;
            bucket.histNFlatsForRent[nHistoryPeriods + period] = bucket.nFlatsForRent;
            bucket.histNFlatsRented[nHistoryPeriods + period] = bucket.nFlatsRented;
            bucket.histNFictiveRentDemand[nHistoryPeriods + period] = bucket.nFictiveRentDemand;
            bucket.histNForSale[nHistoryPeriods + period] = bucket.nForSale;
            bucket.histNSold[nHistoryPeriods + period] = bucket.nSold;
        }

        for (Flat flat : Model.flats.values()) {
            if (flat.isForRent) {
                flat.bucket.neighbourhood.forRentValue += flat.getMarketPrice();
                if (flat.renter != null || flat.rentedByExternal) flat.bucket.neighbourhood.rentIncome += flat.rent;
            }
        }

        calculatePriceIndicesForNeighbourhoods();

        for (Neighbourhood neighbourhood : neighbourhoods.values()) {
            neighbourhood.histPriceIndex[nHistoryPeriods + period] = neighbourhood.priceIndex;
            neighbourhood.histPriceIndexToBeginning[nHistoryPeriods + period] = neighbourhood.priceIndexToBeginning;
            neighbourhood.histReturn[nHistoryPeriods + period] = neighbourhood.rentIncome / neighbourhood.forRentValue;
            if (neighbourhood.forRentValue == 0)
                neighbourhood.histReturn[nHistoryPeriods + period] = neighbourhood.histReturn[nHistoryPeriods + period - 1];
        }


        for (Flat flat : Model.flats.values()) {
            flat.bucket.neighbourhood.nFlats++;
        }
        for (Neighbourhood neighbourhood : neighbourhoods.values()) {
            neighbourhood.histForRentValue[nHistoryPeriods + period] = neighbourhood.forRentValue;
        }
        for (GeoLocation geoLocation : Model.geoLocations.values()) {
            geoLocation.calculateAndSetPriceIndex();
            geoLocation.calculateAndSetPriceIndexToBeginning();
            geoLocation.histPriceIndex[nHistoryPeriods + period] = geoLocation.priceIndex;
            geoLocation.histPriceIndexToBeginning[nHistoryPeriods + period] = geoLocation.priceIndexToBeginning;
            if (geoLocation.priceIndexToBeginning == 0 || Double.isNaN(geoLocation.priceIndexToBeginning))
                geoLocation.histPriceIndexToBeginning[nHistoryPeriods + period] = geoLocation.histPriceIndexToBeginning[nHistoryPeriods + period - 1];
            geoLocation.histRenovationUnitCost[nHistoryPeriods + period] = geoLocation.renovationUnitCost;
            geoLocation.histConstructionUnitCost[nHistoryPeriods + period] = geoLocation.constructionUnitCost;
            geoLocation.histRenovationQuantity[nHistoryPeriods + period] = geoLocation.renovationQuantity;
        }


        if (period == baseMaxPeriodForPriceIndexToBeginning) {
            for (FlatSaleRecord flatSaleRecord : Model.flatSaleRecords.values()) {
                if (flatSaleRecord.periodOfRecord >= baseMinPeriodForPriceIndexToBeginning && flatSaleRecord.periodOfRecord <= baseMaxPeriodForPriceIndexToBeginning) {
                    FlatSaleRecord flatSaleRecordForBeginning = new FlatSaleRecord();
                    flatSaleRecordForBeginning.periodOfRecord = flatSaleRecord.periodOfRecord;
                    flatSaleRecordForBeginning.price = flatSaleRecord.price;
                    flatSaleRecordForBeginning.size = flatSaleRecord.size;
                    flatSaleRecordForBeginning.state = flatSaleRecord.state;
                    flatSaleRecordForBeginning.bucket = flatSaleRecord.bucket;
                    flatSaleRecordsForPriceIndexToBeginning.add(flatSaleRecordForBeginning);
                }
            }
        }

        deleteOutdatedFlatSaleRecords();

        histAverageRealPriceToFirstWage[nHistoryPeriods + period] = sumOwnHomePrice / sumOwnHomePurchaserFirstWage;
        histNominalGDPLevel[nHistoryPeriods + period] = realGDPLevel * priceLevel;


        int nTransactionsInPeriod = 0;
        double sumPriceInPeriod = 0;
        for (FlatSaleRecord flatSaleRecord : flatSaleRecords.values()) {
            if (flatSaleRecord.periodOfRecord == period) {
                nTransactionsInPeriod++;
                sumPriceInPeriod += flatSaleRecord.price;
            }
        }

        int nNewLoanContractsInPeriod = 0;
        int newLoanVolumeInPeriod = 0;
        for (LoanContract loanContract : Model.loanContracts.values()) {
            if (loanContract.isHousingLoan && loanContract.periodOfIssuance==period) {
                nNewLoanContractsInPeriod++;
                newLoanVolumeInPeriod += loanContract.principal;
            }
        }

        nTransactions[period] = nTransactionsInPeriod;
        averagePrice[period] = sumPriceInPeriod / nTransactionsInPeriod;
        nForSale[period] = flatSaleRecords.size();
        nNewLoanContracts[period] = nNewLoanContractsInPeriod;
        newLoanVolume[period] = newLoanVolumeInPeriod;

    }

    public static void investmentPurchasesScheme() {

        refreshFlatsForSaleForInvestment();
        investorInvestmentPurchases();
        householdInvestmentPurchases();

    }

    public static void refreshFlatsForSaleForInvestment() {

        for (Neighbourhood neighbourhood : neighbourhoods.values()) {
            neighbourhood.sizeToFlatMapForInvestment.clear();
            for (Bucket bucket : neighbourhood.buckets) {
                ArrayList<Flat> potentialArrayList = new ArrayList<>();
                neighbourhood.sizeToFlatMapForInvestment.putIfAbsent(bucket.sizeMax, potentialArrayList);
            }
        }

        flatsForSaleForInvestment.clear();

        for (Bucket bucket : Model.buckets.values()) {
            flatsForSaleForInvestment.put(bucket, bucket.neighbourhood.sizeToFlatMapForInvestment.get(bucket.sizeMax));
        }

        for (Flat flat : flats.values()) {
            if ((flat.isForSale || flat.isForcedSale) && flat.forSalePrice < 1.2 * flat.getMarketPrice()) {
                flatsForSaleForInvestment.get(flat.bucket).add(flat);
            }
        }

        if (phase == Phase.FICTIVEDEMANDFORNEWLYBUILTFLATS_AFTERHOUSEHOLDPURCHASES) {

            refreshFictiveFlatsForSale();
            for (Flat flat : fictiveFlatsForSale) {
                if (flat.isForSale || flat.isForcedSale) {
                    flatsForSaleForInvestment.get(flat.bucket).add(flat);
                }
            }

        }

    }

    public static void investorInvestmentPurchases() {
        for (Neighbourhood neighbourhood : neighbourhoods.values()) {
            if (neighbourhood.cumulativeInvestmentProbabilities.getSize() == 0) continue;
            double investmentValue = 0;

            while (investmentValue < neighbourhood.plannedCentralInvestmentValue && neighbourhood.cumulativeInvestmentProbabilities.getSize() > 0) {

                Bucket bucket = neighbourhood.cumulativeInvestmentProbabilities.selectObjectAccordingToCumulativeProbability(rnd.nextDouble());
                Flat flat = getBestInvestmentFlatInBucket(bucket, null);
                if (flat == null) {
                    neighbourhood.cumulativeInvestmentProbabilities.remove(bucket);
                }

                if (flat != null) {
                    if (flat.bucket.isHighQuality == false) {
                        flat.investmentStateIncrease = bucket.stateMax - flat.state;
                    } else flat.investmentStateIncrease = 0;
                    investmentValue += flat.forSalePrice + flat.investmentStateIncrease * flat.size * flat.getGeoLocation().predictedRenovationUnitCost;
                    if (phase == Phase.INVESTMENTPURCHASES) {
                        buyFlat(flat, investors.get(0));
                        neighbourhood.centralInvestmentValue += flat.forSalePrice;
                    } else if (phase == Phase.FICTIVEDEMANDFORNEWLYBUILTFLATS_AFTERHOUSEHOLDPURCHASES) {
                        flat.investmentStateIncrease = 0;
                    }
                }
            }
        }
    }

    public static void householdInvestmentPurchases() {
        neighbourhoodInvestmentProbabilities.clear();
        for (Neighbourhood neighbourhood : neighbourhoods.values()) {
            double weight = neighbourhood.calculateWeightForNeighbourhoodInvestmentProbabilites();
            if (weight > 0) neighbourhoodInvestmentProbabilities.put(weight, neighbourhood);
        }

        for (Household household : households.values()) {
            household.investIfPossible();
        }
    }

    public static void calculateLandPrices() {


        for (Neighbourhood neighbourhood : Model.neighbourhoods.values()) {
            neighbourhood.sumSoldPrice = 0;
            neighbourhood.sumSoldArea = 0;
            neighbourhood.mightNeedOutOfRangeFlatForLandPrice = true;
        }

        for (FlatSaleRecord flatSaleRecord : Model.flatSaleRecords.values()) {
            if (flatSaleRecord.periodOfRecord == period && flatSaleRecord.isNewlyBuilt == false) {
                if (flatSaleRecord.bucket.neighbourhood.mightNeedOutOfRangeFlatForLandPrice == false && (flatSaleRecord.size > maxFlatSizeForLandPrice || flatSaleRecord.state > maxFlatStateForLandPrice))
                    continue;

                if (flatSaleRecord.bucket.neighbourhood.mightNeedOutOfRangeFlatForLandPrice == true && flatSaleRecord.size < maxFlatSizeForLandPrice && flatSaleRecord.state < maxFlatStateForLandPrice) {
                    flatSaleRecord.bucket.neighbourhood.sumSoldPrice = 0;
                    flatSaleRecord.bucket.neighbourhood.sumSoldArea = 0;
                    flatSaleRecord.bucket.neighbourhood.mightNeedOutOfRangeFlatForLandPrice = false;
                }

                flatSaleRecord.bucket.neighbourhood.sumSoldPrice += flatSaleRecord.price;
                flatSaleRecord.bucket.neighbourhood.sumSoldArea += flatSaleRecord.size;

            }
        }

        for (Neighbourhood neighbourhood : Model.neighbourhoods.values()) {
            if (neighbourhood.sumSoldArea > 0) {
                neighbourhood.landPrice = neighbourhood.sumSoldPrice / neighbourhood.sumSoldArea;
            }
        }

    }

    public static void calculatePriceIndicesForNeighbourhoods() {

        for (Neighbourhood neighbourhood : neighbourhoods.values()) {
            neighbourhood.flatSaleRecordsForPriceIndex.clear();
        }

        for (FlatSaleRecord flatSaleRecord : Model.flatSaleRecords.values()) {
            if (useForcedSaleForPriceIndex == false && flatSaleRecord.isForcedSale) continue;
            flatSaleRecord.bucket.neighbourhood.flatSaleRecordsForPriceIndex.add(flatSaleRecord);
        }

        for (Neighbourhood neighbourhood : neighbourhoods.values()) {
            neighbourhood.calculateAndSetPriceIndex();
        }

        for (Neighbourhood neighbourhood : neighbourhoods.values()) {
            if (neighbourhood.priceIndexRefreshed == false) neighbourhood.getPriceIndexOfSimilarNeighbourhoods();
        }

        //ToBeginningPriceIndex
        for (Neighbourhood neighbourhood : neighbourhoods.values()) {
            neighbourhood.flatSaleRecordsForPriceIndexToBeginning.clear();
        }

        for (FlatSaleRecord flatSaleRecord : flatSaleRecordsForPriceIndexToBeginning) {
            flatSaleRecord.bucket.getNeighbourhood().flatSaleRecordsForPriceIndexToBeginning.add(flatSaleRecord);
        }

        for (FlatSaleRecord flatSaleRecord : Model.flatSaleRecords.values()) {
            if (flatSaleRecord.isNewlyBuilt) continue;
            if (useForcedSaleForPriceIndex == false && flatSaleRecord.isForcedSale) continue;
            flatSaleRecord.bucket.neighbourhood.flatSaleRecordsForPriceIndexToBeginning.add(flatSaleRecord);
        }

        for (Neighbourhood neighbourhood : Model.neighbourhoods.values()) {
            neighbourhood.calculateAndSetPriceIndexToBeginning();
        }


    }


    static void deleteOutdatedFlatSaleRecords() {
        Map<Integer, FlatSaleRecord> newFlatSaleRecords = new HashMap<>();
        for (FlatSaleRecord flatSaleRecord : flatSaleRecords.values()) {
            if (flatSaleRecord.periodOfRecord >= period - nPeriodsForFlatSaleRecords + 1) {
                newFlatSaleRecords.put(flatSaleRecord.getId(), flatSaleRecord);
            }
        }

        flatSaleRecords.clear();
        for (FlatSaleRecord flatSaleRecord : newFlatSaleRecords.values()) {
            flatSaleRecords.put(flatSaleRecord.getId(), flatSaleRecord);
        }
    }


    public static void setPeriodicalValues() {
        unemploymentRates = unemploymentRatesPath[period];
        unemploymentProbabilities = unemploymentProbabilitiesPath[period];
        if (phase == Phase.SETUP) unemploymentProbabilities = unemploymentRates;

        realGDPLevel = realGDPLevelPath[period];
        priceLevel = priceLevelPath[period];
        yearlyBaseRate = yearlyBaseRatePath[period];
        LTV = LTVPath[period];
        PTI = PTIPath[period];
        maxDuration = maxDurationPath[period];
        taxRate = taxRatePath[period];


        for (GeoLocation geoLocation : Model.geoLocations.values()) {
            geoLocation.predictedRenovationUnitCost = geoLocation.histRenovationUnitCost[nHistoryPeriods + period - 1];
        }

        if (simulationWithShock) {
            realGDPLevel *= realGDPLevelShockPath[period];
            priceLevel *= priceLevelShockPath[period];
            yearlyBaseRate *= yearlyBaseRateShockPath[period];
            for (int i = 0; i < nTypes; i++) {
                unemploymentRates[i] *= unemploymentRatesShockPath[period][i];
                unemploymentProbabilities[i] *= unemploymentProbabilitiesShockPath[period][i];
            }

        }

        calculateUnemploymentEndingProbabilities();

    }

    public static void nullMiscDerivedVariables() {
        renovationDemand = 0;
        renovationQuantity = 0;
        sumOwnHomePrice = 0;
        sumOwnHomePurchaserFirstWage = 0;
    }

    public static void calculateUnemploymentEndingProbabilities() {
        if (phase != Phase.SETUP) {
            int[] nActive = new int[nTypes];
            int[] nUnemployed = new int[nTypes];
            int[] nUnemployedAboveMinPeriods = new int[nTypes];
            for (Individual individual : Model.individuals.values()) {
                if (individual.workExperience > 0 && individual.ageInPeriods < retirementAgeInPeriods - 1) {
                    nActive[individual.typeIndex]++;
                    if (individual.nPeriodsInUnemployment >= 1) {
                        nUnemployed[individual.typeIndex]++;
                        if (individual.nPeriodsInUnemployment >= minUnemploymentPeriods[individual.typeIndex]) {
                            nUnemployedAboveMinPeriods[individual.typeIndex]++;
                        }
                    }
                }

            }

            for (int i = 0; i < nTypes; i++) {
                unemploymentEndingProbabilities[i] = 0;

                double nUnemployedIfNooneFinishesUnemployment = nUnemployed[i] + unemploymentProbabilities[i] * nActive[i];
                double nUnemployedPeopleWhoShouldLeaveUnemploymentToReachUnemploymentRate = nUnemployedIfNooneFinishesUnemployment - unemploymentRates[i] * nActive[i];
                unemploymentEndingProbabilities[i] = Math.min(1, (nUnemployedPeopleWhoShouldLeaveUnemploymentToReachUnemploymentRate) / (double) (nUnemployedAboveMinPeriods[i]));

            }

        }

    }


    public static void buyFlat(Flat flat, Constructor constructor) {

        ArrayList<Flat> forSaleFlats = Model.flatsForSaleInNeighbourhoodsForConstructionPurchases.get(flat.getBucket().getNeighbourhood());
        forSaleFlats.remove(0);
        payPriceForOwner(flat);
        flat.ownerConstructor = constructor;
        if (flat.isForRent && flat.nPeriodsLeftForRent > 0) {
            flat.renter.rentHome = null;
            flat.renter = null;
        }
        flat.setForRent(false);
        flat.setWillBeForRent(false);
        buyFlatAccounting(flat);
    }

    public static void buyFlat(Flat flat, Investor investor) {
        ArrayList<Flat> forSaleFlats = flatsForSaleForInvestment.get(flat.bucket);
        forSaleFlats.remove(flat);
        payPriceForOwner(flat);
        flat.ownerInvestor = investor;
        investor.properties.add(flat);
        if (flat.nPeriodsLeftForConstruction == 0) {
            flat.isForRent = true;
        } else {
            flat.willBeForRent = true;
        }
        buyFlatAccounting(flat);
    }

    public static void buyFlatForInvestment(Flat flat, Household household) {

        ArrayList<Flat> forSaleFlats = flatsForSaleForInvestment.get(flat.bucket);
        forSaleFlats.remove(flat);
        payPriceForOwner(flat);

        if (household.getDeposit() - household.minDeposit < flat.forSalePrice) {
            household.makeLoanContractForFlat(flat);
        }
        household.chargeDeposit(flat.forSalePrice);


        flat.ownerHousehold = household;
        household.properties.add(flat);
        if (flat.nPeriodsLeftForConstruction == 0) {
            flat.isForRent = true;
        } else {
            flat.willBeForRent = true;
        }
        buyFlatAccounting(flat);

    }

    public static void buyFlat(Flat flat, Household household) { //for own home only

        payPriceForOwner(flat);

        if (household.getDeposit() - household.minDeposit < flat.forSalePrice) {
            household.makeLoanContractForFlat(flat);
        }

        household.chargeDeposit(flat.forSalePrice);

        if (flat.nPeriodsLeftForConstruction == 0) {
            if (household.home != null) {
                household.properties.add(household.home);
            }
            household.home = flat;
            household.setShouldNotRent(true);
        } else {
            household.homeUnderConstruction = flat;
        }


        household.setMoving(false);
        household.setCanBeAskedForFictiveDemandForNewlyBuiltFlats(true);
        household.setNPeriodsWaitingForFlatToBuy(0);

        flat.ownerHousehold = household;
        if (flat.isForRent && flat.nPeriodsLeftForRent > 0) {
            flat.renter.rentHome = null;
            flat.renter = null;
        }
        flat.setForRent(false);
        flat.setWillBeForRent(false);

        buyFlatAccounting(flat);

        household.setFirstBuyer(false);
        household.setCanChangeGeoLocation(false);
        household.preferredGeoLocation = flat.getGeoLocation();

        sumOwnHomePrice += flat.forSalePrice / Model.priceLevel / Model.realGDPLevel;
        sumOwnHomePurchaserFirstWage += household.getSumFirstWage();

    }

    public static void rentFlat(Flat flat, Household household) {
        household.rentHome = flat;
        flat.renter = household;

        flat.nPeriodsLeftForRent = nMaxPeriodsForRent;
        if (flat.isForSale || flat.isForcedSale) flat.nPeriodsLeftForRent = 1;
    }


    public static void payPriceForOwner(Flat flat) {

        if (flat.isForcedSale) {
            if (flat.ownerHousehold != null) {
                flat.ownerHousehold.setShouldNotRent(false);
            }
        }

        if (flat.ownerHousehold != null) {

            if (flat.isForcedSale == false) {
                flat.ownerHousehold.creditDeposit(flat.forSalePrice);
            } else {
                flat.ownerHousehold.creditDeposit(flat.forSalePrice * incomeRatioForBankFromFlatForSalePrice);
            }

            if (flat.loanContract != null) {
                LoanContract loanContract = flat.loanContract;
                if (loanContract.bridgeLoanCollateral == flat) loanContract.endBridgeLoan();
                if (loanContract.collateral == flat) flat.loanContract.endLoanContract();
            }

            if (flat.ownerHousehold.home == flat) {
                flat.ownerHousehold.periodOfTakingUnsoldHomeToMarket = -100;
                flat.ownerHousehold.home = null;
            } else flat.ownerHousehold.properties.remove(flat);

            flat.ownerHousehold = null;

        } else if (flat.ownerConstructor != null) {
            flat.ownerConstructor.flatsReady.remove(flat);
            flat.ownerConstructor = null;
        } else if (flat.ownerInvestor != null) {
            flat.ownerInvestor = null;
        }

    }


    public static void buyFlatAccounting(Flat flat) {

        flat.bucket.nSold++;
        flat.getNeighbourhood().nTransactions++;

        flat.lastMarketPrice = flat.forSalePrice;
        flat.setForSale(false);
        flat.setForcedSale(false);
        flat.setNForSalePeriods(0);


        FlatSaleRecord flatSaleRecord = Model.createFlatSaleRecord();
        flatSaleRecord.setPeriodOfRecord(period);
        flatSaleRecord.setPrice(flat.forSalePrice);
        flatSaleRecord.setBucket(flat.bucket);
        flatSaleRecord.setSize(flat.size);
        flatSaleRecord.setState(flat.state);
        flatSaleRecord.setNeighbourhoodQuality(flat.bucket.neighbourhood.quality);
        flatSaleRecord.setNewlyBuilt(flat.isNewlyBuilt);
        flatSaleRecord.setForcedSale(flat.isForcedSale);
        flatSaleRecord.flatId = flat.id;

        flat.setNewlyBuilt(false);
        flat.boughtNow = true;

    }

    public static Flat getBestInvestmentFlatInBucket(Bucket bucket, Household household) {

        ArrayList<Flat> forSale = flatsForSaleForInvestment.get(bucket);

        Flat bestInvestmentFlat = null;
        double bestUnitPrice = 0;
        for (Flat flat : forSale) {
            if (flat.state > bucket.stateMax) continue;
            if (bestInvestmentFlat == null) {
                bestInvestmentFlat = flat;
                bestUnitPrice = calculateUnitPriceForInvestmentFlat(bucket, flat, household);

            } else {
                double unitPrice = calculateUnitPriceForInvestmentFlat(bucket, flat, household);

                if (unitPrice < bestUnitPrice) {
                    bestInvestmentFlat = flat;
                    bestUnitPrice = unitPrice;
                }
            }
        }
        return bestInvestmentFlat;
    }

    private static double calculateUnitPriceForInvestmentFlat(Bucket bucket, Flat flat, Household household) {
        if (flat.isForcedSale && household != null && household.deposit < household.minDeposit + flat.forSalePrice)
            return household.deposit;
        double unitPrice = flat.forSalePrice / flat.size + (bucket.stateMax - flat.state) * flat.getGeoLocation().predictedRenovationUnitCost * (1 + renovationCostBuffer);
        if (flat.nPeriodsLeftForConstruction > 0)
            unitPrice *= Math.pow(1 + Math.max(0, flat.bucket.neighbourhood.expectedReturn) / 12, flat.nPeriodsLeftForConstruction);
        return unitPrice;
    }

    public static Bank createBank() {
        Bank bank = new Bank();
        addBankToMap(bank);
        return bank;
    }

    private static void addBankToMap(Bank bank) {
        Model.banks.put(bank.getId(), bank);
    }

    public static Bucket createBucket() {
        Bucket bucket = new Bucket();
        addBucketToMap(bucket);
        return bucket;
    }

    private static void addBucketToMap(Bucket bucket) {
        Model.buckets.put(bucket.getId(), bucket);
    }

    public static Flat createFlat() {
        Flat flat = new Flat();
        addFlatToMap(flat);
        return flat;
    }

    private static void addFlatToMap(Flat flat) {
        Model.flats.put(flat.getId(), flat);
        Model.flatsForParallelComputing.get(flat.getId() % nThreads).put(flat.getId(), flat);
    }

    public static FlatSaleRecord createFlatSaleRecord() {
        FlatSaleRecord flatSaleRecord = new FlatSaleRecord();
        addFlatSaleRecordToMap(flatSaleRecord);
        return flatSaleRecord;
    }

    private static void addFlatSaleRecordToMap(FlatSaleRecord flatSaleRecord) {
        Model.flatSaleRecords.put(flatSaleRecord.getId(), flatSaleRecord);
    }

    public static GeoLocation createGeoLocation() {
        GeoLocation geoLocation = new GeoLocation();
        addGeoLocationToMap(geoLocation);
        maxAreaUnderConstructionInGeoLocations.put(geoLocation, Double.POSITIVE_INFINITY);
        return geoLocation;
    }

    public static GeoLocation createGeoLocation(int id) {
        GeoLocation geoLocation = new GeoLocation(id);
        addGeoLocationToMap(geoLocation);
        maxAreaUnderConstructionInGeoLocations.put(geoLocation, Double.POSITIVE_INFINITY);
        return geoLocation;
    }

    private static void addGeoLocationToMap(GeoLocation geoLocation) {
        Model.geoLocations.put(geoLocation.getId(), geoLocation);
        Model.geoLocationsForParallelComputing.get(geoLocation.getId() % nThreads).put(geoLocation.getId(), geoLocation);
    }

    public static Household createHousehold() {
        Household household = new Household();
        addHouseholdToMap(household);
        return household;
    }

    private static void addHouseholdToMap(Household household) {
        Model.households.put(household.getId(), household);
        Model.householdsForParallelComputing.get(household.getId() % nThreads).put(household.getId(), household);
    }

    public static Individual createIndividual() {
        Individual individual = new Individual();
        if (phase != Phase.BEGINNINGOFPERIOD) {
            addIndividualToMap(individual);
        } else {
            individualsToAddtoMap.add(individual);
        }
        return individual;
    }

    private static void addIndividualToMap(Individual individual) {
        Model.individuals.put(individual.getId(), individual);
        Model.individualsForParallelComputing.get(individual.getId() % nThreads).put(individual.getId(), individual);
    }

    public static Investor createInvestor() {
        Investor investor = new Investor();
        addInvestorToMap(investor);
        return investor;
    }

    private static void addInvestorToMap(Investor investor) {
        Model.investors.put(investor.getId(), investor);
    }

    public static LoanContract createLoanContract() {
        LoanContract loanContract = new LoanContract();
        addLoanContractToMap(loanContract);
        return loanContract;
    }

    private static void addLoanContractToMap(LoanContract loanContract) {
        Model.loanContracts.put(loanContract.getId(), loanContract);
        Model.loanContractsForParallelComputing.get(loanContract.getId() % nThreads).put(loanContract.getId(), loanContract);
    }

    public static Neighbourhood createNeighbourhood() {
        Neighbourhood neighbourhood = new Neighbourhood();
        addNeighbourhoodToMap(neighbourhood);
        return neighbourhood;
    }

    private static void addNeighbourhoodToMap(Neighbourhood neighbourhood) {
        Model.neighbourhoods.put(neighbourhood.getId(), neighbourhood);
    }

    public static PriceRegressionFunctionLinear createPriceRegressionFunctionLinear() {
        PriceRegressionFunctionLinear priceRegressionFunctionLinear = new PriceRegressionFunctionLinear();
        addPriceRegressionFunctionLinearToMap(priceRegressionFunctionLinear);
        return priceRegressionFunctionLinear;
    }

    private static void addPriceRegressionFunctionLinearToMap(PriceRegressionFunctionLinear priceRegressionFunctionLinear) {
        Model.priceRegressionFunctionsLinear.put(priceRegressionFunctionLinear.getId(), priceRegressionFunctionLinear);
    }

    public static UtilityFunctionCES createUtilityFunctionCES() {
        UtilityFunctionCES utilityFunctionCES = new UtilityFunctionCES();
        addUtilityFunctionCESToMap(utilityFunctionCES);
        return utilityFunctionCES;
    }

    private static void addUtilityFunctionCESToMap(UtilityFunctionCES utilityFunctionCES) {
        Model.utilityFunctionsCES.put(utilityFunctionCES.getId(), utilityFunctionCES);
    }

    public static Constructor createConstructor() {
        Constructor constructor = new Constructor();
        addConstructorToMap(constructor);
        return constructor;
    }

    private static void addConstructorToMap(Constructor constructor) {
        Model.constructors.put(constructor.getId(), constructor);
    }

    public static double threadNextInt(HasID object, int bound) {
        return rndArray[object.getId() % nThreads].nextInt(bound);
    }

    public static double threadNextDouble(HasID object) {
        return rndArray[object.getId() % nThreads].nextDouble();
    }

    public static double threadNextGaussian(HasID object) {
        return rndArray[object.getId() % nThreads].nextGaussian();
    }

    public static void calculateAndSetAggregateMarketValueForNeighbourhoods() {
        for (Neighbourhood neighbourhood : Model.neighbourhoods.values()) {
            neighbourhood.aggregateEstimatedMarketValue = 0;
        }
        for (Flat flat : Model.flats.values()) {
            if (flat.nPeriodsLeftForConstruction == 0) {
                flat.getNeighbourhood().aggregateEstimatedMarketValue += flat.getEstimatedMarketPrice();
            }
        }
    }

    public static void calculateAverageWageInGeoLocations() {
        for (GeoLocation geoLocation : geoLocations.values()) {
            geoLocation.nFullTimeWorkers = 0;
            geoLocation.sumFullTimeWage = 0;
        }
        for (Individual individual : individuals.values()) {
            if (individual.ageInPeriods < Model.retirementAgeInPeriods && individual.nPeriodsInUnemployment == 0 && individual.wage >= 0.5 * individual.firstWage) {
                Household household = individual.household == null ? individual.parentHousehold : individual.household;
                GeoLocation geoLocation = household.preferredGeoLocation;
                if (household.home != null) {
                    geoLocation = household.home.getGeoLocation();
                } else if (household.rentHome != null) {
                    geoLocation = household.rentHome.getGeoLocation();
                }
                geoLocation.sumFullTimeWage += individual.wage;
                geoLocation.nFullTimeWorkers += 1;
            }
        }
        for (GeoLocation geoLocation : geoLocations.values()) {
            geoLocation.averageWage = geoLocation.sumFullTimeWage / (double) geoLocation.nFullTimeWorkers;
        }
    }

    public static double getMaxBucketSize() {
        double maxBucketSize = 0;
        if (Model.neighbourhoods.get(1) != null) {
            for (Bucket bucket : Model.neighbourhoods.get(1).buckets) {
                if (bucket.sizeMax > maxBucketSize) maxBucketSize = bucket.sizeMax;
            }
        } else {
            for (Bucket bucket : Model.buckets.values()) {
                if (bucket.sizeMax > maxBucketSize) maxBucketSize = bucket.sizeMax;
            }
        }

        return maxBucketSize;
    }

    public static void setSeeds() {
        rnd.setSeed(randomSeed * (numberOfRun + 1));
        rndArray = new Random[nThreads];
        for (int i = 0; i < rndArray.length; i++) {
            rndArray[i] = new Random();
            rndArray[i].setSeed(Model.rnd.nextInt(9999999));
        }
    }

}
