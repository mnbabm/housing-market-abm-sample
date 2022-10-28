package model;

import util.SampleInitializer;

public class MainRun {

    public static void main(String[] args) {

        SampleInitializer.configFileName = "src/main/java/resources/sampleConfiguration.properties";


        ParametersToOverride parametersToOverride = new ParametersToOverride();
        Model.parametersToOverride = parametersToOverride;
        Model.sampleSetup();
        Model.runPeriods();


    }
}