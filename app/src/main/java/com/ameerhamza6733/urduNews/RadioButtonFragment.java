package com.ameerhamza6733.urduNews;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;


/**
 * Created by DELL 3542 on 8/2/2016.
 */
public class RadioButtonFragment extends DialogFragment {
    String singleitem;
    private int witch;

    private String[] item = {"Naskh asiatype", "Fajer noori Nastaleeq", "Pak nastaleeq (default)"};

    @Override
    public void onResume() {
        super.onResume();
      // witch= mySharedPreferences.loadIntPrefs(Constants.RADIO_BUTTON_INDEX_KEY,2,getActivity());

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
       // witch= mySharedPreferences.loadIntPrefs(Constants.RADIO_BUTTON_INDEX_KEY,2,getActivity());
        builder.setTitle("please selet any fount").setSingleChoiceItems(item, witch, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                singleitem= item[which];
                //mySharedPreferences.saveStringPrefs(Constants.FONT_KEY,singleitem,getActivity());
               // mySharedPreferences.saveintPrefs(Constants.RADIO_BUTTON_INDEX_KEY,which,getActivity());
                Toast.makeText(getContext(),"Font is selected"+which,Toast.LENGTH_SHORT).show();

            }
        });

        return builder.create();
    }
}

