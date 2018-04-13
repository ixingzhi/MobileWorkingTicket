package com.shichuang.mobileworkingticket.adapter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.entify.Empty;
import com.shichuang.mobileworkingticket.entify.ReportProcessList;

/**
 * Created by Administrator on 2018/3/16.
 */

public class ReportProcessAdapter extends BaseQuickAdapter<ReportProcessList.ReportProcessModel, BaseViewHolder> {

    public ReportProcessAdapter() {
        super(R.layout.item_report_process);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReportProcessList.ReportProcessModel item) {
        helper.setText(R.id.tv_process_name, item.getProcessName());
        helper.setText(R.id.tv_sum_complete_count, "完成数：" + item.getSumCompleteCount());
        String unfinishedCount = "未完成数：" + item.getSumUnfinishedCount();
        SpannableString spannableString = new SpannableString(unfinishedCount);
        spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.red)), 5, unfinishedCount.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        helper.setText(R.id.tv_sum_unfinished_count, spannableString);
    }
}
