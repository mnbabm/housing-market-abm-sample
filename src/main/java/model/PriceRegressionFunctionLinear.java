package model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class PriceRegressionFunctionLinear implements HasID, PriceRegressionFunction {

    private static int nextId;
    final int id;

    //state variables
    public double constant;
    public double coeffSize;
    public double coeffSize2;
    public double coeffSizeState;

    public PriceRegressionFunctionLinear() {
        id = PriceRegressionFunctionLinear.nextId;
        PriceRegressionFunctionLinear.nextId++;

        Model.priceRegressionFunctionsLinear.put(id, this);
    }


    public double flatPrice(Flat flat) {
        double sizeForSize2 = Math.min(flat.getSize(), Model.maxSizeValueForSize2);
        double state = Math.max(flat.getState(), Model.minStateValueForSizeState);
        double price = constant + coeffSize * flat.getSize() + coeffSize2 * sizeForSize2 * sizeForSize2 + coeffSizeState * flat.getSize() * state;
        return Math.max(price, Model.minForSalePriceForRegression);
    }

    public boolean priceRegressionRefreshed(List<FlatSaleRecord> flatSaleRecords) {

        List<FlatSaleRecord> flatSaleRecordsUsed = new ArrayList<>();
        for (FlatSaleRecord flatSaleRecord : flatSaleRecords) {
            if (flatSaleRecord.isNewlyBuilt == false) {
                flatSaleRecordsUsed.add(flatSaleRecord);
            }
        }

        if (flatSaleRecordsUsed.size() < Model.nFlatsForPriceRegression) return false;

        double[] y = new double[flatSaleRecordsUsed.size()];
        double[][] x = new double[flatSaleRecordsUsed.size()][3];

        for (int i = 0; i < flatSaleRecordsUsed.size(); i++) {
            y[i] = flatSaleRecordsUsed.get(i).price;
            x[i][0] = flatSaleRecordsUsed.get(i).size;
            x[i][1] = flatSaleRecordsUsed.get(i).size * flatSaleRecordsUsed.get(i).size;
            x[i][2] = flatSaleRecordsUsed.get(i).size * flatSaleRecordsUsed.get(i).state;
        }

        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
        double[] beta_regressionPrice;
        try {
            regression.newSampleData(y, x);
            beta_regressionPrice = regression.estimateRegressionParameters();
            if (Math.abs((beta_regressionPrice[1] - coeffSize) / coeffSize) < Model.maxChangeInRegressionParameters && Math.abs((beta_regressionPrice[2] - coeffSize2) / coeffSize2) < Model.maxChangeInRegressionParameters && Math.abs((beta_regressionPrice[3] - coeffSizeState) / coeffSizeState) < Model.maxChangeInRegressionParameters) {
                constant = beta_regressionPrice[0];
                coeffSize = beta_regressionPrice[1];
                coeffSize2 = beta_regressionPrice[2];
                coeffSizeState = beta_regressionPrice[3];
                return true;
            } else return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }

    public void copyRegressionParameters(PriceRegressionFunction priceRegressionFunction) {
        PriceRegressionFunctionLinear priceRegressionFunctionLinear = (PriceRegressionFunctionLinear) priceRegressionFunction;
        constant = priceRegressionFunctionLinear.constant;
        coeffSize = priceRegressionFunctionLinear.coeffSize;
        coeffSize2 = priceRegressionFunctionLinear.coeffSize2;
        coeffSizeState = priceRegressionFunctionLinear.coeffSizeState;
    }
}
