package com.ffcs.z.meetsigndemo.ui.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ffcs.z.meetsigndemo.utils.UIUtils;
import com.ffcs.z.meetsigndemo.utils.bar.Eyes;

import butterknife.ButterKnife;

/**
 * ===========================================
 * 作者 ：曾立强 3042938728@qq.com
 * 时间 ：2018/5/2
 * 描述 ：Activity基类
 * ============================================
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected ProgressDialog progress;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutID() != 0) {
            setContentView(getLayoutID());
        }
        initBind();
        initView();
        initData();

    }


    protected abstract int getLayoutID();

    protected abstract void initView();

    protected abstract void initData();

    protected void initBind() {
        ButterKnife.bind(this);
    }

    public void setStatusBarColor(int color) {
        Eyes.setStatusBarColor(this, UIUtils.getColor(color));
    }

    public void showDialog(String message) {
        if (progress == null) {
            progress = new ProgressDialog(this);
            progress.setMessage(message);
            progress.setCanceledOnTouchOutside(false);
        }
        progress.show();
    }

    public void hideDialog() {
        if (progress != null && progress.isShowing()) {
            progress.hide();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(progress!=null&&progress.isShowing()){
            progress.dismiss();
        }
    }
}
