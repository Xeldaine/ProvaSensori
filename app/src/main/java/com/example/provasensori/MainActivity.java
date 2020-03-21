package com.example.provasensori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    //sensor manager
    private SensorManager mSensorManager;

    // sensors
    private Sensor mSensorProximity;
    private Sensor mSensorLight;
    private Sensor mSensorSteps;
    private Sensor mSensorAccelerometer;
    private Sensor mSensorMagnetometer;
    private Sensor mSensorGyroscope;

    // TextViews to display current sensor values
    private TextView mTextSensorLight;
    private TextView mTextSensorProximity;
    private TextView mTextSteps;
    private TextView mTextAzimuth;
    private TextView mTextPitch;
    private TextView mTextRoll;
    private TextView mTextGyroscopeX;
    private TextView mTextGyroscopeY;
    private TextView mTextGyroscopeZ;

    //accelerometer and magnetometer data
    private float[] mAccelerometerData = new float[3];
    private float[] mMagnetometerData = new float[3];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList  = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        StringBuilder sensorText = new StringBuilder();
        for (Sensor currentSensor : sensorList ) {
            sensorText.append(currentSensor.getName()).append(
                    System.getProperty("line.separator"));
        }

        //text views
        mTextSensorProximity = findViewById(R.id.sensor_proximity);
        mTextSensorLight = findViewById(R.id.sensor_light);
        mTextSteps = findViewById(R.id.steps);
        mTextAzimuth = findViewById(R.id.azimuth);
        mTextPitch = findViewById(R.id.pitch);
        mTextRoll = findViewById(R.id.roll);
        mTextGyroscopeX = findViewById(R.id.gyroscope_x);
        mTextGyroscopeY = findViewById(R.id.gyroscope_y);
        mTextGyroscopeZ = findViewById(R.id.gyroscope_z);

        //sensors
        mSensorProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mSensorLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorSteps = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mSensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        //request permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        float currentValue = event.values[0];
        switch (sensorType) {
            case Sensor.TYPE_PROXIMITY:
                mTextSensorProximity.setText(getResources().getString(R.string.label_proximity, currentValue));
                break;
            case Sensor.TYPE_LIGHT:
                mTextSensorLight.setText(getResources().getString(R.string.label_light, currentValue));
                break;
            case Sensor.TYPE_STEP_COUNTER:
                mTextSteps.setText(getResources().getString(R.string.label_steps, currentValue));
                break;
            case Sensor.TYPE_ACCELEROMETER:
                mAccelerometerData = event.values.clone();
                computeOrientation();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mMagnetometerData = event.values.clone();
                computeOrientation();
                break;
            case Sensor.TYPE_GYROSCOPE:
                mTextGyroscopeX.setText(getResources().getString(R.string.gyroscope_x, event.values[0]));
                mTextGyroscopeY.setText(getResources().getString(R.string.gyroscope_y, event.values[1]));
                mTextGyroscopeZ.setText(getResources().getString(R.string.gyroscope_z, event.values[2]));
                break;
            default:
                // do nothing
        }
    }

    public void computeOrientation(){
        float[] rotationMatrix = new float[9];
        boolean rotationOK = SensorManager.getRotationMatrix(rotationMatrix,
                null, mAccelerometerData, mMagnetometerData);
        float orientationValues[] = new float[3];
        if (rotationOK) {
            SensorManager.getOrientation(rotationMatrix, orientationValues);
            float azimuth = orientationValues[0];
            float pitch = orientationValues[1];
            float roll = orientationValues[2];
            mTextAzimuth.setText(getResources().getString(
                    R.string.azimuth_format, azimuth));
            mTextPitch.setText(getResources().getString(
                    R.string.pitch_format, pitch));
            mTextRoll.setText(getResources().getString(
                    R.string.roll_format, roll));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Utils.isCallActive(this)) Log.i(TAG, "User is calling");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mSensorProximity != null) {
            mSensorManager.registerListener(this, mSensorProximity, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mSensorLight != null) {
            mSensorManager.registerListener(this, mSensorLight, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if(mSensorSteps != null){
            mSensorManager.registerListener(this, mSensorSteps, SensorManager.SENSOR_DELAY_UI);
        }
        if(mSensorAccelerometer != null){
            mSensorManager.registerListener(this, mSensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mSensorMagnetometer != null) {
            mSensorManager.registerListener(this, mSensorMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mSensorGyroscope != null) {
            mSensorManager.registerListener(this, mSensorGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this);
    }
}
