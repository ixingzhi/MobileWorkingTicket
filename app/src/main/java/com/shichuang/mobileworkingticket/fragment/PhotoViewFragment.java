package com.shichuang.mobileworkingticket.fragment;

import android.os.Bundle;
import android.view.View;

import com.github.chrisbanes.photoview.PhotoView;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.common.Constants;
import com.shichuang.open.base.BaseFragment;
import com.shichuang.open.tool.RxGlideTool;

/**
 * Created by Administrator on 2018/4/12.
 */

public class PhotoViewFragment extends BaseFragment {
    private PhotoView mPhotoView;
    private String url;

    public static PhotoViewFragment newInstance(String url) {
        PhotoViewFragment mPhotoViewFragment = new PhotoViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        mPhotoViewFragment.setArguments(bundle);
        return mPhotoViewFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_photo_view;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        url = getArguments().getString("url");
        mPhotoView = view.findViewById(R.id.photo_view);
    }

    @Override
    public void initEvent() {
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void initData() {
        loadPhoto(url);
    }

    public void loadPhoto(String url) {
        RxGlideTool.loadImageView(mContext, Constants.MAIN_ENGINE_PIC + url, mPhotoView);
    }


}
