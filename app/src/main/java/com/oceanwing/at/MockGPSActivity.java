package com.oceanwing.at;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MockGPSActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "MockGPSActivity";

    private Context mContext;

    private MockingStateReceiver mMockingStateReceiver;

    private SharedPreferences mSharedPreferences;

    private SharedPreferences.Editor mSharedPreferencesEditor;

    private Spinner mAPISpinner = null;
    private String mAPI;

    public RadioGroup mRunRadioGroup;
    public RadioButton mRunOnceRadio, mRunForeverRadio;
    private String mRun;

    private EditText mSpeedEditText = null;

    private EditText mOriginLatEditText = null;
    private EditText mOriginLngEditText = null;
    private EditText mDestLatEditText = null;
    private EditText mDestLngEditText = null;

    private Button mStartBtn = null;
    private Button mStopBtn = null;

    private EditText mConsoleEditText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        mContext = this;
        super.onCreate(savedInstanceState);

        if (MockGPSService.ACTION_START.equals(getIntent().getAction())) {
            Uri data = getIntent().getData();
            if (data != null) {
                startByIntent(data);
                finish();
                return;
            }
        } else if (MockGPSService.ACTION_STOP.equals(getIntent().getAction())) {
            stopByIntent();
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        mSharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        mSharedPreferencesEditor = mSharedPreferences.edit();

        // The filter's action is BROADCAST_ACTION
        IntentFilter statusIntentFilter = new IntentFilter(BroadcastNotifier.BROADCAST_ACTION);
        // Sets the filter's category to DEFAULT
        statusIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        mMockingStateReceiver = new MockingStateReceiver();
        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(mMockingStateReceiver, statusIntentFilter);

        mAPISpinner = (Spinner) findViewById(R.id.api_sp);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.apis, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAPISpinner.setAdapter(adapter);
        mAPISpinner.setOnItemSelectedListener(this);

        mRunRadioGroup = (RadioGroup) findViewById(R.id.run_rg);
        mRunOnceRadio = (RadioButton) findViewById(R.id.run_once_rb);
        mRunForeverRadio = (RadioButton) findViewById(R.id.run_forever_rb);
        mRunRadioGroup.setOnCheckedChangeListener(this);

        mSpeedEditText = (EditText) findViewById(R.id.speed_et);
        mOriginLatEditText = (EditText) findViewById(R.id.origin_lat_et);
        mOriginLngEditText = (EditText) findViewById(R.id.origin_lng_et);
        mDestLatEditText = (EditText) findViewById(R.id.dest_lat_et);
        mDestLngEditText = (EditText) findViewById(R.id.dest_lng_et);

        mSpeedEditText.addTextChangedListener(
                new RangeTextValidator(mSpeedEditText, 0, 200, false, false, false, getString(R.string.speed)));
        mOriginLatEditText.addTextChangedListener(
                new RangeTextValidator(mOriginLatEditText, -90, 90, false, false, false, getString(R.string.latitude)));
        mOriginLngEditText.addTextChangedListener(
                new RangeTextValidator(mOriginLngEditText, -180, 180, false, false, false, getString(R.string.longitude)));
        mDestLatEditText.addTextChangedListener(
                new RangeTextValidator(mDestLatEditText, -90, 90, false, false, false, getString(R.string.latitude)));
        mDestLngEditText.addTextChangedListener(
                new RangeTextValidator(mDestLngEditText, -180, 180, false, false, false, getString(R.string.longitude)));

        mAPISpinner.setSelection(mSharedPreferences.getInt(MockGPSService.API, 0));
        mRunRadioGroup.check(mSharedPreferences.getInt(MockGPSService.RUN, R.id.run_once_rb));
        mSpeedEditText.setText(mSharedPreferences.getString(MockGPSService.SPEED, ""));
        mOriginLatEditText.setText(mSharedPreferences.getString(MockGPSService.ORIGIN_LAT, ""));
        mOriginLngEditText.setText(mSharedPreferences.getString(MockGPSService.ORIGIN_LNG, ""));
        mDestLatEditText.setText(mSharedPreferences.getString(MockGPSService.DEST_LAT, ""));
        mDestLngEditText.setText(mSharedPreferences.getString(MockGPSService.DEST_LNG, ""));

        mSpeedEditText.setSelection(mSpeedEditText.getText().length());
        mOriginLatEditText.setSelection(mOriginLatEditText.getText().length());
        mOriginLngEditText.setSelection(mOriginLngEditText.getText().length());
        mDestLatEditText.setSelection(mDestLatEditText.getText().length());
        mDestLngEditText.setSelection(mDestLngEditText.getText().length());

        mStartBtn = (Button) findViewById(R.id.start_btn);
        mStartBtn.setOnClickListener(this);
        mStopBtn = (Button) findViewById(R.id.stop_btn);
        mStopBtn.setEnabled(false);
        mStopBtn.setOnClickListener(this);

        mConsoleEditText = (EditText) findViewById(R.id.console_et);
    }

    private void startByIntent(Uri data) {
        Log.i(TAG, "startByIntent Uri: " + data.toString());
        // mockgps://com.oceanwing.at/path?api=HERE&speed=60.0&run=Once&origin=37.7256676,-122.4496459&destination=36.1633689,-115.1444786
        String scheme = data.getScheme();
        if (!getString(R.string.scheme).equals(scheme)) {
            return;
        }
        String host = data.getHost();
        if (!getString(R.string.host).equals(host)) {
            return;
        }
        String api = data.getQueryParameter("api");
        if (!getString(R.string.api_here).equals(api) && !getString(R.string.api_google).equals(api)) {
            return;
        }
        String speedStr = data.getQueryParameter("speed");
        float speed;
        try {
            speed = Float.parseFloat(speedStr);
        } catch (NumberFormatException e) {
            return;
        }
        if (speed <= 0 || speed >= 200) {
            return;
        }
        String run = data.getQueryParameter("run");
        if (!"".equals(run) && !getString(R.string.run_once).equals(run) && !getString(R.string.run_forever).equals(run)) {
            return;
        }
        if ("".equals(run)) {
            run = getString(R.string.run_once);
        }
        String origin = data.getQueryParameter("origin");
        String[] latLng = TextUtils.split(origin, ",");
        if (latLng.length != 2) {
            return;
        }
        double originLat, originLng;
        try {
            originLat = Double.parseDouble(latLng[0]);
            originLng = Double.parseDouble(latLng[1]);
        } catch (NumberFormatException e) {
            return;
        }
        if (originLat <= -90 || originLat >= 90 || originLng <= -180 && originLng >= 180) {
            return;
        }

        String destination = data.getQueryParameter("destination");
        latLng = TextUtils.split(destination, ",");
        if (latLng.length != 2) {
            return;
        }
        double destLat, destLng;
        try {
            destLat = Double.parseDouble(latLng[0]);
            destLng = Double.parseDouble(latLng[1]);
        } catch (NumberFormatException e) {
            return;
        }
        if (destLat <= -90 || destLat >= 90 || destLng <= -180 && destLng >= 180) {
            return;
        }

        MockGPSService.start(mContext, api, run, originLat, originLng, destLat, destLng, speed);
    }

    private void stopByIntent() {
        Log.i(TAG, "stopByIntent");
        MockGPSService.stop(mContext);
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

        // If the DownloadStateReceiver still exists, unregister it and set it to null
        if (mMockingStateReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mMockingStateReceiver);
            mMockingStateReceiver = null;
        }

        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_btn:
                if (TextUtils.isEmpty(mRun)) {
                    RadioButton checkedBtn = (RadioButton) findViewById(mRunRadioGroup.getCheckedRadioButtonId());
                    mRun = String.valueOf(checkedBtn.getText());
                }
                float speed = 60;
                double originLat = 37.725715, originLng = -122.449539, destLat = 37.792695, destLng = -122.404436;
                String speedStr = mSpeedEditText.getText().toString();
                if (!TextUtils.isEmpty(speedStr)) {
                    speed = Float.valueOf(speedStr).floatValue();
                }
                String originLatStr = mOriginLatEditText.getText().toString();
                if (!TextUtils.isEmpty(originLatStr)) {
                    originLat = Double.valueOf(originLatStr).doubleValue();
                }
                String originLngStr = mOriginLngEditText.getText().toString();
                if (!TextUtils.isEmpty(originLngStr)) {
                    originLng = Double.valueOf(originLngStr).doubleValue();
                }
                String destLatStr = mDestLatEditText.getText().toString();
                if (!TextUtils.isEmpty(destLatStr)) {
                    destLat = Double.valueOf(destLatStr).doubleValue();
                }
                String destLngStr = mDestLngEditText.getText().toString();
                if (!TextUtils.isEmpty(destLngStr)) {
                    destLng = Double.valueOf(destLngStr).doubleValue();
                }
                mSharedPreferencesEditor.putInt(MockGPSService.API, mAPISpinner.getSelectedItemPosition());
                mSharedPreferencesEditor.putInt(MockGPSService.RUN, mRunRadioGroup.getCheckedRadioButtonId());
                mSharedPreferencesEditor.putString(MockGPSService.SPEED, String.valueOf(speed));
                mSharedPreferencesEditor.putString(MockGPSService.ORIGIN_LAT, String.valueOf(originLat));
                mSharedPreferencesEditor.putString(MockGPSService.ORIGIN_LNG, String.valueOf(originLng));
                mSharedPreferencesEditor.putString(MockGPSService.DEST_LAT, String.valueOf(destLat));
                mSharedPreferencesEditor.putString(MockGPSService.DEST_LNG, String.valueOf(destLng));
                mSharedPreferencesEditor.commit();

                MockGPSService.start(mContext, mAPI, mRun, originLat, originLng, destLat, destLng, speed);

                setMockingView(true);
                break;
            case R.id.stop_btn:
                MockGPSService.stop(mContext);
                setMockingView(false);
                break;
            default:
                break;
        }
    }

    private void setMockingView(boolean isMocking) {
        mAPISpinner.setEnabled(!isMocking);
        mRunRadioGroup.setEnabled(!isMocking);
        mRunOnceRadio.setEnabled(!isMocking);
        mRunForeverRadio.setEnabled(!isMocking);
        mSpeedEditText.setEnabled(!isMocking);
        mOriginLatEditText.setEnabled(!isMocking);
        mOriginLngEditText.setEnabled(!isMocking);
        mDestLatEditText.setEnabled(!isMocking);
        mDestLngEditText.setEnabled(!isMocking);
        mStartBtn.setEnabled(!isMocking);
        mStopBtn.setEnabled(isMocking);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String[] apis = getResources().getStringArray(R.array.apis);
        mAPI = apis[pos];
        Log.i(TAG, "API: " + mAPI);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.e(TAG, "API: NothingSelected");
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedID) {
        switch (checkedID) {
            case R.id.run_forever_rb:
                mRun = String.valueOf(mRunForeverRadio.getText());
                break;
            case R.id.run_once_rb:
            default:
                mRun = String.valueOf(mRunOnceRadio.getText());
                break;
        }
    }

    private class MockingStateReceiver extends BroadcastReceiver {

        private MockingStateReceiver() {
            // prevents instantiation by other packages.
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            int len = mConsoleEditText.getText().length();
            if (len > 1024 * 1024) {
                mConsoleEditText.setText(""); // clear text
            }
            int status = intent.getIntExtra(BroadcastNotifier.EXTENDED_DATA_STATUS, BroadcastNotifier.STATE_ACTION_ERROR);
            String log = intent.getStringExtra(BroadcastNotifier.EXTENDED_STATUS_LOG);
            mConsoleEditText.append(log + '\n');
            setMockingView(true);
            switch (status) {
                case BroadcastNotifier.STATE_ACTION_STARTED:
                    Log.i(TAG, "State: STARTED");
                    break;
                case BroadcastNotifier.STATE_ACTION_CONNECTING:
                    Log.i(TAG, "State: CONNECTING");
                    break;
                case BroadcastNotifier.STATE_ACTION_PARSING:
                    Log.i(TAG, "State: PARSING");
                    break;
                case BroadcastNotifier.STATE_ACTION_MOCKING:
                    Log.i(TAG, "State: MOCKING");
                    break;
                case BroadcastNotifier.STATE_ACTION_COMPLETE:
                    Log.i(TAG, "State: COMPLETE");
                    setMockingView(false);
                    break;
                case BroadcastNotifier.STATE_ACTION_ERROR:
                    Log.i(TAG, "State: ERROR");
                default:
                    setMockingView(false);
                    break;
            }
        }
    }

    private class RangeTextValidator extends TextValidator {

        private double mMin, mMax;
        private boolean mIsEqualMin, mIsEqualMax;
        private boolean mIsRequired;
        private String mLabel;

        public RangeTextValidator(TextView textView, double min, double max, boolean isEqualMin, boolean isEqualMax, boolean isRequired, String label) {
            super(textView);
            this.mMin = min;
            this.mMax = max;
            this.mIsEqualMin = isEqualMin;
            this.mIsEqualMax = isEqualMax;
            this.mIsRequired = isRequired;
            this.mLabel = label;
        }

        @Override
        public void validate(TextView textView, String text) {
            if (TextUtils.isEmpty(text)) {
                if (mIsRequired) {
                    Toast.makeText(mContext,
                            String.format(getString(R.string.validation_required), mLabel),
                            Toast.LENGTH_LONG).show();
                }
            } else {
                double value;
                try {
                    value = Double.valueOf(text);
                } catch (NumberFormatException e) {
                    setStartable(false);
                    return;
                }
                if (!((value > mMin || (mIsEqualMin && value == mMin)) && (value < mMax || (mIsEqualMax && value == mMax)))) {
                    setStartable(false);
                    textView.setText("");
                    Toast.makeText(mContext,
                            String.format(getString(R.string.validation_range), mLabel, mMin, mMax),
                            Toast.LENGTH_LONG).show();
                } else {
                    setStartable(true);
                }
            }
        }
    }

    private void setStartable(boolean canStart) {
        if (mStartBtn != null) {
            mStartBtn.setEnabled(canStart);
        }
    }

}
