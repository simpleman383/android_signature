package com.simpleman383.signature;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;
import static com.simpleman383.signature.SignatureUtils.USER_LIST_FILE;
import static com.simpleman383.signature.SignatureUtils.createEmptyFile;
import static com.simpleman383.signature.SignatureUtils.deleteFile;

/**
 * Created by Alex on 28.11.2016.
 */

public class OptionsFragment extends Fragment {

    private static final String CURRENT_USER = "CURRENT_USER";

    private Button mCreate;
    private EditText mNewUserName;
    private Button mSign;
    private Button mDeleteUser;
    private Spinner mUserList;

    private ArrayAdapter<String> dataAdapter;


    private User currentUser;
    private String currentUserName = "";
    private String newUserName = "";
    private List<String> userList = new ArrayList<>();


    public static OptionsFragment newInstance() {
        return new OptionsFragment();
    }




    private void updateSpinner()
    {
        userList = SignatureUtils.ReadFile(USER_LIST_FILE, getContext());
        dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, userList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter.notifyDataSetChanged();
        mUserList.setAdapter(dataAdapter);
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(CURRENT_USER, currentUserName);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.options_fragment, container, false);



        mSign = (Button)v.findViewById(R.id.sign_button);
        mSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (mUserList.getSelectedItem() !=null)
                {
                    currentUserName = mUserList.getSelectedItem().toString();
                }
                else
                {
                    currentUserName = "";
                }

                if (!currentUserName.isEmpty())
                {
                    currentUser = new User(currentUserName, getContext());
                    Intent i = DrawActivity.newIntent(getActivity(), currentUser);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(getContext(),"Please, choose a user", LENGTH_SHORT).show();
                }
            }
        });


        mNewUserName = (EditText)v.findViewById(R.id.new_user_name);
        mNewUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                newUserName = c.toString();
            }
            @Override
            public void afterTextChanged(Editable c) {

            }
        });

        mCreate = (Button)v.findViewById(R.id.create_button);
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mNewUserName.getText().toString();

                if (!name.isEmpty() && !userList.contains(name))
                {
                    SignatureUtils.WriteFile(newUserName, getContext(), USER_LIST_FILE);
                    createEmptyFile(getContext(), newUserName + "_CORPUS.txt");
                    updateSpinner();
                }
                else
                {
                    Toast.makeText(getContext(),"Error, empty name or user already exists", LENGTH_SHORT).show();
                }

            }
        });


        mUserList = (Spinner)v.findViewById(R.id.spinner);
        updateSpinner();

        if (savedInstanceState != null)
        {
            mUserList.setSelection(userList.indexOf(savedInstanceState.getString(CURRENT_USER)));
        }

        mUserList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentUserName = mUserList.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                currentUserName = "";
            }
        });





        mDeleteUser = (Button)v.findViewById(R.id.delete_button);
        mDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                if (!userList.isEmpty())
                {
                    SignatureUtils.deleteUserFromList(currentUserName, getContext());
                    SignatureUtils.deleteFile(getContext(), currentUserName+"_CORPUS.txt");
                    updateSpinner();
                }
                else
                {
                    Toast.makeText(getContext(),"Error, user list is empty", LENGTH_SHORT).show();
                }
            }
        });



        return v;
    }



}
