/*
 * Copyright (C) 2012 The Android Open Source Project
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
 * limitations under the License
 */

package com.buzztimer.timer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buzztimer.timer.R;


public class TimerView extends LinearLayout {

    private TextView mHoursOnes;
    private TextView mMinutesTens, mMinutesOnes;
    private TextView mSecondsTens, mSecondsOnes;
    private TextView mHoursLabel;
    private TextView mMinutesLabel;
    private TextView mSecondsLabel;
    private final Typeface mAndroidClockMonoThin;
    private Typeface mOriginalHoursTypeface;
    private Typeface mOriginalMinutesTypeface;
    private Typeface mOriginalSecondsTypeface;
    private final int mWhiteColor, mGrayColor, mDisabledColor;

    @SuppressWarnings("unused")
    public TimerView(Context context) {
        this(context, null);
    }

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mAndroidClockMonoThin =
                Typeface.createFromAsset(context.getAssets(), "fonts/AndroidClockMono-Thin.ttf");

        Resources resources = context.getResources();
        mWhiteColor = resources.getColor(R.color.clock_white);
        mGrayColor = resources.getColor(R.color.clock_gray);
        mDisabledColor = resources.getColor(R.color.clock_disabled);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mHoursOnes = (TextView) findViewById(R.id.hours_ones);
        if (mHoursOnes != null) {
            mOriginalHoursTypeface = mHoursOnes.getTypeface();
        }        
        
        mMinutesTens = (TextView) findViewById(R.id.minutes_tens);
        if (mHoursOnes != null && mMinutesTens != null) {
            addStartPadding(mMinutesTens);
        }        
        mMinutesOnes = (TextView) findViewById(R.id.minutes_ones);
        if (mMinutesOnes != null) {
            mOriginalMinutesTypeface = mMinutesOnes.getTypeface();
        }
        
        mSecondsTens = (TextView) findViewById(R.id.seconds_tens);
        if (mMinutesOnes != null && mSecondsTens != null) {
            addStartPadding(mSecondsTens);
        }
        mSecondsOnes = (TextView) findViewById(R.id.seconds_ones);
        if (mSecondsOnes != null) {
            mOriginalSecondsTypeface = mSecondsOnes.getTypeface();
        }

        
        mHoursLabel = (TextView) findViewById(R.id.hours_label);
        mMinutesLabel = (TextView) findViewById(R.id.minutes_label);
        mSecondsLabel = (TextView) findViewById(R.id.seconds_label);
    }

    /**
     * Measure the text and add a start padding to the view
     * @param textView view to measure and onb to which add start padding
     */
    private void addStartPadding(TextView textView) {
        final float gapPadding = 0.45f;
        // allDigits will contain ten digits: "0123456789" in the default locale
        String allDigits = String.format("%010d", 123456789);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textView.getTextSize());
        paint.setTypeface(textView.getTypeface());

        float widths[] = new float[allDigits.length()];
        int ll = paint.getTextWidths(allDigits, widths);
        int largest = 0;
        for (int ii = 1; ii < ll; ii++) {
            if (widths[ii] > widths[largest]) {
                largest = ii;
            }
        }
        // Add left padding to the view - Note: layout inherits LTR
        textView.setPadding((int) (gapPadding * widths[largest]), 0, 0, 0);
    }


    public void setTime(int hoursOnesDigit, int minutesTensDigit,
                        int minutesOnesDigit, int secondsTensDigit, int secondsOnesDigit, int inputPointer) {
    	
        if (mHoursOnes != null) {
        	
        	mHoursOnes.setText(String.format("%d", hoursOnesDigit));
        	mHoursOnes.setTypeface(mOriginalHoursTypeface);
        	
        	if(inputPointer==4)
        		mHoursOnes.setTextColor(mWhiteColor);
        	else
        		mHoursOnes.setTextColor(mDisabledColor);
        }

        if (mHoursLabel != null) {
        	
        	if(inputPointer==4)
        		mHoursLabel.setTextColor(mWhiteColor);
        	else
        		mHoursLabel.setTextColor(mDisabledColor);
        }
        
        if (mMinutesTens != null) {
           
        	mMinutesTens.setText(String.format("%d", minutesTensDigit));
        	mMinutesTens.setTypeface(mOriginalMinutesTypeface);

        	if(inputPointer>=3)
        		mMinutesTens.setTextColor(mWhiteColor);
        	else
        		mMinutesTens.setTextColor(mDisabledColor);
        }
        if (mMinutesOnes != null) {
        	mMinutesOnes.setText(String.format("%d", minutesOnesDigit));
        	mMinutesOnes.setTypeface(mOriginalMinutesTypeface);

        	if(inputPointer>=2)
        		mMinutesOnes.setTextColor(mWhiteColor);
        	else
        		mMinutesOnes.setTextColor(mDisabledColor);
        }

        if (mMinutesLabel != null) {
        	
        	if(inputPointer>=2)
        		mMinutesLabel.setTextColor(mWhiteColor);
        	else
        		mMinutesLabel.setTextColor(mDisabledColor);
        }

        if (mSecondsTens != null) {
        	mSecondsTens.setText(String.format("%d", secondsTensDigit));
            mSecondsTens.setTypeface(mOriginalSecondsTypeface);
            
            if(inputPointer>=1)
        		mSecondsTens.setTextColor(mWhiteColor);
        	else
        		mSecondsTens.setTextColor(mDisabledColor);
            
        }
        if (mSecondsOnes != null) {
        	mSecondsOnes.setText(String.format("%d", secondsOnesDigit));
            mSecondsOnes.setTypeface(mOriginalSecondsTypeface);

            if(inputPointer>=0)
        		mSecondsOnes.setTextColor(mWhiteColor);
        	else
        		mSecondsOnes.setTextColor(mDisabledColor);
        }

        if (mSecondsLabel != null) {
        	
        	if(inputPointer>=0)
        		mSecondsLabel.setTextColor(mWhiteColor);
        	else
        		mSecondsLabel.setTextColor(mDisabledColor);
        }
        
    }
}
