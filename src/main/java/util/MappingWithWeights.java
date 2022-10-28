package util;

import java.util.ArrayList;
import java.util.Arrays;

public class MappingWithWeights<T> {
    double[] cumulativeProbabilities;
    ArrayList<Double> weights = new ArrayList<>();
    ArrayList<T> objects = new ArrayList<>();

    boolean cumulativeProbabilitiesAreUpdated = false;
    double sum = 0;

    public void put(double weight, T object) {
        cumulativeProbabilitiesAreUpdated = false;
        weights.add(weight);
        objects.add(object);
        sum += weight;
    }

    public void remove(T object) {
        int index = objects.indexOf(object);
        sum -= weights.get(index);
        objects.remove(index);
        weights.remove(index);
        cumulativeProbabilitiesAreUpdated = false;
    }

    public void putIfAbsent(double weight, T object) {
        cumulativeProbabilitiesAreUpdated = false;
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i) == object) {
                return;
            }
        }
        weights.add(weight);
        objects.add(object);
        sum += weight;
    }

    public double getSize() {
        return weights.size();
    }

    public void increaseWeightBy(double weight, T object) {
        cumulativeProbabilitiesAreUpdated = false;
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).equals(object)) {
                weights.set(i, weights.get(i) + weight);
                sum += weight;
                return;
            }
        }
    }

    private void updateCumulativeProbabilities() {
        cumulativeProbabilities = new double[weights.size()];
        if (weights.size() == 0) return;
        cumulativeProbabilities[0] = weights.get(0) / sum;
        for (int i = 1; i < weights.size(); i++) {
            cumulativeProbabilities[i] = cumulativeProbabilities[i - 1] + weights.get(i) / sum;
        }
        cumulativeProbabilitiesAreUpdated = true;
    }

    public void clear() {
        cumulativeProbabilities = null;
        weights.clear();
        objects.clear();
        cumulativeProbabilitiesAreUpdated = false;
        sum = 0;
    }

    public T selectObjectAccordingToCumulativeProbability(double randomNumber) {
        if (cumulativeProbabilitiesAreUpdated == false) {
            updateCumulativeProbabilities();
        }
        if (cumulativeProbabilities.length == 0) return null;
        int index = Arrays.binarySearch(cumulativeProbabilities, randomNumber);
        index = -(index + 1);
        return objects.get(index);
    }


}
