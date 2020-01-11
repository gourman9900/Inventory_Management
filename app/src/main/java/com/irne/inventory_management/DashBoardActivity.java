package com.irne.inventory_management;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.irne.inventory_management.ui.dashboard.DashboardFragment;
import com.irne.inventory_management.ui.home.HomeFragment;
import com.irne.inventory_management.ui.notifications.NotificationsFragment;


public class DashBoardActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_barcode, R.id.navigation_scanner, R.id.navigation_chat,R.id.navigation_logout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        firebaseAuth = FirebaseAuth.getInstance();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();


    }

    public void alertsignout()
    {
        AlertDialog.Builder alertDialog2 = new
                AlertDialog.Builder(DashBoardActivity.this);

        // Setting Dialog Title
        alertDialog2.setTitle("Confirm SignOut");

        // Setting Dialog Message
        alertDialog2.setMessage("Are you sure you want to Signout?");

        // Setting Positive "Yes" Btn
        alertDialog2.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        firebaseAuth.signOut();
                        Intent i = new Intent(getApplicationContext(),
                                MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                });

        // Setting Negative "NO" Btn
        alertDialog2.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,new DashboardFragment()).commit();
                        bottomNavigationView.setSelectedItemId(R.id.navigation_scanner);
                    }
                });

        // Showing Alert Dialog
        alertDialog2.show();


    }

    public void fragmentToActivity(){

        Intent intent = new Intent(DashBoardActivity.this,ScannerActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment selectedFragment = null;
        switch (menuItem.getItemId()){
            case R.id.navigation_logout:
                alertsignout();
            case R.id.navigation_barcode:
                selectedFragment = new HomeFragment();
                break;
            case R.id.navigation_scanner:
                selectedFragment = new DashboardFragment();
                break;
            case R.id.navigation_chat:
                selectedFragment = new NotificationsFragment();
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.container,selectedFragment).commit();
        return true;
    }
}
