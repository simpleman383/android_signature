package com.simpleman383.signature;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 10.12.2016.
 */

public class SignatureVectorAnalyzer {
    private final double angleAccurance = 4 * 0.0872665;
    private final double lengthAccurancePercentage = 0.25;

    private User user;
    private Signature mSignature;

    public SignatureVectorAnalyzer(User u, Signature sign) {
        this.user = u;
        this.mSignature = sign;
    }


    public void LearningPhase(Signature signature, Context context) {
        List<String> allData = SignatureUtils.ReadFile(user.getSIGNATURE_VECTOR_CHARS(), context);
        SignatureUtils.WriteFile(FormatParams(signature.getAnglesOfPoints()), context, user.getSIGNATURE_VECTOR_CHARS());
        SignatureUtils.WriteFile(FormatParams(signature.getVectorLengthsOfPoints()), context, user.getSIGNATURE_VECTOR_CHARS());
    }


    public double Compare(Signature signature, Context context) {
        List<String> allData = SignatureUtils.ReadFile(user.getSIGNATURE_VECTOR_CHARS(), context);
        double max_matches = 0.0;

        for (int i = 0; i < allData.size() / 2; i++) {
            String anglesString = allData.get(2 * i);
            String lengthsString = allData.get(2 * i + 1);
            ArrayList<Double> averageAngles = ParseStringForDoubleList(anglesString);
            ArrayList<Double> averageLengths = ParseStringForDoubleList(lengthsString);

            ArrayList<Double> signAngles = signature.getAnglesOfPoints();
            ArrayList<Double> signLengths = signature.getVectorLengthsOfPoints();

            int currentSign = 0;
            int currentExampleSign = 0;

            while (currentExampleSign < averageAngles.size() && currentSign < signAngles.size())
            {
                    while (Math.abs(signAngles.get(currentSign) - averageAngles.get(currentExampleSign)) <= angleAccurance && Math.abs(signLengths.get(currentSign) - averageLengths.get(currentExampleSign)) <= lengthAccurancePercentage * averageLengths.get(currentExampleSign) && currentSign < averageAngles.size()) {
                        {
                            currentSign++;
                            if (currentSign >= averageAngles.size())
                                break;
                        }
                }
                currentExampleSign++;
            }

            if (max_matches < (double) currentSign / averageAngles.size())
                max_matches = (double) currentSign / averageAngles.size();

        }
        return max_matches * 1.5;
    }


    private String FormatParams(List<Double> params) {
        String result = "";

        for (Double value : params) {
            result = result + String.valueOf(value) + ",";
        }

        result = result.substring(0, result.length() - 2);
        return result;
    }


    private ArrayList<Double> ParseStringForDoubleList(String data) {
        ArrayList<Double> result = new ArrayList<>();

        String[] arrayString = data.split(",");
        for (int i = 0; i < arrayString.length; i++) {
            result.add(Double.parseDouble(arrayString[i]));
        }

        return result;
    }


}
