package com.arcsoft.sdk_demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

/**
 * Created by gqj3375 on 2017/4/28.
 */

public class Application extends android.app.Application {
    private final String TAG = this.getClass().toString();
    Uri mImage;

    @Override
    public void onCreate() {
        super.onCreate();
        mImage = null;
    }
    
}