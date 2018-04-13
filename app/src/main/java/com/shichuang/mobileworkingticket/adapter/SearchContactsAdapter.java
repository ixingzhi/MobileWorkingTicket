package com.shichuang.mobileworkingticket.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.common.Constants;
import com.shichuang.mobileworkingticket.entify.AddressBook;
import com.shichuang.open.tool.RxGlideTool;

import java.util.List;

/**
 * Created by Administrator on 2018/3/22.
 */

public class SearchContactsAdapter extends BaseQuickAdapter<AddressBook.AddressBookModel,BaseViewHolder> {

    public SearchContactsAdapter() {
        super(R.layout.item_address_book_user);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressBook.AddressBookModel item) {
        RxGlideTool.loadImageView(mContext, Constants.MAIN_ENGINE_PIC + item.getHeadPortrait(), (ImageView) helper.getView(R.id.iv_avatar), R.drawable.ic_avatar_default);
        helper.setText(R.id.tv_user_name, item.getNickName());
        helper.setText(R.id.tv_department_name, item.getDeptName());
    }
}
