package com.example.mobotestapp.rootedstatustesting;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.mobotestapp.R;
import com.example.mobotestapp.bluetoothtesting.BlueToothActivity;
import com.example.mobotestapp.databinding.ActivityRootStatusBinding;
import com.example.mobotestapp.reportData.ReportCard;
import com.scottyab.rootbeer.RootBeer;

import java.io.File;

public class RootStatusActivity extends AppCompatActivity {

    ActivityRootStatusBinding binding;
    AlertDialog alertDialog;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRootStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //using rootbear dependencies
        RootBeer rootBeer = new RootBeer(getApplicationContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showAlertDialogForRoot();
            }
        },150);


        binding.btnNext.setVisibility(View.GONE);

        binding.btnRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.lottieRoot.setAnimation(R.raw.loading_animation);
                binding.lottieRoot.playAnimation();
                if (rootBeer.isRooted()) {
                    //we found indication of root

                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           Toast.makeText(RootStatusActivity.this, "It is a rooted device", Toast.LENGTH_SHORT).show();
                           ReportCard.rootResult=false;

                           binding.lottieRoot.setAnimation(R.raw.rooted_alert);
                           binding.lottieRoot.playAnimation();
                           Toast.makeText(RootStatusActivity.this, "Carry On with The test", Toast.LENGTH_SHORT).show();
                           binding.btnNext.setVisibility(View.VISIBLE);
                       }

                   },5000);


                } else {
                    //we didn't find indication of root
                    new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ReportCard.rootResult=true;
                                Toast.makeText(RootStatusActivity.this, "It is not a  rooted device", Toast.LENGTH_SHORT).show();
                                binding.lottieRoot.setAnimation(R.raw.not_rooted);
                                binding.lottieRoot.playAnimation();
                                Toast.makeText(RootStatusActivity.this, "Carry On with The test", Toast.LENGTH_SHORT).show();
                                binding.btnNext.setVisibility(View.VISIBLE);

                            }
                        },5000);

                }

            }
        });

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RootStatusActivity.this, BlueToothActivity.class));

            }
        });

    }

    private void showAlertDialogForRoot() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this); // Use 'this' for the context
        builder.setTitle("Before You proceed!");
        builder.setMessage("Press the button to check whether rooted or not  ");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }


    //hard coded
    private void executeShellCommand(String su){
        Process process=null;
        try {
            process=Runtime.getRuntime().exec(su);
            Toast.makeText(RootStatusActivity.this, "It is a rooted device", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(RootStatusActivity.this, "It is not rooted device", Toast.LENGTH_SHORT).show();
        }finally {
            if(process!=null){

                try{
                    process.destroy();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void onBackPressed() {

        finishAffinity();
        finish();
    }

}