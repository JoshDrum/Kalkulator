package com.example.student.lifecyclesdemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.annotations.Nullable;

import java.util.List;

public class FileListAdapter extends ArrayAdapter<ImageUpload> {

    private Context context;
    private int resource;
    private List<ImageUpload> listImage;
    String[] matches = new String[] {"jpg", "jpeg", "PNG", "png"};

    public FileListAdapter(@NonNull Context context, int resource, @NonNull List<ImageUpload> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        listImage = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        View v = inflater.inflate(resource,null);
        TextView tvName = v.findViewById(R.id.tvFilename);
        ImageView img = v.findViewById(R.id.obraz);
        String popis=listImage.get(position).getName();

        tvName.setText(popis);

        for (String s : matches)
        {
            if (popis.contains(s))
            {
                Glide.with(context).load(listImage.get(position).getUrl()).into(img);
                break;
            }
            else
                img.setImageResource(R.drawable.no);
        }

        /*if(popis.contains(JPG)){
            Glide.with(context).load(listImage.get(position).getUrl()).into(img);
        }else
        img.setImageResource(R.drawable.no);*/

        return v;
    }
}
