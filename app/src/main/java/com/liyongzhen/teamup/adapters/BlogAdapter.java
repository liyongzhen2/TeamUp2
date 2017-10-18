package com.liyongzhen.teamup.adapters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liyongzhen.teamup.R;
import com.liyongzhen.teamup.models.BlogModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.liyongzhen.teamup.models.User;
import com.liyongzhen.teamup.utils.CommonUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 8/24/2017.
 */

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.CustomViewHolder> {

    private Context mContext;
    private List<BlogModel> mList;
    private String blogType;

    public BlogAdapter(Context context, ArrayList<BlogModel> list, String blogType) {
        mList = list;
        mContext = context;
        this.blogType = blogType;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(com.liyongzhen.teamup.R.layout.cell_blog, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {
        final BlogModel model = mList.get(position);

        holder.setIsRecyclable(false);

//        if (model.getIsBlock().equals("true")) {
//            holder.hideView();
//        } else
            {

            Query query = FirebaseDatabase.getInstance().getReference().child("users").child(model.getFromId());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if(user != null) {
                        holder.userName.setText(user.getUsername());
                        if (!user.getPhotoUrl().isEmpty()) {
                            Picasso.with(mContext).load(user.getPhotoUrl())
                                    .placeholder(R.drawable.photo_default)
                                    .error(R.drawable.photo_default)
                                    .into(holder.personPhoto);
                        }
                    }
                    else
                        holder.personPhoto.setImageResource(R.drawable.photo_default_circle);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            holder.blogContent.setText(model.getText());
            Linkify.addLinks(holder.blogContent, Linkify.WEB_URLS);
            if (!model.getImageUrl().isEmpty()) {
                Picasso.with(mContext).load(model.getImageUrl())
                        .into(holder.blogImage);
            }

            holder.blogTime.setText(CommonUtil.getDifferenceTime(model.getTimestamp()));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout layout;
        final LinearLayout.LayoutParams params;
        private ImageView personPhoto, blogImage;
        private TextView blogContent, userName, blogTime;
        private ImageButton blogBlock;

        public CustomViewHolder(View view) {
            super(view);
            layout =(LinearLayout)view.findViewById(com.liyongzhen.teamup.R.id.ly_blog_cell);
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            personPhoto = (ImageView) view.findViewById(com.liyongzhen.teamup.R.id.person_photo);
            blogContent = (TextView) view.findViewById(com.liyongzhen.teamup.R.id.blogContent);
            blogImage = (ImageView) view.findViewById(com.liyongzhen.teamup.R.id.blogImage);
            userName = (TextView) view.findViewById(com.liyongzhen.teamup.R.id.blogUserName);
            blogTime = (TextView) view.findViewById(com.liyongzhen.teamup.R.id.blogTime);
            blogBlock = (ImageButton) view.findViewById(com.liyongzhen.teamup.R.id.blog_block);
            blogBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PopupMenu popup = new PopupMenu(mContext, blogBlock);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(com.liyongzhen.teamup.R.menu.block_menu, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
//                            BlogModel model = mList.get(getAdapterPosition());
//                            FirebaseDatabase.getInstance().getReference("posts").child(blogType).child(model.getPostId()).child("isBlock")
//                                    .setValue("true");
//                            mList.remove(getAdapterPosition());
//                            notifyDataSetChanged();
                            return true;
                        }
                    });

                    popup.show();//showing popup menu
                }
            });
        }

        public void hideView() {
            params.height = 0;
            layout.setLayoutParams(params);
        }
    }
}
