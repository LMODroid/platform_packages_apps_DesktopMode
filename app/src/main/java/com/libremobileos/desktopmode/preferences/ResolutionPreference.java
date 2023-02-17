package com.libremobileos.desktopmode.preferences;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import androidx.preference.*;

import com.libremobileos.desktopmode.R;

public class ResolutionPreference extends Preference {
    protected final String TAG = getClass().getName();
    protected static final String CUSTOM_NS = "http://schemas.android.com/apk/res-auto";

    protected Context mContext;
    protected int mWidth = 0;
    protected int mHeight = 0;
    protected EditText mWidthText;
    protected EditText mHeightText;
    private int lastSelectionWidth, lastSelectionHeight;

    public ResolutionPreference(Context context) {
        this(context, null);
    }

    public ResolutionPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.preference_resolution);
        mContext = context;

        mWidth = attrs.getAttributeIntValue(CUSTOM_NS, "widthValue", 1280);
        mHeight = attrs.getAttributeIntValue(CUSTOM_NS, "heightValue", 720);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        holder.setDividerAllowedAbove(false);
        holder.setDividerAllowedBelow(false);
        mWidthText = (EditText) holder.findViewById(R.id.width_text_edit);
        mHeightText = (EditText) holder.findViewById(R.id.height_text_edit);
        mWidthText.setText(String.valueOf(mWidth));
        mWidthText.setSelection(Math.min(String.valueOf(mWidth).length(), lastSelectionWidth));
        mHeightText.setText(String.valueOf(mHeight));
        mHeightText.setSelection(Math.min(String.valueOf(mHeight).length(), lastSelectionHeight));
        mWidthText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try {
                    if (s.length() < 1) return;
                    setWidth(Integer.parseInt(s.toString()));
                } catch (Exception e) {
                    Log.e("ResolutionPreference", Log.getStackTraceString(e));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        mHeightText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try {
                    if (s.length() < 1) return;
                    setHeight(Integer.parseInt(s.toString()));
                } catch (Exception e) {
                    Log.e("ResolutionPreference", Log.getStackTraceString(e));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        mWidthText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    mWidthText.clearFocus();
                }
                return false;
            }
        });
        mHeightText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    mHeightText.clearFocus();
                }
                return false;
            }
        });
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setWidth(int width) {
        setWidth(width, true);
    }

    public void setWidth(int width, boolean notifyChanged) {
        if (notifyChanged && !callChangeListener(width)) return;
        if (mWidth == width) return;
        mWidth = width;
        if (notifyChanged) {
            lastSelectionWidth = mWidthText.getSelectionEnd();
            new Handler(Looper.getMainLooper()).post(() ->
                notifyChanged());
        }
    }

    public void setHeight(int height) {
        setHeight(height, true);
    }

    public void setHeight(int height, boolean notifyChanged) {
        if (notifyChanged && !callChangeListener(height)) return;
        if (mHeight == height) return;
        mHeight = height;
        if (notifyChanged) {
            lastSelectionHeight = mHeightText.getSelectionEnd();
            new Handler(Looper.getMainLooper()).post(() ->
                notifyChanged());
        }
    }
}
