package com.example.android.inventromcam;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.io.File;

/**
 * Created by Krishnendu on 12-Aug-17.
 * Fragment that handles and displays all the file names as a list, present in the user designated folder
 */

public class ShowList extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_show_list, container, false);
        TextView tv=(TextView)rootView.findViewById(R.id.name_list);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Documents_Inventrom".toString();  // path where files have been saved

        File f = new File(path);
        if (!f.exists ())   //if directory doesn't exist then create it
            f.mkdir();
        File file[] = f.listFiles();
        StringBuffer buffer = new StringBuffer();
        int count = 0;
        for (int i = file.length-1; i >=0; i--) {
            String.valueOf(count++);                                //indexing for files
            buffer.append(count);
            buffer.append(". ");
            buffer.append(file[i].getName());                       //add names of the files to the sting buffer
            buffer.append(System.getProperty("line.separator"));    // returns a new line
            buffer.append(System.getProperty("line.separator"));

            Log.d("Files", "FileName:" + file[i].getName());        //logging
        }
        tv.setText(buffer);                                         // buffer is saved to TEXTVIEW

        return rootView;
    }
}
