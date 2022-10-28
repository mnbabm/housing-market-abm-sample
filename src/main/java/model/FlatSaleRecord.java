package model;

import lombok.Getter;
import lombok.Setter;
import util.OwnFunctions;

@Getter
@Setter
public class FlatSaleRecord extends RecordForPrice implements HasID {
    public static int nextId;
    final int id;

    //state variables + see other state variables at RecordForPrice
    public double price;
    public int flatId;

    public FlatSaleRecord() {
        id = FlatSaleRecord.nextId;
        FlatSaleRecord.nextId++;

    }


    public void setSize(double value) {
        size = value;
    }


}
