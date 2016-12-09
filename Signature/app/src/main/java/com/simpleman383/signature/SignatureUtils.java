package com.simpleman383.signature;

import android.content.Context;
import android.graphics.PointF;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Created by Alex on 29.11.2016.
 */

public class SignatureUtils {
    public static String USER_LIST_FILE = "SIGNATURE_USER_LIST.txt";

    static void WriteFile(String information, Context context, String filename)
    {
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_APPEND);
            outputStream.write((information+"\n").getBytes()  );
            outputStream.close();
            Log.i("SUCCESS: ", "data was written");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static List<String> ReadFile(String filename, Context context)
    {
        List data = new ArrayList<String>();

        try {
            InputStream inputStream = context.openFileInput(filename);

            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    data.add(line);
                }

                inputStream.close();
                Log.i("DATA_READ: ", data.toString());
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return data;
    }



    static void ReWriteFile(List<String> information, Context context, String filename)
    {
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            for (String line: information) {
                outputStream.write((line + "\n").getBytes());
            }
            outputStream.close();
            Log.i("SUCCESS: ", "data was rewritten");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    static void deleteUserFromList(String username, Context context){
        List<String> userList = ReadFile(USER_LIST_FILE, context);
        List<String> newList = new ArrayList<>();

        for (String user : userList) {
           if (!username.equals(user)) {
               newList.add(user);
            }
        }

        ReWriteFile(newList, context, USER_LIST_FILE);

    }





    static void createEmptyFile(Context context, String filename)
    {
        try {
            FileOutputStream  outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void deleteFile(Context context, String filename)
    {
        File file = new File(context.getFilesDir() , filename);
        boolean deleted = file.delete();
        if (deleted)
            Log.i("SUCCESS: ", "File "+filename+" was deleted");
        else
            Log.i("ERROR ", "File "+filename+" was not deleted");
    }
}
