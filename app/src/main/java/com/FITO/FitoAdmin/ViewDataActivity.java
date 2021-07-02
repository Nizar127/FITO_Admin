package com.FITO.FitoAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewDataActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    couponAdapter thecouponAdapter;
    DatabaseReference mbase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);
        mbase = FirebaseDatabase.getInstance().getReference("Coupon");

        recyclerView = findViewById(R.id.recyclerviewSystem);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        FirebaseRecyclerOptions<couponList> options = new FirebaseRecyclerOptions.Builder<couponList>()
                .setQuery(mbase, new SnapshotParser<couponList>() {
                    @NonNull
                    @Override
                    public com.FITO.FitoAdmin.couponList parseSnapshot(@NonNull DataSnapshot snapshot) {
                        couponList coupondata = new couponList();
                        coupondata.setName(snapshot.child("Coupon_name").getValue().toString());
                        coupondata.setSponsoredName(snapshot.child("Sponsored_Name").getValue().toString());
                        coupondata.setImage(snapshot.child("image").getValue().toString());
                        coupondata.setPoints(snapshot.child("Points").getValue().toString());
                        return coupondata;
                    }
                })
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
