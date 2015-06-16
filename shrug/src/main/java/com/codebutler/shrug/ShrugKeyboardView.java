package com.codebutler.shrug;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class ShrugKeyboardView extends LinearLayout implements View.OnClickListener {

    public static final int KEYCODE_ACTION = -201;
    public static final int KEYCODE_LANGUAGE_SWITCH = -101;
    public static final int KEYCODE_SPACE = 32;

    private ActionKeyButton mButtonKeyAction;
    private KeyboardView.OnKeyboardActionListener mListener;

    public ShrugKeyboardView(Context context) {
        this(context, null);
    }

    public ShrugKeyboardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShrugKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ShrugKeyboardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        RepeatListener listener = new RepeatListener(400, 100, this);
        findViewById(R.id.button_key_shrug).setOnTouchListener(listener);
        findViewById(R.id.button_key_space).setOnTouchListener(listener);
        findViewById(R.id.button_key_delete).setOnTouchListener(listener);

        findViewById(R.id.button_key_switch).setOnClickListener(this);
        findViewById(R.id.button_key_action).setOnClickListener(this);

        mButtonKeyAction = (ActionKeyButton) findViewById(R.id.button_key_action);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_key_shrug:
                if (mListener != null) {
                    mListener.onText(getResources().getString(R.string.shrug));
                }
                break;
            case R.id.button_key_switch:
                if (mListener != null) {
                    mListener.onKey(KEYCODE_LANGUAGE_SWITCH, null);
                }
                break;
            case R.id.button_key_space:
                if (mListener != null) {
                    mListener.onKey(KEYCODE_SPACE, null);
                }
                break;
            case R.id.button_key_delete:
                if (mListener != null) {
                    mListener.onKey(Keyboard.KEYCODE_DELETE, null);
                }
                break;
            case R.id.button_key_action:
                if (mListener != null) {
                    mListener.onKey(KEYCODE_ACTION, null);
                }
                break;
        }
    }

    void setImeOptions(int options) {
        if (mButtonKeyAction != null) {
            mButtonKeyAction.setImeOptions(options);
        }
    }

    void setOnKeyboardActionListener(KeyboardView.OnKeyboardActionListener listener) {
        mListener = listener;
    }
}
