package com.ameerhamza6733.urduNews;

/**
 * Created by DELL 3542 on 7/7/2016.
 */

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
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
    private MySharedPreferences mySharedPreferences = new MySharedPreferences();
    private boolean mySwitchIsChecked;
    private boolean RemoveFlag = true;
    private MySharedPreferences mMySharedPreferences;

    public RecyclerAdapter(List<RssItem> rssItems) {
        this.mDataSet = rssItems;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        mMySharedPreferences= new MySharedPreferences();
        mySwitchIsChecked = mySharedPreferences.loadPrefs(Constants.switchStateKey, true, parent.getContext());
        temp = mySharedPreferences.loadStringPrefs(Constants.FONT_KEY, "Pak nastaleeq (default)", parent.getContext());

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


        if (!mMySharedPreferences.loadPrefs(Constants.switchStateKey, false, holder.mview.getContext())) {
            Picasso.with(holder.mview.getContext())
                    .load(rssItem.getImageUrl())
                   .placeholder(R.drawable.back_ground_image)
                    .fit()
                    .into(holder.mImage);
            Log.i("Inside if in picasso", Boolean.toString(mySwitchIsChecked));

        }else {
            Picasso.with(holder.mview.getContext())
                    .load(rssItem.getImageUrl())

                    .fit()
                    .into(holder.mImage);
            Log.i("Inside else in picasso", Boolean.toString(mySwitchIsChecked));
        }


    }

    private void showPopupMenu(View view, int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popup.getMenu());


     if(mDataSet.get(position).getCategory().equals("حالات حاضرہ") || HalatHazraFragment.isHalatHazraFragmentVisible || mDataSet.get(position).getCategory().equals("صحت"))
     {
         popup.getMenu().getItem(0).setEnabled(false);
     }else {
         popup.getMenu().getItem(0).setEnabled(true);
     }
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

                case R.id.Not_interasted_catugury:
                    String RemoveCategory = mDataSet.get(position).getCategory();

                    if (RemoveCategory.equals("صحت")) {

                        RemoveFlag = false;
                        Toast.makeText(MainActivity.context, "You cannot remove default catagury ", Toast.LENGTH_LONG).show();
                    } else if (RemoveCategory.equals("فن و ثقافت")) {
                        mySharedPreferences.saveStringPrefs(Constants.REMOVE_KEY_CULTURE_CATEGORY, RemoveCategory, MainActivity.context);
                    } else if (RemoveCategory.equals("کھیل")) {
                        mySharedPreferences.saveStringPrefs(Constants.REMOVE_KEY_SPORT_CATEGORY, RemoveCategory, MainActivity.context);
                    } else if (RemoveCategory.equals("سائنس اور ماحول")) {
                        mySharedPreferences.saveStringPrefs(Constants.REMOVE_KEY_SINCE_CATEGORY, RemoveCategory, MainActivity.context);

                    } else if (RemoveCategory.equals("معاشرہ")) {
                        mySharedPreferences.saveStringPrefs(Constants.REMOVE_KEY_SOCITY_CATEGORY, RemoveCategory, MainActivity.context);

                    } else if (RemoveCategory.equals("مہاجرین کا بحران")) {
                        mySharedPreferences.saveStringPrefs(Constants.REMOVE_KEY_Refugee_Crisis, RemoveCategory, MainActivity.context);

                    } else {
                        RemoveFlag = false;
                        Toast.makeText(MainActivity.context, "Non selected ", Toast.LENGTH_SHORT).show();
                    }

                    if (RemoveFlag) {
                        mDataSet.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mDataSet.size());
                        Toast.makeText(MainActivity.context, "Restart the app to apply changes", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                case R.id.No_interasted:
                    mDataSet.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mDataSet.size());
                    Toast.makeText(MainActivity.context, "Done for now", Toast.LENGTH_SHORT).show();
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

            if (temp.equals("Naskh asiatype")) {
                font = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/asunaskh.ttf");

            } else if (temp.equals("Fajer noori Nastaleeq")) {

                font = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/FajerNooriNastalique.ttf");
            } else if (temp.equals("Pak nastaleeq (default)")) {
                font = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/PakNastaleeq.ttf");
            } else {
                font = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/PakNastaleeq.ttf");
            }

            mDetail.setTypeface(font);
            mTitle.setTypeface(font);
            mCategory.setTypeface(font);
            view.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(MainActivity.activity, Uri.parse(mDataSet.get(getAdapterPosition()).getPostLink()));
        }


    }
}