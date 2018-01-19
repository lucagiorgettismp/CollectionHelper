package com.lucagiorgetti.collectionhelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.lucagiorgetti.collectionhelper.fragments.UserSettingsFragment;
import com.lucagiorgetti.collectionhelper.listenerInterfaces.FragmentListenerInterface;
import com.lucagiorgetti.collectionhelper.listenerInterfaces.OnGetListListener;
import com.lucagiorgetti.collectionhelper.listenerInterfaces.OnGetDataListener;
import com.lucagiorgetti.collectionhelper.adapters.DoublesOwnersListAdapter;
import com.lucagiorgetti.collectionhelper.fragments.DoublesFragment;
import com.lucagiorgetti.collectionhelper.fragments.MissingFragment;
import com.lucagiorgetti.collectionhelper.fragments.ProducersFragment;
import com.lucagiorgetti.collectionhelper.fragments.SearchSetsFragment;
import com.lucagiorgetti.collectionhelper.fragments.SetItemsFragment;
import com.lucagiorgetti.collectionhelper.fragments.YearsFragment;
import com.lucagiorgetti.collectionhelper.model.Colors;
import com.lucagiorgetti.collectionhelper.model.Fragments;
import com.lucagiorgetti.collectionhelper.model.Surprise;
import com.lucagiorgetti.collectionhelper.model.User;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener ,
        FragmentListenerInterface {

    private static FragmentManager fragmentManager;
    private FirebaseAuth fireAuth;
    private LoginManager facebookLogin;
    private FirebaseAuth.AuthStateListener fireAuthStateListener;
    private User currentUser = null;
    private TextView nav_user;
    private TextView nav_email;
    private String clickedSetId = null;
    private String clickedSetName = null;
    private String clickedProducerName = null;
    private String clickedProducerId = null;
    private ArrayList<User> doubleOwners = new ArrayList<>();
    private DoublesOwnersListAdapter mAdapter;
    private NavigationView navigationView;

    private String clickedYearId = null;
    private int clickedYearNumber = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fireAuth = FirebaseAuth.getInstance();
        facebookLogin = LoginManager.getInstance();

        fireAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    SystemUtility.openNewActivityWithFinishing(MainActivity.this, getApplicationContext(), LoginActivity.class, null);
                }
            }
        };

        if (fireAuth.getCurrentUser() != null) {
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    SystemUtility.closeKeyboard(MainActivity.this, getCurrentFocus());
                }
            };
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            View hView = navigationView.getHeaderView(0);
            nav_user = (TextView) hView.findViewById(R.id.navbar_title);
            nav_email = (TextView) hView.findViewById(R.id.navbar_subtitle);

            DatabaseUtility.getCurrentUser(new OnGetDataListener() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    currentUser = dataSnapshot.getValue(User.class);
                    //Initializer init = new Initializer(currentUser);
                    //init.insertData();
                    nav_user.setText(currentUser.getUsername());
                    nav_email.setText(currentUser.getEmail().replaceAll(",", "\\."));
                    displayView(Fragments.MISSINGS, false);
                }

                @Override
                public void onStart() {

                }

                @Override
                public void onFailure() {
                    Toast.makeText(MainActivity.this, "Errore nella sincronizzazione dei dati", Toast.LENGTH_SHORT).show();
                }
            }, fireAuth);
        }
    }

    @Override
    public void onSetShortClick(String setId, String setName) {
        this.clickedSetId = setId;
        this.clickedSetName = setName;
        displayView(Fragments.SETITEMS, true);
    }

    @Override
    public void onSetLongClick(final String setId, String setName) {
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(getString(R.string.dialog_add_set_title));
        alertDialog.setMessage(getString(R.string.dialog_add_set_text) + " " + setName + "?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dialog_positive),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseUtility.addMissingsFromSet(currentUser.getUsername(), setId);
                        alertDialog.dismiss();
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

    @Override
    public void onItemAddMissings(String surpId) {
        String username = currentUser.getUsername();
        DatabaseUtility.addMissing(username, surpId);
    }

    @Override
    public void onItemAddDoubles(String surpId) {
        String username = currentUser.getUsername();
        DatabaseUtility.addDouble(username, surpId);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        int id = item.getItemId();

        if (id == R.id.nav_missings) {
            this.clearBackStack();
            displayView(Fragments.MISSINGS, false);
        } else if (id == R.id.nav_doubles) {
            this.clearBackStack();
            displayView(Fragments.DOUBLES, false);
/*        } else if (id == R.id.nav_collectors) {
            displayView(Fragments.COLLECTORS, false);*/
        } else if (id == R.id.nav_logout) {
            logout();
        } else if (id == R.id.nav_settings) {
            displayView(Fragments.USER_SETTINGS, true);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        this.fireAuth.signOut();
        this.facebookLogin.logOut();
    }

    public void displayView(Fragments frag, boolean backable) {
        Fragment fragment = null;
        String fragmentTags = "";
        switch (frag) {
            case MISSINGS:
                navigationView.getMenu().getItem(0).setChecked(true);
                fragment = new MissingFragment();
                Bundle b = new Bundle();
                b.putString("username", currentUser.getUsername());
                fragment.setArguments(b);
                break;
            case DOUBLES:
                fragment = new DoublesFragment();
                Bundle d = new Bundle();
                d.putString("username", currentUser.getUsername());
                fragment.setArguments(d);
                break;
            case COLLECTORS:
                break;
            case SETSEARCH:
                fragment = new SearchSetsFragment();
                Bundle c = new Bundle();
                c.putString("yearId", this.clickedYearId);
                fragment.setArguments(c);
                break;
            case SETITEMS:
                fragment = new SetItemsFragment();
                Bundle e = new Bundle();
                e.putString("set", this.clickedSetId);
                fragment.setArguments(e);
                break;
            case PRODUCERS:
                fragment = new ProducersFragment();
                break;
            case YEARS:
                fragment = new YearsFragment();
                Bundle a = new Bundle();
                a.putString("producer_name", this.clickedProducerName);
                a.putString("producer_id", this.clickedProducerId);
                fragment.setArguments(a);
                break;
            case USER_SETTINGS:
                fragment = new UserSettingsFragment();
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

    @Override
    public void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().goOnline();
        fireAuth.addAuthStateListener(fireAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (fireAuthStateListener != null) {
            fireAuth.removeAuthStateListener(fireAuthStateListener);
        }
        FirebaseDatabase.getInstance().goOffline();
    }

    @Override
    public void onSwipeRemoveMissing(String surpId) {
        String username = currentUser.getUsername();
        DatabaseUtility.removeMissing(username, surpId);
    }

    @Override
    public void onSwipeRemoveDouble(String surpId) {
        String username = currentUser.getUsername();
        DatabaseUtility.removeDouble(username, surpId);
    }

    @Override
    public void onClickOpenProducersFragment() {
        this.displayView(Fragments.PRODUCERS, true);
    }

    @SuppressLint("InflateParams")
    @Override
    public void onSwipeShowDoublesOwner(final Surprise missing) {
        final View view = getLayoutInflater().inflate(R.layout.doubles_dialog, null);
        TextView dialogTitle = (TextView) view.findViewById(R.id.doubles_dialog_title);
        final TextView infoTxv = (TextView) view.findViewById(R.id.doubles_dialog_info);
        final TextView emptyListTxv = (TextView) view.findViewById(R.id.doubles_dialog_empty_list);
        dialogTitle.setBackgroundColor(ContextCompat.getColor(MainActivity.this, Colors.getHexColor(missing.getSet_producer_color())));
        dialogTitle.setText(missing.getCode() + " - " + missing.getDescription());
        mAdapter = new DoublesOwnersListAdapter(MainActivity.this, doubleOwners);
        final ListView listView = (ListView) view.findViewById(R.id.doubles_dialog_list);
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
                sendEmail(owner, missing);
            }
        });
    }

    private void sendEmail(User owner, Surprise missing) {
        String to = owner.getEmail().replaceAll(",", "\\.");
        String subject = "Scambio con " + currentUser.getUsername();

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);

        String html = "Ciao," +
                "<div>Sono " + currentUser.getUsername() + ", e tramite Surprix ho visto che tra i doppi hai " + missing.getCode() + " - " + missing.getDescription() + ", a cui sono interessato.</div>" +
                "<div>Ti andrebbe di scambiare?</div>" +
                "<div><br></div>" +
                "<div>[Mail inviata grazie a Surprix]</div>";
        Spanned body;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            body = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            body = Html.fromHtml(html);
        }
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.setData(Uri.parse("mailto:" + to)); // or just "mailto:" for blank
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        startActivity(intent);
    }

    private void clearBackStack() {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onProducerClick(String id, String name) {
        this.clickedProducerName = name;
        this.clickedProducerId = id;
        this.displayView(Fragments.YEARS, true);
    }

    @Override
    public void onYearClicked(String year, int num) {
        this.clickedYearId = year;
        this.clickedYearNumber = num;
        this.displayView(Fragments.SETSEARCH, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseDatabase.getInstance().goOffline();
    }

    @Override
    public void onLongYearClicked(final String yearId, int year) {
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(getString(R.string.dialog_add_year_title));
        alertDialog.setMessage(getString(R.string.dialog_add_year_text) + " " + year + "?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dialog_positive),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String username = currentUser.getUsername();
                        DatabaseUtility.addMissingsFromYear(username, yearId);
                        alertDialog.dismiss();
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

    // region ActionBarTitle
    @Override
    public void setProducerTitle() {
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.producer);
        }
    }

    @Override
    public void setSearchTitle() {
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(this.clickedProducerName + " - "+ this.clickedYearNumber);
        }
    }

    @Override
    public void setItemsTitle() {
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(this.clickedSetName);
        }
    }

    @Override
    public void setDoublesTitle(int number) {
        if (getSupportActionBar() != null){
            String title;
            if (number > 0){
                title = getResources().getString(R.string.doubles) + " (" + number + ")";
            } else {
                title = getResources().getString(R.string.doubles);
            }
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void setMissingsTitle(int number) {
        if (getSupportActionBar() != null){
            String title;
            if (number > 0){
                title = getResources().getString(R.string.missings) + " (" + number + ")";
            } else {
                title = getResources().getString(R.string.missings);
            }
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void setYearTitle() {
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(this.clickedProducerName);
        }
    }

    @Override
    public void setSettingsTitle() {
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.settings);
        }
    }

    @SuppressLint("InflateParams")
    @Override
    public void openChangePwdDialog() {
        final View view = getLayoutInflater().inflate(R.layout.change_password, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);
        builder.setTitle(R.string.change_password);

        final EditText oldPassword = (EditText) view.findViewById(R.id.edt_dialog_old_pwd);
        final EditText newPassword = (EditText) view.findViewById(R.id.edt_dialog_new_pwd);
        Button btnChangePwd = (Button) view.findViewById(R.id.btn_dialog_submit);

        final AlertDialog changePwd = builder.create();

        changePwd.show();
        btnChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String oldPwd = oldPassword.getText().toString().trim();
                final String newPwd = newPassword.getText().toString().trim();

                final FirebaseUser user;
                user = FirebaseAuth.getInstance().getCurrentUser();

                final String email = user.getEmail();
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
        alertDialog.setTitle(getString(R.string.dialog_delete_user_title));
        alertDialog.setMessage(getString(R.string.dialod_delete_user_text));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dialog_positive),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String username = currentUser.getUsername();
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
                        }, fireAuth, username);
                        alertDialog.dismiss();
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

    // endregion
}
