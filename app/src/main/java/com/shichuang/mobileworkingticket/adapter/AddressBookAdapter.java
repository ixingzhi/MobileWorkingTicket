package com.shichuang.mobileworkingticket.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.common.Constants;
import com.shichuang.mobileworkingticket.entify.AddressBook;
import com.shichuang.open.tool.RxGlideTool;

import java.util.List;

/**
 * Created by Administrator on 2018/3/19.
 */

public class AddressBookAdapter extends BaseMultiItemQuickAdapter<AddressBook.AddressBookModel, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public AddressBookAdapter(List<AddressBook.AddressBookModel> data) {
        super(data);
        addItemType(AddressBook.AddressBookModel.LETTER, R.layout.item_address_book_letter);
        addItemType(AddressBook.AddressBookModel.USER, R.layout.item_address_book_user);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressBook.AddressBookModel item) {
        switch (helper.getItemViewType()) {
            case AddressBook.AddressBookModel.LETTER:
                helper.setText(R.id.tv_index, item.getIndex());
                break;
            case AddressBook.AddressBookModel.USER:
                RxGlideTool.loadImageView(mContext, Constants.MAIN_ENGINE_PIC + item.getHeadPortrait(), (ImageView) helper.getView(R.id.iv_avatar), R.drawable.ic_avatar_default);
                helper.setText(R.id.tv_user_name, item.getNickName());
                helper.setText(R.id.tv_department_name, item.getDeptName());
                break;
        }
    }
}
