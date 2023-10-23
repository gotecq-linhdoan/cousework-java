package com.example.coursework_java;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HikeAdapter extends RecyclerView.Adapter<HikeViewHolder> {
    private Context context;
    private List<Hike> hikeList;


    public HikeAdapter(Context context, List<Hike> hikeList) {
        this.context = context;
        this.hikeList = hikeList;
    }

    @NonNull
    @Override
    public HikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item, parent, false);
        return new HikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HikeViewHolder holder, int position) {
        holder.hikeName.setText(hikeList.get(position).getHikeName());
        holder.hikeDesc.setText(hikeList.get(position).getHikeDesc());
        holder.hikeLength.setText(hikeList.get(position).getHikeLength() + "m");
        holder.hikeLevel.setText(hikeList.get(position).getHikeLevel());

        holder.hikeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("Description", hikeList.get(holder.getAdapterPosition()).getHikeDesc());
                intent.putExtra("Name", hikeList.get(holder.getAdapterPosition()).getHikeName());
                intent.putExtra("Level", hikeList.get(holder.getAdapterPosition()).getHikeLevel());
                intent.putExtra("Length", hikeList.get(holder.getAdapterPosition()).getHikeLength());
                intent.putExtra("Date", hikeList.get(holder.getAdapterPosition()).getHikeDate());
                intent.putExtra("Location", hikeList.get(holder.getAdapterPosition()).getHikeLocation());
                intent.putExtra("Parking", hikeList.get(holder.getAdapterPosition()).getHasParking());
                intent.putExtra("Key", hikeList.get(holder.getAdapterPosition()).getKey());

                context.startActivity(intent);
            }
        });
    }

    public void clear() {
        clear();
    }

    @Override
    public int getItemCount() {
        return hikeList.size();
    }
    public void searchDataList(ArrayList<Hike> searchList){
        hikeList = searchList;
        notifyDataSetChanged();
    }
}
class HikeViewHolder extends RecyclerView.ViewHolder {

    TextView hikeName, hikeLength, hikeDesc, hikeLevel;

    CardView hikeCard;

    public HikeViewHolder(@NonNull View itemView) {
        super(itemView);

        hikeName = itemView.findViewById(R.id.hikeNameItem);
        hikeLength = itemView.findViewById(R.id.hikeLengthItem);
        hikeDesc = itemView.findViewById(R.id.hikeDescItem);
        hikeLevel = itemView.findViewById(R.id.hikeLevelItem);
        hikeCard = itemView.findViewById(R.id.hikeCard);
    }
}
