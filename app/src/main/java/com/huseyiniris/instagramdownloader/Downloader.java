package com.huseyiniris.instagramdownloader;


import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;


public class Downloader extends Fragment{

    ImageView image;
    TextView imageUrl;
    ImageTasker task;
    EditText urlEt;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    Button getImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_downloader,container,false);
        image = (ImageView) v.findViewById(R.id.image);
        getImage = (Button) v.findViewById(R.id.getImage);
        imageUrl = (TextView) v.findViewById(R.id.url);
        builderURL();


        imageUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUrl.getText().toString().startsWith("http") || urlEt.getText().toString().startsWith("https") || urlEt.getText().toString().startsWith("www")
                        && !imageUrl.getText().toString().contains("/'")){
                    task = new ImageTasker(getActivity(),image,urlEt.getText().toString());
                    task.execute();
                }
                else {
                    Toast.makeText(getActivity(), "Check Url", Toast.LENGTH_SHORT).show();
                }

                if (image.getDrawable() != null){
                    saveImage();
                    Gallery fragment = new Gallery();
                    fragment.getFromSdcard();
                    Toast.makeText(getActivity(), "Image Saved", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return v;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private void saveImage() {
        Date date = new Date();
        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        File filename;
        try {
            String path = Environment.getExternalStorageDirectory().toString();

            new File(path + "/InstagramDownloader").mkdirs();
            filename = new File(path + "/InstagramDownloader/"+date.getTime()+".jpg");
            FileOutputStream out = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), filename.getAbsolutePath(), filename.getName(), filename.getName());
            Toast.makeText(getActivity(), "Image has saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void builderURL() {

        urlEt = new EditText(getActivity());
        builder = new AlertDialog.Builder(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        urlEt.setLayoutParams(lp);
        builder.setView(urlEt);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                imageUrl.setText(urlEt.getText().toString());
                dialog.dismiss();


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setTitle("Enter URL");
        dialog = builder.create();
    }
}
