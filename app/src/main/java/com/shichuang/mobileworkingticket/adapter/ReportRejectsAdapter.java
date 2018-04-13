package com.shichuang.mobileworkingticket.adapter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.entify.Empty;
import com.shichuang.mobileworkingticket.entify.ReportRejectsList;

/**
 * Created by Administrator on 2018/3/16.
 */

public class ReportRejectsAdapter extends BaseQuickAdapter<ReportRejectsList.ReportRejectsModel, BaseViewHolder> {

    public ReportRejectsAdapter() {
        super(R.layout.item_report_rejects);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReportRejectsList.ReportRejectsModel item) {
        helper.setText(R.id.tv_scrap_reason, "报废原因：" + item.getCauseContent());
        String scrapCount = "数量：" + item.getScrapCount();
        SpannableString spannableString = new SpannableString(scrapCount);
        spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.red)), 3, scrapCount.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        helper.setText(R.id.tv_scrap_count, spannableString);
    }
}
