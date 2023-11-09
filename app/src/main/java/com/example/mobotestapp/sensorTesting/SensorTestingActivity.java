package com.example.mobotestapp.sensorTesting;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mobotestapp.R;
import com.example.mobotestapp.databinding.ActivitySensorTestingBinding;
import com.example.mobotestapp.firebasepdfactivity.ReportGenerateActivity;
import com.example.mobotestapp.reportData.ReportCard;

public class SensorTestingActivity extends AppCompatActivity  {


    ActivitySensorTestingBinding binding;

    AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySensorTestingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showAlertDialogForSensor();
            }
        },150);



        binding.btnTestProximity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SensorTestingActivity.this,ProximitySensorActivity.class));


            }
        });
        binding.btnTestAccelero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SensorTestingActivity.this,AccelerometerActivity.class));

            }
        });


       binding.btnTestGps.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(SensorTestingActivity.this, GPSActivity.class));
           }
       });

       binding.btnNext.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(SensorTestingActivity.this, ReportGenerateActivity.class));
           }
       });
    }

    private void showAlertDialogForSensor() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this); // Use 'this' for the context
        builder.setTitle("Before You proceed!");
        builder.setMessage("Check by pressing all buttons , and press next to proceed, sensor working result will be shown in final report card ");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();

    }
    public void onBackPressed() {

        finishAffinity();
        finish();
    }

    }

