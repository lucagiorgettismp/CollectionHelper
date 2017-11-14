package com.lucagiorgetti.collectionhelper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lucagiorgetti.collectionhelper.fragments.MissingFragment;
import com.lucagiorgetti.collectionhelper.fragments.SearchSetsFragment;
import com.lucagiorgetti.collectionhelper.fragments.SetItemsFragment;
import com.lucagiorgetti.collectionhelper.model.User;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener ,
        MissingFragment.MissingListener,
        SearchSetsFragment.SetListener,
        SetItemsFragment.SetItemListener {
    private static Fragment fragment = null;
    private static FragmentManager fragmentManager;
    private FirebaseAuth fireAuth;
    private LoginManager facebookLogin;
    private FirebaseAuth.AuthStateListener fireAuthStateListener;
    private User currentUser = null;
    private TextView nav_user;
    private TextView nav_email;
    private String clickedSetId = null;

    private static DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbRef = DatabaseUtility.getDatabase().getReference();
        super.onCreate(savedInstanceState);
        fireAuth = FirebaseAuth.getInstance();
        facebookLogin = LoginManager.getInstance();

        fireAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // User is logged out
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    // Closing all the Activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    // Add new Flag to start new Activity
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    // Staring Login Activity
                    getApplicationContext().startActivity(i);

                    finish();
                }
            }
        };

        if (fireAuth.getCurrentUser() != null) {
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            View hView = navigationView.getHeaderView(0);
            nav_user = (TextView) hView.findViewById(R.id.navbar_title);
            nav_email = (TextView) hView.findViewById(R.id.navbar_subtitle);

            getCurrentUser(new OnGetDataListener() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    currentUser = dataSnapshot.getValue(User.class);
                    Initializer init = new Initializer(currentUser);
                    //init.insertData();
                    nav_user.setText(currentUser.getUsername());
                    nav_email.setText(currentUser.getEmail().replaceAll(",", "\\."));
                    displayView(0, false);
                }

                @Override
                public void onStart() {

                }

                @Override
                public void onFailure() {

                }
            });
        }
    }

    @Override
    public void onSetShortClick(String setId) {
        this.clickedSetId = setId;
        displayView(3, true);
    }

    public interface OnGetDataListener {
        //this is for callbacks
        void onSuccess(DataSnapshot dataSnapshot);

        void onStart();

        void onFailure();
    }

    @Override
    public void onItemAddMissings(String surpId) {
        String username = currentUser.getUsername();
        dbRef.child("missings").child(username).child(surpId).setValue(true);
    }

    @Override
    public void onItemAddDoubles(String surpId) {
        String username = currentUser.getUsername();
        dbRef.child("doubles").child(username).child(surpId).setValue(true);
    }

    private void getCurrentUser(final OnGetDataListener listen) {
        listen.onStart();
        String emailCod = fireAuth.getCurrentUser().getEmail().replaceAll("\\.", ",");
        final String[] username = {null};
        dbRef.child("emails").child(emailCod).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    username[0] = d.getKey();
                }
                if(username[0] != null){
                    dbRef.child("users").child(username[0]).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listen.onSuccess(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listen.onFailure();
            }
        });
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
                String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
                Fragment currentFragment = fragmentManager.findFragmentByTag(fragmentTag);
                fragmentManager.popBackStack();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_missings) {
            displayView(0, false);
        } else if (id == R.id.nav_doubles) {
            displayView(1, true);
        } else if (id == R.id.nav_collectors) {
            displayView(2, true);
        } else if (id == R.id.nav_logout) {
            logout();
        } else if (id == R.id.nav_settings) {
        }

        displayView(0, false); // call search fragment.

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        this.fireAuth.signOut();
        this.facebookLogin.logOut();
    }



    public void displayView(int position, boolean backable) {
        fragment = null;
        String fragmentTags = "";
        switch (position) {
            case 0:
                fragment = new MissingFragment();
                Bundle b = new Bundle();
                b.putString("username", currentUser.getUsername());
                fragment.setArguments(b);
                break;

            case 1:
                //fragment = new DoubleFragment();
                break;

            case 2:
                //fragment = new CollectorsFragment();
                break;

            case 3:
                fragment = new SetItemsFragment();
                Bundle e = new Bundle();
                e.putString("set", this.clickedSetId);
                fragment.setArguments(e);
                break;

            case 4:
                fragment = new SearchSetsFragment();
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
    public void onClickOpenSearchSetFragment() {
        this.displayView(4, true);
    }

    @Override
    public void onSwipeRemoveMissing(String surpId) {
        String username = currentUser.getUsername();
        dbRef.child("missings").child(username).child(surpId).setValue(null);
    }
}

