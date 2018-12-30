package database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by sev_user on 12/12/2017.
 */

public class AlarmComparetor implements Comparator<Alarm>{

    @Override
    public int compare(Alarm alarm, Alarm t1) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "hh:mm-yyyy:MM:dd");

        Date dateAlarm = null;
        Date dateT1 = null;

        try {
            dateAlarm = simpleDateFormat.parse(alarm.getmTime());
            dateT1 = simpleDateFormat.parse(t1.getmTime());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return dateAlarm.compareTo(dateT1);
    }
}
