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

public class AssignTeamMembersDialog extends BaseDialog implements View.OnClickListener {
    private View view;
    private EditText mEtBrandNo;
    private EditText mEtSpecifications;
    private TagFlowLayout mFlowLayout;

    private int selectId = -1;

    public AssignTeamMembersDialog(Context context) {
        super(context);
        view = LayoutInflater.from(mContext).inflate(R.layout.dialog_assign_team_members, null);
        setContentView(view);
        initView();
    }

    private void initView() {
        mEtBrandNo = view.findViewById(R.id.et_brand_no);
        mEtSpecifications = view.findViewById(R.id.et_specifications);
        mFlowLayout = view.findViewById(R.id.flow_layout);
        findViewById(R.id.btn_complete).setOnClickListener(this);
    }

    public void setProcessList(final List<SelectionConditionsTabLayout.Tab.TabList> data) {
        if (data == null) {
            return;
        }
        final List<String> processList = new ArrayList<>();
        for (SelectionConditionsTabLayout.Tab.TabList model : data) {
            processList.add(model.getText());
        }
        mFlowLayout.setMaxSelectCount(1);
        mFlowLayout.setAdapter(new TagAdapter<String>(processList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_current_process_name,
                        mFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        });
        mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                for (int s : selectPosSet) {
                    if (data != null && data.size() >= s + 1) {
                        selectId = data.get(s).getId();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_complete) {
            checkInfo();
        }
    }

    private void checkInfo() {
        String brandNo = mEtBrandNo.getText().toString().trim();
        String specifications = mEtSpecifications.getText().toString().trim();
//        if (TextUtils.isEmpty(brandNo) && TextUtils.isEmpty(specifications)) {
//            RxToastTool.showShort("请输入胚料牌号或规格");
//        } else
        if (selectId == -1) {
            RxToastTool.showShort("请选择当前工序");
        } else {
            if (onScreeningComplete != null) {
                dismiss();
                onScreeningComplete.complete(brandNo, specifications, selectId);
            }
        }
    }

    private OnScreeningComplete onScreeningComplete;

    public interface OnScreeningComplete {
        void complete(String brandNo, String specifications, int nowProcessId);
    }

    public void setOnScreeningComplete(OnScreeningComplete inter) {
        this.onScreeningComplete = inter;
    }

}
