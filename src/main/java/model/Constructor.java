package model;

import lombok.Getter;
import lombok.Setter;
import util.OwnFunctions;

import java.util.*;

@Getter
@Setter
public class Constructor implements HasID {

    public static int nextId;
    final int id;

    //state variables
    Map<Neighbourhood, Double> neighbourhoodArea = new HashMap<>();
    List<Flat> flatsUnderConstruction = new ArrayList<>();
    List<Flat> flatsReady = new ArrayList<>();

    //derived variables
    double renovationUnitCost;
    double constructionUnitCost;

    public Constructor() {
        id = Constructor.nextId;
        Constructor.nextId++;
    }


    public int calculateNFlatsToBuildInBucket(Bucket bucket) {

        if (bucket.isHighQuality) {

            double averageNewlyBuiltDemand = OwnFunctions.average(bucket.histNewlyBuiltDemand,
                    Model.nHistoryPeriods + Model.period - Model.nPeriodsForAverageNewlyBuiltDemand,
                    Model.nHistoryPeriods + Model.period - 1);
            int nFlatsToBuildInBucket = (int) Math.ceil((1 + Model.targetNewlyBuiltBuffer) * averageNewlyBuiltDemand * Model.nPeriodsForConstruction - bucket.nNewlyBuiltFlatsNotYetSold);
            nFlatsToBuildInBucket = (int) Math.min(nFlatsToBuildInBucket, Math.ceil(averageNewlyBuiltDemand * Model.maxNFlatsToBuildInBucketToAverageNewlyBuiltDemandRatio));

            return Math.max(nFlatsToBuildInBucket, 0);

        } else return 0;

    }

}
