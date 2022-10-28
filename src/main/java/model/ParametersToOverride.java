package model;

public class ParametersToOverride {

    //you can add variables here and then write the appropriate code in the overrideParameters() method.
    //By setting a value to a variable in MainRun (before running Model.sampleSetup()), this value will be used instead of what is given in the config file
    double[] LTVPath;
    int nPeriods;
    double additionalPTI = 0;
    int nFlatsToLookAt = 0;

    public void overrideParameters() {

        if (LTVPath != null && Model.LTVPath != null) {
            if (Model.LTVPath.length > LTVPath.length) {
                for (int i = 0; i < Model.LTVPath.length; i++) {
                    if (i < LTVPath.length) {
                        Model.LTVPath[i] = LTVPath[i];
                    } else {
                        if (LTVPath[LTVPath.length - 2] == LTVPath[LTVPath.length - 1]) {
                            Model.LTVPath[i] = Model.LTVPath[i - 1];
                        } else {
                            Model.LTVPath[i] = Model.LTVPath[i - 1] * (LTVPath[LTVPath.length - 1] / LTVPath[LTVPath.length - 2]);
                        }
                    }
                }
            } else {
                Model.LTVPath = LTVPath;
            }

        }

        if (nPeriods != 0) {
            Model.nPeriods = nPeriods;
        }

        if (additionalPTI != 0) {
            Model.additionalPTI = additionalPTI;
        }

        if (nFlatsToLookAt > 0) {
            Model.nFlatsToLookAt = nFlatsToLookAt;
        }

    }
}
