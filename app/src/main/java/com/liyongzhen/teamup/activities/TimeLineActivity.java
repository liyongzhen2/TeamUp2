package com.liyongzhen.teamup.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.liyongzhen.teamup.R;
import com.liyongzhen.teamup.fragments.BlogsFragment;
import com.liyongzhen.teamup.fragments.MyBlogFragment;
import com.liyongzhen.teamup.models.User;
import com.liyongzhen.teamup.utils.CommonValuables;
import com.liyongzhen.teamup.utils.Constants;
import com.liyongzhen.teamup.utils.ViewPagerAdapter;

public class TimeLineActivity extends AppCompatActivity{

    private FloatingActionButton mAddBlogBtn;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String pagetitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
//        initController();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        mAddBlogBtn = (FloatingActionButton) findViewById(R.id.addBlog);
        mAddBlogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TimeLineActivity.this, PostBlogActivity.class);
                i.putExtra(Constants.EXTRA_POSTTYPE, pagetitle);
                startActivity(i);
            }
        });

        checkIsProfile();
    }

    private void checkIsProfile(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user == null) {
                            Log.d(CommonValuables.TAG, "user nill");
                            gotoProfilePage();
                        } else {
                            Log.d(CommonValuables.TAG, user.toString());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(CommonValuables.TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

    private void setupViewPager(ViewPager viewPager) {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(new BlogPeopleFragment(), "people");
//        adapter.addFragment(new BlogWatchListFragment(), "watchlist");
//        adapter.addFragment(new BlogSuggestedFragment(), "suggested");
//        adapter.addFragment(new BlogTrendingFragment(), "trending");

//        adapter.addFragment(new BlogsFragment(), "blogs");
        adapter.addFragment(new BlogsFragment(), "all posts");
        adapter.addFragment(new MyBlogFragment(), "my posts");
        viewPager.setAdapter(adapter);

        pagetitle = "all posts";
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pagetitle = adapter.getPageTitle(position).toString();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // use amvMenu here
        inflater.inflate(R.menu.main_menu, menu);

        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return true;
    }

    private void gotoProfilePage(){
        Intent profileIntent = new Intent(TimeLineActivity.this, ProfileActivity.class);
        startActivity(profileIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Do your actions here

        switch (item.getItemId()) {
            case R.id.menu_item_map:
                Intent alarmIntent = new Intent(TimeLineActivity.this, MapActivity.class);
                startActivity(alarmIntent);
                break;
            case R.id.menu_item_chat:
                Intent chatIntent = new Intent(TimeLineActivity.this, ChatActivity.class);
                startActivity(chatIntent);
                break;
            case R.id.menu_item_users:
                Intent blogIntent = new Intent(TimeLineActivity.this, UsersActivity.class);
                startActivity(blogIntent);
                break;
            case R.id.menu_item_profile:
                gotoProfilePage();
                break;
            case R.id.menu_item_terms:
                Intent termsIntent = new Intent(TimeLineActivity.this, TermsActivity.class);
                startActivity(termsIntent);
                break;
            case R.id.menu_item_policy:
                Intent policyIntent = new Intent(TimeLineActivity.this, PrivacyActivity.class);
                startActivity(policyIntent);
                break;
            case R.id.menu_item_logout:
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
//                SharedPrefUtil.saveBoolean(this, Constants.PREF_LOGINSTATE, false);
                Intent i = new Intent(TimeLineActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                break;
        }
        return true;
    }

}
