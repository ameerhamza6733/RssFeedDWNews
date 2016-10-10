package com.ameerhamza6733.urduNews;


import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by DELL 3542 on 6/30/2016.
 */
public class HeadLineFragment extends Fragment {

    private static final String TAG = "HeadLineFragment_";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    public View view;
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView mRecyclerView;
    protected RecyclerAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected ArrayList<RssItem> mDataset = new ArrayList<>();
    protected ProgressBar mProgressBar;
    protected Activity myActivity;


    private String mImageURL;

    private int mItemNumber;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int indexofThePost;

    private DateFormat df;
    private String CurrentDay;

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


        Log.d(TAG, "onCreate: ");


        setRetainInstance(true);
        new myPagerAdupter(getChildFragmentManager(), getContext());


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.my_recycler_view_fragment, container, false);
        Log.d(TAG, "onCreateView: ");
        this.view = rootView;
        myActivity=getActivity();
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.Progressbar);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout =(SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });


        mLayoutManager = new LinearLayoutManager(getActivity());


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        } else {
            mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
        }

        if (savedInstanceState != null) {

            Log.i(TAG, "save state is not null");
            mDataset = savedInstanceState.getParcelableArrayList("m");
            mAdapter = new RecyclerAdapter(mDataset);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);


        return rootView;
    }

    private void refreshItems() {

        mDataset.clear();
        new LoadRssFeedsItems(getActivity()).execute(Constants.DW_HOME_PAGE_URL);

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mDataset.isEmpty()) {


            Log.i(TAG, "start Asyc task ");

            new LoadRssFeedsItems(getActivity()).execute(Constants.DW_HOME_PAGE_URL);


        }else {
            if (mProgressBar != null && !mDataset.isEmpty()) {

                mProgressBar.setVisibility(View.INVISIBLE);
            }
        }

    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        savedInstanceState.putParcelableArrayList("m", mDataset);
        super.onSaveInstanceState(savedInstanceState);
    }


    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }


    public class LoadRssFeedsItems extends AsyncTask<String, Integer, Void> {
        private Context mContext;
        private String mTitle, mDescription, mLink, mPubDate;
        private String mCategory;


        private String mysubstring;
        private String mMints;


        public LoadRssFeedsItems(Context context) {
            mContext = context;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressBar.setProgress(1);
        }

        protected Void doInBackground(String... urls) {
            try {

                Document rssDocument = Jsoup.connect(urls[0]).timeout(Constants.TIME_OUT).ignoreContentType(true).parser(Parser.xmlParser()).get();
                Elements mItems = rssDocument.select(Constants.ITEM);
                RssItem rssItem;
                mItemNumber = 0;
                for (Element element : mItems) {

                    mItemNumber++;
                    mTitle = element.select("title").first().text();
                    mDescription = element.select("description").first().text();
                    mLink = element.select("link").first().text();
                    mPubDate = element.select("pubDate").first().text();
                    mCategory = element.select("category").first().text();
                    mImageURL = element.select("media|content").attr("url").toString();


                     CurrentDay = mPubDate.substring(0, 3);
                    mMints = mPubDate.substring(16,22);

                     df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
                    String date = df.format(Calendar.getInstance().getTime());
                    if(date.substring(0,3).equals(CurrentDay)) {
                        mPubDate=mPubDate.replace(mPubDate, "آج").concat(mMints);
                    }else {
                        mPubDate=mPubDate.substring(0,3).concat(mMints);
                    }
                   
                    mDescription=Jsoup.parse(mDescription).text();

                    indexofThePost = mDescription.indexOf(Constants.THE_psot);

                     mysubstring = mDescription.substring(indexofThePost, mDescription.length());
                    mDescription = mDescription.replace(mysubstring, "");
                    

                    Log.i(TAG, "Item title: " + (mTitle == null ? "N/A" : mTitle));
                    Log.i(TAG, "Item Description: " + (mDescription == null ? "N/A" : mDescription));
                    Log.i(TAG, "Item link: " + (mLink == null ? "N/A" : mLink));
                    Log.i(TAG, "Item data: " + (mPubDate == null ? "N/A" : mPubDate));
                    Log.i(TAG, "Item image link: " + (mImageURL == null ? "N/A" : mImageURL));
                    Log.i(TAG, "item: : " + mItemNumber);

                       
                        rssItem = new RssItem(mTitle, mDescription, mPubDate, mLink, mCategory, mItemNumber,mImageURL, getActivity(),myActivity);
                        mDataset.add(rssItem);



                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            if (mDataset.isEmpty()) try {


                Snackbar mSnackbar = Snackbar.make(view, "Unable to connect", Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                Snackbar.make(getView(), "CheckIn Cancelled", Snackbar.LENGTH_LONG).show();
                                new LoadRssFeedsItems(mContext).execute(Constants.DW_HOME_PAGE_URL);


                            }
                        });
                mSnackbar.show();
            } catch (NullPointerException n) {
                n.printStackTrace();
            }
            else {
                mProgressBar.setVisibility(View.INVISIBLE);

                mSwipeRefreshLayout.setRefreshing(false);
                mAdapter = new RecyclerAdapter(mDataset);
                mRecyclerView.setAdapter(mAdapter);
              
              
                

            }

        }
    }



}