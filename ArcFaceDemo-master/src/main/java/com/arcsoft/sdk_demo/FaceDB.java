package com.arcsoft.sdk_demo;

import android.util.Log;

import com.arcsoft.facerecognition.AFR_FSDKEngine;
import com.arcsoft.facerecognition.AFR_FSDKError;
import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.arcsoft.facerecognition.AFR_FSDKVersion;
import com.guo.android_extend.java.ExtInputStream;
import com.guo.android_extend.java.ExtOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gqj3375 on 2017/7/11.
 */

public class FaceDB {
	private final String TAG = this.getClass().toString();

	public static String appid = "29LtP4pCXEEcKvGSBcLojjG4kEgtUnT2XXvQ6bnN3PL6";
	public static String ft_key = "Gbrwwsvh3gdseQ66RtSF83quAQorgYCEsdZgNspNwaeF";
	public static String fd_key = "Gbrwwsvh3gdseQ66RtSF83r2Kp51L2hYt4f3XJwDjMc4";
	public static String fr_key = "Gbrwwsvh3gdseQ66RtSF83rWyR7jVyLkBk1p657jVvVV";
	public static String age_key = "Gbrwwsvh3gdseQ66RtSF83rmJDe32QG4AdzFiLQBWF2a";
	public static String gender_key = "Gbrwwsvh3gdseQ66RtSF83rtTcuDYfRYCKsDsXPBSabz";

	String mDBPath;
	AFR_FSDKEngine mFREngine;
	AFR_FSDKVersion mFRVersion;
	boolean mUpgrade;


	public FaceDB(String path) {
		mDBPath = path;
		mFRVersion = new AFR_FSDKVersion();
		mUpgrade = false;
		mFREngine = new AFR_FSDKEngine();
		AFR_FSDKError error = mFREngine.AFR_FSDK_InitialEngine(FaceDB.appid, FaceDB.fr_key);
		if (error.getCode() != AFR_FSDKError.MOK) {
			Log.e(TAG, "AFR_FSDK_InitialEngine fail! error code :" + error.getCode());
		} else {
			mFREngine.AFR_FSDK_GetVersion(mFRVersion);
			Log.d(TAG, "AFR_FSDK_GetVersion=" + mFRVersion.toString());
		}
	}

	public void destroy() {
		if (mFREngine != null) {
			mFREngine.AFR_FSDK_UninitialEngine();
		}
	}
	public boolean upgrade() {
		return false;
	}

}
