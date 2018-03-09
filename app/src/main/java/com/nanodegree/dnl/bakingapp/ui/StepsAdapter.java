package com.nanodegree.dnl.bakingapp.ui;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nanodegree.dnl.bakingapp.R;
import com.nanodegree.dnl.bakingapp.data.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

    private final List<Step> mValues;

    private StepsOnClickHandler mOnClickHandler;

    public interface StepsOnClickHandler {
        void onClick(int position);
    }

    StepsAdapter(List<Step> items, StepsOnClickHandler mOnClickHandler) {
        mValues = items;
        this.mOnClickHandler = mOnClickHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_step, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mIdView.setText(String.format("%d)", position));
        holder.mContentView.setText(mValues.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.id_text)
        TextView mIdView;
        @BindView(R.id.content)
        TextView mContentView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnClickHandler.onClick(getAdapterPosition());
        }
    }
}
