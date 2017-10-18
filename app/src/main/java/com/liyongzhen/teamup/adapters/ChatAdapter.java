package com.liyongzhen.teamup.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.liyongzhen.teamup.R;
import com.liyongzhen.teamup.models.ChatModel;
import com.liyongzhen.teamup.utils.CommonUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 8/24/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.CustomViewHolder> {

    private Context mContext;
    private List<ChatModel> mList;
    private String currentUserId;
    private String friendUrl;

    public ChatAdapter(Context context, ArrayList<ChatModel> list) {
        mList = list;
        mContext = context;
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        friendUrl = "";
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_chat, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {
        ChatModel model = mList.get(position);

        holder.setIsRecyclable(false);

        RelativeLayout.LayoutParams contentparams = (RelativeLayout.LayoutParams)holder.chatContent.getLayoutParams();
        RelativeLayout.LayoutParams imageParam = (RelativeLayout.LayoutParams)holder.chatImage.getLayoutParams();
        RelativeLayout.LayoutParams timeParam = (RelativeLayout.LayoutParams)holder.chatTime.getLayoutParams();
        if (model.getFromId().equals(currentUserId)) {
            holder.cardView.setVisibility(View.GONE);
            if (!model.getText().isEmpty()) {
                contentparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                contentparams.setMargins(100, 0, 0, 0);
                holder.chatContent.setLayoutParams(contentparams);
                holder.chatContent.setBackgroundResource(R.drawable.rounded_corner_primary);
                holder.chatContent.setPadding(16, 25, 50, 25);
            } else {
                holder.chatContent.setVisibility(View.GONE);
            }
            if (!model.getImageUrl().isEmpty()) {
                contentparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                holder.chatImage.setLayoutParams(imageParam);
                holder.chatImage.setScaleType(ImageView.ScaleType.FIT_END);
            }
        } else {
            if (!model.getText().isEmpty()) {
                contentparams.addRule(RelativeLayout.RIGHT_OF, holder.cardView.getId());
                contentparams.setMargins(10, 0, 100, 0);
                holder.chatContent.setLayoutParams(contentparams);
                holder.chatContent.setPadding(50, 25, 16, 25);
            } else {
                holder.chatContent.setVisibility(View.GONE);
            }
            if (!model.getImageUrl().isEmpty()) {
                contentparams.addRule(RelativeLayout.RIGHT_OF, holder.cardView.getId());
                holder.chatImage.setLayoutParams(imageParam);
                holder.chatImage.setScaleType(ImageView.ScaleType.FIT_START);
            }

            timeParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.chatTime.setLayoutParams(timeParam);

            if (friendUrl.isEmpty())
                holder.chatIcon.setImageResource(R.mipmap.ic_launcher);
            else
                Picasso.with(mContext).load(friendUrl).into(holder.chatIcon);
        }
        holder.chatContent.setText(model.getText());
        holder.chatTime.setText(CommonUtil.getDifferenceTime(model.getTimestamp()));

        if(!model.getImageUrl().isEmpty()) {
            Picasso.with(mContext).load(model.getImageUrl()).into(holder.chatImage);
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageView chatIcon, chatImage;
        private TextView chatContent, chatTime;
        private CardView cardView;

        public CustomViewHolder(View view) {
            super(view);
            chatIcon = (ImageView) view.findViewById(R.id.chat_icon);
            chatContent = (TextView) view.findViewById(R.id.chat_content);
            chatImage = (ImageView) view.findViewById(R.id.chat_image);
            chatTime = (TextView) view.findViewById(R.id.chat_time);
            cardView = (CardView) view.findViewById(R.id.cv);
        }
    }

    public void setFriendUrl(String friendUrl) {
        this.friendUrl = friendUrl;
    }
}
