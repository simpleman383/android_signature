package com.simpleman383.signature;

import android.content.Context;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.List;

/**
 * Created by Alex on 14.12.2016.
 */

public class FourLayerPerceptronNetwork
{
    private final double ACCURACY = 0.001;

    private double[] enters;
    private double[] hidden1;
    private double[] hidden2;
    private double outer;

    private double[][] weightsEntersHidden1;
    private double[][] weightsHidden1Hidden2;
    private double[] weightsHidden2Output;

    private double[][] patterns;
    private double[] answers;

    public FourLayerPerceptronNetwork(User user, Context context)
    {
        loadPatternsAnswers(user, context);

        enters = new double[patterns[0].length];

        hidden1 = new double[6];
        hidden2 = new double[6];

        weightsEntersHidden1 = new double[enters.length][hidden1.length];
        weightsHidden1Hidden2 = new double[hidden1.length][hidden2.length];
        weightsHidden2Output = new double[hidden2.length];

        loadWeights(user, context);
        studyPhase();
        saveWeights(user, context);
    }


    private void loadPatternsAnswers(User user, Context context)
    {
        List<String> patternsWithAnswers = SignatureUtils.ReadFile(user.getCORPUS_2_FILE(), context);

        if (patternsWithAnswers.size() != 0)
        {
            this.answers = new double[patternsWithAnswers.size()];

            int sizePattern = patternsWithAnswers.get(0).split(",").length - 1; //must be equal 12

            this.patterns = new double[patternsWithAnswers.size()][sizePattern];

            int cursor = 0;

            for (String pattern: patternsWithAnswers )
            {
                String[] splitted = pattern.split(",");

                this.answers[cursor] = Double.parseDouble(splitted[splitted.length-1]);

                for (int i = 0; i < sizePattern; i++)
                {
                    this.patterns[cursor][i] = Double.parseDouble(splitted[i]);
                }

                cursor++;
            }
        }

    }


    private void loadWeights(User user, Context context){
        List<String> weights = SignatureUtils.ReadFile(user.getPERCEPTRON_WEIGHTS(), context);

        if (weights.size() == 0 || weights.isEmpty() || weights == null)
        {
            init();
        }
        else
        {
            for (int i = 0; i < enters.length; i++)
            {
                for (int k = 0; k < weights.get(i).split(",").length; k++)
                {
                    this.weightsEntersHidden1[i][k] = Double.parseDouble(weights.get(i).split(",")[k]);
                }
            }

            for (int i = enters.length; i < enters.length + hidden1.length; i++)
            {
                for (int k = 0; k < weights.get(i).split(",").length; k++)
                {
                    this.weightsHidden1Hidden2[i-enters.length][k] = Double.parseDouble(weights.get(i).split(",")[k]);
                }
            }

            for (int k = 0; k < weights.get(enters.length + hidden1.length).split(",").length; k++)
            {
                this.weightsHidden2Output[k] = Double.parseDouble(weights.get(enters.length + hidden1.length).split(",")[k]);
            }
        }

    }


    private void saveWeights(User user, Context context)
    {
        FileOutputStream outputStream = null;
        //BufferedWriter bw = null;
        //FileWriter fw = null;
        try {
            outputStream = context.openFileOutput(user.getPERCEPTRON_WEIGHTS(), Context.MODE_PRIVATE);
            //bw = new BufferedWriter(fw);

            for (int i=0; i<weightsEntersHidden1.length;i++)
            {
                String str = "";
                for (int j = 0; j < weightsEntersHidden1[i].length; j++){
                    str += String.valueOf(weightsEntersHidden1[i][j]) + ",";
                }
                str = str.substring(0, str.length()-1);
                outputStream.write((str+"\n").getBytes());
            }

            for (int i=0; i<weightsHidden1Hidden2.length;i++)
            {
                String str = "";
                for (int j = 0; j < weightsHidden1Hidden2[i].length; j++){
                    str += String.valueOf(weightsHidden1Hidden2[i][j]) + ",";
                }
                str = str.substring(0, str.length()-1);
                outputStream.write((str+"\n").getBytes());
            }

            String str = "";
            for (int i=0; i<weightsHidden2Output.length;i++)
            {
                str += String.valueOf(weightsHidden2Output[i]) + ",";
            }

            str = str.substring(0, str.length()-1);
            outputStream.write((str+"\n").getBytes());
            System.out.println("Done");

        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (outputStream != null)
                    outputStream.close();
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }


    private void init()
    {
        for (int i=0; i<weightsEntersHidden1.length; i++){
            for (int j =0; j<weightsEntersHidden1[i].length; j++){
                weightsEntersHidden1[i][j] = Math.random()*0.2+0.1;
            }
        }

        for (int i=0; i<weightsHidden1Hidden2.length; i++){
            for (int j =0; j<weightsHidden1Hidden2[i].length; j++){
                weightsHidden1Hidden2[i][j] = Math.random()*0.2+0.1;
            }
        }

        for (int i=0; i<weightsHidden2Output.length; i++){
            weightsHidden2Output[i] = Math.random()*0.2+0.1;
        }
    }


    private double Sigmoid(double x)
    {
        double res = 1 / (1 + Math.exp(-x));
        return res;
    }

    public double test(double[] data)
    {
        this.enters = data;
        double res = countOuter();
        return res;
    }


    private double countOuter()
    {
        for (int i=0; i<hidden1.length; i++)
        {
            hidden1[i] = 0;
            for (int j=0; j<enters.length; j++)
            {
                hidden1[i] += enters[j]*weightsEntersHidden1[j][i];
            }
            hidden1[i] = Sigmoid(hidden1[i]);
        }


        for (int i=0; i<hidden2.length; i++){
            hidden2[i] = 0;
            for (int j=0; j<hidden1.length; j++)
            {
                hidden2[i] += hidden1[j]*weightsHidden1Hidden2[j][i];
            }
            hidden2[i] = Sigmoid(hidden2[i]);
        }


        outer = 0;
        for (int i=0; i<hidden2.length; i++)
        {
            outer+= hidden2[i]*weightsHidden2Output[i];
        }
        outer = Sigmoid(outer);

        return outer;
    }


    public void studyPhase()
    {
        double[] err1 = new double[hidden1.length];
        double[] err2 = new double[hidden2.length];

        double globalError = 0;

        double step = 0.1;
        int iteration = 0;

        do{

            globalError = 0;

            iteration++;
            if (iteration % 1000000 == 0) step = step/1.5;

            for (int p=0; p<patterns.length; p++)
            {
                for (int i=0; i<enters.length; i++)
                {
                    enters[i] = patterns[p][i];
                }

                countOuter();

                double localError = (answers[p] - outer)*(1 - outer)*outer;

                globalError += ((answers[p] - outer)*(answers[p] - outer))/2;

                for (int i=0; i<hidden2.length; i++)
                {
                    err2[i] = localError*weightsHidden2Output[i]*hidden2[i]*(1-hidden2[i]);
                }

                for (int i = 0; i < hidden1.length; i++){
                    err1[i] = 0;
                    for (int j=0; j < hidden2.length; j++){
                        err1[i]+=weightsHidden1Hidden2[i][j]*err2[j];
                    }
                    err1[i] = err1[i]*hidden1[i]*(1-hidden1[i]);
                }


                for (int i=0; i<enters.length; i++)
                {
                    for (int j=0; j<hidden1.length; j++){
                        weightsEntersHidden1[i][j] += step*err1[j]*enters[i];
                    }
                }


                for (int i=0; i<hidden1.length; i++)
                {
                    for (int j=0; j<hidden2.length; j++)
                    {
                        weightsHidden1Hidden2[i][j] += step*err2[j]*hidden1[i];
                    }
                }

                for (int i=0; i<hidden2.length; i++)
                {
                    weightsHidden2Output[i] += step*localError*hidden2[i];
                }

            }
        } while (globalError > ACCURACY);

        System.out.println(iteration);
    }





}
