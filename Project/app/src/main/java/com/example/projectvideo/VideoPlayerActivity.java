package com.example.projectvideo;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;

public class VideoPlayerActivity extends AppCompatActivity {

    private VideoView video_View;
    private ImageView image_View_Video;
    private LottieAnimationView loadingAnimation;
    private MediaController media_Controller;

    private long lastClickTime;
    private float oldY,newY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        String url = getIntent().getStringExtra("videoUrl");
        image_View_Video=findViewById(R.id.img_view_thumb);
        video_View=findViewById(R.id.video_player);
        video_View.setVideoURI(Uri.parse(url));
        media_Controller=new MediaController(this);
        video_View.setMediaController(media_Controller);
        video_View.setAlpha(0);
        loadingAnimation=findViewById(R.id.animation_loading);

        video_View.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                ObjectAnimator.ofFloat(loadingAnimation, "alpha", 1,0)
                        .setDuration(500)
                        .start();
                loadingAnimation.pauseAnimation();
                ObjectAnimator.ofFloat(video_View, "alpha", 0,1)
                        .setDuration(500)
                        .start();
                video_View.start();

            }
        });

        // 点赞：要传到服务器上，并展示一个点赞动画。点赞有最小时间间隔，要注意：

        video_View.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if((System.currentTimeMillis()-lastClickTime<300)&&(Variables.Global[1])){

                            ObjectAnimator animatorScaleX = ObjectAnimator.ofFloat(image_View_Video, "ScaleX", 2,5);
                            animatorScaleX.setDuration(500);
                            ObjectAnimator animatorScaleY = ObjectAnimator.ofFloat(image_View_Video,"ScaleY",2,5);
                            animatorScaleY.setDuration(500);
                            ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(image_View_Video,"alpha",0,1);
                            animatorAlpha.setDuration(500);

                            AnimatorSet animatorSet = new AnimatorSet();
                            animatorSet.playTogether(animatorScaleX);
                            animatorSet.playTogether(animatorScaleY);
                            animatorSet.playTogether(animatorAlpha);
                            animatorSet.start();

                            ObjectAnimator.ofFloat(image_View_Video,
                                    "alpha",
                                    1,0)
                                    .setDuration(500)
                                    .start();
                            Toast.makeText(v.getContext(), "感谢点赞，喜欢的话就分享给更多朋友吧~",Toast.LENGTH_SHORT).show();
                        }
                        lastClickTime=  System.currentTimeMillis();
                        oldY=event.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        newY=event.getY();
                        break;

                    case MotionEvent.ACTION_UP:
                        if(System.currentTimeMillis()-lastClickTime>300){
                            if(!media_Controller.isShowing())
                                media_Controller.show();
                        }
                        if((newY-oldY>150)&&(Variables.Global[2]))
                            finish();
                        break;
                }
                return true;
            }

        });
        video_View.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(Variables.Global[3]){
                    video_View.seekTo(0);
                    video_View.start();
                } else finish();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}