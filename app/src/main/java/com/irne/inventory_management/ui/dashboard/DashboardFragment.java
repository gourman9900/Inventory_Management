package com.irne.inventory_management.ui.dashboard;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.irne.inventory_management.DashBoardActivity;
import com.irne.inventory_management.R;
import com.irne.inventory_management.ScannerActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class DashboardFragment extends Fragment {

    ConstraintLayout container;
    AnimationDrawable anim;
    private DashboardViewModel dashboardViewModel;
    private BottomNavigationView bottomNavigationView;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_scanner, container, false);

        container = (ConstraintLayout) root.findViewById(R.id.constraint_fragment_scanner);

        anim = (AnimationDrawable) container.getBackground();
        anim.setEnterFadeDuration(30);
        anim.setExitFadeDuration(30);

        bottomNavigationView = (BottomNavigationView) root.findViewById(R.id.nav_view);

        Intent intent = new Intent(getActivity(), ScannerActivity.class);
        startActivity(intent);

        return root;
    }
    @Override
    public void onResume() {
        super.onResume();
        if (anim != null && !anim.isRunning())
            anim.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (anim != null && anim.isRunning())
            anim.stop();
    }

}