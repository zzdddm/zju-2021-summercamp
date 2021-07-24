package com.example.projectvideo;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.projectvideo.netAPI.VideoInfo;

import java.util.ArrayList;
import java.util.List;

public class InfoFragment extends Fragment {

    Boolean isMyself;


    private List<VideoInfo> videoInfo=new ArrayList<VideoInfo>();
    private LottieAnimationView animationLoading;
    private CoverAdapter videoCoverAdapter;
    private RecyclerView videoRecyclerView;

    private NetworkU netU;
    private StorageU stoU;

    public class UINetCallBack implements NetworkU.NetCallback{

        @Override
        public void onRequest(List<VideoInfo> videos) {

            if(!Variables.searchKey.isEmpty()){
                videoInfo=new ArrayList<VideoInfo>();
                for(int i=0;i<videos.size();i++){
                    if(videos.get(i).getInfo().getString("uname").contains(Variables.searchKey))
                        videoInfo.add(videos.get(i));
                }
            }else videoInfo=videos;
            videoCoverAdapter.refreshInfo(videoInfo);
            ObjectAnimator.ofFloat(animationLoading, "alpha",1,0)
                    .setDuration(1000)
                    .start();
            ObjectAnimator.ofFloat(videoRecyclerView, "alpha", 0,1)
                    .setDuration(1000)
                    .start();
        }

        @Override
        public void onError(String reason) {
            Toast.makeText(getContext(), reason, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onRefresh() {
            ObjectAnimator.ofFloat(videoRecyclerView, "alpha", 1,0)
                    .setDuration(1000)
                    .start();
            ObjectAnimator.ofFloat(animationLoading, "alpha",0,1)
                    .setDuration(1000)
                    .start();

        }

        @Override
        public void onSuccess() { return;        }
    }
    private class UIIntentCallBack implements CoverAdapter.CoverCallBack{

        @Override
        public void onClick(String videoURL) {

            Intent videoIntent=new Intent(getActivity(), VideoPlayerActivity.class);
            videoIntent.putExtra("video_url", videoURL);
            startActivity(videoIntent);
        }

        @Override
        public void onLongClick(String videoURL) {

                if(Variables.Global[0])
                    stoU.downloadVideo(videoURL);
                return;

        }
    }
    private void initView(){
        animationLoading=getView().findViewById(R.id.animation_refresh);
        videoRecyclerView=getView().findViewById(R.id.recycler_content);
        videoRecyclerView.setHasFixedSize(true);
        videoRecyclerView.setLayoutManager(new GridLayoutManager(getView().getContext(),2));
        videoCoverAdapter=new CoverAdapter(videoInfo);
        videoRecyclerView.setAdapter(videoCoverAdapter);
        videoCoverAdapter.setCallback(new UIIntentCallBack());
    }
    private void initNetwork(){
        netU=new NetworkU();
        netU.initNetwork(new UINetCallBack());
        stoU=new StorageU(getContext());
    }

    public static InfoFragment newInstance(boolean isMyself) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putBoolean("is_myself",isMyself);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            Bundle args=getArguments();
            isMyself=args.getBoolean("is_myself");
        }else isMyself=false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.info_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initNetwork();
        initView();
        if(isMyself) netU.getVideos(Variables.myID);
        else netU.getVideos("");
    }


}
