package com.shichuang.mobileworkingticket.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shichuang.mobileworkingticket.R;
import com.shichuang.open.tool.RxToastTool;
import com.shichuang.open.widget.BaseDialog;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2018/5/3.
 */

public class WorkingTicketAddNotesDialog extends BaseDialog implements View.OnClickListener {
    private View view;
    private EditText mEtRemarks;

    public WorkingTicketAddNotesDialog(Context context) {
        super(context);
        view = LayoutInflater.from(mContext).inflate(R.layout.dialog_working_ticket_add_notes, null);
        setContentView(view);
        initView();
    }

    private void initView() {
        mEtRemarks = view.findViewById(R.id.et_remarks);
        findViewById(R.id.btn_complete).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_complete) {
            checkInfo();
        }
    }

    private void checkInfo() {
        String remarks = mEtRemarks.getText().toString().trim();
        if (TextUtils.isEmpty(remarks)) {
            RxToastTool.showShort("请输入备注信息");
        } else {
            if (OnClickListener != null) {
                dismiss();
                OnClickListener.complete(remarks);
            }
        }
    }

    private OnCompleteClickListener OnClickListener;

    public interface OnCompleteClickListener {
        void complete(String remarks);
    }

    public void setOnCompleteClickListener(OnCompleteClickListener inter) {
        this.OnClickListener = inter;
    }

}
