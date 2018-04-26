package com.shichuang.mobileworkingticket.widget;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.open.tool.RxAppTool;
import com.shichuang.open.tool.RxToastTool;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/3/19.
 */

public class DateSelectView extends PopupWindow {
    private Context mContext;
    private View view;
    private TextView mTvDateToday;
    private ImageView mIvDateToday;
    private TextView mTvDateWeek;
    private ImageView mIvDateWeek;
    private TextView mTvDateMonth;
    private ImageView mIvDateMonth;
    private TextView mTvStartDate;
    private TextView mTvEndDate;

    public DateSelectView(Context context) {
        this.mContext = context;
        // 设置可以获得焦点
        setFocusable(true);
        // 设置弹窗内可点击
        setTouchable(true);
        // 设置弹窗外可点击
        setOutsideTouchable(true);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        view = LayoutInflater.from(mContext).inflate(R.layout.pop_date_select, null);
        setContentView(view);
        initView();

        getTodayDate();
    }


    private void initView() {
        mTvDateToday = view.findViewById(R.id.tv_date_today);
        mIvDateToday = view.findViewById(R.id.iv_date_today);
        mTvDateWeek = view.findViewById(R.id.tv_date_week);
        mIvDateWeek = view.findViewById(R.id.iv_date_week);
        mTvDateMonth = view.findViewById(R.id.tv_date_month);
        mIvDateMonth = view.findViewById(R.id.iv_date_month);

        mTvStartDate = view.findViewById(R.id.tv_start_date);
        mTvEndDate = view.findViewById(R.id.tv_end_date);


        view.findViewById(R.id.view_empty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        view.findViewById(R.id.rl_date_today).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTodayDate();
            }
        });
        view.findViewById(R.id.rl_date_week).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeekDate();
            }
        });
        view.findViewById(R.id.rl_date_month).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMonthDate();
            }
        });
        view.findViewById(R.id.ll_select_start_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectStartOrEndDate(0);
            }
        });
        view.findViewById(R.id.ll_select_end_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectStartOrEndDate(1);
            }
        });
        view.findViewById(R.id.ll_query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInfo();
            }
        });
    }

    private String getTodayDate() {
        switchDate(0);
        String dateStr = todayDate();
        mTvStartDate.setText(dateStr);
        mTvEndDate.setText(dateStr);
        return dateStr;
    }

    private void getWeekDate() {
        switchDate(1);
        Calendar todayCal = Calendar.getInstance();
        todayCal.setTime(new Date());
        todayCal.add(Calendar.DATE, -7);
        int year = todayCal.get(Calendar.YEAR);
        int month = todayCal.get(Calendar.MONTH);
        int day = todayCal.get(Calendar.DAY_OF_MONTH);
        String dateStr = processingDate(year, month, day);
        mTvStartDate.setText(dateStr);
        mTvEndDate.setText(todayDate());
    }

    private void getMonthDate() {
        switchDate(2);
        Calendar todayCal = Calendar.getInstance();
        todayCal.setTime(new Date());
        todayCal.add(Calendar.MONTH, -1);
        int year = todayCal.get(Calendar.YEAR);
        int month = todayCal.get(Calendar.MONTH);
        int day = todayCal.get(Calendar.DAY_OF_MONTH);
        String dateStr = processingDate(year, month, day);
        mTvStartDate.setText(dateStr);
        mTvEndDate.setText(todayDate());
    }

    private String todayDate() {
        Calendar todayCal = Calendar.getInstance();
        int year = todayCal.get(Calendar.YEAR);
        int month = todayCal.get(Calendar.MONTH);
        int day = todayCal.get(Calendar.DAY_OF_MONTH);
        String dateStr = processingDate(year, month, day);
        return dateStr;
    }

    private void selectStartOrEndDate(final int type) {
        Calendar todayCal = Calendar.getInstance();
        int year = todayCal.get(Calendar.YEAR);
        int month = todayCal.get(Calendar.MONTH) + 1;
        final int day = todayCal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dateStr = processingDate(year, month, dayOfMonth);
                if (type == 0) {
                    mTvStartDate.setText(dateStr);
                } else {
                    mTvEndDate.setText(dateStr);
                }
            }
        }, year, month, day);
        mDialog.getDatePicker().setMaxDate(todayCal.getTimeInMillis());
        mDialog.show();
    }

    private void switchDate(int i) {
        mTvDateToday.setSelected(false);
        mIvDateToday.setSelected(false);
        mTvDateWeek.setSelected(false);
        mIvDateWeek.setSelected(false);
        mTvDateMonth.setSelected(false);
        mIvDateMonth.setSelected(false);

        switch (i) {
            case 0:
                mTvDateToday.setSelected(true);
                mIvDateToday.setSelected(true);
                break;
            case 1:
                mTvDateWeek.setSelected(true);
                mIvDateWeek.setSelected(true);
                break;
            case 2:
                mTvDateMonth.setSelected(true);
                mIvDateMonth.setSelected(true);
                break;
        }
    }

    private String processingDate(int year, int month, int day) {
        String monthStr = "";
        if ((month + 1) < 10) {
            monthStr = "0" + (month + 1);
        } else {
            monthStr = month + "";
        }
        String dayStr = "";
        if (day < 10) {
            dayStr = "0" + day;
        } else {
            dayStr = day + "";
        }
        return year + "-" + monthStr + "-" + dayStr;
    }


    private void checkInfo() {
        String startDate = mTvStartDate.getText().toString().trim();
        String endDate = mTvEndDate.getText().toString().trim();
        if (("开始时间").equals(startDate) || TextUtils.isEmpty(startDate)) {
            RxToastTool.showShort("请选择开始时间");
        } else if (("结束时间").equals(endDate) || TextUtils.isEmpty(endDate)) {
            RxToastTool.showShort("请选择结束时间");
        } else {
            dismiss();
            if (mOnDateSetListener != null) {
                mOnDateSetListener.onDateSet(startDate, endDate);
            }
        }
    }


    public void show(View view) {
        showAsDropDown(view, 0, 5);
    }

    private OnDateSetListener mOnDateSetListener;

    public interface OnDateSetListener {

        void onDateSet(String startDate, String endDate);
    }

    public void setOnDateSetListener(OnDateSetListener onDateSetListener) {
        this.mOnDateSetListener = onDateSetListener;
    }
}
