package com.ameerhamza6733.urduNews;


import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by DELL 3542 on 7/15/2016.
 */
public class MoreNewsFragment extends Fragment {

    private static final String TAG = "MoreNewsFragment";
    private static final String TAG_LIFE = "RecyclerViewLifeCycle";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private final String TAGi = "catugury";
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView mRecyclerView;
    protected RecyclerAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected ArrayList<RssItem> mDataset = new ArrayList<>();
    protected Set<RssItem> mHashSet = new HashSet<>();
    protected Elements metalinks;
    protected String mImageURL;
    protected Document documentImage;
    protected MySharedPreferences mySharedPreferences = new MySharedPreferences();
    protected View view;
    protected String mTitle, mDescription, mLink, mPubDate, mCatgury;
    protected boolean CultureFlag ;
    protected boolean SportFlag, SinceFlag,socityFlag;
    private boolean isVisibleToUser;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LoadRssFeedsItems loadRssFeedsItems;
    private boolean AlreadyRefresh=true;

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);



        Log.i(TAG_LIFE, "Inside onCreat");


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG_LIFE, "Inside onViewCreated");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.my_recycler_view_fragment, container, false);
        Log.i(TAG_LIFE, "Inside onCreateView");
        view = rootView;
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.Progressbar);
        mSwipeRefreshLayout =(SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });





        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        } else {
            mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
        }

        if (savedInstanceState != null) {
            mDataset = savedInstanceState.getParcelableArrayList(Constants.MY_DATA_SET_KEY);
            mAdapter = new RecyclerAdapter(mDataset);
            mRecyclerView.setAdapter(mAdapter);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        return rootView;
    }

    private void refreshItems() {

      if(AlreadyRefresh)
      {
          AlreadyRefresh=false;
          mDataset.clear();
          loadRssFeedsItems=  new LoadRssFeedsItems();

          loadRssFeedsItems.execute(Constants.HEALTH);
      }else {
          Toast.makeText(getActivity(), "please wait refresh in progress",
                  Toast.LENGTH_LONG).show();
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

        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        savedInstanceState.putParcelableArrayList(Constants.MY_DATA_SET_KEY, mDataset);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mDataset.isEmpty()) {

            Log.i(TAG, "start asyncTask");
            new LoadRssFeedsItems().execute(Constants.HEALTH);
        }else {
            if(mProgressBar!=null && !mDataset.isEmpty())
            {

                mProgressBar.setVisibility(View.INVISIBLE);
            }
        }

        Log.i(TAG_LIFE,"setUserVisibleHint"+String.valueOf(this.isVisibleToUser));

    }



    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }


    public class LoadRssFeedsItems extends AsyncTask<String, Void, String> {
        int i = 0;
        private final String Not_interasted_catguty_in_Cutture = mySharedPreferences.loadStringPrefs(Constants.REMOVE_KEY_CULTURE_CATEGORY, "N/A", MainActivity.context);
        private final String Not_interasted_catguty_in_Since = mySharedPreferences.loadStringPrefs(Constants.REMOVE_KEY_SINCE_CATEGORY, "N/A", MainActivity.context);
        private final String Not_interasted_catguty_in_Sport= mySharedPreferences.loadStringPrefs(Constants.REMOVE_KEY_SPORT_CATEGORY, "N/A", MainActivity.context);
        private final String Not_interasted_catguty_in_Socity= mySharedPreferences.loadStringPrefs(Constants.REMOVE_KEY_SOCITY_CATEGORY, "N/A", MainActivity.context);


        public LoadRssFeedsItems() {
        }

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG,"onPreExecute");
            mProgressBar.setProgress(1);
        }


        protected String doInBackground(String... urls) {
            try {
                Document rssDocument = Jsoup.connect(urls[0]).timeout(Constants.BIG_TIME_OUT).ignoreContentType(true).parser(Parser.xmlParser()).get();
                Elements mItems = rssDocument.select(Constants.ITEM);
                RssItem rssItem;
                for (Element element : mItems) {
                    mTitle = element.select("title").first().text();
                    mDescription = element.select("description").first().text();
                    mLink = element.select("link").first().text();
                    mPubDate = element.select("pubDate").first().text();
                    mCatgury = element.select("category").first().text();
                    Log.i(TAG, "Item title: " + (mTitle == null ? "N/A" : mTitle));
                    Log.i(TAG, "Item Description: " + (mDescription == null ? "N/A" : mDescription));
                    Log.i(TAG, "Item link: " + (mLink == null ? "N/A" : mLink));
                    Log.i(TAG, "Item data: " + (mPubDate == null ? "N/A" : mPubDate));
                    Log.i(TAG, "Item image link: " + (mImageURL == null ? "N/A" : mImageURL));
//                    Log.i(TAG, "int i : " + i);
                    Log.i(TAG,mCatgury);
                    if(mCatgury.equals("مہاجرین کا بحران"))
                    {
                        continue;
                    }
                    rssItem = new RssItem(mTitle, mDescription, mPubDate, mLink, mCatgury, i, getActivity());
                    mDataset.add(rssItem);

                }

                //remove duplicate objs


            } catch (Exception e) {
                e.printStackTrace();
            }
            return "d";
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        protected void onPostExecute(String result) {


            if ((CultureFlag && !mDataset.isEmpty()) || SportFlag || SinceFlag || socityFlag) {

                if(CultureFlag)
                {
                    if(mDataset.get(mDataset.size()-1).getCategory().equals("فن و ثقافت"))
                    {
                        Log.i(TAGi,"mDataset.get(mDataset.size()-1).getCategory().equals(\"فن و ثقافت\")");

                        CultureFlag=false;
                        SportFlag=true;

                    }
                    else {
                        Log.i(TAGi,"mDataset.get(mDataset.size()-1).getCategory().equals(\"فن و ثقافت\") else part");


                    }
                }else  if(SportFlag)
                {
                    if(mDataset.get(mDataset.size()-1).getCategory().equals("کھیل"))
                    {

                        Log.i(TAGi,"mDataset.get(mDataset.size()-1).getCategory().equals(\"کھیل\")");
                        SportFlag=false;
                        SinceFlag=true;

                    }else {

                    }
                }else  if(SinceFlag)
                {
                    if(mDataset.get(mDataset.size()-1).getCategory().equals("سائنس اور ماحول"))
                    {
                        Log.i(TAGi,"mDataset.get(mDataset.size()-1).getCategory().equals(\"سائنس اور ماحول\")");
                        SinceFlag=false;
                        socityFlag=true;

                    }
                }else if(socityFlag)
                {
                    if(mDataset.get(mDataset.size()-1).getCategory().equals("معاشرہ"))
                    {
                        Log.i(TAGi,"mDataset.get(mDataset.size()-1).getCategory().equals(\"معاشرہ\")");
                        socityFlag=false;

                    }
                }

            }
            if (mDataset.isEmpty()) {
                try {
                    Snackbar mSnackbar = Snackbar.make(view, "Unable to connect", Snackbar.LENGTH_INDEFINITE)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    new LoadRssFeedsItems().execute(Constants.HEALTH);




                                }
                            });
                    mSnackbar.show();
                } catch (NullPointerException n) {

                }
            }else {
                mProgressBar.setVisibility(View.INVISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);
            }


            if(!mDataset.isEmpty() && mDataset.get(mDataset.size()-1).getCategory().equals("صحت"))
            {
                CultureFlag=true;
            }

            if (CultureFlag) {


                Log.i(TAGi, " CultureFlag");
                if (Not_interasted_catguty_in_Cutture.equals("N/A"))
                {

                    new LoadRssFeedsItems().execute(Constants.ART_AND_CULTURE);

                }
                else
                {
                    SportFlag = true;
                    CultureFlag=false;
                }

            }
            if (SportFlag) {



               if(Not_interasted_catguty_in_Sport.equals("N/A"))
               {
                   Log.i(TAGi, " SportFlag");
                   new LoadRssFeedsItems().execute(Constants.SPORT);
               }else{
                   SinceFlag = true;
                   SportFlag=false;

               }

            }
            if (SinceFlag) {
                if(Not_interasted_catguty_in_Since.equals("N/A"))
                {
                    Log.i(TAGi, " SinceFlag");
                    new LoadRssFeedsItems().execute(Constants.SCIENCE_AND_ENVIMENT);
                }else {
                    SinceFlag=false;
                    socityFlag=true;
                }

            }

            if(socityFlag)
            {
                if(Not_interasted_catguty_in_Socity.equals("N/A"))
                {
                    new LoadRssFeedsItems().execute(Constants.SOCIETY);
                    AlreadyRefresh=true;
                }
                else {
                    socityFlag=false;
                    AlreadyRefresh=true;
                }
            }


            if(!mDataset.isEmpty() && mDataset.get(mDataset.size()-1).getCategory().equals("صحت")){
                mAdapter = new RecyclerAdapter(mDataset);
                Log.i(TAGi, "m data set size: " + mDataset.size());

                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                }else  {
                  if(!mDataset.isEmpty())
                  {

                      mAdapter.notifyDataSetChanged();
                      if (!new MySharedPreferences().loadPrefs(Constants.switchStateKey, false, MainActivity.context)) {
                          getImageUrls get_ImageUrls = new getImageUrls();
                          get_ImageUrls.execute();
                      }
                  }
            }
        }
    }

    private class getImageUrls extends AsyncTask<Void, Void, Void> {
        String mLink;
        String mLastHalfUrl;
        String url;
        int dash_A_index;

        @Override
        protected Void doInBackground(Void... params) {

            Log.i(TAG, "Item image link in seacond thread: " + (mImageURL == null ? "N/A" : mImageURL));
            try {
                for (int i = 0; i < mDataset.size(); i++) {
                    mLink = mDataset.get(i).getPostLink();
                    dash_A_index = mLink.indexOf(Constants.BACK_SLASH_A);
                    mLastHalfUrl = mLink.substring(dash_A_index);
                    url = mLink.replace(Constants.BASE_URL, "");
                    url = url.replace(mLastHalfUrl, "");
                    url = StringUtils.replaceEach(URLEncoder.encode(url, "UTF-8"), new String[]{"+", "*", "%7E"}, new String[]{"%20", "%2A", "~"});
                    StringBuilder sb = new StringBuilder(url);
                    url = sb.append(mLastHalfUrl).toString();
                    url = Constants.BASE_URL + url;
                    documentImage = Jsoup.connect(url).get();
                    metalinks = documentImage.select(Constants.MATA_PROPTY_IMAGE);
                    mImageURL = metalinks.attr(Constants.CONTENT);
                    Log.i(TAG, "Item image link in seacond thread: " + (mImageURL == null ? "N/A" : mImageURL));
                    mDataset.get(i).setImageUrl(mImageURL);

                }

            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            mAdapter.notifyDataSetChanged();
        }
    }


}