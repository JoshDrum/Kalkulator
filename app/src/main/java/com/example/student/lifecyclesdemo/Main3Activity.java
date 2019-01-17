package com.example.student.lifecyclesdemo;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Collections;

public class Main3Activity extends AppCompatActivity implements View.OnClickListener{

    private GestureDetectorCompat gestureObject;
    private ImageView imageView;
    private static final int PICK_IMAGE_REQUEST = 234;
    private static final int PICKFILE_REQUEST_CODE = 33;
    DecimalFormat df = new DecimalFormat("####0.00");

    private Uri filePath;
    private StorageReference mStorageRef;
    /*static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/";*/
    EditText myEditText;
    TextView fileView;
    Button tl1,tl2,tl3,buttonUpload,buttonChoose;
    String nazevSouboru="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        buttonChoose = (Button) findViewById(R.id.open_btn);
        //findViewById(R.id.create_btn);
        buttonUpload = (Button)findViewById(R.id.query_btn);
        imageView=(ImageView) findViewById(R.id.imageView);
        fileView = (TextView) findViewById(R.id.textView12);

        gestureObject = new GestureDetectorCompat(this, new Main3Activity.LearnGesture());
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void uploadFile(){
        if(filePath !=null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Nahrávání..");
            progressDialog.show();

                StorageReference riversRef = mStorageRef.child("uploaded/" + nazevSouboru);
                riversRef.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Soubor nahrán", Toast.LENGTH_LONG);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),exception.getMessage(), Toast.LENGTH_LONG);
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                //kalkulace progresu uploadovani
                                double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                                progressDialog.setMessage(((int) progress) + "% Nahráno..");
                            }
                        });
        }else{

        }
    }

    private void showFileChooser(){
        Intent intent = new Intent();
        //intent.setType("image/*");
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Open File"), PICKFILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICKFILE_REQUEST_CODE && resultCode == RESULT_OK && data!= null && data .getData()!= null){
           filePath = data.getData();
            nazevSouboru=getFileName(filePath);
            String prip = pripona(nazevSouboru);
            //fileView.setText(filePath.getPath() + nazevSouboru);
            fileView.setText("SELECTED : " + nazevSouboru + " SIZE : " + getRealSizeFromUri(filePath) + " kB ");
           if(prip.equals("jpg") || prip.equals("jpeg") || prip.equals("PNG")){
               try {
                   Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                   imageView.setImageBitmap(bitmap);
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
        }
    }

    @Override
    public void onClick(View v) {
        if(v==buttonChoose){
            showFileChooser();
        }else if(v==buttonUpload){
            uploadFile();
        }
    }

    public String pripona(String fileName){

        String extension = "";

        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        if (i > p) {
            extension = fileName.substring(i+1);
        }
        return extension;
    }

    private String getRealSizeFromUri(Uri uri) {
        Cursor cursor = null;
        double value=0;
        String tmp="";
        try {
            String[] proj = { MediaStore.Audio.Media.SIZE };
            cursor = getContentResolver().query(uri, proj, null, null, null);
            //cursor = context.getContentResolver().query(uri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
            cursor.moveToFirst();
            //return cursor.getString(column_index);
            tmp = cursor.getString(column_index);
            value=(Double.parseDouble(tmp) * 0.0009765625);
            return df.format(value);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private final class LearnGesture extends GestureDetector.SimpleOnGestureListener{

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {

                        } else {
                            Intent myIntent = new Intent(Main3Activity.this, Main2Activity.class);
                            finish();
                            startActivity(myIntent);
                            overridePendingTransition(R.anim.slide_in_left,
                                    R.anim.slide_out_right);
                        }
                        result = true;
                    }
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        //onSwipeBottom();
                    } else {
                        //onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }
}
