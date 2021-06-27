package com.FITO.FitoAdmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    couponAdapter thecouponAdapter;
    DatabaseReference mbase;
    Button productBtn, qrBtn;
    couponList couponList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mbase = FirebaseDatabase.getInstance().getReference();
        productBtn = findViewById(R.id.btnAddNewProduct);
        qrBtn = findViewById(R.id.btnGenQRCode);

        couponList = new couponList();

        productBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, newProductActivity.class);
                startActivity(intent);
            }
        });

        qrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GenerateQRCodeActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recyclerviewSystem);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        FirebaseRecyclerOptions<couponList> options = new FirebaseRecyclerOptions.Builder<couponList>()
                                                        .setQuery(mbase, couponList.class)
                                                        .build();

        thecouponAdapter = new couponAdapter(options);
        recyclerView.setAdapter(thecouponAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        thecouponAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        thecouponAdapter.stopListening();
    }
}
