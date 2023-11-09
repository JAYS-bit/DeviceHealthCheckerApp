package com.example.mobotestapp.firebasepdfactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobotestapp.R;
import com.example.mobotestapp.reportData.ReportCard;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportGenerateActivity extends AppCompatActivity {


    com.example.mobotestapp.databinding.ActivityReportGenerateBinding binding;
    RadioButton btn_current;
    boolean isGeneratePDFSelected = false;
    String microphone_test_results;
    String camera_test_results;
    String sensor_test_results;
    String root_test_results;
    String bluetooth_test_results;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= com.example.mobotestapp.databinding.ActivityReportGenerateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        if(ReportCard.microphoneResult==true){
            microphone_test_results="TRUE";
            binding.textMicrophoneGrade.setText(microphone_test_results);
            binding.imgMicrophoneMarked.setImageResource(R.drawable.ic_marked_pass);

        }else{
            microphone_test_results="FALSE";
            binding.textMicrophoneGrade.setText(microphone_test_results);
            binding.imgMicrophoneMarked.setImageResource(R.drawable.ic_marked_fail);
        }

        if(ReportCard.cameraResult==true){
            camera_test_results="TRUE";
            binding.textCameraGrade.setText(camera_test_results);
            binding.imgCameraMarked.setImageResource(R.drawable.ic_marked_pass);
        }else{
            camera_test_results="FALSE";
            binding.textCameraGrade.setText(camera_test_results);
            binding.imgCameraMarked.setImageResource(R.drawable.ic_marked_fail);
        }

        if(ReportCard.sensorResult==true){
            sensor_test_results="TRUE";
            binding.textSensorGrade.setText(sensor_test_results);
            binding.imgSensorMarked.setImageResource(R.drawable.ic_marked_pass);
        }else{
            sensor_test_results="FALSE";
            binding.textSensorGrade.setText(sensor_test_results);
            binding.imgSensorMarked.setImageResource(R.drawable.ic_marked_fail);
        }

        if(ReportCard.rootResult==true){
            root_test_results="TRUE";
            binding.textRootGrade.setText(root_test_results);
            binding.imgRootMarked.setImageResource(R.drawable.ic_marked_pass);
        }else{
            root_test_results="FALSE";
            binding.textRootGrade.setText(root_test_results);
            binding.imgRootMarked.setImageResource(R.drawable.ic_marked_fail);
        }

        if(ReportCard.bluetoothResult==true){
            bluetooth_test_results="TRUE";
            binding.textBluetoothGrade.setText(bluetooth_test_results);
            binding.imgBluetoothMarked.setImageResource(R.drawable.ic_marked_pass);

        }else{
            bluetooth_test_results="FALSE";
            binding.imgBluetoothMarked.setImageResource(R.drawable.ic_marked_fail);
        }




        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                btn_current = findViewById(checkedId);
                if (btn_current != null) {
                    String selectedOption = btn_current.getText().toString();
                    if (selectedOption.equals("Generate PDF")) {
                        isGeneratePDFSelected = true;
                    } else {
                        isGeneratePDFSelected = false;
                    }
                }
            }
        });



        binding.btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isGeneratePDFSelected){

                    Dexter.withContext(getApplicationContext()).withPermissions(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ).withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                            convertXmlToPdf();

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).check();


                }else{


                    String microphonetestresults= binding.textMicrophoneGrade.getText().toString();
                    String cameratestresults=binding.textCameraGrade.getText().toString();
                   String sensortestresults=binding.textSensorGrade.getText().toString();
                   String roottestresults=binding.textRootGrade.getText().toString();
                  String  bluetoothtestresults=binding.textBluetoothGrade.getText().toString();

                    //upload data to firebase
                    uploadDataToFireBase(microphonetestresults,cameratestresults,sensortestresults,roottestresults,bluetoothtestresults);

                }
            }
        });



    }

    private void uploadDataToFireBase(String microphoneTestResults, String cameraTestResults, String sensorTestResults, String rootTestResults, String bluetoothTestResults) {


        FirebaseDatabase database= FirebaseDatabase.getInstance();
        //Initialise firebase
        DatabaseReference userRef = database.getReference("device");
        String userId = userRef.push().getKey();
        UserDetailsModel userDetailsModel= new UserDetailsModel(
                microphoneTestResults,
                cameraTestResults,
                sensorTestResults,
                rootTestResults,
                bluetoothTestResults
        );

       userRef.child(userId).setValue(userDetailsModel);
       Toast.makeText(ReportGenerateActivity.this,"Added Successfully! Please check",Toast.LENGTH_SHORT).show();


    }

    private void convertXmlToPdf() {
        // Create a new PdfDocument
        PdfDocument document = new PdfDocument();

        // Calculate the view size based on the content
        View view = LayoutInflater.from(this).inflate(R.layout.activity_report_generate, null);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();

        // Create a PageInfo with the calculated size
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(width, height, 1).create();

        // Start a new page
        PdfDocument.Page page = document.startPage(pageInfo);

        // Get the canvas to draw on
        Canvas canvas = page.getCanvas();

        // Draw the view on the canvas
        view.layout(0, 0, width, height);
        view.draw(canvas);

        // Access and update the dynamic content (TextViews) here
        TextView textMicrophoneGrade = view.findViewById(R.id.text_microphone_grade);
        TextView textCameraGrade = view.findViewById(R.id.text_camera_grade);
        TextView textSensorGrade = view.findViewById(R.id.text_sensor_grade);
        TextView textRootGrade = view.findViewById(R.id.text_root_grade);
        TextView textBluetoothGrade = view.findViewById(R.id.text_bluetooth_grade);
        ImageView imgMicrophoneMarked = view.findViewById(R.id.img_microphone_marked);
        ImageView imgCameraMarked = view.findViewById(R.id.img_camera_marked);
        ImageView imgSensorMarked = view.findViewById(R.id.img_sensor_marked);
        ImageView imgRootMarked = view.findViewById(R.id.img_root_marked);
        ImageView imgBluetoothMarked = view.findViewById(R.id.img_bluetooth_marked);


        // Update the TextViews with dynamic content
        textMicrophoneGrade.setText(microphone_test_results);
        textCameraGrade.setText(camera_test_results);
        textSensorGrade.setText(sensor_test_results);
        textRootGrade.setText(root_test_results);
        textBluetoothGrade.setText(bluetooth_test_results);

        imgMicrophoneMarked.setVisibility(View.GONE);
        imgCameraMarked.setVisibility(View.GONE);
        imgSensorMarked.setVisibility(View.GONE);
        imgRootMarked.setVisibility(View.GONE);
        imgBluetoothMarked.setVisibility(View.GONE);




        // Finish the page
        document.finishPage(page);

        // Generate a unique filename with a timestamp
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHHmmss", Locale.getDefault());
        String currentTimestamp = sdf.format(new Date());
        String fileName = "Report_" + currentTimestamp + ".pdf";

        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(downloadsDir, fileName);

        try {
            // Write the PDF content to the file
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            document.close();
            fos.close();
            Toast.makeText(this, "PDF saved in Downloads", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save PDF", Toast.LENGTH_SHORT).show();
        }
    }






}
