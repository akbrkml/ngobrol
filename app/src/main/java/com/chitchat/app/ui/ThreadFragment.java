package com.chitchat.app.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.chitchat.app.R;
import com.chitchat.app.io.RestClient;
import com.chitchat.app.io.model.ThreadCallback;
import com.chitchat.app.model.ThreadItem;
import com.chitchat.app.ui.adapter.ThreadAdapter;
import com.chitchat.app.util.Clog;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ThreadFragment extends Fragment {
    private static final String ACTION_GET_ALL_THREADS = "get_all_threads";
    private Context mContext;
    private ArrayList<ThreadItem> mThreadList;
    private ThreadAdapter mThreadAdapter;

    public ThreadFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_thread, container, false);

        mThreadList = new ArrayList<ThreadItem>();
        mThreadAdapter = new ThreadAdapter(mContext, mThreadList);

        final ProgressBar pBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        pBar.setVisibility(View.VISIBLE);
        RestClient.get().getThreads(ACTION_GET_ALL_THREADS, new Callback<ThreadCallback>() {
            @Override
            public void success(ThreadCallback threadCallback, Response response) {
                pBar.setVisibility(View.GONE);
                mThreadList.addAll(threadCallback.getData());
                mThreadAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                pBar.setVisibility(View.GONE);
                Clog.d(error.getCause().toString());
            }
        });

        ListView threadLv = (ListView) view.findViewById(R.id.thread_list);
        View header = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.thread_header, null);

        threadLv.addHeaderView(header);
        threadLv.setAdapter(mThreadAdapter);
        threadLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle args = new Bundle();
                args.putString("threadId", mThreadList.get(i-1).getId());

                Intent intent = new Intent(getActivity(), PostActivity.class);
                intent.putExtras(args);
                startActivity(intent);
            }
        });

        return view;
    }
}
