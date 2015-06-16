package com.codebutler.shrug;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends Activity {

    private ComponentName mComponentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mComponentName = new ComponentName(MainActivity.this, ShrugInputMethodService.class);

        setContentView(R.layout.activity_main);

        findViewById(R.id.bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShrugEnabled()) {
                    startActivity(new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS));
                    return;
                }
                if (!isShrugDefault()) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showInputMethodPicker();
                    return;
                }
                Toast.makeText(MainActivity.this, R.string.shrug, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isShrugEnabled() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        List<InputMethodInfo> enabledInputMethods = imm.getEnabledInputMethodList();
        for (InputMethodInfo method : enabledInputMethods) {
            if (mComponentName.equals(method.getComponent())) {
                return true;
            }
        }
        return false;
    }

    private boolean isShrugDefault() {
        String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
        return mComponentName.equals(ComponentName.unflattenFromString(id));
    }
}
