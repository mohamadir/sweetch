package com.example.sweetchapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wwdablu.soumya.wzip.WZip;
import com.wwdablu.soumya.wzip.WZipCallback;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements WZipCallback {

    public static final int REQUEST_CODE = 100;
    public static final String URL_DOWNLOAD_LINK_1 = "https://test-assets-mobile.s3-us-west-2.amazonaws.com/125%402.zip";
    public static final String URL_DOWNLOAD_LINK_2 = "https://test-assets-mobile.s3-us-west-2.amazonaws.com/127%402.zip";
    public static final String FILE_1 = "file1";
    public static final String FILE_2 = "file2";
    public static final String LAST_DISPLAY_URL = "LAST_DISPLAY_URL";
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.progress1)
    ProgressBar progressBar1;
    @BindView(R.id.mView)
    FrameLayout mView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    boolean isExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mView.setClipToOutline(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CustomAdapter adapter = new CustomAdapter( new String[]{"asd"});
        recyclerView.setAdapter(adapter);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int height = 0;
                if (isExpanded) height = 180;
                else {
                    height = 500;
                    nestedScrollView.post(new Runnable() {
                        @Override
                        public void run() {
                        nestedScrollView.smoothScrollBy(0, 500);
                        }
                    });
                }
                ValueAnimator anim = ValueAnimator.ofInt(mView.getMeasuredHeight(), toDp(height));
                anim.addUpdateListener(valueAnimator -> {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = mView.getLayoutParams();
                    layoutParams.height = val;
                    mView.setLayoutParams(layoutParams);
                });



                anim.setDuration(500);
                anim.start();
                AnimatorSet set = new AnimatorSet();
                set.start();
                isExpanded = !isExpanded;
            }
        });
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE);

    }

    private int toDp(int val){
        Resources r = getResources();
        int px = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, val,r.getDisplayMetrics()));
        return
                px;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void downloadAndUnzipContent(String url) {
        String zipFileName = url.equals(URL_DOWNLOAD_LINK_1) ? "/content1.zip" : "/content12.zip";
        DownloadFileAsync download = new DownloadFileAsync(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() +
                zipFileName,
                this, file -> {

                    // check unzip file now
                    WZip unzip = new WZip();
                    unzip.unzip(file,
                            new File(MainActivity.this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()),
                            url.equals(URL_DOWNLOAD_LINK_1) ? FILE_1 : FILE_2,
                            MainActivity.this);


                });
        download.execute(url);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(SharedPrefUtils.getStringData(MainActivity.this, LAST_DISPLAY_URL).equals(FILE_1)) {
                        downloadAndUnzipContent(URL_DOWNLOAD_LINK_2);
                        SharedPrefUtils.saveData(MainActivity.this,LAST_DISPLAY_URL, FILE_2);
                    } else {
                        downloadAndUnzipContent(URL_DOWNLOAD_LINK_1);
                        SharedPrefUtils.saveData(MainActivity.this,LAST_DISPLAY_URL, FILE_1);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    @Override
    public void onStarted(String identifier) {
    }

    @Override
    public void onZipCompleted(File zipFile, String identifier) {
    }

    @Override
    public void onUnzipCompleted(String identifier) {
        String pngFileName = identifier.equals(FILE_1) ? "/25.png" : "/27.png";
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + pngFileName);
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                if (file.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    imageView.setImageBitmap(myBitmap);
                    progressBar1.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onError(Throwable throwable, String identifier) {

    }
}