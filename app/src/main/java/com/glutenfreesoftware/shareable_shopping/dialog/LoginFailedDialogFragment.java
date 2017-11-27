package com.glutenfreesoftware.shareable_shopping.dialog;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.glutenfreesoftware.shareable_shopping.R;

/**
 * Created by ThomasSTodal on 08/11/2017.
 */

public class LoginFailedDialogFragment extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.login_failed)
                .setNeutralButton(R.string.text_ok, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       Log.i("login", "login failed");
                   }
                });
        return builder.create();
    }
}
