package model;

import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;

@Getter
@Setter
public class Bid {
    double reservationPrice;
    Household household;
    Flat flat;

    Bid(double reservationPrice, Household household, Flat flat) {
        this.reservationPrice = reservationPrice;
        this.household = household;
        this.flat = flat;
    }

    public static Comparator<Bid> comparatorReservationPrice = new Comparator<Bid>() {

        //@Override
        public int compare(Bid o1, Bid o2) {
            return Double.compare(o1.getReservationPrice(), o2.getReservationPrice());
        }
    };
    public static Comparator<Bid> comparatorReservationPriceDecrease = new Comparator<Bid>() {

        //@Override
        public int compare(Bid o1, Bid o2) {
            if (o1.getReservationPrice() == o2.getReservationPrice())
                return Integer.compare(o1.household.getId(), o2.household.getId());
            return Double.compare(-o1.getReservationPrice(), -o2.getReservationPrice());
        }
    };

    public static Comparator<Bid> comparatorSurplusDecrease = new Comparator<Bid>() {

        //@Override
        public int compare(Bid o1, Bid o2) {
            double surplus1 = o1.household.utilityFunctionCES.calculateAbsoluteReservationPriceForFlat(o1.flat) - o1.flat.forSalePrice;
            double surplus2 = o2.household.utilityFunctionCES.calculateAbsoluteReservationPriceForFlat(o2.flat) - o2.flat.forSalePrice;
            return Double.compare(-surplus1, -surplus2);
        }
    };
}
