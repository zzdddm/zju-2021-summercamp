package com.example.projectvideo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.projectvideo.netAPI.VideoInfo;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CoverAdapter extends RecyclerView.Adapter<CoverAdapter.CoverHolder> {

    List<VideoInfo> videoList=null;
    private static CoverCallBack callBack=null;






    public interface CoverCallBack{
        public void onClick(String videoURL);
        public void onLongClick(String videoURL);
    }
    public static class CoverHolder extends RecyclerView.ViewHolder {


        private ImageView coverImg;
        private TextView userName;
        private TextView updateTime;
        private View contentView;
        private String videoURL;

        public CoverHolder(View v) {
            super(v);
            contentView = v;
            coverImg = v.findViewById(R.id.img_cover);
            userName = v.findViewById(R.id.txt_uname);
            updateTime = v.findViewById(R.id.txt_date);
        }


        public void Binding(VideoInfo vinfo) {

            String coverUrl = vinfo.getCover().getString("url");

            //Date:

            Date updateDate = new Date(vinfo.getTime().getLong("update"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            MultiTransformation imgOptions = new MultiTransformation(
                    new CenterCrop(),
                    new RoundedCorners(40)
            );

            Glide.with(contentView).load(coverUrl)
                    .placeholder(R.drawable.loading)
                    .transform(imgOptions)
                    .into(coverImg);
            userName.setText(vinfo.getInfo().getString("uname"));
            updateTime.setText(formatter.format(updateDate));
            videoURL = vinfo.getVideo();
            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.onClick(videoURL);
                }
            });
            contentView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    callBack.onLongClick(videoURL);
                    return true;
                }
            });
        }


    }

    public CoverAdapter(List<VideoInfo> videoList){
        this.videoList=videoList;
        return;
    }
    public void setCallback(CoverCallBack callback){
        CoverAdapter.callBack=callback;
    }

    public void refreshInfo(List<VideoInfo> videoList){
        this.videoList=videoList;
        notifyDataSetChanged();
        return;
    }


    @Override
    public CoverHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new CoverHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.container_cover, viewGroup,false));
    }

    @Override
    public void onBindViewHolder(CoverAdapter.CoverHolder coverViewHolder, int i) {
        coverViewHolder.Binding(videoList.get(i));
    }

    @Override
    public int getItemCount() {
        if(this.videoList==null) return 0;
        else return videoList.size();
    }






}
