package com.example.mobotestapp.sensorTesting;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mobotestapp.R;
import com.example.mobotestapp.databinding.ActivityAccelerometerBinding;
import com.example.mobotestapp.reportData.ReportCard;
import com.google.firebase.database.core.Repo;

public class AccelerometerActivity extends AppCompatActivity implements SensorEventListener {

    ActivityAccelerometerBinding binding;
    private boolean showToast = true;
    float angle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAccelerometerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if(sensorManager!=null){

            Sensor accleroSensor =sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            if(accleroSensor!=null){
                sensorManager.registerListener(this,accleroSensor,SensorManager.SENSOR_DELAY_GAME);
            }

        }else{
            Toast.makeText(this,"Sensor service not detected",Toast.LENGTH_SHORT).show();

        }




    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

////             Calculate the rotation angle
             angle = (float) Math.toDegrees(Math.atan2(y, x));

            // Rotate the image view
            binding.boxTest.setRotation(angle);
            binding.txtCoordinates.setText("X :"+x+"Y:"+y+"Z:"+z);

            if (Math.abs(angle) > 10 && showToast) {
                showToast = false;
                ReportCard.sensorResult=true;
                Toast.makeText(this, "Rotation detected", Toast.LENGTH_SHORT).show();
            }





        }



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}