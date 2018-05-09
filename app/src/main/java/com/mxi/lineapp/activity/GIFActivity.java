package com.mxi.lineapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.Surface;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.mxi.lineapp.R;
import com.mxi.lineapp.adapter.ImageAdapter;
import com.mxi.lineapp.database.SQLitehelper;
import com.mxi.lineapp.model.player;
import com.mxi.lineapp.network.CommonClass;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GIFActivity extends AppCompatActivity {

    String videoPath = "";
    ImageView iv_share;
    int tolranceWidth = 0;
    int heightDifference = 0;
    int widthDifference = 0;
    int tolranceHeight = 0;
    int lay_width_left = 0;
    int lay_width = 0;
    int lay_width_right = 0;
    ArrayList<player> listPlayer1;
    AbsoluteLayout abs_main;
    SQLitehelper dbcon;
    CommonClass cc;
    VideoView videoView;
    //=================================VideoRecording======================
    private static final String TAG = "GIFActivity";
    private static final int REQUEST_CODE = 1000;
    private int mScreenDensity;
    private MediaProjectionManager mProjectionManager;
    private static int DISPLAY_WIDTH = 720;
    private static int DISPLAY_HEIGHT = 1280;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private MediaProjectionCallback mMediaProjectionCallback;
    //    private ToggleButton mToggleButton;
    private MediaRecorder mMediaRecorder;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private static final int REQUEST_PERMISSIONS = 10;

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);

        dbcon = new SQLitehelper(this);
        cc = new CommonClass(this);
        tolranceWidth = cc.loadPrefInt("imageViewWidth") / 2;


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;

        DISPLAY_HEIGHT = metrics.heightPixels;
        DISPLAY_WIDTH = metrics.widthPixels;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mMediaRecorder = new MediaRecorder();

            mProjectionManager = (MediaProjectionManager) getSystemService
                    (Context.MEDIA_PROJECTION_SERVICE);
        }

        iv_share = (ImageView) findViewById(R.id.iv_share);
        iv_share.setVisibility(View.INVISIBLE);

        videoView = (VideoView) findViewById(R.id.videoView);

        abs_main = (AbsoluteLayout) findViewById(R.id.abs_main);

//        DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) videoView.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        params.rightMargin = 0;
        videoView.setLayoutParams(params);

        abs_main.post(new Runnable() {
            @Override
            public void run() {
                tolranceHeight = (abs_main.getHeight() / 4);
                lay_width = abs_main.getWidth();
                lay_width_left = abs_main.getWidth() / 5;
                lay_width_right = lay_width_left * 4;
                setUpPlayer1();
            }
        });

        try{
            String path = "android.resource://" + getPackageName() + "/" + R.raw.shutterstock_video;
            videoView.setVideoURI(Uri.parse(path));
            videoView.start();

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    mp.setLooping(true);

                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        TaskInBackend taskInBackend = new TaskInBackend();
                        taskInBackend.execute();
                    }

                }
            });

        }catch (Exception e){
         e.printStackTrace();
        }


        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File f = new File(Environment
                        .getExternalStoragePublicDirectory(Environment
                                .DIRECTORY_DOWNLOADS) + "/video.mp4");
                Uri uri = Uri.parse("file://" + f.getAbsolutePath());
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("video/mp4");
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_by));
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(share, "Share video File"));
                finish();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setUpPlayer1() {
        listPlayer1 = new ArrayList<player>();


        heightDifference = cc.loadPrefInt("SingleLayoutHeight") - abs_main.getHeight();
        widthDifference = getHeightDifference(false, abs_main);


        Cursor cursor = null;
        cursor = dbcon.getTeamAnim();
        if (cursor.getCount() != 0 && cursor != null) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {

                player player_set = new player();
                player_set.setPlayer_id(Integer.parseInt(cursor.getString(0)));
                player_set.setPlayer_name(cursor.getString(1));
                player_set.setLeft_position(Integer.parseInt(cursor.getString(2)));
                player_set.setTop_position(Integer.parseInt(cursor.getString(3)));
                boolean onField = false;
                if (cursor.getInt(cursor.getColumnIndex("onField")) == 1) {
                    onField = true;
                }
                player_set.setOnField(onField);
                listPlayer1.add(player_set);

                if (i != cursor.getCount() - 1)
                    cursor.moveToNext();
            }
        }

        final int fraction_width = (int) (cc.loadPrefInt("SingleLayoutWidth") * 0.80) / 2;
        final int fraction_height = (int) ((cc.loadPrefInt("SingleLayoutHeight")) * 0.025);
        final int fraction_height_2 = (int) ((cc.loadPrefInt("SingleLayoutHeight")) * 0.1);


        for (int i = 0; i < listPlayer1.size(); i++) {

            final player team_player = listPlayer1.get(i);

            if (team_player.isOnField()) {
                final int left = getAbsoluteLeft(team_player.getLeft_position());
                final int top = getAbsoluteTop(team_player.getTop_position());

                if (team_player.isOnField()) {
                    final TextView textView = new TextView(GIFActivity.this);
                    textView.setText(team_player.getPlayer_name());
                    textView.setTextColor(getResources().getColor(R.color.colorWhite));
                    textView.setGravity(Gravity.CENTER_HORIZONTAL);

                    ImageView image = new ImageView(GIFActivity.this);
                    image.setImageResource(ImageAdapter.mThumbIds[cc.loadPrefInt("playerAnim_T_Shirt")]);
                    final LinearLayout linearLayout = new LinearLayout(this);
                    linearLayout.setGravity(Gravity.CENTER);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    linearLayout.addView(image);
                    linearLayout.addView(textView);
/*
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.measure(0, 0);
                            int tv_width = textView.getMeasuredWidth() / 2;

                            if (tv_width <= tolranceWidth) {
                                tv_width = tolranceWidth;
                            }

                            int height = top - heightDifference;
                            int width = left - tv_width;

                            if (height <= fraction_height_2 && width >= lay_width_right) {
                                tv_width = tv_width + (tolranceWidth * 2);
                            } else if (height <= fraction_height_2 && width <= lay_width_left) {
                                tv_width = tv_width - (tolranceWidth * 2);
                            }

                            AbsoluteLayout.LayoutParams params;
                            params = new AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT, AbsoluteLayout.LayoutParams.WRAP_CONTENT, (left - tv_width), (top - (heightDifference + fraction_height)));

                            abs_main.addView(linearLayout, params);
                        }
                    });*/

                    textView.measure(0, 0);
                    int tv_width = textView.getMeasuredWidth() / 2;

                    if (tv_width <= tolranceWidth) {
                        tv_width = tolranceWidth;
                    }

                    int height = top - heightDifference;
                    int width = left - tv_width;

                    if (height <= fraction_height_2 && width >= lay_width_right) {
                        tv_width = tv_width + (tolranceWidth * 2);
                    } else if (height <= fraction_height_2 && width <= lay_width_left) {
                        tv_width = tv_width - (tolranceWidth * 2);
                    }

                    AbsoluteLayout.LayoutParams params;
                    params = new AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT, AbsoluteLayout.LayoutParams.WRAP_CONTENT, (left - tv_width), (top - (heightDifference + fraction_height)));

                    abs_main.addView(linearLayout, params);

                }
            }
        }
    }


    private int getHeightDifference(boolean b, AbsoluteLayout ll_main) {

        int scr_height = 0;
        int difference = 0;
        int scr_width = 0;
        int lay_height = ll_main.getHeight();
        int lay_width = ll_main.getWidth();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        scr_height = displayMetrics.heightPixels;
        scr_width = displayMetrics.widthPixels;


        if (b) {
            difference = scr_height - lay_height;
        } else {
            difference = scr_width - lay_width;
        }

        return difference;
    }

    public int getAbsoluteLeft(int left) {
        int k = 0;
        int oldWidth = cc.loadPrefInt("SingleLayoutWidth");
//        int currentWidth = ll_team_1.getWidth() / 2;
        int currentWidth = abs_main.getWidth();
        k = (left * currentWidth) / oldWidth;
        return k;
    }

    public int getAbsoluteTop(int top) {
        int k = 0;
        int oldHeight = cc.loadPrefInt("SingleLayoutHeight");
        int currentHeight = abs_main.getHeight();
        k = (top * currentHeight) / oldHeight;
        return k;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private class MediaProjectionCallback extends MediaProjection.Callback {
        @Override
        public void onStop() {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaProjection = null;
            stopScreenSharing();
        }
    }

    private void stopScreenSharing() {
        try {
            if (mVirtualDisplay == null) {
                return;
            }
            mVirtualDisplay.release();
            destroyMediaProjection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void destroyMediaProjection() {
        try {
            if (mMediaProjection != null) {
                mMediaProjection.unregisterCallback(mMediaProjectionCallback);
                mMediaProjection.stop();
                mMediaProjection = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_CODE) {

            return;
        }
        if (resultCode != RESULT_OK) {
            Toast.makeText(this,
                    "Screen Cast Permission Denied", Toast.LENGTH_SHORT).show();
//            mToggleButton.setChecked(false);
            return;
        }

        try {
            mMediaProjectionCallback = new MediaProjectionCallback();
            mMediaProjection = mProjectionManager.getMediaProjection(resultCode, data);
            mMediaProjection.registerCallback(mMediaProjectionCallback, null);
            mVirtualDisplay = createVirtualDisplay();
            mMediaRecorder.start();
            SaveVideoInBackend saveVideoInBackend = new SaveVideoInBackend();
            saveVideoInBackend.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void shareScreen() {
        try {
            if (mMediaProjection == null) {
                startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
                return;
            }
            mVirtualDisplay = createVirtualDisplay();
            mMediaRecorder.start();

            SaveVideoInBackend saveVideoInBackend = new SaveVideoInBackend();
            saveVideoInBackend.execute();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    private VirtualDisplay createVirtualDisplay() {
        VirtualDisplay Display = null;

        try {

            Display =mMediaProjection.createVirtualDisplay("Recording Display", DISPLAY_WIDTH,
                    DISPLAY_HEIGHT, mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    mMediaRecorder.getSurface(), null, null);
        } catch (Exception e) {
                e.printStackTrace();
        }
        return Display ;
    }

    private void initRecorder() {
        try {

//            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mMediaRecorder.setOutputFile(Environment
                    .getExternalStoragePublicDirectory(Environment
                            .DIRECTORY_DOWNLOADS) + "/video.mp4");
            mMediaRecorder.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
//            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setVideoEncodingBitRate(512 * 1000);
            mMediaRecorder.setVideoFrameRate(30);
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            int orientation = ORIENTATIONS.get(rotation);
            mMediaRecorder.setOrientationHint(orientation);
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            mMediaRecorder.stop();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                if ((grantResults.length > 0) && (grantResults[0] +
                        grantResults[1]) == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(intent);
                }
                return;
            }
        }
    }


    public class TaskInBackend extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                initRecorder();
                shareScreen();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            CountDown();
            super.onPostExecute(aVoid);
        }
    }


    public class SaveVideoInBackend extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            CountDown();
            super.onPostExecute(aVoid);
        }
    }

    public void CountDown() {
        CountDownTimer mCountDownTimer = new CountDownTimer(6000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                try {
                    if(mMediaRecorder != null){
                        mMediaRecorder.stop();
                        mMediaRecorder.reset();
                    }
                    stopScreenSharing();
                    iv_share.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    iv_share.setVisibility(View.GONE);
                    e.printStackTrace();
                }

            }
        };
        mCountDownTimer.start();
    }
}
