package com.FITO.FitoAdmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class newProductActivity extends AppCompatActivity {

    private TextView getCategory;

    private String categoryName, desc, point, name, sponsored, term, validity, contact, saveCurrentDate, saveCurrentTime, productRandomKey, downloadImgUrl;
    private Button addNewProdBtn;
    private ImageView inputProdImg, backBtn;
    private EditText inputProdName, inputProdDesc, inputProdPoint, inputProdContact, inputProdTerm, inputProdValidity, inputSponsoredName;
    private static final int GalleryPick = 1;
    private Uri imgUri;
    private StorageReference prodImgRef;
    private DatabaseReference prodRef;
    //private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        prodImgRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        prodRef = FirebaseDatabase.getInstance().getReference().child("Coupon");
        backBtn = findViewById(R.id.back_btn_add_new);

        addNewProdBtn = findViewById(R.id.add_new_product);
        inputProdName = findViewById(R.id.coupon_name);
        inputProdDesc = findViewById(R.id.coupon_highlight);
        inputProdContact = findViewById(R.id.sponsoredContact);
        inputProdPoint = findViewById(R.id.product_points);
        inputProdValidity = findViewById(R.id.couponValidity);
        inputProdTerm = findViewById(R.id.sponsoredTerm);
        inputSponsoredName = findViewById(R.id.sponsoredName);

        inputProdImg = findViewById(R.id.select_product_image);

        inputProdImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity(imgUri).setAspectRatio(1,1).start(newProductActivity.this);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        addNewProdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imgUri = result.getUri();

            inputProdImg.setImageURI(imgUri);
        } else{
            Toast.makeText(this, "Error occurred, please try again.", Toast.LENGTH_SHORT).show();

        }
    }

    private void ValidateProductData() {
        name = inputProdName.getText().toString().trim();
        desc = inputProdDesc.getText().toString().trim();
        point = inputProdPoint.getText().toString().trim();
        sponsored = inputSponsoredName.getText().toString().trim();
        term = inputProdTerm.getText().toString().trim();
        validity = inputProdValidity.getText().toString().trim();
        contact = inputProdContact.getText().toString().trim();




        if (imgUri == null) {
            Toast.makeText(this, "Product image required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Product name required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(desc)) {
            Toast.makeText(this, "Product description required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(point)) {
            Toast.makeText(this, "Product point required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(sponsored)) {
            Toast.makeText(this, "Sponsored name required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(term)) {
            Toast.makeText(this, "Product term and conditions required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(contact)) {
            Toast.makeText(this, "Sponsored Contact required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(validity)) {
            Toast.makeText(this, "Product validity required", Toast.LENGTH_SHORT).show();
        }
        else {
            StoreProductInformation(name,desc, point,sponsored, term, validity, contact);
        }
    }

    private void StoreProductInformation(String name, String desc, String point, String sponsored, String term, String validity, String contact) {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        //To create a unique product random key, so that it doesn't overwrite other product
        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = prodImgRef.child(imgUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imgUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(newProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                //loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(newProductActivity.this, "Product Image uploaded", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        downloadImgUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImgUrl = task.getResult().toString();

                            Toast.makeText(newProductActivity.this, "Product image url received", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase(name,desc, point,sponsored, term, validity, contact);
                        }
                    }

                });
            }
        });
    }

    private void SaveProductInfoToDatabase(String name, String desc, String point, String sponsored, String term, String validity, String contact) {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("image", downloadImgUrl);
        productMap.put("Coupon_name", name);
        productMap.put("nameLower", name.toLowerCase());
        productMap.put("Coupon_Highlight", desc);
        productMap.put("Coupon_Term", term);
        productMap.put("Coupon_Contact", contact);
        productMap.put("Sponsored_Name", sponsored);
        productMap.put("Coupon_Validity", validity);
        productMap.put("Points", point);

        prodRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(newProductActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                            // loadingBar.dismiss();
                            Toast.makeText(newProductActivity.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            // loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(newProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
