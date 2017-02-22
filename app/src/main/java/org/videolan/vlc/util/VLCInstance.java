/**
 * Created by GianniYan on 2016/2/20.
 */

package org.videolan.vlc.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.wgbb.vlc.VLCApplication;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.LibVlcException;

public class VLCInstance {
    public final static String TAG = "VLC/Util/VLCInstance";

    /** A set of utility functions for the VLC application */
    public static LibVLC getLibVlcInstance() throws LibVlcException {
        LibVLC instance = LibVLC.getExistingInstance();
        if (instance == null) {
            //Thread.setDefaultUncaughtExceptionHandler(new VLCCrashHandler());

            instance = LibVLC.getInstance();
            final Context context = VLCApplication.getAppContext();
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            VLCInstance.updateLibVlcSettings(pref);
            instance.init(context);
            instance.setOnNativeCrashListener(new LibVLC.OnNativeCrashListener() {
                @Override
                public void onNativeCrash() {
//                    Intent i = new Intent(context, NativeCrashActivity.class);
//                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    i.putExtra("PID", android.os.Process.myPid());
//                    context.startActivity(i);
                	
//                	Logger.e("[VLCInstance]onNativeCrash...");
                }
            });
        }
        return instance;
    }

    public static void updateLibVlcSettings(SharedPreferences pref) {
        LibVLC instance = LibVLC.getExistingInstance();
        if (instance == null)
            return;

        instance.setSubtitlesEncoding(pref.getString("subtitle_text_encoding", ""));
        instance.setTimeStretching(pref.getBoolean("enable_time_stretching_audio", false));
        instance.setFrameSkip(pref.getBoolean("enable_frame_skip", false));
        instance.setChroma(pref.getString("chroma_format", ""));
        instance.setVerboseMode(pref.getBoolean("enable_verbose_mode", true));

        if (pref.getBoolean("equalizer_enabled", false))
            instance.setEqualizer(Preferences.getFloatArray(pref, "equalizer_values"));

        int aout;
        try {
            aout = Integer.parseInt(pref.getString("aout", "-1"));
        }
        catch (NumberFormatException nfe) {
            aout = -1;
        }
        int vout;
        try {
        	vout = Integer.parseInt(pref.getString("vout", "-1"));
        }
        catch (NumberFormatException nfe) {
        	vout = -1;
        }
        int deblocking;
        try {
            deblocking = Integer.parseInt(pref.getString("deblocking", "-1"));
        }
        catch(NumberFormatException nfe) {
            deblocking = -1;
        }
        int hardwareAcceleration;
        try {
            hardwareAcceleration = Integer.parseInt(pref.getString("hardware_acceleration", "-1"));
        }
        catch(NumberFormatException nfe) {
            hardwareAcceleration = -1;
        }
        int networkCaching = pref.getInt("network_caching_value", 0);
        if(networkCaching > 60000)
            networkCaching = 60000;
        else if(networkCaching < 0)
            networkCaching = 0;
        instance.setAout(aout);
        instance.setVout(vout);
        instance.setDeblocking(deblocking);
        instance.setNetworkCaching(networkCaching);
        instance.setHardwareAcceleration(hardwareAcceleration);
    }


}
