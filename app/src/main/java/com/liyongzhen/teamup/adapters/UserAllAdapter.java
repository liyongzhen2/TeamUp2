package com.liyongzhen.teamup.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.liyongzhen.teamup.activities.ChatRoomActivity;
import com.liyongzhen.teamup.R;
import com.liyongzhen.teamup.models.FollowModel;
import com.liyongzhen.teamup.models.User;
import com.liyongzhen.teamup.utils.CommonValuables;
import com.liyongzhen.teamup.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.liyongzhen.teamup.utils.CommonValuables.referenceFollowings;


public class UserAllAdapter extends RecyclerView.Adapter<UserAllAdapter.CustomViewHolder> {

    private Context mContext;
    private List<User> mList;
    private ArrayList<String> mFollowingList;

    public UserAllAdapter(Context context, ArrayList<User> list) {
        mList = list;
        mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_userspage_all, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {
        User model = mList.get(position);

        holder.setIsRecyclable(false);
        holder.userName.setText(model.getUsername());
        if (model.getPhotoUrl().isEmpty())
            holder.personPhoto.setImageResource(R.drawable.photo_default);
        else
            Picasso.with(mContext).load(model.getPhotoUrl()).into(holder.personPhoto);
        int color;
        if(model.getExperience1()) {
            color = mContext.getResources().getColor(R.color.experience_red);
        }
        else if(model.getExperience2()) {
            color = mContext.getResources().getColor(R.color.experience_blue);
        }
        else if(model.getExperience3()) {
            color = mContext.getResources().getColor(R.color.experience_green);
        }
        else  {
            color = mContext.getResources().getColor(R.color.experience_yellow);
        }
        holder.colorBar.setBackgroundColor(color);

        //sport type
        int sporttype = model.getChooseSport();
        String[] sports = mContext.getResources().getStringArray(R.array.sports);
        holder.sportType.setText(sports[sporttype]);


        String cUId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        referenceFollowings.child(cUId).child(model.getuId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FollowModel followModel = dataSnapshot.getValue(FollowModel.class);
                if(followModel != null && followModel.value){
                    holder.switchFollow.setChecked(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView colorBar;
        private ImageView personPhoto;
        private TextView userName;
        private TextView sportType;
        private ImageButton userBlock;
        private Switch switchFollow;

        private CustomViewHolder(View view) {
            super(view);

            colorBar =  view.findViewById(R.id.colorBar);
            personPhoto =  view.findViewById(R.id.person_photo);
            userName =  view.findViewById(R.id.userName);
            sportType = view.findViewById(R.id.textViewSportType);

            switchFollow = view.findViewById(R.id.switchFollow);
            switchFollow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Log.d(CommonValuables.TAG, "change");
                    boolean value = switchFollow.isChecked();
                    String cUId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    User model = mList.get(getAdapterPosition());
                    FollowModel followModel = new FollowModel(value);
                    FirebaseDatabase.getInstance().getReference("followings").child(cUId).child(model.getuId())
                            .setValue(followModel);
                }
            });

            userBlock =  view.findViewById(R.id.user_block);
            userBlock.setOnClickListener(mOnClickListener);
        }

        private View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.user_block) {
                    PopupMenu popup = new PopupMenu(mContext, userBlock);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.user_start_chat_menu, popup.getMenu());
                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            Intent i = new Intent(mContext, ChatRoomActivity.class);
                            i.putExtra(Constants.EXTRA_USERID, mList.get(getAdapterPosition()).getuId());
                            mContext.startActivity(i);
                            return true;
                        }
                    });
                    popup.show();//showing popup menu
                }
            }
        };
    }
}
