package com.mymonas.ngobrol.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.makeramen.RoundedImageView;
import com.mymonas.ngobrol.R;
import com.mymonas.ngobrol.io.model.PostData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;

/**
 * Created by Huteri on 10/19/2014.
 */
public class PostAdapter extends ArrayAdapter<PostData> {
    private final Context mContext;
    private final ArrayList<PostData> mPostData;
    private final DisplayImageOptions mImageOptions;

    public PostAdapter(Context context, ArrayList<PostData> postData) {
        super(context, R.layout.post_item, postData);
        mContext = context;
        mPostData = postData;

        mImageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.nophoto)
                .showImageOnFail(R.drawable.nophoto)
                .showImageOnLoading(R.drawable.nophoto)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.post_item, parent, false);

            holder = new ViewHolder();
            holder.tvText = (TextView) view.findViewById(R.id.text);
            holder.tvName = (TextView) view.findViewById(R.id.name);
            holder.tvDate = (TextView) view.findViewById(R.id.date);
            holder.profileImg = (RoundedImageView) view.findViewById(R.id.profile_img);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.tvText.setText(mPostData.get(position).getText());
        holder.tvName.setText(mPostData.get(position).getUser().getUsername());
        holder.tvDate.setText(mPostData.get(position).getDateCreated());

        ImageLoader.getInstance().displayImage(mPostData.get(position).getUser().getProfileUrl(), holder.profileImg, mImageOptions);

        return view;

    }

    private static class ViewHolder {
        TextView tvText;
        TextView tvName;
        TextView tvDate;
        RoundedImageView profileImg;
    }
}
