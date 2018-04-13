package com.shichuang.mobileworkingticket.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.common.Constants;
import com.shichuang.open.tool.RxGlideTool;
import com.shichuang.open.tool.RxScreenTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/23.
 */

public class PickingTheAuditDrawingAdapter extends RecyclerView.Adapter<PickingTheAuditDrawingAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mList = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_picking_the_audit_drawing, parent, false);
        // 修改尺寸，大小为，减去屏幕两边的间隔12dp，除以3张排版
        int width = (RxScreenTool.getDisplayMetrics(mContext).widthPixels) / 3;
        view.getLayoutParams().width = width;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(mList.get(position));
                }
            }
        });
        RxGlideTool.loadImageView(mContext, Constants.MAIN_ENGINE_PIC + mList.get(position), holder.mIvPartPic);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIvPartPic;

        public ViewHolder(View itemView) {
            super(itemView);
            mIvPartPic = itemView.findViewById(R.id.iv_part_pic);
        }
    }

    public void addData(List<String> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(String url);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
