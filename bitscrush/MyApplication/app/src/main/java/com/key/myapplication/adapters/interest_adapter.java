package com.key.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.florent37.shapeofview.shapes.RoundRectView;
import com.key.myapplication.R;
import com.key.myapplication.models.interest_model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class interest_adapter extends RecyclerView.Adapter<interest_adapter.MyViewHolder> {

    public static Set<String> add_list = new HashSet<String>();

    ArrayList<interest_model> interest_list;
    Context context;
    ArrayList<Integer> interests_selected_already;
    private ItemClickListener mClickListener;
    private int row_index = -1;


    public interest_adapter(Context context, ArrayList<interest_model> interest_list, ArrayList<Integer> interests_selected_already) {
        this.interests_selected_already = interests_selected_already;
        this.context = context;
        this.interest_list = interest_list;

    }

    @NonNull
    @Override
    public interest_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.interest_layout, parent, false);
        return new interest_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final interest_adapter.MyViewHolder holder, int position) {

        final interest_model intrest_obj = interest_list.get(position);
//

        if (interests_selected_already.size() != 0) {
            if (interests_selected_already.contains(intrest_obj.getId())) {
                intrest_obj.setIs_selected(true);
                add_list.add(String.valueOf(interests_selected_already.get(interests_selected_already.indexOf(intrest_obj.getId()))));
            }

        }

        holder.text.setText(intrest_obj.getName());
        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!intrest_obj.isIs_selected()){
                    intrest_obj.setIs_selected(true);
                    notifyDataSetChanged();
                }else{
                    if (interests_selected_already.contains(intrest_obj.getId())) {
                        interests_selected_already.remove(interests_selected_already.indexOf(intrest_obj.getId()));
                    }
                    intrest_obj.setIs_selected(false);
                    notifyDataSetChanged();
                }


            }

        });

        if(!intrest_obj.isIs_selected()){
            // on click unselect
            holder.text.setTextColor(context.getColor(R.color.pink));
            holder.text.setBackground(context.getResources().getDrawable(R.drawable.red_text_box));
            add_list.remove(String.valueOf(intrest_obj.getId()));
        }else{
            //on click select
            holder.text.setTextColor(context.getColor(R.color.white));
            holder.text.setBackground(context.getResources().getDrawable(R.drawable.pink_button));
            add_list.add(String.valueOf(intrest_obj.getId()));
        }

//
    }

    @Override
    public int getItemCount() {
        return interest_list.size();
    }

    public interest_model getItem(int id) {
        return interest_list.get(id);
    }
    // convenience method for getting data at click position

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView text;

        MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            text = view.findViewById(R.id.interest_text);


        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());

        }
    }
}


