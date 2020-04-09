package com.closer.avatartestingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.VignetteSubfilter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {
    static {
        System.loadLibrary("NativeImageProcessor");
    }

    private ImageView im;
    private GestureDetector mDetector;
    List<Filter> filters;
    int i = 0;
    Bitmap mutableBitmap, workingBitmap, bitmap;
    BitmapDrawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        filters = FilterPack.getFilterPack(MainActivity.this);


        im = findViewById(R.id.combined_image);
        drawable = (BitmapDrawable) im.getDrawable();
        bitmap = drawable.getBitmap();
        workingBitmap = Bitmap.createBitmap(bitmap);
        mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
        mDetector = new GestureDetector(this, new MyGestureListener());

        // Add a touch listener to the view

        View.OnTouchListener touchListener = new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // pass the events to the gesture detector
                // a return value of true means the detector is handling it
                // a return value of false means the detector didn't
                // recognize the event
                return mDetector.onTouchEvent(event);

            }
        };
        im.setOnTouchListener(touchListener);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d("TAG", "onDown: ");

            // don't return false here or else none of the other
            // gestures will work
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.i("TAG", "onSingleTapConfirmed: ");
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.i("TAG", "onLongPress: ");
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.i("TAG", "onDoubleTap: ");
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            Log.i("TAG", "onScroll: ");
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            if (event1.getRawX() < event2.getRawX()) {
                im.setImageDrawable(getDrawable(R.drawable.image));
                bitmap = drawable.getBitmap();
                workingBitmap = Bitmap.createBitmap(bitmap);
                mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
                im.setImageBitmap(filters.get(i).processFilter(mutableBitmap));
                if (i > 0)
                    i--;
                Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
            } else {


                im.setImageDrawable(getDrawable(R.drawable.image));
                bitmap = drawable.getBitmap();
                workingBitmap = Bitmap.createBitmap(bitmap);
                mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
                im.setImageBitmap(filters.get(i).processFilter(mutableBitmap));
                if (i < filters.size())
                    i++;
                Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    }

}
