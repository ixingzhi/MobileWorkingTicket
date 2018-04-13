package com.shichuang.mobileworkingticket.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.common.Constants;
import com.shichuang.mobileworkingticket.entify.WorkingTicketDetails;
import com.shichuang.open.tool.RxGlideTool;

/**
 * Created by Administrator on 2018/3/23.
 */

public class PickingTheAuditProjectTeamAdapter extends BaseQuickAdapter<WorkingTicketDetails.MemberRosModel, BaseViewHolder> {
    public PickingTheAuditProjectTeamAdapter() {
        super(R.layout.item_picking_the_audit_project_name);
    }

    @Override
    protected void convert(BaseViewHolder helper, WorkingTicketDetails.MemberRosModel item) {
        RxGlideTool.loadImageView(mContext, Constants.MAIN_ENGINE_PIC + item.getHeadPortrait(), (ImageView) helper.getView(R.id.iv_avatar), R.drawable.ic_avatar_default);
        helper.setText(R.id.tv_user_name, item.getNickName());
        helper.setText(R.id.tv_department_name, item.getDeptName());
        helper.addOnClickListener(R.id.iv_make_phone_call);

        String workType = "";
        switch (item.getUserTypeOfWork()) {   // 计划员=1，调度员=2，发料员=3，工序组长=4，检验员=5，工序组员=6，发运员=7
            case 1:
                workType = "计划员";
                break;
            case 2:
                workType = "调度员";
                break;
            case 3:
                workType = "发料员";
                break;
            case 4:
                workType = "工序组长";
                break;
            case 5:
                workType = "检验员";
                break;
            case 6:
                workType = "工序组员";
                break;
            case 7:
                workType = "发运员";
                break;

        }
        helper.setText(R.id.tv_work_type, workType);
    }
}
