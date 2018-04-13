package com.shichuang.mobileworkingticket.event;

import com.shichuang.mobileworkingticket.entify.MessageCount;

import java.util.List;

/**
 * Created by Administrator on 2018/3/21.
 */

public class MessageCountEvent {
    public List<MessageCount.MessageCountRows> mList;

    public MessageCountEvent(List<MessageCount.MessageCountRows> list) {
        this.mList = list;
    }
}
