package com.shichuang.mobileworkingticket.entify;

import java.util.List;

/**
 * Created by Administrator on 2018/3/21.
 */

public class MessageCount {
    private List<MessageCountRows> messageCountRows;

    public List<MessageCountRows> getMessageCountRows() {
        return messageCountRows;
    }

    public void setMessageCountRows(List<MessageCountRows> messageCountRows) {
        this.messageCountRows = messageCountRows;
    }

    public static class MessageCountRows {
        private int count;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
