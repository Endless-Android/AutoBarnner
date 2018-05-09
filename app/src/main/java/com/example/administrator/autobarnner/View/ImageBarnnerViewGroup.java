package com.example.administrator.autobarnner.View;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;


import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Eddie on 2018/5/8.
 */

public class ImageBarnnerViewGroup extends ViewGroup {

    private int mChildren;  //子视图个数
    private int mChildHeight;    //子视图高度
    private int mChildWidth;     //子视图宽度
    private int mX; //第一次按下的横坐标，以及每次移动过程中，移动之前的横坐标
    private  int mY;
    private int mIndex = 0; //索引
    private Scroller mScroller;
    private Timer mTimer = new Timer();
    private TimerTask mTimerTask;
    private boolean isAuto = true;
    private boolean isClick ;

    public ImageBarnnerListen getListen() {
        return listen;
    }

    public void setListen(ImageBarnnerListen listen) {
        this.listen = listen;
    }

    public ImageBarnnerListen listen;

    public ImageBarnnerViewGroupDotListen getSelectListen() {
        return selectListen;
    }

    public void setSelectListen(ImageBarnnerViewGroupDotListen selectListen) {
        this.selectListen = selectListen;
    }

    private ImageBarnnerViewGroupDotListen selectListen;

    public interface ImageBarnnerViewGroupDotListen{
        void selectDot(int index);
    }


    public interface ImageBarnnerListen{
        void clickImageIndex(int pos);
    }


    private Handler autoHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){
               case 0:
                   if(++mIndex >= mChildren){
                       mIndex = 0;
                   }
                   scrollTo(mChildWidth * mIndex,0);
                   selectListen.selectDot(mIndex);
                   break;

           }
        }
    };

    private void starAuto(){
        isAuto = true;
    }


    private void stopAuto(){
        isAuto = false;
    }

    public ImageBarnnerViewGroup(Context context) {
        super(context);
        initobj();
    }



    public ImageBarnnerViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initobj();
    }

    public ImageBarnnerViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initobj();
    }


    private void initobj() {
        mScroller = new Scroller(getContext());
        mTimerTask = new TimerTask() {
            @Override
            public void run() {

                if(isAuto){
                    autoHandler.sendEmptyMessage(0);
                }
            }
        };
        mTimer.schedule(mTimerTask,100,2000);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
           scrollTo(mScroller.getCurrX(),0);
           invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //1 求出子视图的个数
        mChildren  = getChildCount();
        if (0 == mChildren){
            setMeasuredDimension(0,0);
        }else {
            //求出测试子视图的宽度和高度
            measureChildren(widthMeasureSpec,heightMeasureSpec);
            View view = getChildAt(0);  //获取第一个子视图
            mChildWidth = view.getMeasuredWidth();
            mChildHeight = view.getMeasuredHeight();
            int width = view.getMeasuredWidth() * mChildren;
            //根据子视图的宽度和高度去设置ViewGroup的宽度和高度
            setMeasuredDimension(width, mChildHeight);
        }
    }

    /**
     *
     * @param change 当ViewGroup的位置发生改变时为true
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean change, int l, int t, int r, int b) {

        if(change){
            int leftMargin = 0;
            for (int i = 0; i < mChildren; i++) {
                View view = getChildAt(i);
                view.layout(leftMargin,0,mChildWidth+leftMargin,mChildHeight);
                leftMargin += mChildWidth;

            }
        }
    }


    /**
     * @param ev
     * @return 返回true  消费事件，返回false 不处理，继续向下传递
     * 如果返回true 真正处理事件的是onTouchEvent()方法
     *
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                stopAuto();
                isClick = true;
                if(!mScroller.isFinished()){
                    mScroller.abortAnimation();
                }
                mX = (int) event.getX();
                break;

            case MotionEvent.ACTION_MOVE:
                isClick = false;
                int moveX = (int) event.getX();
                int distance = moveX - mX;
                scrollBy(-distance,0);
                mX = moveX;
                break;

            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                mIndex = (scrollX + mChildWidth / 2) / mChildWidth;
                if(mIndex < 0 ){
                    mIndex = 0; //滑到最左边
                }else if(mIndex > mChildren - 1){
                    mIndex = mChildren-1;   //滑到最右边
                }

                if(isClick){
                    listen.clickImageIndex(mIndex);
                }else {
                    // scrollTo(mChildWidth * mIndex,0);
                    int dx = mIndex * mChildWidth - scrollX;
                    mScroller.startScroll(scrollX,0,dx,0);
                    postInvalidate();
                    selectListen.selectDot(mIndex);
                }

                starAuto();
                break;
            default:
                break;
        }
        return true; //告诉ViewGroup的父View  这里已经处理了该事件
    }
}
