package com.shichuang.mobileworkingticket.adapter;

import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.entify.WorkingTicketDetails;
import com.shichuang.open.tool.RxTimeTool;

/**
 * Created by Administrator on 2018/3/23.
 */

public class PickingTheAuditWorkflowAdapter extends BaseQuickAdapter<WorkingTicketDetails.WorkFlowModels, BaseViewHolder> {
    public PickingTheAuditWorkflowAdapter() {
        super(R.layout.item_picking_the_audit_workflow);
    }

    @Override
    protected void convert(BaseViewHolder helper, WorkingTicketDetails.WorkFlowModels item) {
        helper.setText(R.id.tv_message, item.getFlowContent());
        helper.setText(R.id.tv_time, RxTimeTool.stringFormat(item.getAddTime()));
        Log.d("test",helper.getAdapterPosition()+"");

        if (getData().size() == 1) {
            helper.setGone(R.id.view_up, false);
            helper.setGone(R.id.view_down, false);
            helper.setBackgroundRes(R.id.view_status, R.drawable.bg_workflow_selected);
        } else if (helper.getAdapterPosition() == 0) {
            helper.setGone(R.id.view_up, false);
            helper.setGone(R.id.view_down, true);
            helper.setBackgroundRes(R.id.view_status, R.drawable.bg_workflow_unselected);
        } else if (helper.getAdapterPosition() == getData().size() - 1) {
            helper.setGone(R.id.view_up, true);
            helper.setGone(R.id.view_down, false);
            helper.setBackgroundRes(R.id.view_status, R.drawable.bg_workflow_selected);
        } else {
            helper.setGone(R.id.view_up, true);
            helper.setGone(R.id.view_down, true);
            helper.setBackgroundRes(R.id.view_status, R.drawable.bg_workflow_unselected);
        }
    }
}
