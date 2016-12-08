package com.simpleman383.signature;

import java.util.ArrayList;

/**
 * Created by Sergey on 08.12.2016.
 */

public class Container {
    public double[] parameters;
    public String classValue;

    public Container(double[] parameters, String classValue){
        this.parameters = parameters;
        this.classValue = classValue;
    }

    public Container(double[] parameters){
        this.parameters = parameters;
    }
}
