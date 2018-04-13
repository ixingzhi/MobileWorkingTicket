package com.shichuang.mobileworkingticket.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.entify.WorkingTicketList;
import com.shichuang.open.tool.RxTimeTool;

/**
 * Created by Administrator on 2018/2/28.
 */

public class WorkingTicketListAdapter extends BaseQuickAdapter<WorkingTicketList.WorkingTicketListModel, BaseViewHolder> {

    public WorkingTicketListAdapter() {
        super(R.layout.item_working_ticket_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, WorkingTicketList.WorkingTicketListModel item) {
        helper.setText(R.id.tv_part_no, "零件号：" + item.getPartsDrawingNo());
        helper.setText(R.id.tv_work_order_no, "工作令号：" + item.getWorkOrderNo());
        helper.setText(R.id.tv_product_name, "产品名称：" + item.getProductDrawingNo());
        helper.setText(R.id.tv_component, "部件：" + item.getComponent());
        helper.setText(R.id.tv_time, RxTimeTool.stringFormat(item.getAddTime()));
        helper.setText(R.id.tv_publisher, "发布人：" + item.getReleaseUserName());

        String workTicketState = "";
        switch (item.getProcessState()) {   // 工票工序状态 1=待激活，2=领料确认 ，3=分配组员，4=生产作业,5=质量检查，6=已完成
            case 1:
                workTicketState = "待激活";
                break;
            case 2:
                workTicketState = "领料确认";
                break;
            case 3:
                workTicketState = "分配组员";
                break;
            case 4:
                workTicketState = "生产作业";
                break;
            case 5:
                workTicketState = "质量检查";
                break;
            case 6:
                workTicketState = "已完成";
                break;
        }
        helper.setText(R.id.tv_part_status, workTicketState);
    }
}
