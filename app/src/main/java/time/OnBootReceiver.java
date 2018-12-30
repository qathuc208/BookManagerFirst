package time;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import database.Alarm;
import database.AlarmComparetor;
import database.DatabaseHelper;

public class OnBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            setAlarm(context);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void setAlarm(Context context, String time) throws ParseException{
        AlarmManager mgr = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Calendar cal = Calendar.getInstance();

        String[] array1 = time.split("-");
        String[] array2 = array1[0].split(":");
        SimpleDateFormat dfDate = new SimpleDateFormat("yyyy:MM:dd");
        Date d1 = null;
        try {
            d1 = dfDate.parse(dfDate.format(cal.getTime()));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        int diffInDays = (int) ((dfDate.parse(array1[1]).getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
        Log.d("diffirence day", diffInDays + "");
        Log.d("HOUR", array2[0] + "");
        Log.d("MINUTEs", array2[1] + "");

        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(array2[0]));
        cal.set(Calendar.MINUTE, Integer.parseInt(array2[1]));
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_YEAR, diffInDays);
        mgr.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, getPendingIntent(context));
        //		Log.d("thoi gian con lai", cal.getTimeInMillis()-d1.getTime())
    }

    public static void setAlarmNext(Context context, int hour, int minute, Date date) {
        AlarmManager mgr = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        if (cal.getTimeInMillis() < System.currentTimeMillis()) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }

        mgr.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, getPendingIntent(context));
    }

    public static void cancelAlarm(Context ctxt) {
        AlarmManager mgr = (AlarmManager) ctxt
                .getSystemService(Context.ALARM_SERVICE);
        mgr.cancel(getPendingIntent(ctxt));
    }

    private static PendingIntent getPendingIntent(Context ctxt) {
        Intent i = new Intent(ctxt, OnAlarmReceiver.class);
        return (PendingIntent.getBroadcast(ctxt, 0, i, 0));
    }

    public static void setAlarm(Context context) throws ParseException{
        try {
            DatabaseHelper mDatabaseHelper = new DatabaseHelper(context);
            ArrayList<Alarm> alarms = (ArrayList<Alarm>) mDatabaseHelper.getAllAlarms();
            AlarmComparetor alarmComparetor = new AlarmComparetor();
            Collections.sort(alarms, alarmComparetor);
            Alarm alarm2 = alarms.get(0);
            OnBootReceiver.cancelAlarm(context);
            OnBootReceiver.setAlarm(context, alarm2.getmTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
