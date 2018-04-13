package com.shichuang.mobileworkingticket.adapter;

import android.view.View;
import android.widget.AdapterView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.entify.ChooseTeamMember;

/**
 * Created by Administrator on 2018/2/28.
 */

public class AssignTeamMembersAdapter extends BaseQuickAdapter<ChooseTeamMember.ChooseTeamMemberModel, BaseViewHolder> {
    public AssignTeamMembersAdapter() {
        super(R.layout.item_assign_team_members);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ChooseTeamMember.ChooseTeamMemberModel item) {
        helper.setText(R.id.tv_user_name, item.getNickName());
        helper.setImageResource(R.id.iv_is_select, item.isSelect() ? R.drawable.ic_date_selected : R.drawable.ic_date_unselected);
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setSelect(item.isSelect() ? false : true);
                notifyItemChanged(helper.getAdapterPosition());
            }
        });
    }
}
