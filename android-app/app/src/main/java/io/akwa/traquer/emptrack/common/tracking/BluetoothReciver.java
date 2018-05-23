package io.akwa.traquer.emptrack.common.tracking;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import io.akwa.aklogs.NBLogger;

import io.akwa.traquer.emptrack.common.utils.PrefUtils;


public class BluetoothReciver extends BroadcastReceiver {

    public static final String TAG="BluetoothReciver";
    public BluetoothReciver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                    == BluetoothAdapter.STATE_OFF) {

                //Tracker.stopTracking();
                statusUpdate(context,0);
                NBLogger.getLoger().writeLog(context,null,"Bluetooth OFF");


            }
            else if(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                    == BluetoothAdapter.STATE_ON)
            {

                startTracking(context);
                 statusUpdate(context,1);
                NBLogger.getLoger().writeLog(context,null,"Bluetooth ON");



            }
        }
    }


    public void statusUpdate(Context context,int  bluetooth)
    {
        boolean isLocation= TraquerScanUtil.isLocationServiceEnable(context);
        int location;
        if(isLocation)
            location=1;
        else
            location=0;
        Util.updateStatus(context,location,bluetooth);
    }

    public void startTracking(Context context)
    {
        if (!PrefUtils.getCode().equals("")&& PrefUtils.getBeaconStatus()) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Tracker.startTracking();
                }
            } else {
                Tracker.startTracking();
            }
        }
    }


}
