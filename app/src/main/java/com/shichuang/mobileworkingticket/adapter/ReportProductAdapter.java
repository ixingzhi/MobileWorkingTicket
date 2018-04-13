package com.shichuang.mobileworkingticket.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.entify.ReportProductList;
import com.shichuang.mobileworkingticket.widget.HorizontalTextProgressBar;

/**
 * Created by Administrator on 2018/3/16.
 */

public class ReportProductAdapter extends BaseQuickAdapter<ReportProductList.ReportProductModel, BaseViewHolder> {

    public ReportProductAdapter() {
        super(R.layout.item_report_product);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReportProductList.ReportProductModel item) {
        helper.setText(R.id.tv_work_order_no, "工作令号：" + item.getWorkOrderNo());
        helper.setText(R.id.tv_product_drawing_no, "产品图号：" + item.getProductDrawingNo());
        HorizontalTextProgressBar mProgressBar = helper.getView(R.id.progress_bar);
        // 比例
        int progress = (int) ((float) item.getFinishCount() * 100 / item.getAllCount());
        mProgressBar.setMax(100);
        mProgressBar.setProgress(progress);
        mProgressBar.setTextProcess(progress);
    }
}
