package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecordForPrice {

    //state variables in case of FlatSaleRecord
    public int periodOfRecord; //DB
    public Bucket bucket;
    public double size;
    public double state;
    public double neighbourhoodQuality;
    public boolean isNewlyBuilt;
    public boolean isForcedSale;

    public boolean recordEligibleForPrice() {
        return periodOfRecord == Model.period - 1 && (Model.sampleRun || isNewlyBuilt == false) && isForcedSale == false;
    }

    public boolean isSimilarRecordForPrice(Flat flat) {

        return Math.abs(size - flat.size) / flat.size < Model.maxSizeDifferenceRatio && Math.abs(state - flat.state) / flat.state < Model.maxStateDifferenceRatio;
    }
}
