package com.liyongzhen.teamup.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.liyongzhen.teamup.R;
import com.liyongzhen.teamup.adapters.BlogAdapter;
import com.liyongzhen.teamup.models.BlogModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlogsFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private BlogAdapter mBlogAdapter;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private String blogType = "charts";
    private boolean isViewShown = false;
    private int rowCount = 10;
    private int pageNum = 0;
    private long lastTime = 0;
    ArrayList<BlogModel> mBlogModels;


    public BlogsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_blogs, container, false);

        mProgressBar = (ProgressBar) v.findViewById(R.id.blogChartProgress);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.blogChartList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        mBlogModels = new ArrayList<>();
        mBlogAdapter = new BlogAdapter(getActivity(), mBlogModels, blogType);
        mRecyclerView.setAdapter(mBlogAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(-1)) {

                } else if (!recyclerView.canScrollVertically(1)) {
                    onScrolledToBottom();
                }
            }
        });

        if (!isViewShown)
            fetchBlogData(blogType, pageNum);

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                pageNum = 0;
                fetchBlogData(blogType, pageNum);
            }
        });

        return v;
    }

    private void fetchBlogData(String typeName, final int page) {
        Query query;
        if (page == 0)
            query = FirebaseDatabase.getInstance().getReference().child("posts").child("posts")
                    .orderByChild("timestamp").limitToLast(rowCount);
        else
            query = FirebaseDatabase.getInstance().getReference().child("posts").child("posts")
                    .orderByChild("timestamp").endAt(lastTime).limitToLast(rowCount);
        Log.e("query", query.toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (page == 0)
                    mBlogModels.clear();
                if (dataSnapshot.exists()) {
                    boolean isLast = true;
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        BlogModel model = item.getValue(BlogModel.class);
                        if (isLast) {
                            lastTime = model.getTimestamp() - 1;
                            isLast = false;
                        }
                        mBlogModels.add(page * rowCount, model);
                    }
                }
                onItemsLoadComplete();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void onScrolledToBottom(){
        pageNum++;
        fetchBlogData("charts", pageNum);
    }

    void onItemsLoadComplete() {
        mBlogAdapter.notifyDataSetChanged();
        mProgressBar.setVisibility(View.GONE);
        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getView() != null) {
            isViewShown = true;
            pageNum = 0;
            if (mBlogModels != null)
                mBlogModels.clear();
            fetchBlogData(blogType, pageNum);
        } else {
            isViewShown = false;
        }
    }

}
