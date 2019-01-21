package com.example.student.lifecyclesdemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FragmentUpload extends Fragment{

    ImageView imageViews;
    TextView information;
    Bitmap mapa=null;

    private String info = "";
    private static FragmentUpload instance = null;

    public FragmentUpload(){

    }

    public static FragmentUpload newInstance(String infor) {

        if(instance == null){
            // new instance
            instance = new FragmentUpload();

            // sets data to bundle
            Bundle bundle = new Bundle();
            bundle.putString("info", infor);
            // set data to fragment
            instance.setArguments(bundle);

            return instance;
        } else {

            return instance;
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fragment_upload, container, false);
       imageViews = v.findViewById(R.id.imageView2);
       information = v.findViewById(R.id.textView13);
       imageViews.setImageResource(0);

        Main3Activity activity = (Main3Activity) getActivity();
        mapa = activity.getBitmapFromActivity();
        String testik = activity.getInfoFromActivity();

        if(testik!=null)
        information.setText(testik);

        if(mapa!=null)
        imageViews.setImageBitmap(mapa);
        else
            imageViews.setImageResource(0);

        return v;
    }
}
