package io.akwa.akproximity.kontakt;

import android.content.Context;
import android.util.Log;

import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.device.BeaconRegion;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.ble.manager.listeners.IBeaconListener;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;
import io.akwa.akcore.BeaconData;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

/**
 * The type Abstract kontakt beacon scanner.
 */
public abstract class AbstractKontaktBeaconScanner {
    protected WeakReference<Context> context;
    /**
     * The constant TAG.
     */
    public static final String TAG = "EstimoteBeaconHandler";
    private ProximityManager proximityManager;
    KontaktIBeaconListener kontaktIBeaconListener;

    public void setKontaktIBeaconListener(KontaktIBeaconListener kontaktIBeaconListener) {
        this.kontaktIBeaconListener = kontaktIBeaconListener;
    }

    /**
     * Instantiates a new Abstract kontakt beacon scanner.
     *
     * @param context the context
     */
    public AbstractKontaktBeaconScanner(Context context) {
        this.context = new WeakReference<>(context);
    }

    /**
     * On beacon detected.
     *
     * @param beaconData the beacon data
     */
    public abstract void onBeaconDetected(BeaconData beaconData);

    /**
     * Start scanning.
     */
    public abstract void startScanning();

    /**
     * Initialize koncat.
     */
    protected void initializeKoncat() {
        KontaktSDK.initialize(BeaconConfig.KONTAKT_KEY);
        proximityManager = ProximityManagerFactory.create(context.get());
        proximityManager.setIBeaconListener(createIBeaconListener());
        if(BeaconConfig.BEACON_UUID!=null&&!BeaconConfig.BEACON_UUID.equals(""))
        {
            proximityManager.spaces().iBeaconRegion(getBeaconRegion(BeaconConfig.BEACON_UUID));

        }


    }

    public void startRangingBeacons() {
        if (proximityManager != null)
        {
            proximityManager.connect(new OnServiceReadyListener() {
                @Override
                public void onServiceReady() {
                    proximityManager.startScanning();
                }
            });
        }
    }

    /**
     * Stop rangin beacon.
     */
    public void stopRanginBeacon() {
        if (proximityManager != null)
            proximityManager.stopScanning();
    }

    private IBeaconListener createIBeaconListener() {

        return new IBeaconListener() {
            @Override
            public void onIBeaconDiscovered(IBeaconDevice iBeacon, IBeaconRegion region) {
                Log.i("Beacons onIBeaconDiscovered","Beacon Minor==="+iBeacon.getMinor());
                onBeaconDetected(getBeaconData(iBeacon));
                if (kontaktIBeaconListener !=null){
                    kontaktIBeaconListener.onBeaconDetected(iBeacon);
                }
            }

            @Override
            public void onIBeaconsUpdated(List<IBeaconDevice> iBeacons, IBeaconRegion region) {
                Log.i("Beacons onIBeaconsUpdated","Beacon size==="+iBeacons.size());

            }

            @Override
            public void onIBeaconLost(IBeaconDevice iBeacon, IBeaconRegion region) {
                Log.i("Beacons onIBeaconLost","Beaonc Minor==="+iBeacon.getMinor());

            }
        };
    }

    private BeaconData getBeaconData(IBeaconDevice iBeacon) {
        BeaconData beaconData = new BeaconData();
        beaconData.setTimestamp(iBeacon.getTimestamp());
        beaconData.setMajor(iBeacon.getMajor());
        beaconData.setMinor(iBeacon.getMinor());
        beaconData.setUuid(iBeacon.getProximityUUID().toString());
        beaconData.setDistance(iBeacon.getDistance());
        beaconData.setRssi(iBeacon.getRssi());
        //format distance
        DecimalFormat df = new DecimalFormat("#.00");
       // String finalDistance = df.format(beaconData.getDistance());
        beaconData.setRange(Util.getProximityNumericValue(iBeacon.getProximity()));
    return beaconData;
    }

    private IBeaconRegion getBeaconRegion(String uuid) {
        BeaconRegion.Builder beaconRegion=new BeaconRegion.Builder();
        beaconRegion.identifier("EmTrack Region");
        beaconRegion.proximity(UUID.fromString(uuid));
        IBeaconRegion region =beaconRegion.build();
        return region;

    }

    public interface KontaktIBeaconListener {
        void onBeaconDetected(IBeaconDevice iBeacon);
    }


}