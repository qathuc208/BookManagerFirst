package time;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.sev_user.bookmanager.R;

import java.text.ParseException;

public class EditPreferences extends PreferenceActivity{
    SharedPreferences pref = null;
    public static int id_book = 1345555;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.alarm_preference);
        Intent call_intent1 = getIntent();
        Bundle bundle = call_intent1.getBundleExtra("mypackage1");

        id_book = bundle.getInt("Id_Book");
        Log.d("ID Book ...........", id_book+"");
    }

    @Override
    protected void onResume() {
        super.onResume();
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        pref.registerOnSharedPreferenceChangeListener(onChange);
    }

    @Override
    protected void onPause() {
        super.onPause();
        pref.unregisterOnSharedPreferenceChangeListener(onChange);
    }

    SharedPreferences.OnSharedPreferenceChangeListener onChange = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if ("alarm".equals(key)) {
                boolean enabled = pref.getBoolean(key, false);
                int flag  = (enabled ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                                : PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
                ComponentName component = new ComponentName(EditPreferences.this, OnBootReceiver.class);
                getPackageManager().setComponentEnabledSetting(component, flag,
                        PackageManager.DONT_KILL_APP);
                if (enabled) {
                    try {
                        OnBootReceiver.setAlarm(EditPreferences.this);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    OnBootReceiver.cancelAlarm(EditPreferences.this);
                }
            } else  if ("alarm_time".equals(key)) {
                OnBootReceiver.cancelAlarm(EditPreferences.this);
                try {
                    OnBootReceiver.setAlarm(EditPreferences.this);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };
}
