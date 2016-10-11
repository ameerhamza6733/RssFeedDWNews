package com.ameerhamza6733.urduNews;

/**
 * Created by DELL 3542 on 7/7/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    public String temp;
    private List<RssItem> mDataSet;

    private boolean mySwitchIsChecked;
    private boolean RemoveFlag = true;
    protected Activity activity;
    private SharedPreferences sharedPref;
    protected int fountType;


    public RecyclerAdapter(List<RssItem> rssItems) {
        this.mDataSet = rssItems;
        activity=rssItems.get(0).getActivity();


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);



        try {


            sharedPref = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
            String getPref = sharedPref.getString("example_list", "");
            fountType = Integer.parseInt(getPref);
            Log.v("fount", "" + fountType);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        RssItem rssItem = mDataSet.get(position);
        holder.mTitle.setText(rssItem.getTitle());
        holder.mDetail.setText(rssItem.getDescription());
        holder.mDate.setText(rssItem.getPubDate());
        holder.mCategory.setText(rssItem.getCategory());

        holder.mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.mImageButton, position);
            }
        });




            Picasso.with(holder.mview.getContext())
                    .load(rssItem.getImageUrl())
                   .placeholder(R.drawable.back_ground_image)
                    .centerCrop()
                    .fit()
                    .into(holder.mImage);
            Log.i("Inside if in picasso", Boolean.toString(mySwitchIsChecked));



    }

    private void showPopupMenu(View view, int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popup.getMenu());



        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position,view));
        popup.show();
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }



    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private int position;
        private View mView;

        public MyMenuItemClickListener(int positon,View view) {
            this.position = positon;
            this.mView=view;
        }


        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {


                case R.id.No_interasted:
                    mDataSet.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mDataSet.size());
                    Toast.makeText(activity, "Done for now", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.Share:

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, mDataSet.get(position).getPostLink());
                    sendIntent.setType("text/plain");
                    mView.getContext().startActivity(Intent.createChooser(sendIntent, ""));

                    return true;
                default:
            }
            return false;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final Typeface font;
        public TextView mTitle, mDetail, mDate, mCategory;
        public CheckBox mCheckBox;
        public ImageButton mImageButton;
        public ImageView mImage;
        public View mview;
        private SparseBooleanArray selectedItems = new SparseBooleanArray();


        public MyViewHolder(View view) {
            super(view);
            mTitle = (TextView) view.findViewById(R.id.item_title);
            mDetail = (TextView) view.findViewById(R.id.item_detail);
            mImage = (ImageView) view.findViewById(R.id.item_thumbnail);
            mDate = (TextView) view.findViewById(R.id.date);
            mCategory = (TextView) view.findViewById(R.id.category);
            mCheckBox = (CheckBox) view.findViewById(R.id.checkbox);
            mImageButton = (ImageButton) view.findViewById(R.id.imageButton);
            mview = view;


            if(fountType==0)
            {

                font=Typeface.DEFAULT;
            }
            else if (fountType==1) {
                font = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/asunaskh.ttf");

            } else if (fountType==2) {

                font = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/FajerNooriNastalique.ttf");
            } else if (fountType==3) {
                font = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/PakNastaleeq.ttf");
            } else {
                font = Typeface.DEFAULT;
            }
            mTitle.setTypeface(font);
            mDetail.setTypeface(font);
            view.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(activity, Uri.parse(mDataSet.get(getAdapterPosition()).getPostLink()));
        }


    }
}