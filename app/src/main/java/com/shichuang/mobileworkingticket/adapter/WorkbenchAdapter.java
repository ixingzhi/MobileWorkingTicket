package com.shichuang.mobileworkingticket.adapter;


import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.entify.Workbench;

import java.util.List;

/**
 * Created by Administrator on 2018/2/27.
 */

public class WorkbenchAdapter extends BaseMultiItemQuickAdapter<Workbench, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public WorkbenchAdapter(List<Workbench> data) {
        super(data);
        addItemType(Workbench.HEADER, R.layout.item_workbench_header);
        addItemType(Workbench.BODY, R.layout.item_workbench_body);
    }

    @Override
    protected void convert(BaseViewHolder helper, Workbench item) {
        switch (helper.getItemViewType()) {
            case Workbench.HEADER:

                break;
            case Workbench.BODY:
                break;


            default:
                break;
        }
    }
}
