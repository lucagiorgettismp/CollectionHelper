package com.lucagiorgetti.collectionhelper.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.lucagiorgetti.collectionhelper.DatabaseUtility;
import com.lucagiorgetti.collectionhelper.FragmentListenerInterface;
import com.lucagiorgetti.collectionhelper.R;
import com.lucagiorgetti.collectionhelper.RecyclerItemClickListener;
import com.lucagiorgetti.collectionhelper.RegistrateActivity;
import com.lucagiorgetti.collectionhelper.adapters.ProducerRecyclerAdapter;
import com.lucagiorgetti.collectionhelper.listenerInterfaces.OnGetDataListener;
import com.lucagiorgetti.collectionhelper.model.Producer;
import com.lucagiorgetti.collectionhelper.model.User;
import com.mikelau.countrypickerx.Country;
import com.mikelau.countrypickerx.CountryPickerCallbacks;
import com.mikelau.countrypickerx.CountryPickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

public class UserSettingsFragment extends Fragment{
    private FragmentListenerInterface listener;
    private User currentUser;
    private Context mContext;
    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtUsername;
    private EditText edtName;
    private EditText edtSurname;
    private EditText edtBirthdate;
    private EditText edtNation;
    private Button submit;
    final Calendar myCalendar = Calendar.getInstance();
    CountryPickerDialog countryPicker = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.registration, container, false);
        currentUser = listener.getCurrentRetrievedUser();
        edtEmail = (EditText) layout.findViewById(R.id.edit_reg_email);
        edtPassword = (EditText) layout.findViewById(R.id.edit_reg_password);
        edtUsername = (EditText) layout.findViewById(R.id.edit_reg_username);
        edtUsername.setEnabled(false);
        edtName = (EditText) layout.findViewById(R.id.edit_reg_name);
        edtSurname = (EditText) layout.findViewById(R.id.edit_reg_surname);
        edtBirthdate =(EditText) layout.findViewById(R.id.edit_reg_birthdate);
        edtNation =(EditText) layout.findViewById(R.id.edit_reg_nation);
        submit = (Button) layout.findViewById(R.id.btn_reg_submit);

        String[] dateArray = new String[0];
        try {
            dateArray = currentUser.getBirthday().split("/");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        myCalendar.set(Integer.parseInt(dateArray[2]),Integer.parseInt(dateArray[1]) - 1,Integer.parseInt(dateArray[0]));
        edtEmail.setEnabled(false);
        edtPassword.setVisibility(View.GONE);

        edtEmail.setText(currentUser.getEmail().replaceAll(",", "\\."));
        edtUsername.setText(currentUser.getUsername());
        edtName.setText(currentUser.getName());
        edtSurname.setText(currentUser.getSurname());
        edtUsername.setText(currentUser.getUsername());
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ITALIAN);
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
                /* Get Country Name: country.getCountryName(context); */
                /* Call countryPicker.dismiss(); to prevent memory leaks */
                        }

          /* Set to false if you want to disable Dial Code in the results and true if you want to show it
             Set to zero if you don't have a custom JSON list of countries in your raw file otherwise use
             resourceId for your customly available countries */
                    }, false, 0);
                    countryPicker.show();
                }
            }
        });

        TextView lblInfoFirstLogin = (TextView) layout.findViewById(R.id.lbl_reg_info_firstlogin);
        lblInfoFirstLogin.setVisibility(View.GONE);
        submit.setText("Salva");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myFormat = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ITALIAN);

                String name = edtName.getText().toString().trim();
                String surname = edtSurname.getText().toString().trim();
                String nation = edtNation.getText().toString();
                Date birthDate = null;
                try {
                    birthDate = sdf.parse(edtBirthdate.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                DatabaseUtility.updateUser(currentUser.getUsername(), name, surname, birthDate, nation);
                listener.refreshUser();
                currentUser = listener.getCurrentRetrievedUser();
                Snackbar.make(v, "Utente aggiornato", Snackbar.LENGTH_SHORT).show();
            }
        });
        return layout;
    }

    private void updateLabel(){
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ITALIAN);
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


