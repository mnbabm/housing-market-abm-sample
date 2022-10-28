package util;


public class OwnStopper {
    long startTime;
    long endTime;

    public OwnStopper() {
        startTime = System.nanoTime();
    }

    public double getElapsedTimeInMilliseconds() {
        return (System.nanoTime() - startTime) / 1000000.0;
    }

    public void printElapsedTimeInMilliseconds() {
        System.out.println("Elapsed time: " + getElapsedTimeInMilliseconds() + " ms.");
    }

    public void printElapsedTimeInMilliseconds(String string) {
        System.out.println("Elapsed time for " + string + ": " + getElapsedTimeInMilliseconds() + " ms.");
    }

    public void startStopper() {
        startTime = System.nanoTime();
    }
}
