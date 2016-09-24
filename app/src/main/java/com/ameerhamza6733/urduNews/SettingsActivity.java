package com.ameerhamza6733.urduNews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    public static boolean mySwitchIsChecked ;
    private Switch mySwitch;
    private String Tag = "SettingsActivity";
    private MySharedPreferences mySharedPreferences = new MySharedPreferences();

    @Override
    protected void onStart() {
        super.onStart();
        mySwitch.setChecked(mySharedPreferences.loadPrefs(Constants.switchStateKey, mySwitchIsChecked, getApplicationContext()));
    }

    @Override
    protected void onResume() {
        super.onResume();

        mySwitch.setChecked(mySharedPreferences.loadPrefs(Constants.switchStateKey, mySwitchIsChecked, getApplicationContext()));
        Log.i(Tag, "onResume" + String.valueOf(mySharedPreferences.loadPrefs(Constants.switchStateKey, mySwitchIsChecked, getApplicationContext())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mySwitch = (Switch) findViewById(R.id.mySwitch);



        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    mySwitchIsChecked = true;
                    Log.i(Tag, "isChecked" + mySwitchIsChecked);
                    Toast.makeText(SettingsActivity.this, "Please restart app to apply changes",
                            Toast.LENGTH_LONG).show();
                    // The toggle is enabled
                } else {
                    mySwitchIsChecked = false;
                    Toast.makeText(SettingsActivity.this, "Please restart app to apply changes",
                            Toast.LENGTH_LONG).show();
                    Log.i(Tag, "isChecked" + mySwitchIsChecked);


                    // savePrefs("switchKey",false);
                    // The toggle is disabled
                }
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();

        mySharedPreferences.savePrefs(Constants.switchStateKey, mySwitchIsChecked, getApplicationContext());
        Log.i(Tag, "onStop" + mySwitchIsChecked);

    }



    private final String RadioButtonFragment_TAG="RadioButtonFragment";
    private final String CheckBoxFragment_TAG="CheckBoxFragment";
    public void select_fount(View v) {

        RadioButtonFragment radioButtonFragment = new RadioButtonFragment();
        radioButtonFragment.show(getSupportFragmentManager(), RadioButtonFragment_TAG);
    }

    public void show_diloge(View view)
    {
        CheckBoxFragment checkBoxFragment = new CheckBoxFragment();
        checkBoxFragment.show(getSupportFragmentManager(),CheckBoxFragment_TAG);
    }

}
