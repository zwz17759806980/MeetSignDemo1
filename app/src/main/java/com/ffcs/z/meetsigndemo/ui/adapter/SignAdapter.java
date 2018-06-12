package com.ffcs.z.meetsigndemo.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ffcs.z.meetsigndemo.R;
import com.ffcs.z.meetsigndemo.bean.FaceWebInfoApi;
import com.ffcs.z.meetsigndemo.utils.UIUtils;
import com.ffcs.z.meetsigndemo.wight.CircleImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * ===========================================
 * 作者 ：曾立强 3042938728@qq.com
 * 时间 ：2018/5/24
 * 描述 ：
 * ============================================
 */

public class SignAdapter extends RecyclerView.Adapter<SignAdapter.ViewHolder> {
    List<FaceWebInfoApi> beans;

    public SignAdapter(List<FaceWebInfoApi> beans){
        this.beans=beans;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SignAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sign_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FaceWebInfoApi bean = beans.get(position);
        holder.name.setText(bean.getHitFacename());
        holder.signTime.setText(bean.getPassTime());
        Glide.with(UIUtils.getContext()).load(bean.getHitFaceImageUrl()).error(R.drawable.ic_avarter_blue).into(holder.avatarPick);
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_sign_name)
        TextView name;
        @Bind(R.id.item_sign_time)
        TextView signTime;
        @Bind(R.id.item_sign_avatar_pick)
        CircleImageView avatarPick;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
