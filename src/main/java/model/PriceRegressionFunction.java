package model;

import java.util.List;

public interface PriceRegressionFunction {

    double flatPrice(Flat flat);

    boolean priceRegressionRefreshed(List<FlatSaleRecord> flatSaleRecords);

    void copyRegressionParameters(PriceRegressionFunction priceRegressionFunction);
}
