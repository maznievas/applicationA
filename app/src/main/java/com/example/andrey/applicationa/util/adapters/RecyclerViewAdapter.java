package com.example.andrey.applicationa.util.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.andrey.applicationa.R;
import com.example.andrey.applicationa.database.ReferenceData;
import com.example.andrey.applicationa.util.ItemSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<ReferenceData> referencesList;
    private ItemSelectedListener itemSelectedListener;

    public RecyclerViewAdapter() {
        referencesList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reference_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReferenceData referenceData = referencesList.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        holder.dateTextView.setText(sdf.format(referenceData.getLastOpened()));
        holder.urlTextView.setText(referenceData.getUrl());
        switch (referenceData.getStatus()) {
            case 1:
                holder.itemParentLayout.setBackgroundResource(R.color.colorGreen);
                break;
            case 2:
                holder.itemParentLayout.setBackgroundResource(R.color.colorRed);
                break;
            case 3:
                holder.itemParentLayout.setBackgroundResource(R.color.colorGrey);
                break;
            default:
                holder.itemParentLayout.setBackgroundResource(R.color.colorGrey);
                break;
        }

        holder.itemParentLayout.setOnClickListener(listener -> {
            itemSelectedListener.selectedReference(referenceData);
        });
    }

    @Override
    public int getItemCount() {
        if (referencesList != null)
            return referencesList.size();
        else
            return 0;
    }

    public void updateReferencesList(List<ReferenceData> referencesList) {
        this.referencesList.addAll(referencesList);
        notifyDataSetChanged();
    }

    public void setReferencesList(List<ReferenceData> referenceDataList) {
        this.referencesList = referenceDataList;
        notifyDataSetChanged();
    }

    public void setItemSelectedListener(ItemSelectedListener itemSelectedListener) {
        this.itemSelectedListener = itemSelectedListener;
    }

    public void sortByDate() {
        Collections.sort(referencesList, new Comparator<ReferenceData>() {
            @Override
            public int compare(ReferenceData o1, ReferenceData o2) {
                Date firstDate = o1.getLastOpened();
                Date secondDate = o2.getLastOpened();
                if (firstDate.getTime() > secondDate.getTime())
                    return -1;
                else
                    return 1;
            }
        });
        notifyDataSetChanged();
    }

    public void sortByStatus() {
        Collections.sort(referencesList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemParentLayout)
        ViewGroup itemParentLayout;

        @BindView(R.id.textViewDate)
        TextView dateTextView;

        @BindView(R.id.textViewUrl)
        TextView urlTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
