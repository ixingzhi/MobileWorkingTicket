package com.shichuang.mobileworkingticket.adapter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.entify.PartsByProductList;
import com.shichuang.mobileworkingticket.entify.ReportProcessList;
import com.shichuang.mobileworkingticket.widget.HorizontalTextProgressBar;

/**
 * Created by Administrator on 2018/3/16.
 */

public class StatisticsListAdapter extends BaseQuickAdapter<PartsByProductList.PartsByProductModel, BaseViewHolder> {

    public StatisticsListAdapter() {
        super(R.layout.item_statistics_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, PartsByProductList.PartsByProductModel item) {
        helper.setText(R.id.tv_work_order_no, "工作令号："+item.getWorkOrderNo());
        helper.setText(R.id.tv_component_no, "部件号：" + item.getComponent());
        helper.setText(R.id.tv_product_drawing_no, "产品图号：" + item.getProductDrawingNo());
        HorizontalTextProgressBar mProgressBar = helper.getView(R.id.progress_bar);
        // 比例
        int progress = (int) ((float) item.getFinishCount() * 100 / item.getAllCount());
        mProgressBar.setMax(100);
        mProgressBar.setProgress(progress);
        mProgressBar.setTextProcess(progress);
    }
}
