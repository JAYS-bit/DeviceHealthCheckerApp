package com.example.mobotestapp.sensorTesting;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mobotestapp.R;
import com.example.mobotestapp.databinding.ActivityProximitySensorBinding;
import com.example.mobotestapp.reportData.ReportCard;

public class ProximitySensorActivity extends AppCompatActivity implements SensorEventListener {

    ActivityProximitySensorBinding binding;
    int initialWidth, initialHeight;
    int enlargedWidth, enlargedHeight;
    boolean isEnlarged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProximitySensorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialWidth = binding.boxTest.getLayoutParams().width;
        initialHeight = binding.boxTest.getLayoutParams().height;
        enlargedWidth = initialWidth * 2; // Double the size
        enlargedHeight = initialHeight * 2; // Double the size

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (sensorManager != null) {
            Sensor proxySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

            if (proxySensor != null) {
                sensorManager.registerListener(this, proxySensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] <= 0 && !isEnlarged) {
                ReportCard.sensorResult=true;
                // Object (like your hand) is detected, enlarge the view
                isEnlarged = true;
                binding.boxTest.setLayoutParams(new LinearLayout.LayoutParams(enlargedWidth, enlargedHeight));
                Toast.makeText(this, "Proximity Working fine ", Toast.LENGTH_SHORT).show();
            } else if (event.values[0] > 0 && isEnlarged) {
                // No object is detected, revert to the initial size
                isEnlarged = false;
                binding.boxTest.setLayoutParams(new LinearLayout.LayoutParams(initialWidth, initialHeight));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing
    }
}

