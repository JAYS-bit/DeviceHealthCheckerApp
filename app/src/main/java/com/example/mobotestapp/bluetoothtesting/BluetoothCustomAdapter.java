package com.example.mobotestapp.bluetoothtesting;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobotestapp.R;
import com.example.mobotestapp.reportData.ReportCard;

import java.util.Set;

public class BluetoothCustomAdapter extends RecyclerView.Adapter<BluetoothCustomAdapter.MyViewHolder> {


    Set<BluetoothDevice>deviceSet;
    Context context;

    public BluetoothCustomAdapter(Set<BluetoothDevice> deviceSet, Context context) {
        this.deviceSet = deviceSet;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.single_row_device,parent,false);
        MyViewHolder viewHolder= new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BluetoothDevice device = (BluetoothDevice) deviceSet.toArray()[position];
        if(device!=null) {
            holder.txt.setText(device.getName());
            ReportCard.bluetoothList=true;

        }else{
            Toast.makeText(context, "no device", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public int getItemCount() {
        return deviceSet.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txt;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt=itemView.findViewById(R.id.bluetoohDeviceTextView);

        }
    }

}
