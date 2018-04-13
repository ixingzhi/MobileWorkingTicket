package com.shichuang.mobileworkingticket.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.open.tool.RxScreenTool;
import com.shichuang.open.tool.RxToastTool;

import java.util.List;

/**
 * 条件选择TabLayout
 * Created by Administrator on 2018/3/19.
 */

public class SelectionConditionsTabLayout extends LinearLayout {
    private int mTabCount;
    private int mWidth, mHeight;
    private TabView mOldTabView;

    public SelectionConditionsTabLayout(Context context) {
        super(context);
        init();
    }

    public SelectionConditionsTabLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SelectionConditionsTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setOrientation(HORIZONTAL);
        mWidth = RxScreenTool.getDisplayMetrics(getContext()).widthPixels;
        mHeight = RxScreenTool.dip2px(getContext(), 48);
    }

    public void addTabs(List<Tab> tabs) {
        mTabCount = tabs == null ? 0 : tabs.size();
        for (final Tab model : tabs) {
            int tabWidth = mWidth / mTabCount;
            LayoutParams params = new LayoutParams(tabWidth, mHeight);
            TabView mTabView = createTabView(model);
            mTabView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    final TabView mCurrentView = (TabView) v;
                    if (mCurrentView == mOldTabView) {
                        mCurrentView.setSelected(true);
                    } else {
                        mCurrentView.setSelected(true);
                        if (mOldTabView != null) {
                            mOldTabView.setSelected(false);
                        }
                        mOldTabView = mCurrentView;
                    }

                    final SelectionConditionsView pop = new SelectionConditionsView(getContext());
                    if (mCurrentView.getTab().getTabList() != null) {
                        pop.addData(mCurrentView.getTab().getTabList());
                        pop.show(v);
                    } else {
                        RxToastTool.showShort("条件数据为空");
                    }

                    if (pop.mAdapter != null) {
                        pop.mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                mCurrentView.getTab().setText(pop.mAdapter.getData().get(position).getText());
                                mCurrentView.update();
                                for (int i = 0; i < mCurrentView.getTab().getTabList().size(); i++) {
                                    if (i == position) {
                                        mCurrentView.getTab().getTabList().get(i).setSelect(true);
                                    } else {
                                        mCurrentView.getTab().getTabList().get(i).setSelect(false);
                                    }
                                }
                                pop.dismiss();
                                if (onTabItemSelectedListener != null) {
                                    onTabItemSelectedListener.onTabItemSelected(mCurrentView.getTab().getTabType(),
                                            pop.mAdapter.getData().get(position).getId());
                                }
                            }
                        });
                    }
                }
            });
            addView(mTabView, params);
        }
    }

    public Tab newTab() {
        Tab mTab = new Tab();
        return mTab;
    }

    public TabView getTabView(int i) {
        TabView tabView = (TabView) getChildAt(i);
        return tabView;
    }

    private TabView createTabView(Tab tab) {
        TabView mTabView = new TabView(getContext());
        mTabView.setTab(tab);
        if (tab != null && tab.getTabList() != null) {
            for (Tab.TabList model : tab.getTabList()) {
                if (model.isSelect()) {
                    mTabView.getTab().setText(model.getText());
                    mTabView.update();
                }
            }
        }
        return mTabView;
    }

    public class TabView extends LinearLayout {
        private Tab mTab;
        private TextView mTextView;
        private ImageView mIconView;

        public TabView(Context context) {
            super(context);
            setOrientation(HORIZONTAL);
            setGravity(Gravity.CENTER);

            LayoutInflater.from(getContext()).inflate(R.layout.layout_selection_conditions_tab_view, this);
            mTextView = findViewById(R.id.tv_text);
            mIconView = findViewById(R.id.iv_icon);
        }

        public void setTab(Tab tab) {
            this.mTab = tab;
            update();
        }

        public Tab getTab() {
            return mTab;
        }

        @Override
        public void setSelected(boolean selected) {
            super.setSelected(selected);
            mTextView.setSelected(selected);
            mIconView.setSelected(selected);
        }

        private void update() {
            String text = mTab.getText() != null ? mTab.getText() : "";
            if (mTab != null) {
                if (mTextView != null) {
                    mTextView.setText(text);
                }
            }
        }
    }

    public static final class Tab {
        private Object mTag;
        private int mTabType;
        private String mText;
        private List<TabList> mTabList;

        public Object getTag() {
            return mTag;
        }

        public String getText() {
            return mText;
        }

        public Tab setTag(Object tag) {
            this.mTag = tag;
            return this;
        }

        public int getTabType() {
            return mTabType;
        }

        public Tab setTabType(int tabType) {
            this.mTabType = tabType;
            return this;
        }

        public Tab setText(String text) {
            this.mText = text;
            return this;
        }

        public List<TabList> getTabList() {
            return mTabList;
        }

        public Tab setTabList(List<TabList> tabList) {
            this.mTabList = tabList;
            return this;
        }

        public static class TabList {
            private int mId;
            private String mText;
            private boolean isSelect;

            public TabList(int id, String text) {
                this.mId = id;
                this.mText = text;
            }

            public TabList(int id, String text, boolean isSelect) {
                this.mId = id;
                this.mText = text;
                this.isSelect = isSelect;
            }

            public int getId() {
                return mId;
            }

            public void setId(int id) {
                this.mId = id;
            }

            public String getText() {
                return mText;
            }

            public void setText(String text) {
                this.mText = text;
            }

            public boolean isSelect() {
                return isSelect;
            }

            public void setSelect(boolean select) {
                isSelect = select;
            }
        }
    }

    private OnTabItemSelectedListener onTabItemSelectedListener;

    public interface OnTabItemSelectedListener {
        void onTabItemSelected(int tabType, int id);
    }

    public void addOnTabItemSelectedListener(OnTabItemSelectedListener onTabItemSelectedListener) {
        this.onTabItemSelectedListener = onTabItemSelectedListener;
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

}
