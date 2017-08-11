package com.example.android.inventromcam;
/**
 * Created by Krishnendu on 11-08-2017.
 * Activity that handles the implementation of the explicit camera and the details form,
 * and finally storing the data into a text file and the image into a folder named Inventrom
 */

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;
import android.hardware.Camera.PictureCallback;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraTest extends AppCompatActivity {

    private CameraView mImageSurfaceView;
    private Camera camera;
    private String name,age,address,gender;
    private FrameLayout cameraPreviewLayout;;
    private ImageView capturedImageHolder;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //setting the screen orientation to the potrait mode

        cameraPreviewLayout = (FrameLayout) findViewById(R.id.camera_preview);//connecting the FrameLayout of the XML file
        capturedImageHolder = (ImageView) findViewById(R.id.captured_image); //connecting the ImageView of the XML file

        camera = checkDeviceCamera(); //checking whether the device has a camera
        mImageSurfaceView = new CameraView(CameraTest.this, camera); //Casting the image feed received on the screen
        cameraPreviewLayout.addView(mImageSurfaceView);

        Button captureButton = (Button) findViewById(R.id.button); //when the capture button is clicked
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, pictureCallback); //the in built take picture function is called

            }
        });
    }

    private Camera checkDeviceCamera() {
        Camera mCamera = null;
        try {
            mCamera = Camera.open();   //if device camera successfully opened
        } catch (Exception e) {
            e.printStackTrace();  //if problem arise during accessing the camera
        }
        return mCamera;   //return the camera object
    }

    //taking the image
    PictureCallback pictureCallback = new PictureCallback()
    {
        @Override
        public void onPictureTaken(byte[] data, Camera camera)
        {
            final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);  //image fetched is decoded as a byte array
            if (bitmap == null)   //if image is not captured
            {
                Toast.makeText(getApplicationContext(), "Captured image is empty", Toast.LENGTH_LONG).show(); //Failed to capture image
                return;
            }
            else       //upon successful capture of the image
            {
                Toast.makeText(getApplicationContext(), "Casting Image", Toast.LENGTH_LONG).show();

                //create the dialog box and connect with the dialog box detail_dialog
                dialog = new Dialog(CameraTest.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.details_dialog);
                dialog.show();     //display dialog box

                //connect with the dialog box detail_dialog through the synchronisation of the components of the dialog box

                final EditText temp_name=(EditText)dialog.findViewById(R.id.name);
                EditText temp_age=(EditText)dialog.findViewById(R.id.age);
                EditText temp_address=(EditText)dialog.findViewById(R.id.address);
                RadioButton male=(RadioButton)dialog.findViewById(R.id.male);
                RadioButton female=(RadioButton)dialog.findViewById(R.id.female);

                //fetching the details provided by the user and storing the details accordingly
                name=temp_name.getText().toString();
                age=temp_age.getText().toString();
                address=temp_address.getText().toString();
                if(male.isChecked())   //if male check box is enabled then :
                {
                    gender="Male";     //gender is male
                }
                else if(female.isChecked())   //if female check box is enabled then :
                {
                    gender="Female";      //gender is female
                }
                Button submit=(Button)dialog.findViewById(R.id.submit) ;   //connecting the submit button of the dialog box
                submit.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        //Saving the bitmap image captured to the imageView
                        saveImage(bitmap,temp_name.getText().toString());    //saving the bitmap image to the device's internal memory according to the name inputted by the user
                        capturedImageHolder.setImageBitmap(scaleDownBitmapImage(bitmap, 300, 200));  //casting the image to the imageview

                    }
                });

            }
        }

    };

    //Resizing the b3itmap image
    private Bitmap scaleDownBitmapImage(Bitmap bitmap, int newWidth, int newHeight)
    {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true); //Scaling according to the given width and height
        return resizedBitmap;    //return the resized bitmap image
    }
    public void saveImage(Bitmap bitmap,String name)   //saving the image to the device's internal memory
    {
        //getting the public shared directory and appending the name of the user directory
        File myDir = new File( Environment.getExternalStorageDirectory(),File.separator+"Inventrom");//Folder name where I want to save.
        Log.v("File path",": "+myDir);
        myDir.mkdirs(); //creating the directory

        String time=new SimpleDateFormat("yyyymmdd_HHmmss").format(new Date()); //fetching the device time
        String fname = name + time + ".jpg";        //file name is set as per the name received as input from the user followed by the device time
        File file = new File (myDir, fname);   //creating the new file as per the above mentioned rules
        if (file.exists ()) file.delete ();    //checking whether the file exists or not if yes then remove it
        try {
            FileOutputStream out = new FileOutputStream(file); //from here it goes to catch block if any error occurs
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);  //compressing the image
            out.flush();
            out.close();
            String[] paths = {file.toString()};
            String[] mimeTypes = {"/image/jpeg"};
            MediaScannerConnection.scanFile(this, paths, mimeTypes, null);  //media scanner scans to display the image in the device gallery
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
