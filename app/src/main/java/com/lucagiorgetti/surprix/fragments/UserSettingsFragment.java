package com.lucagiorgetti.surprix.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.listenerInterfaces.FragmentListenerInterface;
import com.lucagiorgetti.surprix.model.User;
import com.lucagiorgetti.surprix.utility.DatabaseUtility;
import com.lucagiorgetti.surprix.utility.SystemUtility;
import com.mikelau.countrypickerx.Country;
import com.mikelau.countrypickerx.CountryPickerCallbacks;
import com.mikelau.countrypickerx.CountryPickerDialog;

import static com.facebook.FacebookSdk.getApplicationContext;

public class UserSettingsFragment extends Fragment{
    private FragmentListenerInterface listener;
    private User currentUser;
    private Context mContext;
    private EditText edtNation;
    CountryPickerDialog countryPicker = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.activity_registration, container, false);
        currentUser = listener.getCurrentRetrievedUser();
        EditText edtEmail = layout.findViewById(R.id.edit_reg_email);
        EditText edtUsername = layout.findViewById(R.id.edit_reg_username);
        ImageView usernameImage = layout.findViewById(R.id.img_reg_username);
        ImageView emailImage = layout.findViewById(R.id.img_reg_email);
        TextView lblPrivacyPolicy = layout.findViewById(R.id.lbl_reg_policy);

        lblPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPrivacyPolicy();
            }
        });

        edtUsername.setEnabled(false);
        edtEmail.setEnabled(false);
        usernameImage.setColorFilter(ContextCompat.getColor(mContext, R.color.disabledIcon));
        emailImage.setColorFilter(ContextCompat.getColor(mContext, R.color.disabledIcon));

        edtNation = layout.findViewById(R.id.edit_reg_nation);
        View layPassword = layout.findViewById(R.id.layout_reg_password);
        View layButtonsModify = layout.findViewById(R.id.layout_reg_modify);

        TextView changePwd = layout.findViewById(R.id.btn_reg_change_pwd);
        TextView deleteUser = layout.findViewById(R.id.btn_reg_delete_account);
        Button submit = layout.findViewById(R.id.btn_reg_submit);

        layPassword.setVisibility(View.GONE);

        layButtonsModify.setVisibility(View.VISIBLE);
        if(currentUser.isFacebook()){
            changePwd.setVisibility(View.GONE);
        }
        deleteUser.setVisibility(View.VISIBLE);

        edtEmail.setText(currentUser.getEmail().replaceAll(",", "\\."));
        edtUsername.setText(currentUser.getUsername());
        edtNation.setText(currentUser.getCountry());

        edtNation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    countryPicker = new CountryPickerDialog(mContext, new CountryPickerCallbacks() {
                        @Override
                        public void onCountrySelected(Country country, int flagResId) {
                            edtNation.setText(country.getCountryName(getApplicationContext()));
                            countryPicker.dismiss();
                        }
                    }, false, 0);
                    countryPicker.show();
                }
            }
        });

        TextView lblInfoFirstLogin = layout.findViewById(R.id.lbl_reg_info_firstlogin);
        lblInfoFirstLogin.setVisibility(View.GONE);
        submit.setText(R.string.save);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemUtility.closeKeyboard(getActivity(),getView());
                String nation = edtNation.getText().toString();
                DatabaseUtility.updateUser(currentUser.getUsername(), nation);
                listener.refreshUser();
                currentUser = listener.getCurrentRetrievedUser();
                Snackbar.make(v, R.string.user_added, Snackbar.LENGTH_SHORT).show();
            }
        });

        changePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.openChangePwdDialog();
            }
        });

        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.openDeleteUserDialog();
            }
        });
        return layout;
    }

    private void showPrivacyPolicy() {
        final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle(getString(R.string.privacy_policy));

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            alertDialog.setMessage(Html.fromHtml(getString(R.string.privacy_policy_text), Html.FROM_HTML_MODE_LEGACY));
        } else {
            alertDialog.setMessage(Html.fromHtml(getString(R.string.privacy_policy_text)));
        }

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dialog_positive),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof FragmentListenerInterface){
            this.listener = (FragmentListenerInterface) context;
        }
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }

    @Override
    public void onResume() {
        listener.setSettingsTitle();
        super.onResume();
    }
}


