package setting;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.example.sev_user.bookmanager.R;

import java.util.Locale;

/**
 * Created by Administrator on 13/12/2017.
 */

public class SettingPreferences extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    SharedPreferences pref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_preferences);

        pref = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        pref.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        pref.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pref.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pref.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals("language")) {

            String lang = pref.getString(s, "en");
            // Log.d("MainActivity", lang);
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
