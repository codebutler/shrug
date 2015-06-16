package com.codebutler.shrug;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;

public class ActionKeyButton extends ImageButton {

    public static final int[] STATE_ACTIVE = { android.R.attr.state_active };

    public ActionKeyButton(Context context) {
        this(context, null);
    }

    public ActionKeyButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActionKeyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ActionKeyButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        int[] state = super.onCreateDrawableState(extraSpace + 1);
        mergeDrawableStates(state, STATE_ACTIVE);
        return state;
    }

    public void setImeOptions(int options) {
        switch (options & (EditorInfo.IME_MASK_ACTION | EditorInfo.IME_FLAG_NO_ENTER_ACTION)) {
            case EditorInfo.IME_ACTION_GO:
                setImageResource(R.drawable.sym_keyboard_go_lxx_light);
                setContentDescription(getResources().getString(R.string.content_description_go));
                break;
            case EditorInfo.IME_ACTION_NEXT:
                setImageResource(R.drawable.sym_keyboard_next_lxx_light);
                setContentDescription(getResources().getString(R.string.content_description_next));
                break;
            case EditorInfo.IME_ACTION_PREVIOUS:
                setImageResource(R.drawable.sym_keyboard_previous_lxx_light);
                setContentDescription(getResources().getString(R.string.content_description_previous));
                break;
            case EditorInfo.IME_ACTION_SEARCH:
                setImageResource(R.drawable.sym_keyboard_search_lxx_light);
                setContentDescription(getResources().getString(R.string.content_description_search));
                break;
            case EditorInfo.IME_ACTION_SEND:
                setImageResource(R.drawable.sym_keyboard_send_lxx_light);
                setContentDescription(getResources().getString(R.string.content_description_send));
                break;
            default:
                setImageResource(R.drawable.sym_keyboard_return_lxx_light);
                setContentDescription(getResources().getString(R.string.content_description_return));
                break;
        }
    }
}
