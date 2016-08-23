package com.oceanwing.at;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.oceanwing.at.model.Task;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RunnerActivity extends AppCompatActivity {

    private static final String TAG = RunnerActivity.class.getSimpleName();

    private RunnerActivity mContext;

    private MockingStateReceiver mMockingStateReceiver;

    @BindView(R.id.task_stop)
    FloatingActionButton mTaskStop;

    @OnClick(R.id.task_stop)
    void stop() {
        MockGPSService.stop(mContext);
        finish();
    }

    @BindView(R.id.task_pause)
    FloatingActionButton mTaskPause;

    @OnClick(R.id.task_pause)
    void pause() {
        boolean paused = !mTaskPause.isSelected();
        MockGPSService.setPaused(paused);
        setTaskPauseUI(paused);
    }

    @BindView(R.id.task_gps)
    FloatingActionButton mTaskGPS;

    @OnClick(R.id.task_gps)
    void setGPS() {
        boolean enabled = !mTaskGPS.isSelected();
        MockGPSService.setGPSEnabled(enabled);
        setTaskGPSUI(enabled);
    }

    @BindView(R.id.task_runner_console)
    EditText mTaskConsole;

    private String mTaskName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runner);
        ButterKnife.bind(this);

        getSupportActionBar().setElevation(0);

        setUI();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Task.EXTRA_TASK_NAME)) {
            mTaskName = intent.getStringExtra(Task.EXTRA_TASK_NAME);
        }

        registerReceiver();
    }

    private void setUI() {
        setTaskStopUI(MockGPSService.isCreated());
        setTaskPauseUI(MockGPSService.isPaused());
        setTaskGPSUI(MockGPSService.isGPSEnabled());
    }

    private void registerReceiver() {
        // The filter's action is BROADCAST_ACTION
        IntentFilter statusIntentFilter = new IntentFilter(BroadcastNotifier.BROADCAST_ACTION);
        // Sets the filter's category to DEFAULT
        statusIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        mMockingStateReceiver = new MockingStateReceiver();
        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(mMockingStateReceiver, statusIntentFilter);
    }


    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
        setUI();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
        // If the DownloadStateReceiver still exists, unregister it and set it to null
        if (mMockingStateReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mMockingStateReceiver);
            mMockingStateReceiver = null;
        }
    }


    public void setTaskStopUI(boolean created) {
        mTaskStop.setEnabled(created);
    }


    private void setTaskPauseUI(boolean paused) {
        mTaskPause.setSelected(paused);
        mTaskPause.setIcon(paused ? R.drawable.ic_play_arrow_white_24dp
                : R.drawable.ic_pause_white_24dp);
        mTaskPause.setColorNormalResId(paused ? R.color.amber700
                : R.color.cyan700);
    }

    private void setTaskGPSUI(boolean enabled) {
        mTaskGPS.setSelected(enabled);
        mTaskGPS.setIcon(enabled ? R.drawable.ic_gps_fixed_white_24dp
                : R.drawable.ic_gps_off_white_24dp);
        mTaskGPS.setColorNormalResId(enabled ? R.color.cyan700
                : R.color.amber700);
    }

    private class MockingStateReceiver extends BroadcastReceiver {

        private MockingStateReceiver() {
            // prevents instantiation by other packages.
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(BroadcastNotifier.EXTENDED_DATA_STATUS, BroadcastNotifier.STATE_ACTION_ERROR);
            int step = intent.getIntExtra(BroadcastNotifier.EXTENDED_DATA_STEP, 0);
            String log = intent.getStringExtra(BroadcastNotifier.EXTENDED_DATA_LOG);
            int len = mTaskConsole.getText().length();
            if (len > 1024 * 1024) {
                mTaskConsole.setText(""); // clear text
            }
            mTaskConsole.append(log + '\n');

        }
    }

}
