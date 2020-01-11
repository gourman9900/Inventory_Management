package com.irne.inventory_management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class MaintainanceActivity extends AppCompatActivity implements View.OnFocusChangeListener{

    CardView cardView;
    TextView maintainance_SN;
    EditText maintainance_date,maintainance_parts,maintainance_remarks,maintainance_id;
    Button maintainance_update;
    DatePickerDialog.OnDateSetListener maintainance_date_set_listener;
    FirebaseDatabase database;
    QrCodes qrCode;
    Maintainance maintainance;
    String sn1 = "",mdate1 = "",name1 = "";
    DatabaseReference reff,reff_qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintainance);


        SharedPreferences sharedPreferences = getSharedPreferences("value",MODE_PRIVATE);
        sn1 = sharedPreferences.getString("sn","");
        mdate1 = sharedPreferences.getString("mdate","");
        name1 = sharedPreferences.getString("name","");
        cardView = (CardView) findViewById(R.id.card_maintainance);
        cardView.setBackgroundResource(R.drawable.card_can_you_feel_the_love_tonight);


        maintainance_SN = (TextView) findViewById(R.id.update_SN_maintainance);
        maintainance_date = (EditText) findViewById(R.id.maintainance_date);
        maintainance_parts = (EditText) findViewById(R.id.maintainance_part);
        maintainance_remarks = (EditText) findViewById(R.id.maintainance_remark);
        maintainance_id = (EditText) findViewById(R.id.maintainance_id);
        maintainance_update = (Button) findViewById(R.id.update_maintainance);

        maintainance_SN.setText(sn1);

        maintainance_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MaintainanceActivity.this,
                        android.R.style.Theme_DeviceDefault_Dialog,maintainance_date_set_listener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                datePickerDialog.show();
            }
        });
        maintainance_date_set_listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                String date = day + "/" + month + "/" + year;
                maintainance_date.setText(date);

            }
        };

        maintainance_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Toast.makeText(MaintainanceActivity.this, "one", Toast.LENGTH_SHORT).show();
                    if (TextUtils.isEmpty(maintainance_parts.getText().toString())) {
                        maintainance_parts.setError("Required");
                        maintainance_parts.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(maintainance_remarks.getText().toString())) {
                        maintainance_remarks.setError("Required");
                        maintainance_remarks.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(maintainance_id.getText().toString().trim())){
                        maintainance_id.setError("Required");
                        maintainance_id.requestFocus();
                        return;
                    }

                    SharedPreferences sharedPreferences = getSharedPreferences("value",MODE_PRIVATE);
                    sn1 = sharedPreferences.getString("sn","");
                    mdate1 = sharedPreferences.getString("mdate","");
                    name1 = sharedPreferences.getString("name","");


                    String date = maintainance_date.getText().toString().trim();
                    String part = maintainance_parts.getText().toString().trim();
                    String remarks = maintainance_remarks.getText().toString().trim();
                    String id = maintainance_id.getText().toString().trim();

                    database = FirebaseDatabase.getInstance();
                    Log.i("String", sn1 + part);
                    reff = FirebaseDatabase.getInstance().getReference().child("Maintainance").child(sn1).child(id);
                    reff_qr = FirebaseDatabase.getInstance().getReference().child("QrCodes").child(sn1);


                    Log.i("String",sn1);
                    reff_qr.child("sn").setValue(sn1);
                    reff_qr.child("mdate").setValue(mdate1);
                    reff_qr.child("name").setValue(name1);
                    reff_qr.child("maintainance_part").setValue(part);
                    reff_qr.child("maintainance_remark").setValue(remarks);
                    reff_qr.child("maintainanceDate").setValue(date);

                    maintainance = new Maintainance(sn1,part,remarks,date,id);

                    reff.setValue(maintainance);

                    Toast.makeText(MaintainanceActivity.this, "Maintainance Added", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    public void onFocusChange(View view, boolean b) {

    }

}
