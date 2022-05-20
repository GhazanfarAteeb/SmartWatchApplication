package com.app.smartwatchapplication.Adapters;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.smartwatchapplication.Activities.WatchScanActivity;
import com.app.smartwatchapplication.R;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.scan.ScanResult;

import java.util.ArrayList;
import java.util.List;


public class WatchAdapter extends RecyclerView.Adapter<WatchAdapter.ViewHolder> {
    List<RxBleDevice> watchList = new ArrayList<>();
    WatchConnection watchConnection;
    Context context;

    public WatchAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_watch, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RxBleDevice watch = watchList.get(position);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        holder.tvName.setText(watch.getBluetoothDevice().getName());
        holder.tvMacAddress.setText(watch.getBluetoothDevice().getAddress());
        holder.itemView.setOnClickListener( (view) -> {
                    WatchScanActivity.scanDisposable.dispose();
                    watchConnection.sendConnectionRequest(watchList.get(position));
//                    return true;
        });
        holder.cvWatch.setTag(holder);
    }

    @Override
    public int getItemCount() {
        return watchList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cvWatch;
        TextView tvName;
        TextView tvMacAddress;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cvWatch = itemView.findViewById(R.id.cv_watch);
            tvName = itemView.findViewById(R.id.tv_watch_name);
            tvMacAddress = itemView.findViewById(R.id.tv_watch_mac_address);
        }
    }

    public void addScanResult(ScanResult scanResult) {
        int existIndex = -1;
        for (int i = 0; i < watchList.size(); i++) {
            RxBleDevice watch = watchList.get(i);
            if (watch.getBluetoothDevice().getAddress().equals(scanResult.getBleDevice().getMacAddress())) {
                existIndex = i;
                break;
            }
        }
        if (existIndex == -1) {
            watchList.add(scanResult.getBleDevice());
        } else {
            watchList.set(existIndex, scanResult.getBleDevice());
        }
        notifyDataSetChanged();
    }

    public void setWatchConnectionListener(WatchConnection watchConnectionListener) {
        this.watchConnection = watchConnectionListener;
    }

    public interface WatchConnection {
        void sendConnectionRequest(RxBleDevice watch);
    }
}
