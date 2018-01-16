/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.buzztimer.timer;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

/**
 * DeskClock clock view for desk docks.
 */
public class TimerActivity extends Activity {
	private static final String TAG = "TimerActivity";
    
//    @Override
//    public void onNewIntent(Intent newIntent) {
//        super.onNewIntent(newIntent);
//        if (DEBUG) Log.d(LOG_TAG, "onNewIntent with intent: " + newIntent);
//
//        // update our intent so that we can consult it to determine whether or
//        // not the most recent launch was via a dock event
//        setIntent(newIntent);
//
//        // Timer receiver may ask to go to the timers fragment if a timer expired.
//        int tab = newIntent.getIntExtra(SELECT_TAB_INTENT_EXTRA, -1);
//        if (tab != -1) {
//            if (mActionBar != null) {
//                mActionBar.setSelectedNavigationItem(tab);
//            }
//        }
//    }

    @Override
    protected void onCreate(Bundle icicle) {
    	Log.d(TAG , "TimerActivity:onCreate()");
        super.onCreate(icicle);

        setContentView(R.layout.activity_timer);
        
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container); 
        if (fragment == null) { 
        	fragment = new TimerFragment(); 
        	fm.beginTransaction()
        	.add(R.id.fragment_container, fragment) 
        	.commit(); 
        }
        
        AppRater.query_metrics(this);
//        AppRater.showRateDialog(this, null);  //Uncomment to debug AppRater
    }

    @Override
    protected void onResume() {
    	Log.d(TAG , "TimerActivity:onResume()");
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Timers.NOTIF_APP_OPEN, true);
        editor.apply();
        Intent timerIntent = new Intent();
        timerIntent.setAction(Timers.NOTIF_IN_USE_CANCEL);
        sendBroadcast(timerIntent);
    }

    @Override
    public void onPause() {
    	Log.d(TAG , "TimerActivity:onPause()");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Timers.NOTIF_APP_OPEN, false);
        editor.apply();
        Utils.showInUseNotifications(this);

        super.onPause();
    }

    public static abstract class OnTapListener implements OnTouchListener {
        private float mLastTouchX;
        private float mLastTouchY;
        private long mLastTouchTime;
        private final TextView mMakePressedTextView;
        private final int mPressedColor, mGrayColor;
        private final float MAX_MOVEMENT_ALLOWED = 20;
        private final long MAX_TIME_ALLOWED = 500;

        public OnTapListener(Activity activity, TextView makePressedView) {
            mMakePressedTextView = makePressedView;
            mPressedColor = activity.getResources().getColor(Utils.getPressedColorId());
            mGrayColor = activity.getResources().getColor(Utils.getGrayColorId());
        }

        @Override
        public boolean onTouch(View v, MotionEvent e) {
            switch (e.getAction()) {
                case (MotionEvent.ACTION_DOWN):
                    mLastTouchTime = Utils.getTimeNow();
                    mLastTouchX = e.getX();
                    mLastTouchY = e.getY();
                    if (mMakePressedTextView != null) {
                        mMakePressedTextView.setTextColor(mPressedColor);
                    }
                    break;
                case (MotionEvent.ACTION_UP):
                    float xDiff = Math.abs(e.getX()-mLastTouchX);
                    float yDiff = Math.abs(e.getY()-mLastTouchY);
                    long timeDiff = (Utils.getTimeNow() - mLastTouchTime);
                    if (xDiff < MAX_MOVEMENT_ALLOWED && yDiff < MAX_MOVEMENT_ALLOWED
                            && timeDiff < MAX_TIME_ALLOWED) {
                        if (mMakePressedTextView != null) {
                            v = mMakePressedTextView;
                        }
                        processClick(v);
                        resetValues();
                        return true;
                    }
                    resetValues();
                    break;
                case (MotionEvent.ACTION_MOVE):
                    xDiff = Math.abs(e.getX()-mLastTouchX);
                    yDiff = Math.abs(e.getY()-mLastTouchY);
                    if (xDiff >= MAX_MOVEMENT_ALLOWED || yDiff >= MAX_MOVEMENT_ALLOWED) {
                        resetValues();
                    }
                    break;
                default:
                    resetValues();
            }
            return false;
        }

        private void resetValues() {
            mLastTouchX = -1*MAX_MOVEMENT_ALLOWED + 1;
            mLastTouchY = -1*MAX_MOVEMENT_ALLOWED + 1;
            mLastTouchTime = -1*MAX_TIME_ALLOWED + 1;
            if (mMakePressedTextView != null) {
                mMakePressedTextView.setTextColor(mGrayColor);
            }
        }

        protected abstract void processClick(View v);
    }

//    /** Called by the LabelDialogFormat class after the dialog is finished. **/
//    @Override
//    public void onDialogLabelSet(TimerObj timer, String label, String tag) {
//        Fragment frag = getFragmentManager().findFragmentByTag(tag);
//        if (frag instanceof TimerFragment) {
//            ((TimerFragment) frag).setLabel(timer, label);
//        }
//    }
}
