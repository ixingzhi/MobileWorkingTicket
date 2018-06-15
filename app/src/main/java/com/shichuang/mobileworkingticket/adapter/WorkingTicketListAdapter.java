package com.shichuang.mobileworkingticket.adapter;

import android.graphics.Color;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.entify.WorkingTicketList;
import com.shichuang.open.tool.RxTimeTool;

/**
 * Created by Administrator on 2018/2/28.
 */

public class WorkingTicketListAdapter extends BaseQuickAdapter<WorkingTicketList.WorkingTicketListModel, BaseViewHolder> {
    // 是否组员分配
    private boolean isTeamMembersAssignedSelect;

    public WorkingTicketListAdapter() {
        super(R.layout.item_working_ticket_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, WorkingTicketList.WorkingTicketListModel item) {
        if (isTeamMembersAssignedSelect) {
            helper.setGone(R.id.iv_is_select, true);
            helper.addOnClickListener(R.id.iv_is_select);
            helper.getView(R.id.iv_is_select).setSelected(item.isSelect() ? true : false);
        }
        helper.setText(R.id.tv_part_no, "零件号：" + item.getPartsDrawingNo());
        helper.setText(R.id.tv_work_order_no, "工作令号：" + item.getWorkOrderNo());
        helper.setText(R.id.tv_product_name, "产品名称：" + item.getProductDrawingNo());
        helper.setText(R.id.tv_component, "部件：" + item.getComponent());
        helper.setText(R.id.tv_brand_no, "胚料牌号：" + item.getBrandNo());
        helper.setText(R.id.tv_overall_dimensions, "落料尺寸：" + item.getOverallDimensions());
        helper.setText(R.id.tv_specifications, "规格：" + item.getSpecifications());
        helper.setText(R.id.tv_time, RxTimeTool.stringFormat(item.getAddTime()));
        if (TextUtils.isEmpty(item.getProcessName())) {
            helper.setGone(R.id.tv_current_progress_name, false);
        }else{
            helper.setGone(R.id.tv_current_progress_name, true);
            helper.setText(R.id.tv_current_progress_name, "当前工序：" + item.getProcessName());
        }
        String workTicketState = "";
        switch (item.getProcessState()) {   // 工票工序状态 1=待激活，2=领料确认 ，3=分配组员，4=生产作业,5=质量检查，6=已完成
            case 1:
                workTicketState = "待激活";
                helper.setTextColor(R.id.tv_part_status, mContext.getResources().getColor(R.color.red));
                break;
            case 2:
                workTicketState = "领料确认";
                helper.setTextColor(R.id.tv_part_status, mContext.getResources().getColor(R.color.red));
                break;
            case 3:
                workTicketState = "分配组员";
                helper.setTextColor(R.id.tv_part_status, mContext.getResources().getColor(R.color.red));
                break;
            case 4:
                workTicketState = "生产作业";
                if (item.getProcessMemberStatus() == 2) {  // 已开始，设置字体颜色（#0000C6），其他为红色
                    helper.setTextColor(R.id.tv_part_status, Color.parseColor("#0000C6"));
                } else {
                    helper.setTextColor(R.id.tv_part_status, mContext.getResources().getColor(R.color.red));
                }
                break;
            case 5:
                workTicketState = "质量检查";
                helper.setTextColor(R.id.tv_part_status, mContext.getResources().getColor(R.color.red));
                break;
            case 6:
                workTicketState = "已完成";
                helper.setTextColor(R.id.tv_part_status, mContext.getResources().getColor(R.color.red));
                break;
        }
        helper.setText(R.id.tv_part_status, workTicketState);
    }

    public void setTeamMembersAssignedSelectStatus(boolean bool) {
        isTeamMembersAssignedSelect = bool;
    }
}
