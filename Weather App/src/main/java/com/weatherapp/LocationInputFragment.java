package com.weatherapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

public class LocationInputFragment extends Fragment {

    ViewGroup rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_location, container, false);
        init(rootView);
        return rootView;
    }

    //Listener for the button
    OnButtonPressListener buttonListener;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            buttonListener = (OnButtonPressListener) getActivity();
        } catch(ClassCastException e){
            throw new ClassCastException(activity.toString());
        }
    }

    //Sets the listener, gets and sends the text to the main fragment
    void init(ViewGroup root){
        ImageView b = (ImageView) root.findViewById((R.id.searchButton));
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Finds the tet input area
                    EditText text = (EditText) rootView.findViewById(R.id.location_text);
                    //Converts the text to a string
                    String message = text.getText().toString();
                    //Sets the text area to ""
                    text.setText("");
                    //Send the text from the message on interface
                    buttonListener.onButtonPressed(message);
                }
            });
    }
}
