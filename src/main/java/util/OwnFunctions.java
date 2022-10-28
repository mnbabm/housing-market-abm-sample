package util;

import model.*;

import java.text.NumberFormat;
import java.util.*;

public class OwnFunctions {

    public static <K, V> Map<K, V> copyMap(Map<K, V> mapToCopy) {
        Map<K, V> copiedMap = new HashMap<K, V>();
        for (K i : mapToCopy.keySet()) {
            copiedMap.put(i, mapToCopy.get(i));
        }
        return copiedMap;
    }

    public static <T> ArrayList<T> copyArrayList(ArrayList<T> originalArrayList) {
        ArrayList<T> copiedArrayList = new ArrayList<>();
        for (T element : originalArrayList) {
            copiedArrayList.add(element);
        }
        return copiedArrayList;
    }

    public static double[] probabilitiesTo1(double[] originalProbabilities) {
        double[] newProbabilities = new double[originalProbabilities.length];
        double sumProbabilities = 0;
        for (int i = 0; i < newProbabilities.length; i++) {
            newProbabilities[i] = originalProbabilities[i];
            sumProbabilities += newProbabilities[i];
        }

        for (int i = 0; i < newProbabilities.length; i++) {
            newProbabilities[i] /= sumProbabilities;
        }

        return newProbabilities;

    }

    public static double[] makeCumulatedArray(double[] originalArray) {
        double[] newArray = new double[originalArray.length];
        newArray[0] = originalArray[0];
        if (originalArray.length > 0) {
            for (int i = 1; i < newArray.length; i++) {
                newArray[i] = newArray[i - 1] + originalArray[i];
            }
        }

        return newArray;
    }

    public static int indexOfIntervalInIncreasingArray(double[] cumulatedArray, double number) {
        int index = Arrays.binarySearch(cumulatedArray, number);
        if (index < 0)
            index = -index - 1;
        return index;
    }

    public static void pauseHere(String message) {
        Scanner pauseMessage0 = new Scanner(System.in);

        try {
            System.out.println(message);
            int pauseInt = pauseMessage0.nextInt();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public static double average(double[] vector, int firstIndex, int lastIndex) { //lastIndex inclusive
        double sum = 0;
        for (int i = firstIndex; i <= lastIndex; i++) {
            sum += vector[i];
        }

        return sum / (lastIndex - firstIndex + 1);
    }

    public static double average(int[] vector, int firstIndex, int lastIndex) { //lastIndex inclusive
        double sum = 0;
        for (int i = firstIndex; i <= lastIndex; i++) {
            sum += vector[i];
        }

        double ez1 = sum / (lastIndex - firstIndex + 1);
        double ez2 = sum / (double) (lastIndex - firstIndex + 1);
        return sum / (lastIndex - firstIndex + 1);
    }

    public static double doubleInRange(double value, double minValue, double maxValue) {
        if (value > maxValue) return maxValue;
        if (value < minValue) return minValue;
        return value;

    }

    public static double randomNumberFromTruncatedGaussian(double mean, double std, double min, double max) {

        double randomNumber;
        double result = min - 1;

        while (result < min
                || result > max) {

            randomNumber = Model.rnd.nextGaussian();
            result = mean * (1 + std * randomNumber);
        }

        return result;
    }

    public static double threadRandomNumberFromTruncatedGaussian(HasID object, double mean, double std, double min, double max) {

        double randomNumber;
        double result = min - 1;

        while (result < min
                || result > max) {

            randomNumber = Model.threadNextGaussian(object);
            result = mean * (1 + std * randomNumber);
        }

        return result;
    }

    public static <T> double maxOfDoubleMapValues(Map<T, Double> map) {
        double maxValue = Double.NEGATIVE_INFINITY;
        for (Double number : map.values()) {
            if (number > maxValue) maxValue = number;
        }
        return maxValue;
    }

    public static <T> double sumOfDoubleMapValues(Map<T, Double> map) {
        double sum = 0;
        for (Double number : map.values()) {
            sum += number;
        }
        return sum;
    }

    public static <T> int sumOfIntegerMapValues(Map<T, Integer> map) {
        int sum = 0;
        for (Integer number : map.values()) {
            sum += number;
        }
        return sum;
    }

    public static <T> void increaseDoubleMapValue(Map<T, Double> map, T key, double value) {
        map.replace(key, map.get(key) + value);
    }

    public static <T> void increaseIntegerMapValue(Map<T, Integer> map, T key, int value) {
        map.replace(key, map.get(key) + value);
    }

    public static <T> int sumOfIntegerMapValues(Map<T, Integer> map, T key) {
        int sum = 0;
        for (T object : map.keySet()) {
            sum += map.get(object);
        }
        return sum;
    }

    public static <T> double sumOfDoubleMapValues(Map<T, Double> map, T key) {
        double sum = 0;
        for (T object : map.keySet()) {
            sum += map.get(object);
        }
        return sum;
    }


    public static double sumDoubleArray(double[] array) {
        double result = 0;
        for (int i = 0; i < array.length; i++) {
            result += array[i];
        }
        return result;
    }

    public static int sumIntArray(int[] array) {
        int result = 0;
        for (int i = 0; i < array.length; i++) {
            result += array[i];
        }
        return result;
    }

    public static void printMemoryStats() {
        Runtime runtime = Runtime.getRuntime();

        NumberFormat format = NumberFormat.getInstance();

        StringBuilder sb = new StringBuilder();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();

        sb.append("free memory: " + format.format(freeMemory / 1024) + "<br/>");
        sb.append("allocated memory: " + format.format(allocatedMemory / 1024) + "<br/>");
        sb.append("max memory: " + format.format(maxMemory / 1024) + "<br/>");
        sb.append("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + "<br/>");

        System.out.println(sb);

    }

    public static double average(ArrayList<Double> numbers) {
        if (numbers.size() == 0) return 0;
        double sum = 0;
        for (int i = 0; i < numbers.size(); i++) {
            sum += numbers.get(i);
        }
        return sum / numbers.size();
    }

    public static double average(ArrayList<Double> numbers, int firstIndex, int lastIndexExclusive) {
        if (lastIndexExclusive <= firstIndex || firstIndex >= numbers.size() || lastIndexExclusive > numbers.size())
            return 0;
        double sum = 0;
        for (int i = firstIndex; i < lastIndexExclusive; i++) {
            sum += numbers.get(i);
        }
        return sum / (lastIndexExclusive - firstIndex);
    }

    public static int firstNDecimalNumbersOfNonegativeDouble(double doubleNumber, int nDecimalNumbers) {
        return (int) Math.round((doubleNumber - Math.floor(doubleNumber)) * Math.pow(10, nDecimalNumbers));


    }

    public static int doubleToIntegerWithProperProbability(double value) {
        double decimalPart = value - Math.floor(value);
        return (int) Math.floor(value) + (Model.rnd.nextDouble() < decimalPart ? 0 : 1);
    }

}
