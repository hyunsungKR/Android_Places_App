package com.hyunsungkr.placesapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hyunsungkr.placesapp.R;
import com.hyunsungkr.placesapp.model.Place;

import java.util.ArrayList;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

    public interface OnItemClickListener{
        void onCardViewClick(int index);
    }
    public OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    Context context;
    ArrayList<Place> placeList;

    public PlaceAdapter(Context context, ArrayList<com.hyunsungkr.placesapp.model.Place> placeList) {
        this.context = context;
        this.placeList = placeList;
    }

    @NonNull
    @Override
    public PlaceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceAdapter.ViewHolder holder, int position) {

        Place place = placeList.get(position);

        if(place.getName() != null){
            holder.txtName.setText(place.getName());
        }else {
            holder.txtName.setText("상점명 없음");
        }
        if(place.getVicinity() != null){
            holder.txtVicinity.setText(place.getVicinity());
        }else {
            holder.txtVicinity.setText("주소 없음");
        }
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView txtName;
        TextView txtVicinity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            txtName = itemView.findViewById(R.id.txtName);
            txtVicinity = itemView.findViewById(R.id.txtVicinity);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                int index = getAdapterPosition();

                listener.onCardViewClick(index);




                }
            });

        }
    }
}
