package com.irne.inventory_management.ui.home;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialogListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.irne.inventory_management.Maintainance;
import com.irne.inventory_management.QrCodes;
import com.irne.inventory_management.R;
import com.irne.inventory_management.ScannerActivity;
import com.irne.inventory_management.UpdateActivity;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class HomeFragment extends Fragment implements View.OnClickListener,View.OnFocusChangeListener{

    CardView cardView;
    EditText storage_SN,storage_Name,storage_mdate,storage_MaintainanceDate,maintainance_id;
    Button storage;
    LottieAnimationView animationView;
    ImageView barcode;
    DatePickerDialog.OnDateSetListener date;
    Calendar myCalendar;
    public boolean ismdate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePickerDialog.OnDateSetListener MaintainanceDateSetListener;
    DatabaseReference reff;
    QrCodes qrCode;
    FirebaseDatabase firebaseDatabase;

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_barcode, container, false);
        /*final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        cardView = (CardView) root.findViewById(R.id.card_input);
        cardView.setBackgroundResource(R.drawable.card_love_and_liberty);


        storage_SN = root.findViewById(R.id.storage_SN);
        storage_Name = root.findViewById(R.id.storage_Name);
        storage_mdate = root.findViewById(R.id.storage_mdate);
        storage_MaintainanceDate = root.findViewById(R.id.storage_MaintainanceDate);
        maintainance_id = root.findViewById(R.id.storage_maintainanceid);

        qrCode = new QrCodes();
        reff = FirebaseDatabase.getInstance().getReference().child("QrCodes");

        storage = (Button) root.findViewById(R.id.store);
        animationView = (LottieAnimationView) root.findViewById(R.id.animationView);
        barcode = (ImageView) root.findViewById(R.id.barcode);
        storage.setOnClickListener(this);
        barcode.setOnClickListener(this);
        storage_SN.setOnFocusChangeListener(this);
        storage_Name.setOnFocusChangeListener(this);
        storage_mdate.setOnFocusChangeListener(this);
        storage_MaintainanceDate.setOnFocusChangeListener(this);
        storage_mdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),android.R.style.Theme_DeviceDefault_Dialog,mDateSetListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                datePickerDialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                String date = day + "/" + month + "/" + year;
                storage_mdate.setText(date);

            }
        };
        storage_MaintainanceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),android.R.style.Theme_DeviceDefault_Dialog,MaintainanceDateSetListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                datePickerDialog.show();
            }
        });
        MaintainanceDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                storage_MaintainanceDate.setText(date);
            }
        };
        return root;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.store:
                Log.i("value",storage_SN.getText().toString().trim());
                if (TextUtils.isEmpty(storage_SN.getText().toString())){
                    storage_SN.setError("Serial number is required");
                    storage_SN.requestFocus();
                    break;
                }
                if (TextUtils.isEmpty(storage_Name.getText().toString())){
                    storage_Name.setError("Name is required");
                    storage_Name.requestFocus();
                    break;
                }
                if (TextUtils.isEmpty(storage_mdate.getText().toString())){
                    storage_mdate.setError("Manufacturing Date is required");
                    storage_mdate.requestFocus();
                    break;
                }
                if (TextUtils.isEmpty(maintainance_id.getText().toString().trim())){
                    maintainance_id.setError("Required");
                    maintainance_id.requestFocus();
                    break;
                }
                String text = storage_SN.getText().toString().trim();// Whatever you need to encode in the QR code
                DatabaseReference reff;

                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    barcode.setImageBitmap(bitmap);
                    animationView.setVisibility(View.GONE);
                    barcode.setVisibility(View.VISIBLE);

                    reff = FirebaseDatabase.getInstance().getReference().child("QrCodes");

                    String name = storage_Name.getText().toString().trim();
                    String mdate = storage_mdate.getText().toString().trim();
                    String MaintainanceDate = storage_MaintainanceDate.getText().toString().trim();
                    String maintainance_part = "InitialMaintainance";
                    String maintainance_remark = "InitialMaintainance";
                    String maintainanceid1 = maintainance_id.getText().toString().trim();

                    qrCode.setsn(text);
                    qrCode.setName(name);
                    qrCode.setMdate(mdate);
                    qrCode.setMaintainanceDate(MaintainanceDate);
                    qrCode.setMaintainance_part(maintainance_part);
                    qrCode.setMaintainance_remark(maintainance_remark);
                    qrCode.setMaintainanceid(maintainanceid1);

                    reff.child(text).setValue(qrCode);

                    Toast.makeText(getContext(),"QR Saved Successfully",Toast.LENGTH_SHORT).show();


                } catch (WriterException e) {
                    e.printStackTrace();
                }
            case R.id.barcode:
                String text1 = storage_SN.getText().toString().trim();
                MultiFormatWriter multiFormatWriter1 = new MultiFormatWriter();
                try {
                    int WRITE_EXTERNAL_STROAGE = 1;
                    int READ_EXTERNAL_STORAGE = 1;
                    if (ContextCompat.checkSelfPermission(
                            getContext(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat
                                .requestPermissions(
                                        getActivity(),
                                        new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE },
                                        WRITE_EXTERNAL_STROAGE);
                    }
                    else{
                        Toast.makeText(getContext(),"Permission Granted",Toast.LENGTH_SHORT).show();
                    }
                    if (ContextCompat.checkSelfPermission(
                            getContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat
                                .requestPermissions(
                                        getActivity(),
                                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE },
                                        READ_EXTERNAL_STORAGE);
                    }
                    else{
                        Toast.makeText(getContext(),"Permission Granted",Toast.LENGTH_SHORT).show();
                    }
                    BitMatrix bitMatrix = multiFormatWriter1.encode(text1, BarcodeFormat.QR_CODE,200,200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    PdfDocument pdfDocument = new PdfDocument();
                    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(),bitmap.getHeight(),1).create();

                    PdfDocument.Page page = pdfDocument.startPage(pageInfo);
                    Canvas canvas = page.getCanvas();
                    Paint paint = new Paint();
                    paint.setColor(Color.parseColor("#ffffff"));
                    canvas.drawPaint(paint);


                    bitmap = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(),bitmap.getHeight(),true);
                    paint.setColor(Color.BLACK);
                    canvas.drawBitmap(bitmap,0,0,null);

                    pdfDocument.finishPage(page);

                    File root = new File(Environment.getExternalStorageDirectory()+"/Barcodes/");


                    if (!root.exists()){
                        root.mkdirs();
                    }

                    File file = new File(root,storage_Name.getText().toString().trim()+storage_SN.getText().toString().trim()+".pdf");
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        pdfDocument.writeTo(fileOutputStream);
                        Toast.makeText(getContext(),"Barcode Saved",Toast.LENGTH_SHORT).show();
                        storage_SN.setText("");
                        storage_Name.setText("");
                        storage_mdate.setText("");
                        storage_MaintainanceDate.setText("");
                    }catch(IOException e){
                        e.printStackTrace();
                    }

                    pdfDocument.close();
                }catch (WriterException e){

                }
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.storage_SN:
                if (!hasFocus) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reff = FirebaseDatabase.getInstance().getReference();
                    reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            DataSnapshot dataSnapshot1 = dataSnapshot.child("QrCodes");
                            for (DataSnapshot qr : dataSnapshot1.getChildren()) {
                                if (qr.getKey().equals(storage_SN.getText().toString().trim())) {
                                    new TTFancyGifDialog.Builder(getActivity())
                                            .setTitle("Duplicate Record Found")
                                            .setMessage("Serial number already in the databse to you want " +
                                                    "to update it")
                                            .setPositiveBtnText("Update")
                                            .setPositiveBtnBackground("#22b573")
                                            .setNegativeBtnText("Change SN")
                                            .setNegativeBtnBackground("#c1272d")
                                            .setGifResource(R.drawable.dialog_load)      //pass your gif, png or jpg
                                            .isCancellable(false)
                                            .OnPositiveClicked(new TTFancyGifDialogListener() {
                                                @Override
                                                public void OnClick() {
                                                    Intent intent = new Intent(getActivity(), UpdateActivity.class);
                                                    intent.putExtra("SN", storage_SN.getText().toString().trim());
                                                    intent.putExtra("Maintainance_Date",storage_MaintainanceDate.getText().toString().trim());
                                                    startActivity(intent);
                                                }
                                            })
                                            .OnNegativeClicked(new TTFancyGifDialogListener() {
                                                @Override
                                                public void OnClick() {
                                                    storage_SN.requestFocus();
                                                }
                                            })
                                            .build();
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


            }
        }
    }
    }
