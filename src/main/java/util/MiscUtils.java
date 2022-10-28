package util;

import model.Flat;

import java.util.ArrayList;

public class MiscUtils {

    public static int FlatPriceSearchForIndex(ArrayList<Flat> flats, double value) {

        //if flats is sorted in ascending order according to forSalePrice, then the function returns the index where a flat with the given value could be inserted

        //implementation based on: https://en.wikipedia.org/wiki/Binary_search_algorithm
        int L = 0;
        int R = flats.size();
        while (L < R) {

            int m = (L + R) / 2;

            if (flats.get(m).getForSalePrice() < value) {
                L = m + 1;
            } else {
                R = m;
            }

        }

        return L;
    }

    public static int FlatRentSearchForIndex(ArrayList<Flat> flats, double value) {

        //if flats is sorted in ascending order according to forSalePrice, then the function returns the index where a flat with the given value could be inserted

        //implementation based on: https://en.wikipedia.org/wiki/Binary_search_algorithm
        int L = 0;
        int R = flats.size();
        while (L < R) {

            int m = (L + R) / 2;

            if (flats.get(m).getRent() < value) {
                L = m + 1;
            } else {
                R = m;
            }

        }

        return L;
    }
}
