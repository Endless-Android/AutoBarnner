package com.example.administrator.autobarnner.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.autobarnner.R;

import java.util.List;

/**
 * Created by Eddie on 2018/5/8.
 */

public class ImageBarnnerFramLayout extends FrameLayout  implements ImageBarnnerViewGroup.ImageBarnnerViewGroupDotListen,ImageBarnnerViewGroup.ImageBarnnerListen{

    private ImageBarnnerViewGroup imageBarnnerViewGroup;
    private LinearLayout linearLayout;

    public FramLayoutListen getListen() {
        return listen;
    }

    public void setListen(FramLayoutListen listen) {
        this.listen = listen;
    }

    private FramLayoutListen listen;

    public ImageBarnnerFramLayout(@NonNull Context context) {
        super(context);
        initImageBarnnerViewGroup();
        initDotLinearLayout();
    }



    public ImageBarnnerFramLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initImageBarnnerViewGroup();
        initDotLinearLayout();
    }

    public ImageBarnnerFramLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initImageBarnnerViewGroup();
        initDotLinearLayout();
    }

    private void initImageBarnnerViewGroup() {
        imageBarnnerViewGroup = new ImageBarnnerViewGroup(getContext());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        imageBarnnerViewGroup.setLayoutParams(lp);
        imageBarnnerViewGroup.setSelectListen(this);
        imageBarnnerViewGroup.setListen(this);
        addView(imageBarnnerViewGroup);
    }

    private void initDotLinearLayout() {
        linearLayout = new LinearLayout(getContext());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 40);
        linearLayout.setLayoutParams(lp);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setBackgroundColor(Color.RED);
        addView(linearLayout);
        FrameLayout.LayoutParams layoutParams = (LayoutParams) linearLayout.getLayoutParams();
        layoutParams.gravity = Gravity.BOTTOM;
        linearLayout.setLayoutParams(layoutParams);
        if(Build.VERSION.SDK_INT>=11){
            linearLayout.setAlpha(0.5f);
        }else{
            linearLayout.getBackground().setAlpha(100);
        }


    }

    public void addBitmapView(List<Bitmap> list) {
        for (int i = 0; i < list.size(); i++) {
            Bitmap bitmap = list.get(i);
            addBitmapToBarnner(bitmap);
            addDotTOBarnner();
        }
    }

    private void addDotTOBarnner() {
        ImageView imageView = new ImageView(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5,5,5,5);
        imageView.setLayoutParams(lp);
        imageView.setImageResource(R.drawable.dot_normal);
        linearLayout.addView(imageView);
    }

    private void addBitmapToBarnner(Bitmap bitmap) {
        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(C.WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setImageBitmap(bitmap);
        imageBarnnerViewGroup.addView(imageView);
    }


    @Override
    public void selectDot(int index) {
        int count = linearLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            ImageView iv = (ImageView) linearLayout.getChildAt(i);
            if(i == index){
                iv.setImageResource(R.drawable.dot_select);
            }else{
                iv.setImageResource(R.drawable.dot_normal);
            }
        }
    }

    @Override
    public void clickImageIndex(int pos) {
        listen.clickImageIndex(pos);
    }

    public interface FramLayoutListen{
        void clickImageIndex(int pos);
    }
}
