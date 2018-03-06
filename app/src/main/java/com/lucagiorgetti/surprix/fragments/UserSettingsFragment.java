package com.lucagiorgetti.surprix.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lucagiorgetti.surprix.utility.DatabaseUtility;
import com.lucagiorgetti.surprix.utility.SystemUtility;
import com.lucagiorgetti.surprix.listenerInterfaces.FragmentListenerInterface;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.model.User;
import com.mikelau.countrypickerx.Country;
import com.mikelau.countrypickerx.CountryPickerCallbacks;
import com.mikelau.countrypickerx.CountryPickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

public class UserSettingsFragment extends Fragment{
    private FragmentListenerInterface listener;
    private User currentUser;
    private Context mContext;
    private EditText edtName;
    private EditText edtSurname;
    private EditText edtBirthdate;
    private EditText edtNation;
    final Calendar myCalendar = Calendar.getInstance();
    CountryPickerDialog countryPicker = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.activity_registration, container, false);
        currentUser = listener.getCurrentRetrievedUser();
        EditText edtEmail = (EditText) layout.findViewById(R.id.edit_reg_email);
        EditText edtUsername = (EditText) layout.findViewById(R.id.edit_reg_username);
        ImageView usernameImage = (ImageView) layout.findViewById(R.id.img_reg_username);
        ImageView emailImage = (ImageView) layout.findViewById(R.id.img_reg_email);

        edtUsername.setEnabled(false);
        edtEmail.setEnabled(false);
        usernameImage.setColorFilter(ContextCompat.getColor(mContext, R.color.disabledIcon));
        emailImage.setColorFilter(ContextCompat.getColor(mContext, R.color.disabledIcon));

        edtName = (EditText) layout.findViewById(R.id.edit_reg_name);
        edtSurname = (EditText) layout.findViewById(R.id.edit_reg_surname);
        edtBirthdate =(EditText) layout.findViewById(R.id.edit_reg_birthdate);
        edtNation =(EditText) layout.findViewById(R.id.edit_reg_nation);
        View layPassword = layout.findViewById(R.id.layout_reg_password);
        View layButtonsModify = layout.findViewById(R.id.layout_reg_modify);

        TextView changePwd = (TextView) layout.findViewById(R.id.btn_reg_change_pwd);
        TextView deleteUser = (TextView) layout.findViewById(R.id.btn_reg_delete_account);
        Button submit = (Button) layout.findViewById(R.id.btn_reg_submit);

        String[] dateArray = new String[0];
        try {
            dateArray = currentUser.getBirthday().split("/");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        myCalendar.set(Integer.parseInt(dateArray[2]),Integer.parseInt(dateArray[1]) - 1,Integer.parseInt(dateArray[0]));
        layPassword.setVisibility(View.GONE);


        layButtonsModify.setVisibility(View.VISIBLE);
        if(currentUser.isFacebook()){
            changePwd.setVisibility(View.GONE);
        }
        deleteUser.setVisibility(View.VISIBLE);

        edtEmail.setText(currentUser.getEmail().replaceAll(",", "\\."));
        edtUsername.setText(currentUser.getUsername());
        edtName.setText(currentUser.getName());
        edtSurname.setText(currentUser.getSurname());
        edtUsername.setText(currentUser.getUsername());
        try {
            edtBirthdate.setText(currentUser.getBirthday());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        edtNation.setText(currentUser.getCountry());

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        edtBirthdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    new DatePickerDialog(mContext, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });

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

        TextView lblInfoFirstLogin = (TextView) layout.findViewById(R.id.lbl_reg_info_firstlogin);
        lblInfoFirstLogin.setVisibility(View.GONE);
        submit.setText(R.string.save);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SystemUtility.closeKeyboard(getActivity(),getView());

                String name = edtName.getText().toString().trim();
                String surname = edtSurname.getText().toString().trim();
                String nation = edtNation.getText().toString();
                String birthDate = edtBirthdate.getText().toString();

                DatabaseUtility.updateUser(currentUser.getUsername(), name, surname, birthDate, nation);
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

    private void updateLabel(){
        SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.dateFormat), Locale.ITALIAN);
        edtBirthdate.setText(sdf.format(myCalendar.getTime()));
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


