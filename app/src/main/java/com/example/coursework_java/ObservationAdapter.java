package com.example.coursework_java;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationViewHolder> {

    private Context context;
    private List<Observation> observationList;

    public ObservationAdapter(Context context, List<Observation> observationList) {
        this.context = context;
        this.observationList = observationList;
    }

    @NonNull
    @Override
    public ObservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.obs_recycle_item, parent, false);
        return new ObservationViewHolder(view);
    }

    public Bitmap getImageFromBlob(byte[] blobData) {
        if (blobData != null) {
            return BitmapFactory.decodeByteArray(blobData, 0, blobData.length);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ObservationViewHolder holder, int position) {
        try {
            Bitmap imageBitmap = getImageFromBlob(observationList.get(position).getImageData());
            if (imageBitmap != null) {
                holder.obsImage.setImageBitmap(imageBitmap);
            }
        } catch (Exception e) {
            // Handle the error, log it, or display a placeholder image
            e.printStackTrace();
        }
        holder.obsName.setText(observationList.get(position).getName());
        holder.obsDesc.setText(observationList.get(position).getDesc());
        holder.obsTime.setText(observationList.get(position).getTime());

        holder.obsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ObsDetailActivity.class);
                intent.putExtra("Image", observationList.get(holder.getAdapterPosition()).getImageData());
                intent.putExtra("Description", observationList.get(holder.getAdapterPosition()).getDesc());
                intent.putExtra("Name", observationList.get(holder.getAdapterPosition()).getName());
                intent.putExtra("Key",observationList.get(holder.getAdapterPosition()).getKey());
                intent.putExtra("HikeId", observationList.get(holder.getAdapterPosition()).getHikeId());
                intent.putExtra("Time", observationList.get(holder.getAdapterPosition()).getTime());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return observationList.size();
    }

}

class ObservationViewHolder extends RecyclerView.ViewHolder{

    ImageView obsImage;
    TextView obsName, obsDesc, obsTime;
    CardView obsCard;

    public ObservationViewHolder(@NonNull View itemView) {
        super(itemView);

        obsImage = itemView.findViewById(R.id.obsItemImage);
        obsName = itemView.findViewById(R.id.obsItemName);
        obsDesc = itemView.findViewById(R.id.obsItemDesc);
        obsTime = itemView.findViewById(R.id.obsItemTime);
        obsCard = itemView.findViewById(R.id.obsCard);
    }
}