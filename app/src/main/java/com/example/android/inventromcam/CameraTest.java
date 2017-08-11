package com.example.android.inventromcam;

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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.hardware.Camera.PictureCallback;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraTest extends AppCompatActivity {

    private CameraView mImageSurfaceView;
    private Camera camera;

    private FrameLayout cameraPreviewLayout;
    private ImageView capturedImageHolder;

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
            mCamera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mCamera;
    }

    //taking the image
    PictureCallback pictureCallback = new PictureCallback()
    {
        @Override
        public void onPictureTaken(byte[] data, Camera camera)
        {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (bitmap == null)   //if image is not captured
            {
                Toast.makeText(getApplicationContext(), "Captured image is empty", Toast.LENGTH_LONG).show(); //Failed to capture image
                return;
            }
            else       //upon successful capture of the image
            {
                Toast.makeText(getApplicationContext(), "Casting Image", Toast.LENGTH_LONG).show();
                //Saving the bitmap image captured to the imageview
                saveImage(bitmap);
                capturedImageHolder.setImageBitmap(scaleDownBitmapImage(bitmap, 300, 200));
            }



        }

    };

    //Resizing the b3itmap image
    private Bitmap scaleDownBitmapImage(Bitmap bitmap, int newWidth, int newHeight)
    {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true); //Scaling according to the given width and height
        return resizedBitmap;
    }
    public void saveImage(Bitmap bitmap)
    {
        //getting the public shared directory and appending the name of the user directory
        File myDir = new File( Environment.getExternalStorageDirectory(),File.separator+"Inventrom");//Folder name where I want to save.
        Log.v("File path",": "+myDir);
        myDir.mkdirs(); //creating the directory

        String time=new SimpleDateFormat("yyyymmdd_HHmmss").format(new Date()); //fetching the device time
        String fname = time + "-.jpg";        //file name is set as per the system time
        File file = new File (myDir, fname);   //creating the new file as per the above mentioned rules
        if (file.exists ()) file.delete ();    //checking whether the file exists or not if yes then remove it
        try {
            FileOutputStream out = new FileOutputStream(file); //from here it goes to catch block if any error occurs
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            String[] paths = {file.toString()};
            String[] mimeTypes = {"/image/jpeg"};
            MediaScannerConnection.scanFile(this, paths, mimeTypes, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
