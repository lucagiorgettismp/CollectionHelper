package com.lucagiorgetti.surprix;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.lucagiorgetti.surprix.listenerInterfaces.OnGetDataListener;
import com.lucagiorgetti.surprix.model.User;
import com.lucagiorgetti.surprix.utility.DatabaseUtility;
import com.lucagiorgetti.surprix.utility.SystemUtility;

public class Main2Activity extends AppCompatActivity {
    private LoginManager facebookLogin;
    private FirebaseAuth fireAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_missing_list, R.id.navigation_double_list, R.id.navigation_catalog)
                .build();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        NavigationUI.setupWithNavController(toolbar, navController);

        fireAuth = SurprixApplication.getInstance().getFirebaseAuth();
        facebookLogin = LoginManager.getInstance();

        if (fireAuth.getCurrentUser() != null) {
            SystemUtility.enableFCM();

            DatabaseUtility.getCurrentUser(new OnGetDataListener() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    User currentUser = dataSnapshot.getValue(User.class);
                    if (currentUser != null) {
                        String username = currentUser.getUsername();
                        String email = currentUser.getEmail().replaceAll(",", "\\.");
                        SystemUtility.writeUserInfo(username, email);
                    }

                    SurprixApplication.getInstance().setUser(currentUser);
                }

                @Override
                public void onStart() {
                }

                @Override
                public void onFailure() {
                    Toast.makeText(Main2Activity.this, getResources().getString(R.string.data_sync_error), Toast.LENGTH_SHORT).show();
                }
            }, fireAuth);

        }
    }
}
