package com.simpleman383.signature;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.IllegalFormatException;

/**
 * Created by Sergey on 08.12.2016.
 */

public class RangeClassifier {
    public ArrayList<Container> data;
    private ArrayList<Container> dataOfFirstClass;
    private ArrayList<Container> dataOfSecondClass;
    private ArrayList<Double> minsOfFirstClass;
    private ArrayList<Double> maxsOfFirstClass;
    private ArrayList<Double> minsOfSecondClass;
    private ArrayList<Double> maxsOfSecondClass;
    private final double CONST_MULTIPLIER = 1;
    private final double CONST_PERCENTAGE = 0.5;


    public RangeClassifier() {
        this.data = new ArrayList<>();
        this.minsOfFirstClass = new ArrayList<>();
        this.maxsOfFirstClass = new ArrayList<>();
        this.minsOfSecondClass = new ArrayList<>();
        this.maxsOfSecondClass = new ArrayList<>();
    }

    public String classify(double[] params) {
        this.splitDataToClasses();
        this.calculateMinsForFirstClass();
        this.calculateMaxsForFirstClass();
        this.calculateMinsForSecondClass();
        this.calculateMaxsForSecondClass();
        Container objToClassify = new Container(params);
        int correctCounter = 0;
        for (int i = params.length - 9; i <= params.length - 1 ; i++){
            if ((params[i] >= this.minsOfFirstClass.get(i)) && (params[i] <= this.maxsOfFirstClass.get(i))){
                correctCounter++;
            }
        }
        Double percentage = (double)correctCounter / 10;
        if (percentage.compareTo(CONST_PERCENTAGE) > 0) {
            return dataOfFirstClass.get(0).classValue;
        }
        else return dataOfSecondClass.get(0).classValue;
    }




    private void calculateMinsForFirstClass() {
        double min;
        for (int i = 0; i < data.get(0).parameters.length; i++) {
            min = dataOfFirstClass.get(0).parameters[i];
            for (Container elem : dataOfFirstClass) {
                if (elem.parameters[i] < min) min = elem.parameters[i];
            }
            minsOfFirstClass.add(min);
        }
    }


    private void calculateMinsForSecondClass() {
        double min;
        for (int i = 0; i < data.get(0).parameters.length; i++) {
            min = dataOfSecondClass.get(0).parameters[i];
            for (Container elem : dataOfSecondClass) {
                if (elem.parameters[i] < min) min = elem.parameters[i];
            }
            minsOfSecondClass.add(min);
        }
    }


    private void calculateMaxsForFirstClass() {
        double max;
        for (int i = 0; i < data.get(0).parameters.length; i++) {
            max = dataOfFirstClass.get(0).parameters[i];
            for (Container elem : dataOfFirstClass) {
                if (elem.parameters[i] > max) max = elem.parameters[i];
            }
            maxsOfFirstClass.add(max);
        }
    }


    private void calculateMaxsForSecondClass() {
        double max;
        for (int i = 0; i < data.get(0).parameters.length; i++) {
            max = dataOfSecondClass.get(0).parameters[i];
            for (Container elem : dataOfSecondClass) {
                if (elem.parameters[i] > max) max = elem.parameters[i];
            }
            maxsOfSecondClass.add(max);
        }
    }


    public void loadData(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        ArrayList<String> stringList = new ArrayList<String>();
        String line;
        while ((line = reader.readLine()) != null) {
            stringList.add(line);
        }
        this.setDataToContainer(stringList);
    }


    private void setDataToContainer(ArrayList<String> stringList) throws IllegalFormatException {
        for (String line : stringList) {
            if (line.equals("")) {
                break;
            }
            String[] stringTemp = line.split(",");
            double[] doubleTemp = new double[stringTemp.length - 1];
            for (int iter = 0; iter < stringTemp.length - 1; iter++) {
                doubleTemp[iter] = Double.parseDouble(stringTemp[iter]);
            }
            String classValue = stringTemp[stringTemp.length - 1];
            this.data.add(new Container(doubleTemp, classValue));
        }
    }


    private void splitDataToClasses() {
        this.dataOfFirstClass = new ArrayList<Container>();
        this.dataOfSecondClass = new ArrayList<Container>();
        String firstClass = data.get(0).classValue;
        for (Container elem : data) {
            if (elem.classValue.equals(firstClass)) {
                this.dataOfFirstClass.add(elem);
            } else {
                this.dataOfSecondClass.add(elem);
            }
        }
    }
}
