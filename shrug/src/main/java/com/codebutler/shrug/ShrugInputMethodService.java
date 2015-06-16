/*
 * Copyright (C) 2008-2009 The Android Open Source Project
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

package com.codebutler.shrug;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

public class ShrugInputMethodService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private InputMethodManager mInputMethodManager;
    private ShrugKeyboardView mKeyboardView;

    public static final int IME_ACTION_CUSTOM_LABEL = EditorInfo.IME_MASK_ACTION + 1;

    @Override
    public void onCreate() {
        super.onCreate();

        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    }

    @Override
    @SuppressLint("InflateParams")
    public View onCreateInputView() {
        mKeyboardView = (ShrugKeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        mKeyboardView.setOnKeyboardActionListener(this);

        EditorInfo info = getCurrentInputEditorInfo();
        if (info != null) {
            mKeyboardView.setImeOptions(info.imeOptions);
        }

        return mKeyboardView;
    }

    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);
        if (mKeyboardView != null) {
            mKeyboardView.setImeOptions(attribute.imeOptions);
        }
    }

    @Override
    public void onPress(int primaryCode) { }

    @Override
    public void onRelease(int primaryCode) { }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                InputConnection inputConnection = getCurrentInputConnection();
                String shrug = getString(R.string.shrug);
                if (inputConnection.getTextBeforeCursor(shrug.length(), 0).equals(shrug)) {
                    inputConnection.deleteSurroundingText(shrug.length(), 0);
                } else {
                    keyDownUp(KeyEvent.KEYCODE_DEL);
                }
                break;
            case ShrugKeyboardView.KEYCODE_LANGUAGE_SWITCH:
                handleLanguageSwitch();
                break;
            case ShrugKeyboardView.KEYCODE_ACTION:
                handleAction();
                break;
            default:
                handleCharacter(primaryCode, keyCodes);
                break;
        }
    }

    private void handleAction() {
        InputConnection inputConnection = getCurrentInputConnection();
        EditorInfo editorInfo = getCurrentInputEditorInfo();
        int actionId = getImeOptionsActionIdFromEditorInfo(editorInfo);
        if (IME_ACTION_CUSTOM_LABEL == actionId) {
            inputConnection.performEditorAction(editorInfo.actionId);
        } else if (actionId != EditorInfo.IME_ACTION_NONE) {
            inputConnection.performEditorAction(actionId);
        } else {
            keyDownUp(KeyEvent.KEYCODE_ENTER);
        }
    }

    @Override
    public void onText(CharSequence text) {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;
        ic.beginBatchEdit();
        ic.commitText(text, 0);
        ic.endBatchEdit();
    }

    @Override
    public void swipeLeft() { }

    @Override
    public void swipeRight() { }

    @Override
    public void swipeDown() { }

    @Override
    public void swipeUp() { }

    private void keyDownUp(int keyEventCode) {
        InputConnection inputConnection = getCurrentInputConnection();
        inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, keyEventCode));
        inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, keyEventCode));
    }

    private void handleCharacter(int primaryCode, int[] keyCodes) {
        getCurrentInputConnection().commitText(String.valueOf((char) primaryCode), 1);
    }

    private void handleLanguageSwitch() {
        mInputMethodManager.switchToNextInputMethod(getToken(), false);
    }

    private IBinder getToken() {
        final Dialog dialog = getWindow();
        if (dialog == null) {
            return null;
        }
        final Window window = dialog.getWindow();
        if (window == null) {
            return null;
        }
        return window.getAttributes().token;
    }

    private static int getImeOptionsActionIdFromEditorInfo(final EditorInfo editorInfo) {
        if ((editorInfo.imeOptions & EditorInfo.IME_FLAG_NO_ENTER_ACTION) != 0) {
            return EditorInfo.IME_ACTION_NONE;
        } else if (editorInfo.actionLabel != null) {
            return IME_ACTION_CUSTOM_LABEL;
        } else {
            // Note: this is different from editorInfo.actionId, hence "ImeOptionsActionId"
            return editorInfo.imeOptions & EditorInfo.IME_MASK_ACTION;
        }
    }
}
