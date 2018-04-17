package com.shichuang.mobileworkingticket.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.activity.OrganizationalStructureActivity;
import com.shichuang.mobileworkingticket.entify.DepartmentList;
import com.shichuang.open.tool.RxActivityTool;

import me.drakeet.multitype.ItemViewBinder;


/**
 * Created by Administrator on 2017/9/30.
 */

public class DepartmentDepItemViewBinder extends ItemViewBinder<DepartmentList.Department, DepartmentDepItemViewBinder.ViewHolder> {
    private Context mContext;

    public DepartmentDepItemViewBinder() {

    }

    @NonNull
    @Override
    protected DepartmentDepItemViewBinder.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        this.mContext = parent.getContext();
        View view = inflater.inflate(R.layout.item_organizational_structure, parent, false);
        return new DepartmentDepItemViewBinder.ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final DepartmentList.Department item) {
        holder.tvDepartmentName.setText(item.getName());
        holder.tvDepartmentNum.setText(item.getUserCount() + "");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getUserCount() != 0) {  // >0就是有 =0没有         item.getHasChild() != 0 && item.getUserCount() != 0
                    Bundle bundle = new Bundle();
                    bundle.putString("title", item.getName());
                    bundle.putInt("parentId", item.getId());
                    RxActivityTool.skipActivity(mContext, OrganizationalStructureActivity.class, bundle);
                }
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDepartmentName;
        TextView tvDepartmentNum;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDepartmentName = itemView.findViewById(R.id.tv_department_name);
            tvDepartmentNum = itemView.findViewById(R.id.tv_department_num);
        }
    }

}
