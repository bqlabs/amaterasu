package com.camera.simplemjpeg;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.net.URI;

public class MjpegActivity extends AppCompatActivity {
    //-- Activity things
    private static final boolean DEBUG = false;
    private static final String TAG = "MJPEG";

    //-- Fullscreen properties
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int UI_ANIMATION_DELAY = 300;

    private View mContentView;
    private View mControlsView;
    private boolean mVisible;

    //-- Viewers
    private MjpegView left_eye_mv = null;
    private MjpegView right_eye_mv = null;
    String left_eye_URL;
    String right_eye_URL;

    //-- Stream settings (network and resolution)
    private static final int REQUEST_SETTINGS = 0;

    private int width = 640;
    private int height = 480;

    //-- Settings for the left eye
    private int left_eye_ip_ad1 = 192;
    private int left_eye_ip_ad2 = 168;
    private int left_eye_ip_ad3 = 2;
    private int left_eye_ip_ad4 = 1;
    private int left_eye_ip_port = 80;
    private String left_eye_ip_command = "?action=stream";
    private Boolean left_eye_invert_image = false;

    //-- Settings for the right eye
    private int right_eye_ip_ad1 = 192;
    private int right_eye_ip_ad2 = 168;
    private int right_eye_ip_ad3 = 2;
    private int right_eye_ip_ad4 = 1;
    private int right_eye_ip_port = 80;
    private String right_eye_ip_command = "?action=stream";
    private Boolean right_eye_invert_image = false;

    private boolean suspending = false;

    final Handler imErrorHandler = new Handler();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //-- Get Streamer properties
        SharedPreferences preferences = getSharedPreferences("SAVED_VALUES", MODE_PRIVATE);
        //-- Common
        width = preferences.getInt("width", width);
        height = preferences.getInt("height", height);
        //-- Left eye
        left_eye_ip_ad1 = preferences.getInt("left_eye_ip_ad1", left_eye_ip_ad1);
        left_eye_ip_ad2 = preferences.getInt("left_eye_ip_ad2", left_eye_ip_ad2);
        left_eye_ip_ad3 = preferences.getInt("left_eye_ip_ad3", left_eye_ip_ad3);
        left_eye_ip_ad4 = preferences.getInt("left_eye_ip_ad4", left_eye_ip_ad4);
        left_eye_ip_port = preferences.getInt("left_eye_ip_port", left_eye_ip_port);
        left_eye_ip_command = preferences.getString("left_eye_ip_command", left_eye_ip_command);
        left_eye_invert_image = preferences.getBoolean("left_eye_invert_image", left_eye_invert_image);

        StringBuilder sb = new StringBuilder();
        String s_http = "http://";
        String s_dot = ".";
        String s_colon = ":";
        String s_slash = "/";
        sb.append(s_http);
        sb.append(left_eye_ip_ad1);
        sb.append(s_dot);
        sb.append(left_eye_ip_ad2);
        sb.append(s_dot);
        sb.append(left_eye_ip_ad3);
        sb.append(s_dot);
        sb.append(left_eye_ip_ad4);
        sb.append(s_colon);
        sb.append(left_eye_ip_port);
        sb.append(s_slash);
        sb.append(left_eye_ip_command);
        left_eye_URL = new String(sb);

        //-- Right eye
        right_eye_ip_ad1 = preferences.getInt("right_eye_ip_ad1", right_eye_ip_ad1);
        right_eye_ip_ad2 = preferences.getInt("right_eye_ip_ad2", right_eye_ip_ad2);
        right_eye_ip_ad3 = preferences.getInt("right_eye_ip_ad3", right_eye_ip_ad3);
        right_eye_ip_ad4 = preferences.getInt("right_eye_ip_ad4", right_eye_ip_ad4);
        right_eye_ip_port = preferences.getInt("right_eye_ip_port", right_eye_ip_port);
        right_eye_ip_command = preferences.getString("right_eye_ip_command", right_eye_ip_command);
        right_eye_invert_image = preferences.getBoolean("right_eye_invert_image", right_eye_invert_image);

        StringBuilder r_sb = new StringBuilder();
        r_sb.append(s_http);
        r_sb.append(right_eye_ip_ad1);
        r_sb.append(s_dot);
        r_sb.append(right_eye_ip_ad2);
        r_sb.append(s_dot);
        r_sb.append(right_eye_ip_ad3);
        r_sb.append(s_dot);
        r_sb.append(right_eye_ip_ad4);
        r_sb.append(s_colon);
        r_sb.append(right_eye_ip_port);
        r_sb.append(s_slash);
        r_sb.append(right_eye_ip_command);
        right_eye_URL = new String(r_sb);

        //-- Set Activity layout
        setContentView(R.layout.main);

        //-- Set left and right eye viewers
        left_eye_mv = (MjpegView) findViewById(R.id.left_eye_mv);
        if (left_eye_mv != null) {
            left_eye_mv.setResolution(width, height);
        }

        right_eye_mv = (MjpegView) findViewById(R.id.right_eye_mv);
        if (right_eye_mv != null) {
            right_eye_mv.setResolution(width, height);
        }

        //--  Set title
        setTitle(R.string.title_connecting);

        //-- Setup fullscreen things
        mVisible = true;
        mControlsView = null;
        //mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        //-- Call streamer functions
        new DoRead().execute(left_eye_URL, "left", left_eye_invert_image.toString());
        new DoRead().execute(right_eye_URL, "right", right_eye_invert_image.toString());
    }

    public void onResume() {
        if (DEBUG) Log.d(TAG, "onResume()");
        super.onResume();
        if (left_eye_mv != null && right_eye_mv != null) {
            if (suspending) {
                new DoRead().execute(left_eye_URL, "left", left_eye_invert_image.toString());
                new DoRead().execute(right_eye_URL, "right", right_eye_invert_image.toString());
                suspending = false;
            }
        }

    }

    public void onStart() {
        if (DEBUG) Log.d(TAG, "onStart()");
        super.onStart();
    }

    public void onPause() {
        if (DEBUG) Log.d(TAG, "onPause()");
        super.onPause();
        if (left_eye_mv != null) {
            if (left_eye_mv.isStreaming()) {
                left_eye_mv.stopPlayback();
                suspending = true;
            }
        }
        if (right_eye_mv != null) {
            if (right_eye_mv.isStreaming()) {
                right_eye_mv.stopPlayback();
                suspending = true;
            }
        }
    }

    public void onStop() {
        if (DEBUG) Log.d(TAG, "onStop()");
        super.onStop();
    }

    public void onDestroy() {
        if (DEBUG) Log.d(TAG, "onDestroy()");

        if (left_eye_mv != null) {
            left_eye_mv.freeCameraMemory();
        }

        if (right_eye_mv != null) {
            right_eye_mv.freeCameraMemory();
        }

        super.onDestroy();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.layout.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent settings_intent = new Intent(MjpegActivity.this, SettingsActivity.class);
                //-- Common
                settings_intent.putExtra("width", width);
                settings_intent.putExtra("height", height);
                //-- Left eye
                settings_intent.putExtra("left_eye_ip_ad1", left_eye_ip_ad1);
                settings_intent.putExtra("left_eye_ip_ad2", left_eye_ip_ad2);
                settings_intent.putExtra("left_eye_ip_ad3", left_eye_ip_ad3);
                settings_intent.putExtra("left_eye_ip_ad4", left_eye_ip_ad4);
                settings_intent.putExtra("left_eye_ip_port", left_eye_ip_port);
                settings_intent.putExtra("left_eye_ip_command", left_eye_ip_command);
                settings_intent.putExtra("left_eye_invert_image", left_eye_invert_image);
                //-- Right eye
                settings_intent.putExtra("right_eye_ip_ad1", right_eye_ip_ad1);
                settings_intent.putExtra("right_eye_ip_ad2", right_eye_ip_ad2);
                settings_intent.putExtra("right_eye_ip_ad3", right_eye_ip_ad3);
                settings_intent.putExtra("right_eye_ip_ad4", right_eye_ip_ad4);
                settings_intent.putExtra("right_eye_ip_port", right_eye_ip_port);
                settings_intent.putExtra("right_eye_ip_command", right_eye_ip_command);
                settings_intent.putExtra("right_eye_invert_image", right_eye_invert_image);
                Log.i(TAG, "Request configuration parameters");
                startActivityForResult(settings_intent, REQUEST_SETTINGS);
                return true;
        }
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SETTINGS:
                if (resultCode == Activity.RESULT_OK) {
                    //-- Common
                    width = data.getIntExtra("width", width);
                    height = data.getIntExtra("height", height);
                    //-- Left eye
                    left_eye_ip_ad1 = data.getIntExtra("left_eye_ip_ad1", left_eye_ip_ad1);
                    left_eye_ip_ad2 = data.getIntExtra("left_eye_ip_ad2", left_eye_ip_ad2);
                    left_eye_ip_ad3 = data.getIntExtra("left_eye_ip_ad3", left_eye_ip_ad3);
                    left_eye_ip_ad4 = data.getIntExtra("left_eye_ip_ad4", left_eye_ip_ad4);
                    left_eye_ip_port = data.getIntExtra("left_eye_ip_port", left_eye_ip_port);
                    left_eye_ip_command = data.getStringExtra("left_eye_ip_command");
                    left_eye_invert_image = data.getBooleanExtra("left_eye_invert_image", left_eye_invert_image);
                    //-- Right eye
                    right_eye_ip_ad1 = data.getIntExtra("right_eye_ip_ad1", right_eye_ip_ad1);
                    right_eye_ip_ad2 = data.getIntExtra("right_eye_ip_ad2", right_eye_ip_ad2);
                    right_eye_ip_ad3 = data.getIntExtra("right_eye_ip_ad3", right_eye_ip_ad3);
                    right_eye_ip_ad4 = data.getIntExtra("right_eye_ip_ad4", right_eye_ip_ad4);
                    right_eye_ip_port = data.getIntExtra("right_eye_ip_port", right_eye_ip_port);
                    right_eye_ip_command = data.getStringExtra("right_eye_ip_command");
                    right_eye_invert_image = data.getBooleanExtra("right_eye_invert_image", right_eye_invert_image);
                    if (left_eye_mv != null) {
                        left_eye_mv.setResolution(width, height);
                    }
                    if (right_eye_mv != null) {
                        right_eye_mv.setResolution(width, height);
                    }

                    Log.i(TAG, "Received configuration parameters");

                    SharedPreferences preferences = getSharedPreferences("SAVED_VALUES", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    //-- Common
                    editor.putInt("width", width);
                    editor.putInt("height", height);
                    //-- Left eye
                    editor.putInt("left_eye_ip_ad1", left_eye_ip_ad1);
                    editor.putInt("left_eye_ip_ad2", left_eye_ip_ad2);
                    editor.putInt("left_eye_ip_ad3", left_eye_ip_ad3);
                    editor.putInt("left_eye_ip_ad4", left_eye_ip_ad4);
                    editor.putInt("left_eye_ip_port", left_eye_ip_port);
                    editor.putString("left_eye_ip_command", left_eye_ip_command);
                    editor.putBoolean("left_eye_invert_image", left_eye_invert_image);
                    //-- Right eye
                    editor.putInt("right_eye_ip_ad1", right_eye_ip_ad1);
                    editor.putInt("right_eye_ip_ad2", right_eye_ip_ad2);
                    editor.putInt("right_eye_ip_ad3", right_eye_ip_ad3);
                    editor.putInt("right_eye_ip_ad4", right_eye_ip_ad4);
                    editor.putInt("right_eye_ip_port", right_eye_ip_port);
                    editor.putString("right_eye_ip_command", right_eye_ip_command);
                    editor.putBoolean("right_eye_invert_image", right_eye_invert_image);

                    editor.commit();

                    Log.i(TAG, "Saved configuration parameters");

                    new RestartApp().execute();
                }
                break;
        }
    }

    public void setImageError() {
        imErrorHandler.post(new Runnable() {
            @Override
            public void run() {
                setTitle(R.string.title_imageerror);
                return;
            }
        });


    }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        if (mControlsView != null) {
            mControlsView.setVisibility(View.GONE);
        }
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            if (mControlsView != null) {
                mControlsView.setVisibility(View.VISIBLE);
            }
        }
    };

    private final Handler mHideHandler = new Handler();
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    public class DoRead extends AsyncTask<String, Void, MjpegInputStream> {

        private String eye = null;
        private Boolean invert = false;

        protected MjpegInputStream doInBackground(String... params) {
            //-- Get params
            String url = params[0];
            eye = params[1];
            invert = Boolean.parseBoolean(params[2]);

            //TODO: if camera has authentication deal with it and don't just not work
            HttpResponse res = null;
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpParams httpParams = httpclient.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 5 * 1000);
            HttpConnectionParams.setSoTimeout(httpParams, 5 * 1000);
            if (DEBUG) Log.d(TAG, "1. Sending http request");
            try {
                res = httpclient.execute(new HttpGet(URI.create(url)));
                if (DEBUG)
                    Log.d(TAG, "2. Request finished, status = " + res.getStatusLine().getStatusCode());
                if (res.getStatusLine().getStatusCode() == 401) {
                    //You must turn off camera User Access Control before this will work
                    return null;
                }
                return new MjpegInputStream(res.getEntity().getContent());
            } catch (ClientProtocolException e) {
                if (DEBUG) {
                    e.printStackTrace();
                    Log.d(TAG, "Request failed-ClientProtocolException", e);
                }
                //Error connecting to camera
            } catch (IOException e) {
                if (DEBUG) {
                    e.printStackTrace();
                    Log.d(TAG, "Request failed-IOException", e);
                }
                //Error connecting to camera
            }
            return null;
        }

        protected void onPostExecute(MjpegInputStream result) {
            if (eye.equals("left")) {
                Log.i(TAG, "Left eye frame");
                left_eye_mv.setSource(result);
                if (result != null) {
                    result.setSkip(1);
                    setTitle(R.string.app_name);
                } else {
                    setTitle(R.string.title_disconnected);
                }
                left_eye_mv.setDisplayMode(MjpegView.SIZE_BEST_FIT);
                left_eye_mv.showFps(false);
                left_eye_mv.setInverted(left_eye_invert_image);
            }
            else if (eye.equals("right")) {
                Log.i(TAG, "Right eye frame");
                right_eye_mv.setSource(result);
                if (result != null) {
                    result.setSkip(1);
                    setTitle(R.string.app_name);
                } else {
                    setTitle(R.string.title_disconnected);
                }
                right_eye_mv.setDisplayMode(MjpegView.SIZE_BEST_FIT);
                right_eye_mv.showFps(false);
                right_eye_mv.setInverted(right_eye_invert_image);
            }
        }
    }

    public class RestartApp extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... v) {
            MjpegActivity.this.finish();
            return null;
        }

        protected void onPostExecute(Void v) {
            startActivity((new Intent(MjpegActivity.this, MjpegActivity.class)));
        }
    }
}
