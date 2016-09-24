package com.codebutler.shrug;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

/**
 * A class, that can be used as a TouchListener on any view (e.g. a Button).
 * It cyclically runs a clickListener, emulating keyboard-like behaviour. First
 * click is fired immediately, next after initialInterval, and subsequent after
 * normalInterval.
 *
 * <p>Interval is scheduled after the onClick completes, so it has to run fast.
 * If it runs slow, it does not generate skipped onClicks.
 *
 * Based on code from here:
 * http://stackoverflow.com/questions/4284224/android-hold-button-to-repeat-action
 */
class RepeatListener implements OnTouchListener {

    private final int mInitialInterval;
    private final int mNormalInterval;

    private final OnClickListener mOnClickListener;

    private final Handler mHandler = new Handler();

    private final Runnable mHandlerRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(this, mNormalInterval);
            mOnClickListener.onClick(mDownView);
        }
    };

    @Nullable private View mDownView;

    /**
     * @param initialInterval The interval after first click event
     * @param normalInterval The interval after second and subsequent click events
     * @param clickListener The OnClickListener, that will be called periodically
     */
    RepeatListener(int initialInterval, int normalInterval, OnClickListener clickListener) {
        if (clickListener == null) {
            throw new IllegalArgumentException("null runnable");
        }
        if (initialInterval < 0 || normalInterval < 0) {
            throw new IllegalArgumentException("negative interval");
        }
        mInitialInterval = initialInterval;
        mNormalInterval = normalInterval;
        mOnClickListener = clickListener;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() > 1) {
            return false;
        }
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mHandler.removeCallbacks(mHandlerRunnable);
                mHandler.postDelayed(mHandlerRunnable, mInitialInterval);
                mDownView = view;
                mDownView.setPressed(true);
                mOnClickListener.onClick(view);
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mHandler.removeCallbacks(mHandlerRunnable);
                if (mDownView != null) {
                    mDownView.setPressed(false);
                    mDownView = null;
                }
                return true;
        }
        return false;
    }
}
