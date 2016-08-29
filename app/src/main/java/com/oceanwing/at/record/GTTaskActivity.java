package com.oceanwing.at.record;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.oceanwing.at.MockGPSService;
import com.oceanwing.at.R;
import com.oceanwing.at.RunnerActivity;
import com.oceanwing.at.TaskFile;
import com.oceanwing.at.model.Position;
import com.oceanwing.at.model.RunnerConfig;
import com.oceanwing.at.model.Task;

import org.apache.commons.lang3.StringUtils;
import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class GTTaskActivity extends AppCompatActivity {

    private static final String TAG = GTTaskActivity.class.getSimpleName();

    private GTTaskActivity mContext;

    private Task mTask;

    @BindView(R.id.task_name)
    TextView mTaskName;

    @BindView(R.id.task_position_range)
    RangeSeekBar mTaskPositionRange;

    @BindView(R.id.task_run)
    RadioGroup mTaskRun;

    @BindView(R.id.task_run_once)
    RadioButton mTaskRunOnce;

    @BindView(R.id.task_run_forever)
    RadioButton mTaskRunForever;

    @OnClick({R.id.task_run_once, R.id.task_run_forever})
    void checkTaskRun(RadioButton button) {
        boolean checked = button.isChecked();
        switch (button.getId()) {
            case R.id.task_run_once:
                if (checked) {
                    mTask.getRunnerConfig().setRun(mTaskRunOnce.getText().toString());
                }
                break;
            case R.id.task_run_forever:
                if (checked) {
                    mTask.getRunnerConfig().setRun(mTaskRunForever.getText().toString());
                }
                break;
        }
    }

    @BindView(R.id.task_run_n)
    EditText mTaskRunN;

    @OnTextChanged(value = R.id.task_run_n, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterTaskRunNChanged(Editable taskRunN) {
        if (StringUtils.isNotBlank(taskRunN.toString())) {
            mTaskRun.clearCheck();
            mTask.getRunnerConfig().setRun(taskRunN.toString());
        } else {
            mTaskRunOnce.setChecked(true);
            mTask.getRunnerConfig().setRun(mTaskRunOnce.getText().toString());
        }
    }

    @BindView(R.id.task_start)
    Button mTaskStart;

    @OnClick(R.id.task_start)
    void onTaskStartClick(Button taskStart) {
        int max = mTaskPositionRange.getSelectedMaxValue().intValue();
        int min = mTaskPositionRange.getSelectedMinValue().intValue();
        RunnerConfig runnerConfig = mTask.getRunnerConfig();
        List<Position> positions = runnerConfig.getPositions();
        runnerConfig.setPositions(positions.subList(min - 1, max));
        mTask.setRunnerConfig(runnerConfig);

        MockGPSService.start(mContext, mTask);

        Intent intent = new Intent(this, RunnerActivity.class);
        intent.putExtra(Task.EXTRA_TASK_NAME, mTask.getName());
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        mContext = this;
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_gt);
        ButterKnife.bind(this);

        getSupportActionBar().setElevation(0);

        try {
            getTask();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            finish();
            return;
        }

        setUI();
    }

    private void getTask() throws IOException {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Task.EXTRA_TASK_NAME)) {
            String taskName = intent.getStringExtra(Task.EXTRA_TASK_NAME);
            if (StringUtils.isNotBlank(taskName)) {
                mTask = new Task();
                mTask.setName(taskName);
                mTask.setType(Task.Type.RECORD);
                mTask.setRoutingConfig(null);
                RunnerConfig runnerConfig = new RunnerConfig();
                runnerConfig.setOnline(false);
                runnerConfig.setSteady(false);
                runnerConfig.setUpdateInterval(1);
                List<Position> positions = TaskFile.parseGTFile(mContext, taskName);
                runnerConfig.setPositions(positions);
                mTask.setRunnerConfig(runnerConfig);
            }
        }
    }

    private void setUI() {
        if (mTask != null && mTask.getRunnerConfig() != null &&
                mTask.getRunnerConfig().getPositions() != null) {
            int size = mTask.getRunnerConfig().getPositions().size();
            if (size > 1) {
                mTaskName.setText(mTask.getName());
                mTaskPositionRange.setEnabled(true);
                mTaskPositionRange.setRangeValues(1, size);
                mTaskStart.setEnabled(true);
            }
        }
    }
}
