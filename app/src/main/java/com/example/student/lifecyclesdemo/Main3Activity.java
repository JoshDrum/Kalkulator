package com.example.student.lifecyclesdemo;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DecimalFormat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class Main3Activity extends AppCompatActivity implements View.OnClickListener,FragmentDownload.OnFragmentInteractionListener {

    public static final String FB_STORAGE_PATH = "uploaded/";
    public static final String FB_DATABASE_PATH = "uploaded";
    private static final int PICKFILE_REQUEST_CODE = 33;
    DecimalFormat df = new DecimalFormat("####0.00");

    private Uri filePath;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    Bitmap bitmap = null;
    Button buttonExit,buttonUpload,buttonChoose;
    String nazevSouboru="";
    String info="";

    private static final int NUM_PAGES = 3;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        buttonChoose = findViewById(R.id.open_btn);
        buttonExit= findViewById(R.id.exit_btn);
        buttonUpload = findViewById(R.id.query_btn);
        buttonUpload.setEnabled(false);
        buttonChoose.setOnClickListener(this);
        buttonExit.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);


        mPager = findViewById(R.id.viewpager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            int CurrentPossition = 0;
            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                /*Toast.makeText(Main3Activity.this,
                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();*/
                CurrentPossition = position;

                mPagerAdapter.notifyDataSetChanged();
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });
    }

    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //return new ScreenSlidePageFragment();
           switch (position) {
                case 0:
                    return Fragment.instantiate(getApplicationContext(), FragmentOne.class.getName());
                case 1:
                    return Fragment.instantiate(getApplicationContext(), FragmentDownload.class.getName());
                case 2:
                    return Fragment.instantiate(getApplicationContext(), FragmentUpload.class.getName());
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

    public void changeFragment(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if(view == findViewById(R.id.storage_btn)){
            mPager.setCurrentItem(0);
            mPager.setCurrentItem(1);
            buttonUpload.setEnabled(false);
        }
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

            final StorageReference riversRef = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + nazevSouboru );
            UploadTask uploadTask = riversRef.putFile(filePath);

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
                            ImageUpload imageUpload = new ImageUpload(nazevSouboru, downloadUri.toString());
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
        }else{

        }
    }

    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent,"Open File"), PICKFILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentUpload uploadFr = new FragmentUpload();
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();

        if(requestCode == PICKFILE_REQUEST_CODE && resultCode == RESULT_OK && data!= null && data.getData()!= null){
           filePath = data.getData();
            bitmap=null;
            buttonUpload.setEnabled(true);
           //ziskani informaci o souboru
            nazevSouboru=getFileName(filePath);
            String prip = pripona(nazevSouboru);
            info = nazevSouboru + " SIZE : " + getRealSizeFromUri(filePath) + " kB ";

           if(prip.equals("jpg") || prip.equals("jpeg") || prip.equals("PNG")){
               try {
                   bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }else{
               bitmap = BitmapFactory.decodeResource(this.getResources(),
                       R.drawable.no);
           }
            mPager.setCurrentItem(0);
            mPager.setCurrentItem(2);
        }
    }

    //odeslani bitmapy fragmentu
    public Bitmap getBitmapFromActivity() {
        return bitmap;
    }

    //odeslani informaci fragmentu
    public String getInfoFromActivity() {
        return info;
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
        }else if(v==buttonExit){
            Intent myIntent = new Intent(Main3Activity.this, MainActivity.class);
            finish();
            startActivity(myIntent);
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
}
