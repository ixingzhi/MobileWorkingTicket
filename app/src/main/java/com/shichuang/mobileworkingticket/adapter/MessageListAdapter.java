package com.shichuang.mobileworkingticket.adapter;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.entify.Empty;
import com.shichuang.mobileworkingticket.entify.MessageList;
import com.shichuang.open.tool.RxDataTool;
import com.shichuang.open.tool.RxTimeTool;

/**
 * Created by Administrator on 2018/3/16.
 */

public class MessageListAdapter extends BaseQuickAdapter<MessageList.MessageListModel, BaseViewHolder> {

    public MessageListAdapter() {
        super(R.layout.item_message_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageList.MessageListModel item) {
        helper.setGone(R.id.view_unread, item.getIsRead() > 0 ? false : true);
        helper.setText(R.id.tv_time, RxTimeTool.stringFormat(item.getAddTime()));
        helper.setText(R.id.tv_title, item.getMessageTitle());
        helper.setText(R.id.tv_message_content, item.getMessageContent());

        if(item.isOpenMessageContent()){
            helper.setImageResource(R.id.iv_open_status,R.drawable.ic_arrows_up_gray);
            ((TextView)helper.getView(R.id.tv_message_content)).setMaxLines(Integer.MAX_VALUE);
        }else{
            helper.setImageResource(R.id.iv_open_status,R.drawable.ic_arrows_down_gray);
            ((TextView)helper.getView(R.id.tv_message_content)).setMaxLines(1);
        }
    }


}
