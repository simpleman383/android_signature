package com.simpleman383.signature;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.IllegalFormatException;

/**
 * Created by Sergey on 11.12.2016.
 */

public class ShoombaBoomba {
    private final double REGULAR_PARAMETERS_CONST = 5;
    private final double RESULT_CONST = 30;
    private Double[] dataForClassification = null;
    private ArrayList<Container> data = null;
    private ArrayList<Double> mins = null;
    private ArrayList<Double> maxs = null;

    public ShoombaBoomba(){
        this.mins = new ArrayList<>();
        this.maxs = new ArrayList<>();
        this.data = new ArrayList<>();
    }

    public void loadData(String path) throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(path));
        ArrayList<String> stringList = new ArrayList<String>();
        String line;
        while ((line = reader.readLine()) != null) {
            stringList.add(line);
        }
        this.setDataToContainer(stringList);
    }

    public boolean classify (String dataToClassificate){
        this.dataForClassification = setDataToArray(dataToClassificate);
        calculateMinsAndMaxs();
        double correctCounter = 0;
        for (int i = 0; i < dataForClassification.length; i++){
                if (this.dataForClassification[i] >= mins.get(i) && this.dataForClassification[i] <= maxs.get(i)) {
                    correctCounter+=REGULAR_PARAMETERS_CONST;
                }

        }
        if (correctCounter >= RESULT_CONST) return true;
        else return false;
    }


    private Double[] setDataToArray(String data){
        String[] arrayString = data.split(",");
        Double[] result = new Double[arrayString.length];
        for (int i = 0; i < arrayString.length; i++) {
            result [i] = Double.parseDouble(arrayString[i]);
        }

        return result;
    }

    private void setDataToContainer(ArrayList<String> stringList) throws IllegalFormatException {
        for (String line : stringList) {
            if (line.equals("")) {
                break;
            }
            String[] stringTemp = line.split(",");
            double[] doubleTemp = new double[12];
            int iter2 = 0;
            for (int iter = stringTemp.length - 11; iter < stringTemp.length - 1; iter++) {
                doubleTemp[iter2] = Double.parseDouble(stringTemp[iter]);
                iter2++;
            }
            this.data.add(new Container(doubleTemp));
        }
    }

    private void calculateMinsAndMaxs() {
        double min;
        double max;
        if (data.size() != 0) {
            for (int i = 0; i < data.get(0).parameters.length; i++) {
                min = data.get(0).parameters[i];
                max = min;
                for (Container elem : data) {
                    if (elem.parameters[i] < min) min = elem.parameters[i];
                    if (elem.parameters[i] > max) max = elem.parameters[i];
                }
                mins.add(min);
                maxs.add(max);
            }
        }
    }

}
