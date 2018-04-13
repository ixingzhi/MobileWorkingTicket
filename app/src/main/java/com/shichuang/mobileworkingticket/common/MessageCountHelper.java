package com.shichuang.mobileworkingticket.common;

import android.content.Context;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.mobileworkingticket.entify.AMBaseDto;
import com.shichuang.mobileworkingticket.entify.MessageCount;
import com.shichuang.mobileworkingticket.event.MessageCountEvent;
import com.shichuang.open.tool.RxToastTool;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2018/3/21.
 */

public class MessageCountHelper {
    private static MessageCountHelper mInstance;

    public static MessageCountHelper getInstance() {
        if (mInstance == null) {
            mInstance = new MessageCountHelper();
        }
        return mInstance;
    }

    /**
     * 更新消息数量
     */
    public void update(Context context) {
        OkGo.<AMBaseDto<MessageCount>>get(Constants.messageCountUrl)
                .tag(context)
                .params("token", TokenCache.getToken(context))
                .execute(new NewsCallback<AMBaseDto<MessageCount>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<MessageCount>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<MessageCount>> response) {
                        if (response.body().code == 0) {
                            if (response.body().data != null && response.body().data.getMessageCountRows() != null) {
                                EventBus.getDefault().post(new MessageCountEvent(response.body().data.getMessageCountRows()));
                            }
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<MessageCount>> response) {
                        super.onError(response);
                        RxToastTool.showShort("数据异常：" + response.getException().getMessage());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }
}
