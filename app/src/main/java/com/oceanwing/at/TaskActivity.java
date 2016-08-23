package com.oceanwing.at;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.DecimalMax;
import com.mobsandgeeks.saripaar.annotation.DecimalMin;
import com.mobsandgeeks.saripaar.annotation.Max;
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.oceanwing.at.model.RoutingConfig;
import com.oceanwing.at.model.Task;
import com.oceanwing.at.model.Waypoint;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;

public class TaskActivity extends AppCompatActivity implements Validator.ValidationListener {

    private static final String TAG = TaskActivity.class.getSimpleName();

    private Context mContext;
    private Validator mValidator;

    private Task mTask;

    @BindView(R.id.task_name)
    @NotEmpty
    EditText mTaskName;

    @BindView(R.id.task_waypoint_origin_lat)
    @NotEmpty
    @DecimalMin(-90)
    @DecimalMax(90)
    EditText mTaskWaypointOriginLat;
    @BindView(R.id.task_waypoint_origin_lng)
    @NotEmpty
    @DecimalMin(-180)
    @DecimalMax(180)
    EditText mTaskWaypointOriginLng;
    @BindView(R.id.task_waypoint_origin_parking)
    EditText mTaskWaypointOriginParking;

    @BindView(R.id.task_waypoint_dest_lat)
    @NotEmpty
    @DecimalMin(-90)
    @DecimalMax(90)
    EditText mTaskWaypointDestLat;
    @BindView(R.id.task_waypoint_dest_lng)
    @NotEmpty
    @DecimalMin(-180)
    @DecimalMax(180)
    EditText mTaskWaypointDestLng;
    @BindView(R.id.task_waypoint_dest_parking)
    EditText mTaskWaypointDestParking;


    @BindView(R.id.task_api)
    Spinner mTaskAPI;
    @BindArray(R.array.taskAPIs)
    String[] mTaskAPIs;


    @OnItemSelected(R.id.task_api)
    void selectTaskAPI(Spinner taskAPI, int position) {
        mTask.getRoutingConfig().setAPI(RoutingConfig.API.valueOf(mTaskAPIs[position].toUpperCase()));
        Log.i(TAG, "selected EXTRA_API: " + mTask.getRoutingConfig().getAPI());
        // reset
        setTaskModeUI();
    }

    @BindView(R.id.task_transport)
    Spinner mTaskTransport;
    @BindArray(R.array.taskTransports)
    String[] mTaskTransports;

    @OnItemSelected(R.id.task_api)
    void selectTaskTransports(Spinner taskTransport, int position) {
        mTask.getRoutingConfig().setTransport(mTaskTransports[position]);
    }

    @BindView(R.id.task_mode)
    Spinner mTaskMode;
    @BindArray(R.array.taskHereModes)
    String[] mTaskHereModes;
    @BindArray(R.array.taskGoogleModes)
    String[] mTaskGoogleModes;

    @OnItemSelected(R.id.task_api)
    void selectTaskMode(Spinner taskMode, int position) {
        switch (mTask.getRoutingConfig().getAPI()) {
            case HERE:
                mTask.getRoutingConfig().setMode(mTaskHereModes[position]);
                break;
            case GOOGLE:
                mTask.getRoutingConfig().setMode(mTaskGoogleModes[position]);
                break;
        }
    }

    @BindView(R.id.task_avoid_tolls)
    CheckBox mTaskAvoidTolls;

    @OnClick(R.id.task_avoid_tolls)
    void setTaskAvoidTolls(CheckBox taskAvoidTolls) {
        mTask.getRoutingConfig().setAvoidTolls(taskAvoidTolls.isChecked());
    }

    @BindView(R.id.task_avoid_ferries)
    CheckBox mTaskAvoidFerries;

    @OnClick(R.id.task_avoid_ferries)
    void setTaskAvoidFerries(CheckBox taskAvoidFerries) {
        mTask.getRoutingConfig().setAvoidFerries(taskAvoidFerries.isChecked());
    }

    @BindView(R.id.task_avoid_highways)
    CheckBox mTaskAvoidHighways;

    @OnClick(R.id.task_avoid_highways)
    void setTaskAvoidHighways(Switch taskAvoidHighways) {
        mTask.getRoutingConfig().setAvoidHighways(taskAvoidHighways.isChecked());
    }

    @BindView(R.id.task_traffic)
    Switch mTaskTraffic;

    @OnClick(R.id.task_traffic)
    void setTaskTraffic(Switch taskTraffic) {
        mTask.getRoutingConfig().setTraffic(taskTraffic.isChecked());
    }


    @BindView(R.id.task_online)
    Switch mTaskOnline;

    @OnClick(R.id.task_online)
    void setTaskOnline(Switch taskOnline) {
        mTask.getRunnerConfig().setOnline(taskOnline.isChecked());
    }

    @BindView(R.id.task_speed)
    @NotEmpty
    @Min(10)
    @Max(200)
    EditText mTaskSpeed;

    @BindView(R.id.task_steady)
    Switch mTaskSteady;

    @OnClick(R.id.task_online)
    void setTaskSteady(Switch taskSteady) {
        mTask.getRunnerConfig().setSteady(taskSteady.isChecked());
    }

    @BindView(R.id.task_interval)
    @NotEmpty
    @Min(1)
    EditText mTaskUpdateInterval;

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

    @BindView(R.id.task_save)
    Button mTaskSave;

    @OnClick(R.id.task_save)
    void saveTask() {
        if (!mValidator.isValidating()) {
            mValidator.validate();
        }
    }

    @BindView(R.id.task_start)
    Button mTaskStart;

    @OnClick(R.id.task_start)
    void onTaskStartClick(Button taskStart) {
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
        setContentView(R.layout.activity_task);
        ButterKnife.bind(this);

        getSupportActionBar().setElevation(0);

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);

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
        boolean isNewTask = false;
        if (Task.ACTION_NEW.equals(intent.getAction())) {
            mTask = new Task();
            Log.i(TAG, String.format("new Task [%s]", mTask.getName()));
            isNewTask = true;

        } else if (Task.ACTION_OPEN.equals(intent.getAction())) {
            String taskName = intent.getStringExtra(Task.EXTRA_TASK_NAME);
            if (StringUtils.isNotBlank(taskName)) {
                Log.i(TAG, String.format("open Task [%s]", taskName));
                mTask = TaskFile.getTask(mContext, taskName);
                isNewTask = false;
            }
            if (mTask == null) {
                mTask = new Task();
                Log.i(TAG, String.format("failed to open Task [%s], new Task [%s]", taskName, mTask.getName()));
                isNewTask = true;

            }
        }
        mTaskStart.setEnabled(!isNewTask);
    }

    private void setUI() {
        mTaskName.setText(mTask.getName());
        setTaskWaypointsUI();

        setTaskAPIUI();
        setTaskTransportUI();
        setTaskModeUI();
        setTaskAvoidsUI();
        mTaskTraffic.setChecked(mTask.getRoutingConfig().isTraffic());

        mTaskOnline.setChecked(mTask.getRunnerConfig().isOnline());
        mTaskSpeed.setText(String.format("%.0f", mTask.getRunnerConfig().getSpeed()));
        mTaskSteady.setChecked(mTask.getRunnerConfig().isSteady());
        mTaskUpdateInterval.setText(String.valueOf(mTask.getRunnerConfig().getUpdateInterval()));

        setTaskRunUI();
    }

    private void setTaskWaypointsUI() {
        List<Waypoint> waypoints = mTask.getRoutingConfig().getWaypoints();
        if (waypoints != null) {
            if (waypoints.size() > 0) {
                Waypoint origin = waypoints.get(0);
                mTaskWaypointOriginLat.setText(String.format("%.6f", origin.getLatLng().getLat()));
                mTaskWaypointOriginLng.setText(String.format("%.6f", origin.getLatLng().getLng()));
                mTaskWaypointOriginParking.setText(String.valueOf(origin.getParking()));
            }
            if (waypoints.size() > 1) {
                Waypoint origin = waypoints.get(1);
                mTaskWaypointDestLat.setText(String.format("%.6f", origin.getLatLng().getLat()));
                mTaskWaypointDestLng.setText(String.format("%.6f", origin.getLatLng().getLng()));
                mTaskWaypointDestParking.setText(String.valueOf(origin.getParking()));
            }
        }
    }

    private void setTaskAPIUI() {
        ArrayAdapter<CharSequence> apiAdapter = ArrayAdapter.createFromResource(this, R.array.taskAPIs, android.R.layout.simple_spinner_item);
        apiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTaskAPI.setAdapter(apiAdapter);

        for (int i = 0; i < mTaskAPIs.length; i++) {
            if (mTaskAPIs[i].equalsIgnoreCase(mTask.getRoutingConfig().getAPI().value())) {
                mTaskAPI.setSelection(i);
                break;
            }
        }

    }

    private void setTaskTransportUI() {
        ArrayAdapter<CharSequence> taskTransportsAdapter = ArrayAdapter.createFromResource(this, R.array.taskTransports, android.R.layout.simple_spinner_item);
        taskTransportsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTaskTransport.setAdapter(taskTransportsAdapter);
        for (int i = 0; i < mTaskTransports.length; i++) {
            if (mTaskTransports[i].equalsIgnoreCase(mTask.getRoutingConfig().getTransport())) {
                mTaskTransport.setSelection(i);
                break;
            }
        }
    }

    private void setTaskModeUI() {
        ArrayAdapter<CharSequence> hereModeAdapter = ArrayAdapter.createFromResource(this, R.array.taskHereModes, android.R.layout.simple_spinner_item);
        hereModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> googleModeAdapter = ArrayAdapter.createFromResource(this, R.array.taskGoogleModes, android.R.layout.simple_spinner_item);
        googleModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        switch (mTask.getRoutingConfig().getAPI()) {
            case HERE:
                mTaskMode.setAdapter(hereModeAdapter);
                for (int i = 0; i < mTaskHereModes.length; i++) {
                    if (mTaskHereModes[i].equalsIgnoreCase(mTask.getRoutingConfig().getMode())) {
                        mTaskMode.setSelection(i);
                        break;
                    }
                }
                break;
            case GOOGLE:
                mTaskMode.setAdapter(googleModeAdapter);
                for (int i = 0; i < mTaskGoogleModes.length; i++) {
                    if (mTaskGoogleModes[i].equalsIgnoreCase(mTask.getRoutingConfig().getMode())) {
                        mTaskMode.setSelection(i);
                        break;
                    }
                }
                break;
        }
    }

    private void setTaskAvoidsUI() {
        mTaskAvoidTolls.setChecked(mTask.getRoutingConfig().isAvoidTolls());
        mTaskAvoidFerries.setChecked(mTask.getRoutingConfig().isAvoidFerries());
        mTaskAvoidHighways.setChecked(mTask.getRoutingConfig().isAvoidHighways());
    }


    private void setTaskRunUI() {
        mTaskRun.clearCheck();
        boolean checked = false;
        int count = mTaskRun.getChildCount();
        for (int i = 0; i < count; i++) {
            RadioButton child = (RadioButton) mTaskRun.getChildAt(i);
            if (mTask.getRunnerConfig().getRun().equalsIgnoreCase(child.getText().toString())) {
                mTaskRun.check(i);
                child.setChecked(true);
                checked = true;
                break;
            }
        }
        if (!checked) {
            mTaskRunN.setText(mTask.getRunnerConfig().getRun());
        }
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
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
    }

    @Override
    public void onValidationSucceeded() {
        mTaskStart.setEnabled(true);

        try {
            setTask();

            TaskFile.saveTask(mContext, mTask);

            Toast.makeText(this, getString(R.string.taskSaveSucc), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            finish();
        }
    }

    private void setTask() {
        mTask.setName(mTaskName.getText().toString());

        List<Waypoint> waypoints = new ArrayList<>();

        long originParking = 0;
        if (StringUtils.isNotBlank(mTaskWaypointOriginParking.getText().toString())) {
            originParking = Long.valueOf(mTaskWaypointOriginParking.getText().toString());
        }
        waypoints.add(new Waypoint(Double.valueOf(mTaskWaypointOriginLat.getText().toString()),
                Double.valueOf(mTaskWaypointOriginLng.getText().toString()),
                originParking));
        long destParking = 0;
        if (StringUtils.isNotBlank(mTaskWaypointDestParking.getText().toString())) {
            destParking = Long.valueOf(mTaskWaypointDestParking.getText().toString());
        }
        waypoints.add(new Waypoint(Double.valueOf(mTaskWaypointDestLat.getText().toString()),
                Double.valueOf(mTaskWaypointDestLng.getText().toString()),
                destParking));
        mTask.getRoutingConfig().setWaypoints(waypoints);

        mTask.getRunnerConfig().setSpeed(Float.valueOf(mTaskSpeed.getText().toString()));

        mTask.getRunnerConfig().setUpdateInterval(Long.valueOf(mTaskUpdateInterval.getText().toString()));

        String runN = mTaskRunN.getText().toString();
        if (StringUtils.isNoneBlank(runN)) {
            mTask.getRunnerConfig().setRun(runN);
        } else {
            mTask.getRunnerConfig().setRun(mTaskRunOnce.isChecked() ? mTaskRunOnce.getText().toString()
                    : mTaskRunForever.getText().toString());
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        mTaskStart.setEnabled(false);

        Toast.makeText(this, getString(R.string.taskSaveFail), Toast.LENGTH_SHORT).show();

        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

}
