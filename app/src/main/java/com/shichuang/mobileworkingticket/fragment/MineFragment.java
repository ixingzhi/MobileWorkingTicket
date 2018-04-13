package com.shichuang.mobileworkingticket.fragment;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.lzy.okrx2.adapter.ObservableResponse;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.activity.AddressBookActivity;
import com.shichuang.mobileworkingticket.activity.HelpCenterActivity;
import com.shichuang.mobileworkingticket.activity.SettingActivity;
import com.shichuang.mobileworkingticket.common.Constants;
import com.shichuang.mobileworkingticket.common.Convert;
import com.shichuang.mobileworkingticket.common.NewsCallback;
import com.shichuang.mobileworkingticket.common.TokenCache;
import com.shichuang.mobileworkingticket.common.UserCache;
import com.shichuang.mobileworkingticket.entify.AMBaseDto;
import com.shichuang.mobileworkingticket.entify.Empty;
import com.shichuang.mobileworkingticket.entify.UploadFile;
import com.shichuang.mobileworkingticket.entify.User;
import com.shichuang.open.base.BaseFragment;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxGlideTool;
import com.shichuang.open.widget.RxEmptyLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2018/2/27.
 */

public class MineFragment extends BaseFragment {
    private ImageView mIvAvatar;
    private TextView mTvUserName;
    private TextView mTvPositionName;
    private TextView mTvCompanyName;
    private TextView mTvDepartmentName;

    private RxEmptyLayout mEmptyLayout;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mIvAvatar = view.findViewById(R.id.iv_avatar);
        mTvUserName = view.findViewById(R.id.tv_user_name);
        mTvPositionName = view.findViewById(R.id.tv_position_name);
        mTvCompanyName = view.findViewById(R.id.tv_company_name);
        mTvDepartmentName = view.findViewById(R.id.tv_department_name);

        mEmptyLayout = (RxEmptyLayout) view.findViewById(R.id.empty_layout);
        mEmptyLayout.setOnEmptyLayoutClickListener(new RxEmptyLayout.OnEmptyLayoutClickListener() {
            @Override
            public void onEmptyLayoutClick(int status) {
                if (status != RxEmptyLayout.NETWORK_LOADING) {
                    getUserInfoData();
                }
            }
        });
    }

    @Override
    public void initEvent() {
        mIvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(MineFragment.this)
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(1)
                        .compress(true)
                        .enableCrop(true)
                        .circleDimmedLayer(true)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
            }
        });
        mContentView.findViewById(R.id.ll_address_book).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxActivityTool.skipActivity(mContext, AddressBookActivity.class);
            }
        });
        mContentView.findViewById(R.id.ll_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxActivityTool.skipActivity(mContext, SettingActivity.class);
            }
        });
        mContentView.findViewById(R.id.ll_help_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxActivityTool.skipActivity(mContext, HelpCenterActivity.class);
            }
        });
    }


    @Override
    public void initData() {
        getUserInfoData();
    }

    private void getUserInfoData() {
        OkGo.<AMBaseDto<User>>get(Constants.getUserInfoUrl)
                .tag(mContext)
                .params("token", TokenCache.getToken(mContext))
                .execute(new NewsCallback<AMBaseDto<User>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<User>, ? extends Request> request) {
                        super.onStart(request);
                        mEmptyLayout.show(RxEmptyLayout.NETWORK_LOADING);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<User>> response) {
                        if (response.body().code == 0 && response.body().data != null && response.body().data.getUserRow() != null) {
                            UserCache.update(mContext, response.body().data.getUserRow());
                            setData(response.body().data.getUserRow());
                        } else {
                            //mEmptyLayout.show(RxEmptyLayout.NETWORK_ERROR);
                            showToast(response.body().msg);
                            if (UserCache.isUserLogin(mContext)) {
                                setData(UserCache.user(mContext));
                            } else {
                                mEmptyLayout.show(RxEmptyLayout.NETWORK_ERROR);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<User>> response) {
                        super.onError(response);
                        if (UserCache.isUserLogin(mContext)) {
                            setData(UserCache.user(mContext));
                        } else {
                            mEmptyLayout.show(RxEmptyLayout.NETWORK_ERROR);
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        mEmptyLayout.hide();
                    }
                });
    }

    private void setData(User.UserModel userRow) {
        RxGlideTool.loadImageView(mContext, Constants.MAIN_ENGINE_PIC + userRow.getHeadPortrait(), mIvAvatar, R.drawable.ic_avatar_default);
        mTvUserName.setText(userRow.getNickName());
        mTvPositionName.setText(userRow.getPositionName());
        mTvDepartmentName.setText(userRow.getDeptName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            if (selectList != null && selectList.size() > 0) {
                uploadFiles(selectList.get(0).getCutPath());
            }
        }
    }

    private void uploadFiles(final String cropImagePath) {
        ArrayList<File> files = new ArrayList<>();
        files.add(new File(cropImagePath));

        OkGo.<String>post(Constants.uploadFile)//
                .tag(this)//
                .addFileParams("file", files)
                .converter(new StringConvert())//
                .adapt(new ObservableResponse<String>())//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                    }
                })//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(new Observer<Response<String>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        showLoading();
                    }

                    @Override
                    public void onNext(@NonNull Response<String> response) {
                        UploadFile uploadFile = Convert.fromJson(response.body(), UploadFile.class);
                        if (uploadFile.getStatus() == 1) {
                            updateUserData(uploadFile.getPath(), cropImagePath);
                        } else {
                            showToast(uploadFile.getMsg());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        dismissLoading();
                        showToast("网络异常，稍后再试");
                    }

                    @Override
                    public void onComplete() {
                        dismissLoading();
                    }
                });
    }

    private void updateUserData(final String url, final String cropImagePath) {
        OkGo.<AMBaseDto<Empty>>post(Constants.replaceHeadPortraitUrl)
                //.cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)  //缓存模式先使用缓存,然后使用网络数据
                .params("token", TokenCache.getToken(mContext))
                .params("headPortraitUrl", url)
                .execute(new NewsCallback<AMBaseDto<Empty>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Empty>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(Response<AMBaseDto<Empty>> response) {
                        showToast(response.body().msg);
                        if (response.body().code == 0) {
                            mIvAvatar.setImageBitmap(BitmapFactory.decodeFile(cropImagePath));
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Empty>> response) {
                        super.onError(response);
                        showToast("网络错误");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissLoading();
                    }
                });
    }


}
