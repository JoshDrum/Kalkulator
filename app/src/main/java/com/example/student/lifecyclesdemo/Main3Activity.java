package com.example.student.lifecyclesdemo;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class Main3Activity extends AppCompatActivity implements View.OnClickListener,FragmentDownload.OnFragmentInteractionListener {

    private GestureDetectorCompat gestureObject;
    public static final String FB_STORAGE_PATH = "uploaded/";
    public static final String FB_DATABASE_PATH = "uploaded";
    private static final int PICKFILE_REQUEST_CODE = 33;
    DecimalFormat df = new DecimalFormat("####0.00");

    private Uri filePath;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    Bitmap bitmap = null;
    /*static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/";*/
    EditText myEditText;
    TextView fileView;
    Button buttonDownload,buttonUpload,buttonChoose;
    String nazevSouboru="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        buttonChoose = (Button) findViewById(R.id.open_btn);
        //findViewById(R.id.create_btn);
        buttonUpload = (Button)findViewById(R.id.query_btn);
        buttonUpload.setEnabled(false);
        //imageView=(ImageView) findViewById(R.id.imageView);
        //fileView = (TextView) findViewById(R.id.textView12);

        gestureObject = new GestureDetectorCompat(this, new Main3Activity.LearnGesture());
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            FragmentOne firstFragment = new FragmentOne();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }
    }

    public void changeFragment(View view){
        Fragment fragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if(view == findViewById(R.id.storage_btn)){
            fragment = new FragmentDownload();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
            buttonUpload.setEnabled(false);
        }
        /*if(view == findViewById(R.id.download_btn)){
            fragment = new FragmentUpload();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
        }*/
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

                //StorageReference riversRef = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + nazevSouboru );
            final StorageReference riversRef = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + nazevSouboru );
            UploadTask uploadTask = riversRef.putFile(filePath);

            //Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        //throw task.getException();
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_LONG);
                    }

                    // Continue with the task to get the download URL
                    return riversRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Dokončeno", Toast.LENGTH_LONG);
                        //uloz nazev a url do databaze
                        //String uploadId = mDatabaseRef.push().getKey();
                        //mDatabaseRef.child(uploadId).setValue(imageUpload);
                        ImageUpload imageUpload = new ImageUpload(nazevSouboru, downloadUri.toString());
                        //String uploadId = mDatabaseRef.push().getKey();
                        //mDatabaseRef.child(uploadId).setValue(imageUpload);
                        mDatabaseRef.push().setValue(imageUpload);
                        buttonUpload.setEnabled(false);
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage((int) progress + "% Nahráno..");
                }
            });


                        /*.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            riversRef.getDownloadUrl();
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Soubor nahrán", Toast.LENGTH_LONG);
                                ImageUpload imageUpload = new ImageUpload(nazevSouboru, taskSnapshot.getResult().toString()) ;
                                //taskSnapshot.getMetadata().getReference().getDownloadUrl().toString()
                                //uloz nazev a url do databaze
                                String uploadId = mDatabaseRef.push().getKey();
                                mDatabaseRef.child(uploadId).setValue(imageUpload);
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
                                progressDialog.setMessage((int) progress + "% Nahráno..");
                            }
                        });*/
        }else{

        }
    }

    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Open File"), PICKFILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentUpload uploadFr = new FragmentUpload();
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();

        if(requestCode == PICKFILE_REQUEST_CODE && resultCode == RESULT_OK && data!= null && data .getData()!= null){
           filePath = data.getData();
            bitmap=null;
            buttonUpload.setEnabled(true);
           //ziskani informaci o souboru
            nazevSouboru=getFileName(filePath);
            String prip = pripona(nazevSouboru);
            //odeslani informaci fragmentu
            Bundle b2 = new Bundle();
            b2.putString("info", "SELECTED : " + nazevSouboru + " SIZE : " + getRealSizeFromUri(filePath) + " kB ");
            uploadFr.setArguments(b2);
            t.replace(R.id.fragment_container, uploadFr);
            t.addToBackStack(null);
            t.commit();
           if(prip.equals("jpg") || prip.equals("jpeg") || prip.equals("PNG")){
               try {
                   bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
               } catch (IOException e) {
                   e.printStackTrace();
               }
                   /*Bundle b3 = new Bundle();
                   b3.putString("selected_image", image_url);
                   uploadFr.setArguments(b3);*/
           }
        }
    }

    String myString="";
    public Bitmap getMyData() {
        return bitmap;
    }

    public void onFragmentInteraction(Uri uri) {
        // The user selected the headline of an article from the HeadlinesFragment
        // Do something here to display that article
    }

    @Override
    public void onClick(View v) {
        if(v==buttonChoose){
            showFileChooser();
        }else if(v==buttonUpload){
            uploadFile();
        }else if(v==buttonDownload){

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
        try {
            String[] proj = { MediaStore.Audio.Media.SIZE };
            cursor = getContentResolver().query(uri, proj, null, null, null);
            //cursor = context.getContentResolver().query(uri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
            cursor.moveToFirst();
            value=(Double.parseDouble(cursor.getString(column_index)) * 0.0009765625);
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
                            Intent myIntent = new Intent(Main3Activity.this, MainActivity.class);
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
