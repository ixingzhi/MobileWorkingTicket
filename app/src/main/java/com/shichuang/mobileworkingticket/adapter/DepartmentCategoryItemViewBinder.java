package com.shichuang.mobileworkingticket.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.entify.Category;

import me.drakeet.multitype.ItemViewBinder;


/**
 * Created by Administrator on 2017/9/30.
 */

public class DepartmentCategoryItemViewBinder extends ItemViewBinder<Category, DepartmentCategoryItemViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected DepartmentCategoryItemViewBinder.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_category, parent, false);
        return new DepartmentCategoryItemViewBinder.ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final Category item) {
        holder.tvIndex.setText(item.title);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIndex;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIndex = (TextView) itemView.findViewById(R.id.tv_index);
        }
    }
}
