package com.simpleman383.signature;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.util.List;


import libsvm.SelfOptimizingLinearLibSVM;
import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.KNearestNeighbors;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.FileHandler;

import static com.simpleman383.signature.DrawActivity.*;

/**
 * Created by Alex on 29.11.2016.
 */

public class DrawControlFragment extends Fragment {

    public static DrawControlFragment newInstance() {
        return new DrawControlFragment();
    }


    private Button mOptions;
    private Button mDone;
    private Button mClear;
    private ImageView mImage;
    private CanvasView mCanvasView;

    private Signature signature;


    private String MODE="KNN-5";

    private User curUser;
    boolean NEWBYE_MODE;
    private int exampleRemain = 10;
    private static final int AdditionalParamsNumb = 11;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.draw_fragment, container, false);


        if (this.getArguments() != null)
        {
            curUser = new User((String) this.getArguments().getSerializable(DrawActivity.CURRENT_USER), getContext());
            MODE = (String) this.getArguments().getSerializable(DrawActivity.SELECTED_CLASSIFIER);
        } else
        {
            String name = (String) getActivity().getIntent().getSerializableExtra(DrawActivity.CURRENT_USER); //decide whether the user is new
            curUser = new User(name, getContext());
            MODE = (String)getActivity().getIntent().getSerializableExtra(DrawActivity.SELECTED_CLASSIFIER);
        }

        NEWBYE_MODE = IsCurrentUserNew(curUser, getContext());



        mOptions = (Button) v.findViewById(R.id.options_button);
        mOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), OptionsActivity.class);
                startActivity(intent);
            }
        });

        mCanvasView = (CanvasView) v.findViewById(R.id.canvas_view);
        mImage = (ImageView) v.findViewById(R.id.imageView2);
        mImage.setVisibility(View.INVISIBLE);


        mClear = (Button) v.findViewById(R.id.clear_button);
        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResetFragment();
            }
        });


        mDone = (Button) v.findViewById(R.id.done_button);
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCanvasView.CenterSignature();
                signature = new Signature(mCanvasView.getBitmap(), mCanvasView.getTouchCounter(), mCanvasView.getTimePeriodOnTouch(), mCanvasView.getSignatureControlPoints(), mCanvasView.getSignatureActionUpPoints(), mCanvasView.getTimesOfGettingPoints());

                if (signature.getTouches() == 0) {
                    Toast.makeText(getContext(), "Please, write something...", Toast.LENGTH_SHORT).show();
                    ResetFragment();
                } else {
                    if (NEWBYE_MODE) {
                        GetExamples();

                    } else {
                        SignatureUtils.deleteFile(getContext(), "temporary.txt");
                        String data = FormatSignatureParams(signature);
                        String decision = "";

                        switch (MODE)
                        {
                            case "KNN-5":
                                try
                                {
                                    File corpus = new File(getContext().getFilesDir() , curUser.getCORPUS_FILE());
                                    int size = SignatureUtils.ReadFile(curUser.getCORPUS_FILE(), getContext()).get(0).split(",").length - 1;
                                    Dataset dataset = FileHandler.loadDataset(corpus, size , ",");

                                    Classifier classifier = new KNearestNeighbors(5);
                                    classifier.buildClassifier(dataset);

                                    SignatureUtils.WriteFile(data, getContext(), "temporary.txt");
                                    File forClassification = new File(getContext().getFilesDir() , "temporary.txt");
                                    Dataset dataForClassification = FileHandler.loadDataset(forClassification, size-1 , ",");
                                    for (Instance inst : dataForClassification)
                                    {
                                        Object result = classifier.classify(inst);
                                        decision = result.toString();
                                        Log.i("DECISION: ", decision);
                                    }
                                    SignatureUtils.deleteFile(getContext(), "temporary.txt");
                                    break;
                                } catch (Exception e)
                                {
                                    e.printStackTrace();
                                    SignatureUtils.deleteFile(getContext(), "temporary.txt");
                                }

                            case "Range Classifier":
                                decision = RangeClassifierMode(data, getContext());
                                break;
                        }

                        //show a dialog window that asks whether the decision was correct
                        FragmentManager manager = getFragmentManager();
                        ResultFragment dialog = ResultFragment.newInstance(curUser.getUserName(), decision, data);
                        dialog.show(manager, "RESULT");

                        ResetFragment();
                    }

                }
            }
        });


        return v;
    }


    private boolean IsCurrentUserNew(User curUser, Context context) {
        List<String> existingCorpus = SignatureUtils.ReadFile(curUser.getCORPUS_FILE(), context);

        if (existingCorpus.size() < 10) {
            exampleRemain = 10 - existingCorpus.size();
            Toast.makeText(context, "Give an example of your signature " + String.valueOf(exampleRemain) + " times", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }


    private void ResetFragment() {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        DrawControlFragment drawFragment = DrawControlFragment.newInstance();

        Bundle args = new Bundle();
        args.putSerializable(CURRENT_USER, curUser.getUserName());
        args.putSerializable(SELECTED_CLASSIFIER, MODE);
        drawFragment.setArguments(args);

        manager.beginTransaction().replace(R.id.fragmentContainer, drawFragment).commit();
        return;
    }


    public String FormatSignatureParams(Signature sign)
    {
       String dataForTest = "";

        for (int x=0; x < sign.getSignatureBitmap().getWidth(); x++)
        {
            for (int y=0; y < sign.getSignatureBitmap().getHeight(); y++)
            {
                int color = 1;

                if (sign.getSignatureBitmap().getPixel(x, y) == -1)
                    color = 0;

                dataForTest = dataForTest + String.valueOf(color) + ",";
            }
        }

        dataForTest = dataForTest + String.valueOf(sign.getTouches()) + "," + String.valueOf(sign.getMinTimeOnTouch()) + "," + String.valueOf(sign.getMaxTimeOnTouch()) + ","+ String.valueOf(sign.getAverageTimeOnTouch()) + "," + String.valueOf(sign.getTotalTimeOnTouch());

        dataForTest = dataForTest + "," + String.valueOf(sign.getMaxSpeed())  + "," + String.valueOf(sign.getMinSpeedNotNull());
        dataForTest = dataForTest + "," + String.valueOf(sign.getMaxVelocityProjectionX())  + "," + String.valueOf(sign.getMinVelocityProjectionX());
        dataForTest = dataForTest + "," + String.valueOf(sign.getMaxVelocityProjectionY())  + "," + String.valueOf(sign.getMinVelocityProjectionY());

        Log.i("DATA_SET: ", dataForTest);
        return dataForTest;
    }


    private void GetExamples()
    {
        String data = FormatSignatureParams(signature);//getData


        if (exampleRemain%2==0)
        {
            SignatureUtils.WriteFile(data + ",true", getContext(), curUser.getCORPUS_FILE()); //write data in User_Corpus_File and mark it true
        }
        else
        {
            SignatureUtils.WriteFile(data + ",false", getContext(), curUser.getCORPUS_FILE()); //write data in User_Corpus_File and mark it true
        }

        exampleRemain--;
        if (exampleRemain == 0)
            NEWBYE_MODE = false;

        if (exampleRemain == 0)
            Toast.makeText(getContext(), "Enough. Now write again", Toast.LENGTH_SHORT).show();
        else
        {
            if (exampleRemain%2==0)
            {
                Toast.makeText(getContext(), "Now write true signature\n" + String.valueOf(exampleRemain) + " examples remain", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getContext(), "Now write fake signature\n" + String.valueOf(exampleRemain) + " examples remain", Toast.LENGTH_SHORT).show();
            }

        }
        ResetFragment();
    }



    private String KNN_5_MODE(String data, Context context)
    {
        String decision = "";
        try
        {
            File corpus = new File(getContext().getFilesDir() , curUser.getCORPUS_FILE());
            int size = SignatureUtils.ReadFile(curUser.getCORPUS_FILE(), getContext()).get(0).split(",").length - 1;
            Dataset dataset = FileHandler.loadDataset(corpus, size , ",");

            Classifier classifier = new KNearestNeighbors(5);
            classifier.buildClassifier(dataset);

            SignatureUtils.WriteFile(data, getContext(), "temporary.txt");
            File forClassification = new File(getContext().getFilesDir() , "temporary.txt");
            Dataset dataForClassification = FileHandler.loadDataset(forClassification, size-1 , ",");
            for (Instance inst : dataForClassification)
            {
                Object result = classifier.classify(inst);
                decision = result.toString();
                Log.i("DECISION: ", decision);
            }
            SignatureUtils.deleteFile(getContext(), "temporary.txt");
            return decision;

        } catch (Exception e)
        {
            e.printStackTrace();
            SignatureUtils.deleteFile(getContext(), "temporary.txt");
            return "";
        }
    }


    private String RangeClassifierMode(String data, Context context){
         try
         {
             File corpus = new File(context.getFilesDir() , curUser.getCORPUS_FILE());

             RangeClassifier classifier = new RangeClassifier();
             classifier.loadData(corpus.getPath());

             String line = data;
             String[] stringTemp = line.split(",");
             double[] doubleTemp = new double[stringTemp.length - 1];

             for (int iter = 0; iter < stringTemp.length - 1; iter++) {
                 doubleTemp[iter] = Double.parseDouble(stringTemp[iter]);
             }

             String decision = classifier.classify(doubleTemp);
             Log.i("DECISION: ", decision);

             return decision;

         } catch (Exception e)
         {
             e.printStackTrace();
             return "";
         }

    }


}
