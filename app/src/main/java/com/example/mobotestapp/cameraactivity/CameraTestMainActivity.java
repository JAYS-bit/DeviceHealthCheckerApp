package com.example.mobotestapp.cameraactivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobotestapp.R;
import com.example.mobotestapp.reportData.ReportCard;
import com.example.mobotestapp.rootedstatustesting.RootStatusActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

public class CameraTestMainActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 101;
    private long pressedTime;

    private ImageView imageView;
    private Button captureButton;
    private Button detectTextButton;
    private Button buttonnext;
    private TextView textView;  // Added TextView
    private AlertDialog alertDialog;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_test_main);
        imageView = findViewById(R.id.imageView);
        captureButton = findViewById(R.id.captureButton);
        detectTextButton = findViewById(R.id.detectTextButton);
        textView = findViewById(R.id.textView);// Initialize TextView
        buttonnext=findViewById(R.id.btn_next);


        buttonnext.setVisibility(View.GONE);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showAlertDialogForTesting();
            }
        },150);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check and request camera permission
                if (ContextCompat.checkSelfPermission(CameraTestMainActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CameraTestMainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                } else {
                    // Launch the camera to capture an image
                    captureImage();
                }
            }
        });

        detectTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Detect text from the captured image
                detectTextFromImage();
            }
        });
        buttonnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(CameraTestMainActivity.this, RootStatusActivity.class);
                startActivity(i);

            }
        });
    }

    private void showAlertDialogForTesting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this); // Use 'this' for the context
        builder.setTitle("Before You proceed");
        builder.setMessage("Capture one image with text in it and press detect camera to test your camera");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    private void detectTextFromImage() {
        if (imageView.getDrawable() == null) {
            Toast.makeText(this, "No image to detect text from.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(((BitmapDrawable) imageView.getDrawable()).getBitmap());
        FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();

        textRecognizer.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                        // Handle the detected text
                        String detectedText = firebaseVisionText.getText();
                        // Update the TextView with the detected text

                        if(!detectedText.isEmpty()) {
                            textView.setText("Detected Text: " + detectedText);
                            Toast.makeText(CameraTestMainActivity.this, "Text Detected!", Toast.LENGTH_SHORT).show();
                            ReportCard.cameraResult = true;
                            detectedText="";
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CameraTestMainActivity.this, "Click on Next to Proceed furthur", Toast.LENGTH_SHORT).show();
                                    buttonnext.setVisibility(View.VISIBLE);
                                }
                            }, 3000);
                        }else{
                            Toast.makeText(CameraTestMainActivity.this, "Please Capture some image ", Toast.LENGTH_SHORT).show();
                        }



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the failure
                        Toast.makeText(CameraTestMainActivity.this, "Text detection failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        ReportCard.cameraResult=false;


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                buttonnext.setVisibility(View.VISIBLE);
                                Toast.makeText(CameraTestMainActivity.this, "Click on Next to carry on with the test", Toast.LENGTH_SHORT).show();
                            }
                        },3000);

                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                captureImage();
            } else {
                Toast.makeText(this, "Camera permission is required to capture an image.", Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override

    public void onBackPressed() {

        finishAffinity();
        finish();
    }


}