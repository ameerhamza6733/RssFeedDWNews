package com.ameerhamza6733.urduNews;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DELL 3542 on 7/12/2016.
 */
public class RssItem implements Parcelable {

   private String title;
    private String description;
    private String postLink;
    private String sourceName;
    private String sourceUrl;
    private String imageUrl;
    private String category;
    private String pubDate;
    private int categoryImgId;
    private Bitmap bitmap;
    private Context context;
    private int itemNumber;
    private Activity activity;

    protected RssItem(Parcel in) {
        title = in.readString();
        description = in.readString();
        postLink = in.readString();
        sourceName = in.readString();
        sourceUrl = in.readString();
        imageUrl = in.readString();
        category = in.readString();
        pubDate = in.readString();
        categoryImgId = in.readInt();
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
        itemNumber = in.readInt();
    }

    public static final Creator<RssItem> CREATOR = new Creator<RssItem>() {
        @Override
        public RssItem createFromParcel(Parcel in) {
            return new RssItem(in);
        }

        @Override
        public RssItem[] newArray(int size) {
            return new RssItem[size];
        }
    };

    public Activity getActivity() {
        return activity;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public Context getContext() {
        return context;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }


    public RssItem(String title, String description, String pubDate, String postLink, String category, int itemNumber, String mImageURL, Context context, Activity activity) {
        this.title = title;
        this.description = description;
        this.pubDate=pubDate;
        this.context =context;
        this.imageUrl=mImageURL;
        this.postLink=postLink;
        this.itemNumber= itemNumber;
        this.category=category;
        this.activity=activity;

    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostLink() {
        return postLink;
    }

    public void setLink(String link) {
        this.postLink = link;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public int getCategoryImgId() {
        return categoryImgId;
    }

    public void setCategoryImgId(int categoryImgId) {
        this.categoryImgId = categoryImgId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(postLink);
        dest.writeString(sourceName);
        dest.writeString(sourceUrl);
        dest.writeString(imageUrl);
        dest.writeString(category);
        dest.writeString(pubDate);
        dest.writeInt(categoryImgId);
        dest.writeParcelable(bitmap, flags);
        dest.writeInt(itemNumber);
    }
}
