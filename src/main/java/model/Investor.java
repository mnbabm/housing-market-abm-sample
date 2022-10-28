package model;

import lombok.Getter;
import lombok.Setter;
import util.OwnFunctions;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Investor implements HasID {
    public static int nextId;
    final int id;

    //state variables
    List<Flat> properties = new ArrayList<>();

    public Investor() {
        id = Investor.nextId;
        Investor.nextId++;
    }


    public void renovateFlat(Flat flat) {
        double renovationCost = flat.size * (flat.investmentStateIncrease - Model.stateDepreciation) * flat.getGeoLocation().renovationUnitCost;
        if (renovationCost > 0) flat.bucket.neighbourhood.centralInvestmentValue += renovationCost;
        flat.setState(flat.state + flat.investmentStateIncrease);
        flat.getGeoLocation().renovationQuantity += flat.size * flat.investmentStateIncrease;
    }

    public void removeSoldFlatsFromProperties() {
        List<Flat> newProperties = new ArrayList<>();
        for (Flat flat : properties) {
            if (flat.ownerInvestor == this) {
                newProperties.add(flat);
            }
        }
        properties = newProperties;
    }
}
