package com.liyongzhen.teamup.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.liyongzhen.teamup.activities.ChatRoomActivity;
import com.liyongzhen.teamup.R;
import com.liyongzhen.teamup.models.User;
import com.liyongzhen.teamup.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 8/24/2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.CustomViewHolder> {

    private Context mContext;
    private List<User> mList;

    public UserAdapter(Context context, ArrayList<User> list) {
        mList = list;
        mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_user, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {
        User model = mList.get(position);

        holder.setIsRecyclable(false);
        holder.userName.setText(model.getUsername());
        if (model.getPhotoUrl().isEmpty())
            holder.personPhoto.setImageResource(R.mipmap.ic_launcher);
        else
            Picasso.with(mContext).load(model.getPhotoUrl()).into(holder.personPhoto);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageView personPhoto;
        private TextView userName;
        private ImageButton userBlock;

        public CustomViewHolder(View view) {
            super(view);
            personPhoto = (ImageView) view.findViewById(R.id.person_photo);
            userName = (TextView) view.findViewById(R.id.userName);
            userBlock = (ImageButton) view.findViewById(R.id.user_block);
            userBlock.setOnClickListener(mOnClickListener);
            view.setOnClickListener(mOnClickListener);
        }

        private View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.user_block) {
                    PopupMenu popup = new PopupMenu(mContext, userBlock);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.block_menu, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            String cUId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            User model = mList.get(getAdapterPosition());
                            FirebaseDatabase.getInstance().getReference("blocks").child(cUId).child(model.getuId())
                                    .setValue(1);
                            mList.remove(getAdapterPosition());
                            notifyDataSetChanged();
                            return true;
                        }
                    });

                    popup.show();//showing popup menu
                } else {
                    Intent i = new Intent(mContext, ChatRoomActivity.class);
                    i.putExtra(Constants.EXTRA_USERID, mList.get(getAdapterPosition()).getuId());
                    mContext.startActivity(i);
                }
            }
        };
    }
}
