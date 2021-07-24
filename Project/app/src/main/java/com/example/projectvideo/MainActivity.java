package com.example.projectvideo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.projectvideo.netAPI.VideoInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String video_Path,cover_Path;
    NetworkU netU;

    private final static int REQUEST_CODE_RECORD = 1;
    private final static int REQUEST_CODE_PICK_VIDEO = 2;
    private final static int REQUEST_CODE_TAKE_PICTURE = 3;
    private final static int REQUEST_CODE_PICK_PICTURE = 4;
    private boolean[] settingLocal={false,true,true,true,false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //相机权限
        boolean hasCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean hasAudioPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
        if (!hasCameraPermission||!hasAudioPermission) {
            List<String> permission = new ArrayList<String>();
            if (!hasCameraPermission) {
                permission.add(Manifest.permission.CAMERA);
            }
            if (!hasAudioPermission) {
                permission.add(Manifest.permission.RECORD_AUDIO);
            }
            ActivityCompat.requestPermissions(MainActivity.this, permission.toArray(new String[permission.size()]), 1);
        }

        StorageU.loadConfig(MainActivity.this);
        netU = new NetworkU();
        netU.initNetwork(new NetworkU.NetCallback() {
            @Override
            public void onRequest(List<VideoInfo> videos) {

            }

            @Override
            public void onError(String reason) {

                Toast.makeText(MainActivity.this,reason,Toast.LENGTH_SHORT).show();
                return;
            }

            @Override
            public void onRefresh() {

            }

            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this,"视频上传成功!",Toast.LENGTH_SHORT).show();
                ViewPager viewpager=findViewById(R.id.pager_info);
                viewpager.getAdapter().notifyDataSetChanged();
                return;

            }
        });
        FloatingActionButton upload=findViewById(R.id.fab_upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items={"相机录制","从本地视频中选取"};
                AlertDialog.Builder builder;
                builder=new AlertDialog.Builder(v.getContext())
                        .setIcon(R.drawable.ic_baseline_video_library_24)
                        .setTitle("选择视频源")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent;
                                switch(which){
                                    case 0:
                                        intent=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                                        video_Path=StorageU.getOutputMediaPath(true);
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, StorageU.getUriForFile(MainActivity.this,video_Path));
                                        if(Variables.Global[4])
                                            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,10);
                                        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
                                        startActivityForResult(intent,REQUEST_CODE_RECORD);
                                        break;
                                    case 1:
                                        intent=new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                                        startActivityForResult(intent,REQUEST_CODE_PICK_VIDEO);
                                        break;
                                }
                            }
                        });
                builder.show();

            }
        });
        setSupportActionBar(findViewById(R.id.toolbar_func));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //设置
        Toolbar toolbar=findViewById(R.id.toolbar_func);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.account, null);
                EditText inputID=view.findViewById(R.id.input_id);
                EditText inputName=view.findViewById(R.id.input_name);
                inputID.setText(Variables.myID);
                inputName.setText(Variables.myName);
                builder = new AlertDialog.Builder(MainActivity.this)
                        .setView(view)
                        .setTitle("修改个人信息")
                        .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(inputID.getText().toString().isEmpty()||inputName.getText().toString().isEmpty()){
                                    Toast.makeText(MainActivity.this, "请填写学号和昵称", Toast.LENGTH_SHORT).show();
                                }else{
                                    Variables.myID=inputID.getText().toString();
                                    Variables.myName=inputName.getText().toString();
                                    StorageU.storeConfig(MainActivity.this);
                                    ViewPager viewpager=findViewById(R.id.pager_info);
                                    viewpager.getAdapter().notifyDataSetChanged();
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create().show();
            }
        });

        //搜索
        SearchView searchView=findViewById(R.id.search_uName);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Variables.searchKey=s;
                ViewPager viewpager=findViewById(R.id.pager_info);
                viewpager.getAdapter().notifyDataSetChanged();
                return true;
            }
        });

        ViewPager infoPager=findViewById(R.id.pager_info);
        TabLayout idTab=findViewById(R.id.tab_id);
        infoPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                Fragment fragment=null;
                switch(position){
                    case 0:
                        fragment= InfoFragment.newInstance(false);
                        break;
                    case 1:
                        fragment=InfoFragment.newInstance(true);
                        break;
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return Variables.PAGE_COUNT;
            }
            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }

            @Override
            public CharSequence getPageTitle(int position){
                String title="";
                switch (position){
                    case 0:
                        title="全部视频";
                        break;
                    case 1:
                        title="我的视频";
                        break;
                }
                return title;
            }
        });

        idTab.setupWithViewPager(infoPager);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==REQUEST_CODE_RECORD){
                refreshGallery(video_Path);
                chooseCover(MainActivity.this);
            }else if(requestCode==REQUEST_CODE_PICK_VIDEO){
                video_Path=Uri2Path(data.getData());
//                Log.d("VideoPath",videoPath);
                chooseCover(MainActivity.this);
            }else if(requestCode==REQUEST_CODE_TAKE_PICTURE){
                refreshGallery(cover_Path);
                netU.uploadVideo(this,video_Path,cover_Path);
            }else if(requestCode==REQUEST_CODE_PICK_PICTURE){
                cover_Path=Uri2Path(data.getData());
//                Log.d("PhotoPath",coverPath);
                netU.uploadVideo(this,video_Path,cover_Path);
            }
        }else{
            if(requestCode==REQUEST_CODE_RECORD){
                if(new File(video_Path).exists())
                    refreshGallery(video_Path);
            }else if(requestCode==REQUEST_CODE_TAKE_PICTURE){
                if(new File(cover_Path).exists())
                    refreshGallery(cover_Path);
            }
        }
    }

    //刷新
    private void refreshGallery(String path){
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(path)));
        sendBroadcast(intent);
    }
    //选择封面
    private void chooseCover(Context ctx){
        final String[] items={"使用相机拍摄","从本地图片中选取","使用视频首帧"};
        AlertDialog.Builder builder;
        builder=new AlertDialog.Builder(ctx)
                .setTitle("请选择封面来源：")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent;
                        switch(which){
                            case 0:
                                intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                cover_Path=StorageU.getOutputMediaPath(false);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, StorageU.getUriForFile(MainActivity.this,cover_Path));
                                startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
                                break;
                            case 1:
                                intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(intent,REQUEST_CODE_PICK_PICTURE);
                                break;
                            case 2:
                                Bitmap firstFrame=null;
                                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                                try {
                                    retriever.setDataSource(video_Path);
                                    firstFrame=retriever.getFrameAtTime();
                                } catch (IllegalArgumentException e) {
                                    e.printStackTrace();
                                } finally {
                                    retriever.release();
                                }
                                cover_Path=saveFirstFrame(firstFrame);
                                netU.uploadVideo(MainActivity.this,video_Path,cover_Path);
                                break;
                        }
                    }
                });
        builder.show();
    }
    //保存首诊做封面
    public String saveFirstFrame(Bitmap firstFrame) {
        String path=StorageU.getOutputMediaPath(false);
        File file;
        try {
            file=new File(path);
            if (!file.exists()) file.createNewFile();
            FileOutputStream fos=new FileOutputStream(file);
            firstFrame.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }
    //URI转成地址
    private String Uri2Path(Uri contentUri){
        String filePath;
        Cursor cursor = getContentResolver().query(contentUri,new String[]{MediaStore.MediaColumns.DATA},null,null,null);
        cursor.moveToFirst();
        filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
        cursor.close();
        return filePath;
    }
}