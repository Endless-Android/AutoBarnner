package com.example.administrator.autobarnner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.autobarnner.View.C;
import com.example.administrator.autobarnner.View.ImageBarnnerFramLayout;
import com.example.administrator.autobarnner.View.ImageBarnnerViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements ImageBarnnerFramLayout.FramLayoutListen {

    private ImageBarnnerFramLayout image_viewGroup;
    private int [] Ids = new int[]{R.drawable.timg,R.drawable.kd,R.drawable.ic_launcher_background};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        C.WIDTH = displayMetrics.widthPixels;
        List<Bitmap> list = new ArrayList<>();
        image_viewGroup = findViewById(R.id.image_ViewGroup);
        for (int i = 0; i < Ids.length; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),Ids[i]);
            list.add(bitmap);
        }
        image_viewGroup.addBitmapView(list);
        image_viewGroup.setListen(this);
      /*  for (int i = 0; i < Ids.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView.setImageResource(Ids[i]);
            image_viewGroup.addView(imageView);
        }
         image_viewGroup.setListen(this);
        */


    }

    @Override
    public void clickImageIndex(int pos) {
        Log.i("aaaaa", "clickImageIndex: "+pos);

    }
}
