package com.example.projectvideo;

import android.content.Context;

import com.example.projectvideo.netAPI.UploadResponse;
import com.example.projectvideo.netAPI.VideoGetService;
import com.example.projectvideo.netAPI.VideoInfo;
import com.example.projectvideo.netAPI.VideoList;
import com.example.projectvideo.netAPI.VideoUploadService;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkU {
    private static final String netToken=" ";
    private static final int sizeLimitMB=60;
    private Retrofit retrofit=null;
    private VideoGetService videoGet=null;
    private VideoUploadService videoUpload=null;
    private NetCallback Callback=null;

    public interface NetCallback{
        public void onRequest(List<VideoInfo> videos);
        public void onError(String reason);
        public void onRefresh();
        public void onSuccess();
    }

    public void initNetwork(NetCallback mCallback){
        this.Callback=mCallback;
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS).build();
        retrofit=new Retrofit.Builder()
                .baseUrl("https://api-android-camp.bytedance.com/zju/invoke/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        videoGet=retrofit.create(VideoGetService.class);
        videoUpload=retrofit.create(VideoUploadService.class);
    }

    public void getVideos(String student_id){
        Callback.onRefresh();
        Call<VideoList> request=videoGet.getVideoList(student_id,netToken);
        request.enqueue(new Callback<VideoList>() {
            @Override
            public void onResponse(Call<VideoList> call, Response<VideoList> response) {
                VideoList list=response.body();
                if(list==null||!list.isSucceed())
                    Callback.onError("服务器异常");
                else
                    Callback.onRequest(list.getList());
            }

            @Override
            public void onFailure(Call<VideoList> call, Throwable t) {
                Callback.onError("无网络或网络错误");
                t.printStackTrace();
            }
        });
    }

    public void uploadVideo(Context ctx, String videoPath, String coverPath) {
        File videoFile,coverFile;
        videoFile=new File(videoPath);
        coverFile=new File(coverPath);
        if (videoFile.length()+ coverFile.length() > sizeLimitMB * 1024 * 1024) {
            Callback.onError("视频和封面超过" + sizeLimitMB + "MB，请重新选择");
            return;
        } else { //Start Uploading
            MultipartBody.Part coverPart = MultipartBody.Part.createFormData("cover_image", coverFile.getName(),
                    RequestBody.create(MediaType.parse("multipart/form-data"),coverFile));

            MultipartBody.Part videoPart = MultipartBody.Part.createFormData("video", videoFile.getName(),
                    RequestBody.create(MediaType.parse("multipart/form-data"), videoFile));

            Call<UploadResponse> submit = videoUpload.submitMessage(Variables.myID, Variables.myName, "", coverPart, videoPart, netToken);

            submit.enqueue(new Callback<UploadResponse>() {
                @Override
                public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                    if (!response.isSuccessful()) {
                        Callback.onError("服务器异常");
                    } else {
                        UploadResponse res = response.body();
                        if (!res.isSuccess()) {
                            Callback.onError("上传失败");
                        } else {
                            Callback.onSuccess();
                        }
                    }
                }
                @Override
                public void onFailure(Call<UploadResponse> call, Throwable t) {
                    Callback.onError("无网络或网络错误");
                    t.printStackTrace();
                }
            });
            return;
        }
    }
}
