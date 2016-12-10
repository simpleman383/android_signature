package com.simpleman383.signature;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 10.12.2016.
 */

public class SignatureVectorAnalyzer
{
    private final int LEARN_SIGNATURE_AMOUNT_LIMIT = 10;

    private final double angleAccurance = 0.0872665;
    private final double lengthAccurancePercentage = 0.1;
    private final int limitPointsInTail = 100;

    private User user;
    private Signature mSignature;

    public SignatureVectorAnalyzer(User u, Signature sign)
    {
        this.user = u;
        this.mSignature = sign;
    }


    public void LearningPhase(Signature signature, Context context)
    {
        List<String> allData = SignatureUtils.ReadFile(user.getSIGNATURE_VECTOR_CHARS(), context);

        if (allData.isEmpty() || allData == null)
        {
            SignatureUtils.WriteFile(FormatParams(signature.getAnglesOfPoints()),context,user.getSIGNATURE_VECTOR_CHARS());
            SignatureUtils.WriteFile(FormatParams(signature.getVectorLengthsOfPoints()),context,user.getSIGNATURE_VECTOR_CHARS());
        } else {

            String anglesString = allData.get(0);
            String lengthsString = allData.get(1);
            ArrayList<Double> averageAngles = ParseStringForDoubleList(anglesString);
            ArrayList<Double> averageLengths = ParseStringForDoubleList(lengthsString);



        }
    }


    public boolean Compare(Signature signature, Context context)
    {
        List<String> allData = SignatureUtils.ReadFile(user.getSIGNATURE_VECTOR_CHARS(), context);
        String anglesString = allData.get(0);
        String lengthsString = allData.get(1);
        ArrayList<Double> averageAngles = ParseStringForDoubleList(anglesString);
        ArrayList<Double> averageLengths = ParseStringForDoubleList(lengthsString);

        ArrayList<Double> signAngles = signature.getAnglesOfPoints();
        ArrayList<Double> signLengths = signature.getVectorLengthsOfPoints();

        int currentSign = 0;
        int currentExampleSign = 0;

        while (currentExampleSign < averageAngles.size() && currentSign < signAngles.size())
        {
            if (Math.abs(signAngles.get(currentSign) - averageAngles.get(currentExampleSign)) <= angleAccurance && Math.abs(signLengths.get(currentSign) - averageLengths.get(currentExampleSign)) <= lengthAccurancePercentage * averageLengths.get(currentExampleSign)) {
                currentExampleSign++;
                currentSign++;
            } else {
                currentExampleSign++;
            }
        }

        if (currentExampleSign == averageAngles.size() && currentSign == signAngles.size())
            return true;

        if (currentExampleSign == averageAngles.size() && currentSign != signAngles.size())
        {
            if (Math.abs(currentSign - signAngles.size()) <= limitPointsInTail)
                return true;
            else
                return false;
        }


        if (currentExampleSign == averageAngles.size() && currentSign != signAngles.size())
        {
            if (Math.abs(currentSign - signAngles.size()) <= limitPointsInTail)
                return true;
            else
                return false;
        }

        if (currentExampleSign != averageAngles.size() && currentSign == signAngles.size()){
            if (Math.abs(currentExampleSign - averageAngles.size()) <= limitPointsInTail)
                return true;
            else
                return false;
        }

        return false;
    }





    private String FormatParams(List<Double> params)
    {
        String result = "";

        for (Double value: params) {
            result = result + String.valueOf(value) + ",";
        }

        result = result.substring(0, result.length()-2);
        return  result;
    }


    private ArrayList<Double> ParseStringForDoubleList(String data)
    {
        ArrayList<Double> result = new ArrayList<>();

        String[] arrayString = data.split(",");
        for (int i=0; i<arrayString.length; i++)
        {
            result.add(Double.parseDouble(arrayString[i]));
        }

        return result;
    }



}
