package com.swash.kencommerce.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.swash.kencommerce.R;
import com.swash.kencommerce.Utils.DialogType;

/**
 * Created by Kranti on 12/12/2016.
 */

public class ConfirmationDialog extends Dialog implements View.OnClickListener {
    private DialogType mDialogType;
    private Context mContext;
    private TextView txt_dialog_privacy_terms, privacy_txt_accept, privacy_txt_reject;
    private TextView txt_title;
    LinearLayout lin_progress;
    private Confiramtion mConfiramtion;

    public interface Confiramtion {
        public void accept();
    }

    public ConfirmationDialog(Context context, DialogType dialogType,Confiramtion mConfiramtion) {
        super(context);
        getWindow().setBackgroundDrawableResource(
                R.color.progressdialog_transparent);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.confirmation_dialog);
        this.mDialogType = dialogType;
        this.mConfiramtion = mConfiramtion;
        this.mContext = context;
        txt_dialog_privacy_terms = (TextView) findViewById(R.id.txt_dialog_privacy_terms);
        lin_progress = (LinearLayout) findViewById(R.id.lin_progress);
        txt_title = (TextView) findViewById(R.id.txt_title);
        privacy_txt_accept = (TextView) findViewById(R.id.privacy_txt_accept);
        privacy_txt_reject = (TextView) findViewById(R.id.privacy_txt_reject);
        privacy_txt_accept.setOnClickListener(this);
        privacy_txt_reject.setOnClickListener(this);
        setDialog();
    }

    private void setDialog() {
        switch (mDialogType) {
            case LOGOUT:
                txt_title.setText("LOGOUT");
                txt_dialog_privacy_terms.setText("Do you want to logout of the app?");
                privacy_txt_accept.setText("Proceed");
                privacy_txt_reject.setText("Cancel");
                break;

            case DELETE_ACCOUNT:
                txt_title.setText("DELETE ACCOUNT");
                txt_dialog_privacy_terms.setText("Would you like to delete your account?");
                privacy_txt_accept.setText("Delete");
                privacy_txt_reject.setText("Cancel");
                break;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.privacy_txt_accept:
                dismiss();
                mConfiramtion.accept();
                break;
            case R.id.privacy_txt_reject:
                dismiss();
                break;
        }
    }
}
