package com.irne.inventory_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import spencerstudios.com.ezdialoglib.EZDialog;
import spencerstudios.com.ezdialoglib.EZDialogListener;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialogListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView ScannerView;
    String mdate = "",name = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        ScannerView = (ZXingScannerView) findViewById(R.id.scannerView1);

        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                ScannerView.setResultHandler(ScannerActivity.this);
                ScannerView.startCamera();
                Toast.makeText(ScannerActivity.this,"ScanActivity",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

            }
        }).check();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScannerView.setResultHandler(ScannerActivity.this);
        ScannerView.startCamera();
    }

    @Override
    public void handleResult(final Result rawResult) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("QrCodes");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot dataSnapshot1 = dataSnapshot.child(rawResult.toString().trim());
                QrCodes qrCodes = dataSnapshot1.getValue(QrCodes.class);
                mdate = qrCodes.getMdate();
                name = qrCodes.getName();
                new TTFancyGifDialog.Builder(ScannerActivity.this)
                        .setTitle("Scan Successful")
                        .setMessage("SN : " + qrCodes.getsn() +
                                "\n" + "Name : " + qrCodes.getName() +
                                "\n" + "Manufacturing Date : " + qrCodes.getMdate() +
                                "\n" + "Last Maintainance Date : " + qrCodes.getMaintainanceDate())
                        .setPositiveBtnText("View Maintainance")
                        .setPositiveBtnBackground("#22b573")
                        .setNegativeBtnText("Update Maintainance Record")
                        .setNegativeBtnBackground("#c1272d")
                        .setGifResource(R.drawable.dialog_load)      //pass your gif, png or jpg
                        .isCancellable(false)
                        .OnPositiveClicked(new TTFancyGifDialogListener() {
                            @Override
                            public void OnClick() {
                                String sn = rawResult.getText().toString().trim();
                                Intent intent = new Intent(ScannerActivity.this,MaintainanceDisplayActivity.class);
                                intent.putExtra("sn",sn);
                                startActivity(intent);

                            }
                        })
                        .OnNegativeClicked(new TTFancyGifDialogListener() {
                            @Override
                            public void OnClick() {
                                String sn = rawResult.getText().toString().trim();
                                SharedPreferences sharedPreferences = getSharedPreferences("value",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("sn",sn);
                                editor.putString("mdate",mdate);
                                editor.putString("name",name);
                                editor.apply();
                                Intent intent = new Intent(ScannerActivity.this,MaintainanceActivity.class);
                                startActivity(intent);
                                ScannerView.resumeCameraPreview(ScannerActivity.this);
                            }
                        })
                        .build();
                ScannerView.resumeCameraPreview(ScannerActivity.this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
