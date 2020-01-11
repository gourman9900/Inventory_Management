package com.irne.inventory_management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class UpdateActivity extends AppCompatActivity implements View.OnFocusChangeListener,View.OnClickListener{

    CardView cardView;
    TextView update_SN;
    EditText update_mdate,update_name,update_Maintainance_Date,update_maintainance_part,update_maintainance_remarks,update_maintainance_id;
    Button update;
    FirebaseDatabase database;
    DatabaseReference reff;
    String sn,Maintainance_Date;
    LottieAnimationView update_animation;
    QrCodes qrCode;
    Maintainance maintainance;
    DatabaseReference reff_maintainance;
    DatePickerDialog.OnDateSetListener update_mdatedatesetListener,update_MaintainanceDatesetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        cardView = (CardView) findViewById(R.id.cardinput_update);
        cardView.setBackgroundResource(R.drawable.card_can_you_feel_the_love_tonight);

        Intent intent = getIntent();
        update_SN = (TextView) findViewById(R.id.update_SN);
        sn = intent.getStringExtra("SN");
        Maintainance_Date = intent.getStringExtra("Maintainance_Date");
        update_SN.setText("SN : " + sn);

        qrCode = new QrCodes();

        update_name = (EditText) findViewById(R.id.storage_update_Name);
        update_mdate = (EditText) findViewById(R.id.storage_update_mdate);
        update_Maintainance_Date = (EditText) findViewById(R.id.storage_update_MaintainanceDate);

        update_maintainance_part = (EditText) findViewById(R.id.storage_update_maintainance_part);
        update_maintainance_remarks = (EditText) findViewById(R.id.storage_update_maintainance_remark);
        update_maintainance_id = (EditText) findViewById(R.id.storage_update_maintainance_id);


        update_animation = (LottieAnimationView) findViewById(R.id.animation_update);


        update = (Button) findViewById(R.id.store_update);

        update_name.setOnFocusChangeListener(this);
        update_mdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateActivity.this,
                        android.R.style.Theme_DeviceDefault_Dialog,update_mdatedatesetListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                datePickerDialog.show();
            }
        });
        update_mdatedatesetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                String date = day + "/" + month + "/" + year;
                update_mdate.setText(date);
            }
        };
        update_Maintainance_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateActivity.this,
                        android.R.style.Theme_DeviceDefault_Dialog,update_MaintainanceDatesetListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                datePickerDialog.show();
            }
        });
        update_MaintainanceDatesetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                String date = day + "/" + month + "/" + year;
                update_Maintainance_Date.setText(date);
            }
        };
        update.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.store_update:
                if (TextUtils.isEmpty(update_name.getText().toString())){
                    update_name.setError("Name is required");
                    update_name.requestFocus();
                    break;
                }
                if (TextUtils.isEmpty(update_mdate.getText().toString())){
                    update_mdate.setError("Manufacture date is required");
                    update_mdate.requestFocus();
                    break;
                }
                if (TextUtils.isEmpty(update_Maintainance_Date.getText().toString())){
                    update_Maintainance_Date.setError("Required");
                    update_Maintainance_Date.requestFocus();
                    break;
                }
                if(TextUtils.isEmpty(update_maintainance_part.getText().toString())){
                    update_maintainance_part.setError("Required");
                    update_maintainance_part.requestFocus();
                    break;
                }
                if(TextUtils.isEmpty(update_maintainance_remarks.getText().toString())){
                    update_maintainance_remarks.setError("Remarks is required");
                    update_maintainance_remarks.requestFocus();
                    break;
                }
                if(TextUtils.isEmpty(update_maintainance_id.getText().toString().trim())){
                    update_maintainance_id.setError("Required");
                    update_maintainance_id.requestFocus();
                    break;
                }
                update.setVisibility(View.GONE);
                update_animation.setVisibility(View.VISIBLE);
                String name = update_name.getText().toString().trim();
                String update_Mdate = update_mdate.getText().toString().trim();
                String update_MaintainanceDate = update_Maintainance_Date.getText().toString().trim();
                String parts = update_maintainance_part.getText().toString().trim();
                String remarks = update_maintainance_remarks.getText().toString().trim();
                String id = update_maintainance_id.getText().toString().trim();

                database = FirebaseDatabase.getInstance();
                reff = FirebaseDatabase.getInstance().getReference().child("QrCodes").child(sn);

                reff_maintainance = FirebaseDatabase.getInstance().getReference().child("Maintainance").child(sn).child(id);

                qrCode.setsn(sn);
                qrCode.setName(name);
                qrCode.setMdate(update_Mdate);
                qrCode.setMaintainanceDate(update_MaintainanceDate);
                qrCode.setMaintainance_part(parts);
                qrCode.setMaintainance_remark(remarks);

                maintainance = new Maintainance(sn,parts,remarks,update_MaintainanceDate,id);


                reff.setValue(qrCode);

                reff_maintainance.setValue(maintainance);

                update_animation.setVisibility(View.GONE);
                update.setVisibility(View.VISIBLE);
                Toast.makeText(UpdateActivity.this,"Updated",Toast.LENGTH_SHORT).show();


        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        switch (view.getId()){
            case R.id.storage_update_Name:
                if (!hasFocus) {
                    if (TextUtils.isEmpty(update_name.getText().toString())) {
                        update_name.setError("Name is required");
                        update_name.requestFocus();
                        break;
                    }
                }
        }

    }
}
