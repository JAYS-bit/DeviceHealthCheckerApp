package com.example.mobotestapp.bluetoothtesting;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mobotestapp.R;
import com.example.mobotestapp.databinding.ActivityBlueToothBinding;
import com.example.mobotestapp.microphonetesting.MicroPhoneTestActivity;
import com.example.mobotestapp.reportData.ReportCard;
import com.example.mobotestapp.sensorTesting.SensorTestingActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;
import java.util.Set;

public class BlueToothActivity extends AppCompatActivity {

    // Objects of our views
    ActivityBlueToothBinding binding;
    Set<BluetoothDevice> ad;
    BluetoothAdapter adapter;
    private LottieAnimationView lottieAnimationView;
    AlertDialog alertDialog;

    // Constants for Bluetooth Adapter class
    private static final int REQUEST_ENABLE_BLUETOOTH = 2;
    BluetoothCustomAdapter bluetoothCustomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBlueToothBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        lottieAnimationView = findViewById(R.id.lottie_bluetooth);
        binding.btnNext.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showAlertDialogForBluetooth();
            }
        }, 150);

        // Request Bluetooth-related permissions using Dexter
        Dexter.withContext(getApplicationContext()).withPermissions(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN

        ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                    setupBluetooth();
                } else {

                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
        binding.btnTurnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!adapter.isEnabled()) {
                    Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH);
                    // Move the ReportCard.bluetoothResult = true; to onActivityResult
                } else {
                    Toast.makeText(BlueToothActivity.this, "Bluetooth is already enabled.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Disable Bluetooth
        binding.btnTurnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.isEnabled()) {
                    adapter.disable();
                    Toast.makeText(BlueToothActivity.this, "Bluetooth Disabled!", Toast.LENGTH_SHORT).show();
                    lottieAnimationView.setAnimation(R.raw.bluetooth_disconnected);
                    lottieAnimationView.playAnimation();
                    // ReportCard.bluetoothResult = true; // Remove this line
                } else {
                    Toast.makeText(BlueToothActivity.this, "Bluetooth is already disabled.", Toast.LENGTH_SHORT).show();
                    lottieAnimationView.setAnimation(R.raw.bluetooth_disconnected);
                    lottieAnimationView.playAnimation();
                }
            }
        });


        // Show a list of available devices
        binding.btnListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad = adapter.getBondedDevices();
                binding.listView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                bluetoothCustomAdapter = new BluetoothCustomAdapter(ad, getApplicationContext());
                binding.listView.setAdapter(bluetoothCustomAdapter);
                ReportCard.bluetoothResult = true;
            }
        });

        binding.btnListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad = adapter.getBondedDevices();
                binding.listView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                bluetoothCustomAdapter = new BluetoothCustomAdapter(ad, getApplicationContext());
                binding.listView.setAdapter(bluetoothCustomAdapter);
                ReportCard.bluetoothResult = true;
            }
        });

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BlueToothActivity.this, MicroPhoneTestActivity.class));
            }
        });
    }

    private void setupBluetooth() {
        binding.btnNext.setVisibility(View.GONE);

        adapter = BluetoothAdapter.getDefaultAdapter();

        if (adapter == null) {
            Log.e("BluetoothActivity", "BluetoothAdapter is null");
            Toast.makeText(getApplicationContext(), "Bluetooth is not supported on this device", Toast.LENGTH_LONG).show();
            return; // Exit the method if Bluetooth is not supported
        }

    }
        // Enable the Bluetooth using a button





    private void showAlertDialogForBluetooth() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Before You proceed!");
        builder.setMessage("Check for the following, Turn On the Bluetooth, Check whether paired devices are shown or not, and then turn off the Bluetooth");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                // Bluetooth is enabled
                Toast.makeText(BlueToothActivity.this, "Bluetooth Enabled!", Toast.LENGTH_SHORT).show();
                lottieAnimationView.setAnimation(R.raw.bluetooth_connected);
                lottieAnimationView.playAnimation();
                ReportCard.bluetoothResult = true;
                if (ReportCard.bluetoothResult) {
                    Toast.makeText(getApplicationContext(), "Continue to the next screen", Toast.LENGTH_SHORT).show();
                    binding.btnNext.setVisibility(View.VISIBLE);
                }
            } else {
                // Bluetooth activation request was canceled or failed
                Toast.makeText(BlueToothActivity.this, "Bluetooth Activation Canceled or Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onBackPressed() {
        finishAffinity();
        finish();
    }
}
