package com.FITO.FitoAdmin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

public class couponAdapter extends FirebaseRecyclerAdapter<couponList, couponAdapter.couponAdapterViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public couponAdapter(@NonNull FirebaseRecyclerOptions<couponList> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull couponAdapterViewHolder holder, int position, @NonNull couponList model) {
        holder.couponName.setText(model.getName());
        holder.sponsoredName.setText(model.getSponsoredName());
        holder.points.setText(model.getPoints());
        Picasso.get().load(model.getImage()).into(holder.couponImg);
    }

    @NonNull
    @Override
    public couponAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coupon_ui,parent,false);
        return new couponAdapterViewHolder(view);

    }

    class couponAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView couponName, sponsoredName, points;
        ImageView couponImg;

        public couponAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            couponImg = itemView.findViewById(R.id.couponImage);
            couponName = itemView.findViewById(R.id.couponName);
            sponsoredName = itemView.findViewById(R.id.couponSponsoredName);
            points = itemView.findViewById(R.id.textPoints);
        }
    }
}
