package com.shichuang.mobileworkingticket.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.activity.PersonalDetailsActivity;
import com.shichuang.mobileworkingticket.entify.DepartmentList;
import com.shichuang.open.tool.RxActivityTool;

import me.drakeet.multitype.ItemViewBinder;


/**
 * Created by Administrator on 2017/9/30.
 */

public class DepartmentMembersItemViewBinder extends ItemViewBinder<DepartmentList.DepartmentUser, DepartmentMembersItemViewBinder.ViewHolder> {
    private Context mContext;

    public DepartmentMembersItemViewBinder() {
    }

    @NonNull
    @Override
    protected DepartmentMembersItemViewBinder.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        mContext = parent.getContext();
        View view = inflater.inflate(R.layout.item_department_members, parent, false);
        return new DepartmentMembersItemViewBinder.ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final DepartmentList.DepartmentUser item) {
        holder.tvUserName.setText(item.getNickName());
        holder.tvPosition.setText(item.getPositionName());
//        // 进入个人详情页面
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("userId", item.getId());
                RxActivityTool.skipActivity(mContext, PersonalDetailsActivity.class, bundle);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName;
        TextView tvPosition;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvPosition = itemView.findViewById(R.id.tv_position);
        }
    }
}
