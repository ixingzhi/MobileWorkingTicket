package com.shichuang.mobileworkingticket.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.entify.Empty;
import com.shichuang.mobileworkingticket.entify.SparePartsByParts;
import com.shichuang.mobileworkingticket.widget.HorizontalTextProgressBar;

/**
 * Created by Administrator on 2018/3/28.
 */

public class ReportDetailsAdapter extends BaseQuickAdapter<SparePartsByParts.DataRows.Rows, BaseViewHolder> {
    public ReportDetailsAdapter() {
        super(R.layout.item_report_details);
    }

    @Override
    protected void convert(BaseViewHolder helper, SparePartsByParts.DataRows.Rows item) {
        helper.setText(R.id.tv_work_ticket_num, item.getWorkTicketNum());
        helper.setText(R.id.tv_parts_drawing_no, item.getPartsDrawingNo());
        HorizontalTextProgressBar mProgressBar = helper.getView(R.id.progress_bar);
        // 比例
        int progress = (int) ((float) item.getFinishCount() * 100 / item.getAllCount());
        mProgressBar.setMax(100);
        mProgressBar.setProgress(progress);
        mProgressBar.setTextProcess(progress);
    }
}
