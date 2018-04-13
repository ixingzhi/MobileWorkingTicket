package com.shichuang.mobileworkingticket.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.entify.Empty;
import com.shichuang.open.tool.RxDeviceTool;
import com.shichuang.open.tool.RxScreenTool;
import com.shichuang.open.tool.RxToastTool;

import java.util.List;
import java.util.logging.Handler;

/**
 * Created by Administrator on 2018/3/19.
 */

public class SelectionConditionsView extends PopupWindow {
    private Context mContext;
    private RecyclerView mRecyclerView;
    public SelectionConditionsAdapter mAdapter;

    public SelectionConditionsView(Context context) {
        this.mContext = context;
        // 设置可以获得焦点
        setFocusable(true);
        // 设置弹窗内可点击
        setTouchable(true);
        // 设置弹窗外可点击
        setOutsideTouchable(true);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new BitmapDrawable());

        setContentView(LayoutInflater.from(mContext).inflate(R.layout.pop_selection_conditions, null));

        initView();
    }

    private void initView() {
        mRecyclerView = getContentView().findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new SelectionConditionsAdapter();
        mAdapter.setPreLoadNumber(2);
        mRecyclerView.setAdapter(mAdapter);

        getContentView().findViewById(R.id.view_empty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public class SelectionConditionsAdapter extends BaseQuickAdapter<SelectionConditionsTabLayout.Tab.TabList, BaseViewHolder> {

        public SelectionConditionsAdapter() {
            super(R.layout.item_selection_conditions);
        }

        @Override
        protected void convert(BaseViewHolder helper, SelectionConditionsTabLayout.Tab.TabList item) {
            helper.setText(R.id.tv_text, item.getText());
            if (item.isSelect()) {
                helper.setTextColor(R.id.tv_text, mContext.getResources().getColor(R.color.colorPrimary));
            } else {
                helper.setTextColor(R.id.tv_text, mContext.getResources().getColor(R.color.textColor2));
            }
        }
    }

    public void addData(List<SelectionConditionsTabLayout.Tab.TabList> list) {
        mAdapter.replaceData(list);
    }

    public void show(final View view) {
        // 根据内容的高度，判断设置RecyclerView的高度,单个Item的高度 40dp
        int screenHeight = RxScreenTool.getDisplayMetrics(mContext).heightPixels;
        if (mAdapter.getData().size() * RxScreenTool.dip2px(mContext, 40) > screenHeight / 2) {
            mRecyclerView.getLayoutParams().height = screenHeight / 2;
        }
        showAsDropDown(view, 0, 5);
    }
}
