package com.lucagiorgetti.surprix.views;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.adapters.DoublesOwnersListAdapter;
import com.lucagiorgetti.surprix.fragments.DoublesFragment;
import com.lucagiorgetti.surprix.fragments.MissingFragment;
import com.lucagiorgetti.surprix.fragments.ThanksToFragment;
import com.lucagiorgetti.surprix.fragments.UserSettingsFragment;
import com.lucagiorgetti.surprix.listenerInterfaces.FragmentListenerInterface;
import com.lucagiorgetti.surprix.listenerInterfaces.OnGetDataListener;
import com.lucagiorgetti.surprix.listenerInterfaces.OnGetListListener;
import com.lucagiorgetti.surprix.model.Colors;
import com.lucagiorgetti.surprix.model.Fragments;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.model.User;
import com.lucagiorgetti.surprix.utility.DatabaseUtility;
import com.lucagiorgetti.surprix.utility.SystemUtility;
import com.lucagiorgetti.surprix.utility.TitleHelper;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener ,
        FragmentListenerInterface {

    private static FragmentManager fragmentManager;
    private LoginManager facebookLogin;
    private FirebaseAuth fireAuth;
    private FirebaseAuth.AuthStateListener fireAuthStateListener;

    private User currentUser = null;
    private TextView nav_user;
    private TextView nav_email;
    private ArrayList<User> doubleOwners = new ArrayList<>();
    private DoublesOwnersListAdapter mAdapter;
    private NavigationView navigationView;

    // region Override standard methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fireAuth = SurprixApplication.getInstance().getFirebaseAuth();
        facebookLogin = LoginManager.getInstance();

        fireAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    SystemUtility.openNewActivityWithFinishing(MainActivity.this, LoginActivity.class, null);
                }
            }
        };

        if (fireAuth.getCurrentUser() != null) {
            setContentView(R.layout.activity_main);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    SystemUtility.closeKeyboard(MainActivity.this, Objects.requireNonNull(getCurrentFocus()));
                }
            };
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            View hView = navigationView.getHeaderView(0);
            nav_user = hView.findViewById(R.id.navbar_title);
            nav_email = hView.findViewById(R.id.navbar_subtitle);

            DatabaseUtility.getCurrentUser(new OnGetDataListener() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    currentUser = dataSnapshot.getValue(User.class);
                    if (currentUser != null){
                        String username = currentUser.getUsername();
                        String email = currentUser.getEmail().replaceAll(",", "\\.") ;
                        nav_user.setText(username);
                        nav_email.setText(email);
                        SystemUtility.writeUserInfo(username, email);
                    }

                    displayView(Fragments.MISSINGS, false);
                }

                @Override
                public void onStart() {

                }

                @Override
                public void onFailure() {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.data_sync_error), Toast.LENGTH_SHORT).show();
                }
            }, fireAuth);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        fireAuth.addAuthStateListener(fireAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (fireAuthStateListener != null) {
            fireAuth.removeAuthStateListener(fireAuthStateListener);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            fragmentManager = getSupportFragmentManager();
            int count = fragmentManager.getBackStackEntryCount();
            if (count == 0) {
                super.onBackPressed();

            } else {
                fragmentManager.popBackStack();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        int id = item.getItemId();

        switch (id){
            case R.id.nav_missings:
                this.clearBackStack();
                displayView(Fragments.MISSINGS, false);
                break;
            case R.id.nav_doubles:
                this.clearBackStack();
                displayView(Fragments.DOUBLES, false);
                break;
            case R.id.nav_tutorial:
                SystemUtility.openNewActivity(OnboardActivity.class, null);
                break;
            case R.id.nav_logout:
                logout();
                break;
            case R.id.nav_settings:
                displayView(Fragments.USER_SETTINGS, false);
                break;
            case R.id.nav_thanks:
                displayView(Fragments.THANKS_TO, false);
                break;
            case R.id.nav_facebook:
                showInfoDialog();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // endregion

    // region Custom methods
    @Override
    public User getCurrentRetrievedUser() {
        return currentUser;
    }

    @Override
    public void refreshUser() {
        DatabaseUtility.getCurrentUser(new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot data) {
                currentUser = data.getValue(User.class);
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {

            }
        }, fireAuth);
    }

    private void logout() {
        this.fireAuth.signOut();
        this.facebookLogin.logOut();

        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        edit.remove(SystemUtility.USER_USERNAME);
        edit.remove(SystemUtility.USER_EMAIL);
        edit.apply();
    }

    public void displayView(Fragments frag, boolean backable) {
        Fragment fragment = null;
        String fragmentTags = "";
        switch (frag) {
            case MISSINGS:
                navigationView.getMenu().getItem(0).setChecked(true);
                fragment = new MissingFragment();
                break;
            case DOUBLES:
                fragment = new DoublesFragment();
                break;
            case COLLECTORS:
                break;
            case USER_SETTINGS:
                fragment = new UserSettingsFragment();
                break;
            case THANKS_TO:
                fragment = new ThanksToFragment();
                break;
            default:
                break;
        }

        if (fragment != null) {
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.content_frame, fragment, fragmentTags);
            if (backable) {
                transaction.addToBackStack(null);
            }
            transaction.commit();
        }
    }

    private void sendEmailToUser(User owner, Surprise missing) {
        String to = owner.getEmail().replaceAll(",", "\\.");
        String subject = getString(R.string.mail_subject, currentUser.getUsername());
        String html = getString(R.string.mail_exchange_body, currentUser.getUsername(), missing.getCode(), missing.getDescription());
        Spanned body;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            body = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            body = Html.fromHtml(html);
        }

        SystemUtility.sendMail(MainActivity.this, to, subject, body);
    }

    private void clearBackStack() {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
    // endregion

    // region Dialog
    private void showInfoDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(getString(R.string.info_images_dialog_title));
        alertDialog.setMessage(getString(R.string.info_images_dialog_content));

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.email),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SystemUtility.sendMail(MainActivity.this, getResources().getString(R.string.surprix_mail), null, null);
                        alertDialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.dialog_negative),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    @SuppressLint("InflateParams")
    @Override
    public void openChangePwdDialog() {
        final View view = getLayoutInflater().inflate(R.layout.dialog_change_password, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);
        builder.setTitle(R.string.change_password);

        final EditText oldPassword = view.findViewById(R.id.edt_dialog_old_pwd);
        final EditText newPassword = view.findViewById(R.id.edt_dialog_new_pwd);
        Button btnChangePwd = view.findViewById(R.id.btn_dialog_submit);

        final AlertDialog changePwd = builder.create();

        changePwd.show();
        btnChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String oldPwd = oldPassword.getText().toString().trim();
                final String newPwd = newPassword.getText().toString().trim();

                final FirebaseUser user;
                user = SurprixApplication.getInstance().getFirebaseAuth().getCurrentUser();

                String email = null;
                if (user != null) {
                    email = user.getEmail();
                }
                if (email != null){

                    AuthCredential credential = EmailAuthProvider.getCredential(email, oldPwd);

                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                user.updatePassword(newPwd).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(!task.isSuccessful()){
                                            Toast.makeText(MainActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(MainActivity.this, R.string.password_changed, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else {
                                Toast.makeText(MainActivity.this, R.string.old_password_wrong, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                changePwd.dismiss();
            }
        });
    }

    @Override
    public void openDeleteUserDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(getString(R.string.delete_account));
        alertDialog.setMessage(getString(R.string.dialod_delete_user_text));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dialog_positive),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseUtility.deleteUser(new OnGetDataListener() {
                            @Override
                            public void onSuccess(DataSnapshot data) {

                            }

                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onFailure() {

                            }
                        });
                        alertDialog.dismiss();
                        logout();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.dialog_negative),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onClickOpenProducers() {
        SystemUtility.openNewActivity(ProducersActivity.class, null);
    }

    @Override
    public void onBannerClicked(String url) {
        SystemUtility.openUrl(MainActivity.this, url);
    }
    // endregion

    // region Swipe
    @Override
    public void onSwipeRemoveMissing(String surpId) {
        DatabaseUtility.removeMissing(surpId);
    }

    @Override
    public void onSwipeRemoveDouble(String surpId) {
        DatabaseUtility.removeDouble(surpId);
    }

    @SuppressLint("InflateParams")
    @Override
    public void onSwipeShowDoublesOwner(final Surprise missing) {
        final View view = getLayoutInflater().inflate(R.layout.dialog_doubles, null);
        TextView dialogTitle = view.findViewById(R.id.doubles_dialog_title);
        final TextView infoTxv = view.findViewById(R.id.doubles_dialog_info);
        final TextView emptyListTxv = view.findViewById(R.id.doubles_dialog_empty_list);
        dialogTitle.setBackgroundColor(ContextCompat.getColor(MainActivity.this, Colors.getHexColor(missing.getSet_producer_color())));
        dialogTitle.setText(String.format(getString(R.string.double_owners_dialog_title), missing.getCode(), missing.getDescription()));
        mAdapter = new DoublesOwnersListAdapter(MainActivity.this, doubleOwners);
        final ListView listView = view.findViewById(R.id.doubles_dialog_list);
        listView.setAdapter(mAdapter);
        DatabaseUtility.getDoubleOwners(missing.getId(), new OnGetListListener<User>() {
            @Override
            public void onSuccess(ArrayList<User> users) {
                if(users != null){
                    if(!users.isEmpty()){
                        final ArrayList<User> owners = new ArrayList<>();
                        final ArrayList<User> abroad_owners = new ArrayList<>();
                        for(User u: users){
                            if(Objects.equals(u.getCountry(), currentUser.getCountry())){
                                owners.add(u);
                            } else {
                                abroad_owners.add(u);
                            }
                        }
                        owners.addAll(abroad_owners);
                        mAdapter = new DoublesOwnersListAdapter(MainActivity.this, owners);
                        emptyListTxv.setVisibility(View.GONE);
                        infoTxv.setVisibility(View.VISIBLE);
                        listView.setAdapter(mAdapter);
                    }
                }
            }

            @Override
            public void onStart() {
                doubleOwners.clear();
            }

            @Override
            public void onFailure() {

            }
        });
        if(doubleOwners.isEmpty()){
            infoTxv.setVisibility(View.GONE);
            emptyListTxv.setVisibility(View.VISIBLE);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog alert = builder.create();
        alert.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                alert.dismiss();
                final User owner = mAdapter.getItem(position);
                sendEmailToUser(owner, missing);
            }
        });
    }
    // endregion

    // region ActionBarTitle
    @Override
    public void setDoublesTitle(int number) {
        TitleHelper.setDoublesTitle(MainActivity.this, getSupportActionBar(), number);
    }

    @Override
    public void setMissingsTitle(int number) {
        TitleHelper.setMissingsTitle(MainActivity.this, getSupportActionBar(), number);
    }

    @Override
    public void setSettingsTitle() {
        TitleHelper.setSettingsTitle(MainActivity.this, getSupportActionBar());
    }

    @Override
    public void setThanksToTitle() {
        TitleHelper.setThanksTitle(MainActivity.this, getSupportActionBar());
    }
    // endregion
}