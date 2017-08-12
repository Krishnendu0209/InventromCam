package com.example.android.inventromcam;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Krishnendu on 11-08-2017.
 * Action fragment that handles the click event of the explicit camera
 */

public class Actions extends Fragment {
    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_actions, container, false); //connecting the fragment_actions (xml file)
        Button click=(Button)myView.findViewById(R.id.click); //connecting the click button of the xml file.
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pass the context and the Activity class you need to open from the Fragment Class, to the Intent
                Intent intent = new Intent(getContext(), CameraTest.class);//The CameraTest class has the explicit intent to implement the app's camera
                intent.setData(null); //no data required to be operated on
                startActivity(intent); //start activity
            }
        });

        return myView;  //returning the view
    }
}
