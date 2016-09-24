package com.ameerhamza6733.urduNews;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by DELL 3542 on 8/12/2016.
 */
public class CheckBoxFragment extends DialogFragment {
    public MySharedPreferences mySharedPreferences = new MySharedPreferences();
    private String[] item = {"کھیل", "فن و ثقافت", "سائنس اور ماحول", "معاشرہ"};
    private boolean[] checkedItems = {false, false, false, false};
    private String TAG = "CheckBoxFragment";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");

        checkedItems[0] = isUserInterestInSport();
        checkedItems[1] = isUserInterestInCulture();
        checkedItems[2] = isUserInterestInSince();
        checkedItems[3] = isUserInterestInSocity();


        super.onCreate(savedInstanceState);
    }


    private boolean isUserInterestInSince() {
        String Not_interasted_catguty_in_Cutture = mySharedPreferences.loadStringPrefs(Constants.REMOVE_KEY_SINCE_CATEGORY, "N/A", getActivity());

        if (Not_interasted_catguty_in_Cutture.equals("N/A")) {
            return true;
        } else {
            return false;
        }

    }

    private boolean isUserInterestInSport() {
        String Not_interasted_catguty_in_Cutture = mySharedPreferences.loadStringPrefs(Constants.REMOVE_KEY_SPORT_CATEGORY, "N/A", getActivity());

        if (Not_interasted_catguty_in_Cutture.equals("N/A")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isUserInterestInCulture() {

        String Not_interasted_catguty_in_Cutture = mySharedPreferences.loadStringPrefs(Constants.REMOVE_KEY_CULTURE_CATEGORY, "N/A", getActivity());

        if (Not_interasted_catguty_in_Cutture.equals("N/A")) {
            return true;
        } else {
            return false;
        }


    }

    private boolean isUserInterestInSocity() {
        String Not_interasted_catguty_in_Cutture = mySharedPreferences.loadStringPrefs(Constants.REMOVE_KEY_SOCITY_CATEGORY, "N/A", getActivity());

        if (Not_interasted_catguty_in_Cutture.equals("N/A")) {
            return true;
        } else {
            return false;
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i(TAG, "onCreateDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("choise ").setMultiChoiceItems(item, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                if (which == 0 && !isChecked) {
                    mySharedPreferences.saveStringPrefs(Constants.REMOVE_KEY_SPORT_CATEGORY, "کھیل", getActivity());
                    Toast.makeText(getActivity(), "Remove from your interest", Toast.LENGTH_SHORT).show();
                } else if (which == 1 && !isChecked) {
                    mySharedPreferences.saveStringPrefs(Constants.REMOVE_KEY_CULTURE_CATEGORY, "فن و ثقافت", getActivity());
                    Toast.makeText(getActivity(), "Remove from your interest", Toast.LENGTH_SHORT).show();
                } else if (which == 2 && !isChecked) {
                    mySharedPreferences.saveStringPrefs(Constants.REMOVE_KEY_SINCE_CATEGORY, "سائنس اور ماحول", getActivity());
                    Toast.makeText(getActivity(), "Remove from your interest", Toast.LENGTH_SHORT).show();
                } else if (which == 3 && !isChecked) {
                    mySharedPreferences.saveStringPrefs(Constants.REMOVE_KEY_SOCITY_CATEGORY, "معاشرہ", getActivity());
                } else if (which == 0 && isChecked) {
                    mySharedPreferences.deletePrefs(Constants.REMOVE_KEY_SPORT_CATEGORY, getActivity());
                } else if (which == 1 && isChecked) {
                    Toast.makeText(getActivity(), "save  your interest", Toast.LENGTH_SHORT).show();
                    mySharedPreferences.deletePrefs(Constants.REMOVE_KEY_CULTURE_CATEGORY, getActivity());
                } else if (which == 2 && isChecked) {
                    Toast.makeText(getActivity(), "save  your interest", Toast.LENGTH_SHORT).show();
                    mySharedPreferences.deletePrefs(Constants.REMOVE_KEY_SINCE_CATEGORY, getActivity());

                } else if (which == 3 && isChecked) {
                    Toast.makeText(getActivity(), "save  your interest", Toast.LENGTH_SHORT).show();
                    mySharedPreferences.deletePrefs(Constants.REMOVE_KEY_SOCITY_CATEGORY, getActivity());
                } else {
                    Toast.makeText(getActivity(), "Something is wrong report to developer ", Toast.LENGTH_SHORT).show();
                }


            }
        });
        return builder.create();
    }
}
