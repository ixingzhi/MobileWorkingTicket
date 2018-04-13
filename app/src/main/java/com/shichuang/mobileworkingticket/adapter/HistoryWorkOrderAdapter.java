package com.shichuang.mobileworkingticket.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.entify.Empty;

/**
 * Created by Administrator on 2018/2/28.
 */

public class HistoryWorkOrderAdapter extends BaseQuickAdapter<Empty,BaseViewHolder> {
    public HistoryWorkOrderAdapter() {
        super(R.layout.item_history_work_order);
    }

    @Override
    protected void convert(BaseViewHolder helper, Empty item) {

    }
}
