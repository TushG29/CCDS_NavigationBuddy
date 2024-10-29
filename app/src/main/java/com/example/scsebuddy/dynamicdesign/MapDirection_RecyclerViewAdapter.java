package com.example.scsebuddy.dynamicdesign;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scsebuddy.PanoramaViewActivity;
import com.example.scsebuddy.R;
import com.example.scsebuddy.requestsresults.Direction;

import java.util.ArrayList;

public class MapDirection_RecyclerViewAdapter extends RecyclerView.Adapter<MapDirection_RecyclerViewAdapter.MyViewHolder> {
    Context context;
    static Context context1;
    ArrayList<Direction> direction;
    static String[] path = new String[1];

    public MapDirection_RecyclerViewAdapter(Context context, ArrayList<Direction> direction) {
        this.context = context;
        this.direction = direction;
    }

    @NonNull
    @Override
    public MapDirection_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.direction_row, parent, false); //Add new card VIEW!! TODO
        return new MapDirection_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MapDirection_RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.directionMessageTextView.setText(direction.get(position).getDirectionMessage());

        int imageSrc = R.drawable.logo;
        switch (direction.get(position).getPhotoSrc()) {
            case "horizontal_arrow":
                imageSrc = R.drawable.horizontal_arrow;
                break;
            case "up_arrow":
                imageSrc = R.drawable.up_arrow;
                break;
            case "down_arrow":
                imageSrc = R.drawable.down_arrow;
                break;
        }
        holder.directionArrowImageView.setImageResource(imageSrc);
        holder.photoIDTextView.setText(direction.get(position).getNextLocation());
        holder.photo360ImageView.setImageResource(chooseDrawable(direction.get(position).getNextLocation()));
    }

    @Override
    public int getItemCount() {
        return direction.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView directionMessageTextView, photoIDTextView;
        ImageView photo360ImageView;
        ImageView directionArrowImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            context1 = itemView.getContext();
            directionMessageTextView = itemView.findViewById(R.id.directionMessageTextView);
            photo360ImageView = itemView.findViewById(R.id.photo360ImageView);
            directionArrowImageView = itemView.findViewById(R.id.directionArrowImageView);
            photoIDTextView = itemView.findViewById(R.id.photoIDTextView);
        }

        @Override
        public void onClick(View view) {
            final  Intent intent;
            path[0] = photoIDTextView.getText().toString();
            intent = new Intent(context1, PanoramaViewActivity.class);
            intent.putExtra("path", path);
            context1.startActivity(intent);

        }
    }

        private int chooseDrawable(String drawableName) {
            switch (drawableName) {
                case "a1":
                    return R.drawable.a1;
                case "a2":
                    return R.drawable.a2;
                case "a3":
                    return R.drawable.a3;
                case "a4":
                    return R.drawable.a4;
                case "a5":
                    return R.drawable.a5;
                case "a6":
                    return R.drawable.a6;
                case "a7":
                    return R.drawable.a7;
                case "a8":
                    return R.drawable.a8;
                case "a9":
                    return R.drawable.a9;
                case "a10":
                    return R.drawable.a10;
                case "a11":
                    return R.drawable.a11;
                case "a12":
                    return R.drawable.a12;
                case "a13":
                    return R.drawable.a13;
                case "a14":
                    return R.drawable.a14;
                case "a15":
                    return R.drawable.a15;
                case "a16":
                    return R.drawable.a16;
                case "a17":
                    return R.drawable.a17;
                case "a18":
                    return R.drawable.a18;
                case "a19":
                    return R.drawable.a19;
                case "a20":
                    return R.drawable.a20;
                case "a21":
                    return R.drawable.a21;
                case "a22":
                    return R.drawable.a22;
                case "a23":
                    return R.drawable.a23;
                case "a24":
                    return R.drawable.a24;
                case "a25":
                    return R.drawable.a25;
                case "a26":
                    return R.drawable.a26;
                case "a27":
                    return R.drawable.a27;
                case "a28":
                    return R.drawable.a28;
                case "a29":
                    return R.drawable.a29;
                case "a30":
                    return R.drawable.a30;
                case "a31":
                    return R.drawable.a31;
                case "a32":
                    return R.drawable.a32;
                case "a33":
                    return R.drawable.a33;
                case "a34":
                    return R.drawable.a34;
                case "a35":
                    return R.drawable.a35;
                case "a36":
                    return R.drawable.a36;
                case "a37":
                    return R.drawable.a37;
                case "a38":
                    return R.drawable.a38;
                case "a39":
                    return R.drawable.a39;
                case "a40":
                    return R.drawable.a40;
                case "a41":
                    return R.drawable.a41;
                case "a42":
                    return R.drawable.a42;
                case "a43":
                    return R.drawable.a43;
                default:
                    break;
            }
            return -1;
        }

}
