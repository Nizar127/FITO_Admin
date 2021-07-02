package com.FITO.FitoAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import in.dd4you.appsconfig.DD4YouConfig;

import static androidx.constraintlayout.motion.widget.MotionScene.TAG;

public class GenerateQRCodeActivity extends AppCompatActivity {

    EditText input, input2;
    Button btnGenerate, getID, saveBtn;
    ImageView outputQR, downloadBtn;
    TextView uuid;
    private Activity Activity;
    private DD4YouConfig dd4YouConfig;
    private DatabaseReference prodRef;
    String saveCurrentDate, saveCurrentTime, productRandomKey;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_q_r_code);

        input = findViewById(R.id.inputText);
        input2 = findViewById(R.id.inputText2);
        btnGenerate = findViewById(R.id.generateBtn);
        outputQR = findViewById(R.id.outputQR);
        uuid = findViewById(R.id.uniqueID);
        downloadBtn = findViewById(R.id.btnDownload);
        getID = findViewById(R.id.getID);
       // saveBtn = findViewById(R.id.saveBtn);

        dd4YouConfig = new DD4YouConfig(this);

        prodRef = FirebaseDatabase.getInstance().getReference().child("qrcode");




        ActivityCompat.requestPermissions(GenerateQRCodeActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        ActivityCompat.requestPermissions(GenerateQRCodeActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);


        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
            }
        });

        getID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // String uniqueID = UUID.randomUUID().toString();
                //get unique ID
                uuid.setText(dd4YouConfig.generateUniqueID(10));
            }
        });

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generate();

            }
        });
    }

    private void generate() {
        String userID = uuid.getText().toString().trim();
        String stext  = input.getText().toString().trim();
        String pointtext  = input2.getText().toString().trim();
        MultiFormatWriter writer = new MultiFormatWriter();
        try{
            //BitMatrix matrix = writer.encode(stext, BarcodeFormat.QR_CODE, 350, 350);
            BitMatrix matrix = writer.encode(pointtext, BarcodeFormat.QR_CODE, 350, 350);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            outputQR.setImageBitmap(bitmap);
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(input.getApplicationWindowToken(), 0);
            manager.hideSoftInputFromWindow(input2.getApplicationWindowToken(), 0);


        }catch (WriterException e){
            e.printStackTrace();
        }

        saveData(userID, stext, pointtext);

        //set save button to be working
       // saveBtn.setVisibility(View.VISIBLE);
    }

    private void saveData(String userID, String stext, String pointtext) {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        //To create a unique product random key, so that it doesn't overwrite other product
        productRandomKey = saveCurrentDate + saveCurrentTime;


        HashMap<String, Object> codemap = new HashMap<>();
        codemap.put("uniquedID", userID);
        codemap.put("pid", productRandomKey);
        codemap.put("couponname",stext);
        codemap.put("points", pointtext);



        prodRef.child(productRandomKey).updateChildren(codemap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(GenerateQRCodeActivity.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                }else{
                    String message = task.getException().toString();
                    Toast.makeText(GenerateQRCodeActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void saveImage() {
        Log.d(TAG, "saveImage: downloading");

        BitmapDrawable bitmapDrawable = (BitmapDrawable) outputQR.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        //Bitmap bitmap = (BitmapDrawable) downloadBtn.getDrawable().getBitmap();
        FileOutputStream outputStream = null;
        //File file = getExternalCacheDir();
        //File dir = new File(file.getAbsolutePath() + "/QRPics:");
        File file = new File(getExternalFilesDir(null) + "/QRPics:");
        file.mkdir();
        Log.d(TAG,"Working File: " + file);

        String filename = String.format("%d.png", System.currentTimeMillis());
        File outfile = new File(file, filename);
        try{
            outputStream = new FileOutputStream(outfile);
        }catch (Exception e){
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        try{
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            outputStream.close();
            Toast.makeText(GenerateQRCodeActivity.this,"Data Inserted",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
