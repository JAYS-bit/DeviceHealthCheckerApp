package com.example.mobotestapp.microphonetesting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.mobotestapp.R;
import com.example.mobotestapp.databinding.ActivityMicroPhoneTestBinding;
import com.example.mobotestapp.reportData.ReportCard;
import com.example.mobotestapp.sensorTesting.SensorTestingActivity;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MicroPhoneTestActivity extends AppCompatActivity {



    ActivityMicroPhoneTestBinding binding;
      private static final int REQUEST_AUDIO_PERMISSION_CODE=101;
      MediaRecorder mediaRecorder;
      MediaPlayer mediaPlayer;
      boolean isRecording=false;
      boolean isPlaying=false;
      AlertDialog alertDialog;

      int seconds=0;
      String path=null;
      int dummySecond=0;
      int playableSeconds=0;
      Handler handler;


      ExecutorService executorService= Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMicroPhoneTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnNext.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showAlertDialogForMicrophone();
            }
       } ,150);
        mediaPlayer= new MediaPlayer();



        binding.ibRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(checkRecordingPermission()){

                  if(!isRecording){

                      isRecording=true;
                      executorService.execute(new Runnable() {
                          @Override
                          public void run() {
                               mediaRecorder= new MediaRecorder();
                               mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                               mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                               mediaRecorder.setOutputFile(getRecordingFilePath());
                               path=getRecordingFilePath();

                               mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                              try {
                                  mediaRecorder.prepare();
                              } catch (IOException e) {
                                  throw new RuntimeException(e);
                              }
                              mediaRecorder.start();
                              runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      binding.ivSimpleBg.setVisibility(View.VISIBLE);
                                      binding.lavPlaying.setVisibility(View.GONE);
                                      binding.tvRecordingPath.setText(getRecordingFilePath());
                                      playableSeconds=0;
                                      seconds=0;
                                      dummySecond=0;
                                      binding.ibRecord.setImageDrawable(ContextCompat.getDrawable(MicroPhoneTestActivity.this,R.drawable.recording_active));
                                      runTimer();
                                  }
                              });
                          }

                      });
                  }
                  else{

                      executorService.execute(new Runnable() {
                          @Override
                          public void run() {
                              mediaRecorder.stop();
                               mediaRecorder.release();
                               mediaRecorder=null;
                               playableSeconds=seconds;
                               dummySecond=seconds;
                               seconds=0;
                               isRecording=false;

                               runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       binding.ivSimpleBg.setVisibility(View.VISIBLE);
                                       binding.lavPlaying.setVisibility(View.GONE);
                                       handler.removeCallbacksAndMessages(null);
                                       binding.ibRecord.setImageDrawable(ContextCompat.getDrawable(MicroPhoneTestActivity.this,R.drawable.baseline_mic_24));

                                   }
                               });
                          }
                      });

                  }


              }  else{
                  requestRecordingPermission();
              }
            }
        });

        binding.ibPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPlaying){

                    if(path!=null){
                        try {
                            mediaPlayer.setDataSource(getRecordingFilePath());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }else{
                        Toast.makeText(MicroPhoneTestActivity.this, "No recordings Presents", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    // Add an OnCompletionListener to detect when playback is completed
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            ReportCard.microphoneResult=true;
                            // Add a toast message to indicate that the recording has been played successfully
                            Toast.makeText(MicroPhoneTestActivity.this, "Microphone test passed successfully, Click on Next to continue", Toast.LENGTH_LONG).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    binding.btnNext.setVisibility(View.VISIBLE);
                                }
                            },3000);
                        }
                    });

                    mediaPlayer.start();
                    isPlaying=true;
                    binding.ibPlay.setImageDrawable(ContextCompat.getDrawable(MicroPhoneTestActivity.this,R.drawable.baseline_pause_24));
                    binding.ivSimpleBg.setVisibility(View.GONE);
                    binding.lavPlaying.setVisibility(View.VISIBLE);
                    runTimer();


                }else{
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer=null;
                    mediaPlayer=new MediaPlayer();
                    isPlaying=false;
                    seconds=0;

                    handler.removeCallbacksAndMessages(null);
                    binding.ivSimpleBg.setVisibility(View.VISIBLE);
                    binding.lavPlaying.setVisibility(View.GONE);
                    binding.ibPlay.setImageDrawable(ContextCompat.getDrawable(MicroPhoneTestActivity.this,R.drawable.baseline_play_circle_24));





                }

            }
        });

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MicroPhoneTestActivity.this, SensorTestingActivity.class));
            }
        });
    }

    private void runTimer() {

        handler= new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int minutes=(seconds%3600)/60;
                int secs= seconds%60;
                String time= String.format(Locale.getDefault(),"%02d:%02d",minutes,secs);
                binding.tvTime.setText(time);

                if(isRecording || (isPlaying && playableSeconds!=-1)){

                    seconds++;
                    playableSeconds--;


                    if(playableSeconds==-1 && isPlaying){

                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer=null;
                        mediaPlayer=new MediaPlayer();
                        playableSeconds=dummySecond;
                        seconds=0;
                        handler.removeCallbacksAndMessages(null);
                        binding.ivSimpleBg.setVisibility(View.GONE);
                        binding.lavPlaying.setVisibility(View.GONE);
                        binding.ibPlay.setImageDrawable(ContextCompat.getDrawable(MicroPhoneTestActivity.this,R.drawable.baseline_play_circle_24));
                        return;


                    }
                }
                handler.postDelayed(this,1000);


            }
        });
    }

    private String getRecordingFilePath() {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File music= contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file= new File(music,"testFile"+".mp3");
        return  file.getPath();
    }

    private void requestRecordingPermission() {
        ActivityCompat.requestPermissions(MicroPhoneTestActivity.this,
                new String[]{Manifest.permission.RECORD_AUDIO},REQUEST_AUDIO_PERMISSION_CODE);



    }

    private boolean checkRecordingPermission() {

        if(ContextCompat.checkSelfPermission(MicroPhoneTestActivity.this,Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_DENIED){
            requestRecordingPermission();
            return false;
        }else{
            return true;
        }


            }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_AUDIO_PERMISSION_CODE){
            if(grantResults.length>0){
                boolean permissionToRecord= grantResults[0]==PackageManager.PERMISSION_GRANTED;
                if(permissionToRecord){

                    Toast.makeText(getApplicationContext(),"Granted",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Denied", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private void showAlertDialogForMicrophone() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this); // Use 'this' for the context
        builder.setTitle("Before You proceed!");
        builder.setMessage("Record your voice, it will automatically tells you to proceed once the recording is saved completely ");
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
