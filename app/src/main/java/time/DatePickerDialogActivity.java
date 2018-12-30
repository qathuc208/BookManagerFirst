package time;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.sev_user.bookmanager.MainActivity;
import com.example.sev_user.bookmanager.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import database.Alarm;
import database.AlarmComparetor;
import database.DatabaseHelper;

public class DatePickerDialogActivity extends DialogPreference implements DialogInterface.OnClickListener, View.OnClickListener {
    Context context;
    String mDateString = "";
    String mTimeString = "";
    String mDateCurren = "";
    String mTimeCurren = "";

    public DatePickerDialogActivity(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public DatePickerDialogActivity(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setPositiveButtonText("Set");
        setNegativeButtonText("Cancel");
    }

    public DatePickerDialogActivity(Context context, AttributeSet attrs) {
        super(context, attrs);
        context = context;
    }

    public DatePickerDialogActivity(Context context) {
        super(context);
    }

    // Widget GUI
    Button btnCalendar, btnTimePicker;
    EditText txtDate, txtTime;

    // Variable for storing current date and time
    private int mYear, mMonth, mDay, mHour, mMinute;

    /** Called when the activity is first created. */

    @Override
    protected View onCreateDialogView() {
        View view;
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.get_date_time, null);
        btnCalendar = (Button) view.findViewById(R.id.btnCalendar);
        btnTimePicker = (Button) view.findViewById(R.id.btnTimePicker);

        txtDate = (EditText) view.findViewById(R.id.txtDate);
        txtTime = (EditText) view.findViewById(R.id.txtTime);

        btnCalendar.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        return view;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
    }

    @Override
    public void onClick(View view) {
        if (view == btnCalendar) {
            Calendar mcurrentTime = Calendar.getInstance();
            mYear = mcurrentTime.get(Calendar.YEAR);
            mMonth = mcurrentTime.get(Calendar.MONTH);
            mDay = mcurrentTime.get(Calendar.DAY_OF_MONTH);
            mDateCurren = mYear + ":" + (mMonth + 1) + ":" + mDay;

            DatePickerDialog mDatePickerDialog;
            mDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDate) {
                    mDateString = "";
                    mDateString = "" + selectedYear;
                    mDateString += ":" + (selectedMonth + 1);
                    mDateString += ":" + selectedDate;

                    txtDate.setText(mDateString);
                }
            },mYear, mMonth, mDay);

            mDatePickerDialog.setTitle("Select Time");
            mDatePickerDialog.show();
        }

        if (view == btnTimePicker) {
            Calendar mcurrentTime = Calendar.getInstance();
            mHour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            mMinute = mcurrentTime.get(Calendar.MINUTE);
            mTimeCurren = mHour + ":" + mMinute;
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getContext(),
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker,
                                              int selectedHour, int selectedMinute) {
                            mTimeString = "";

                            mTimeString += selectedHour;
                            mTimeString += ":" + selectedMinute;
                            txtTime.setText(mTimeString);
                        }
                    }, mHour, mMinute, false);// No 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            if ((txtDate.getText().toString().compareTo("") != 0)
                    || (txtTime.getText().toString().compareTo("") != 0)) {

                String timeSelected = mTimeString + "-" + mDateString;
                String timeCurren = mTimeCurren + "-" + mDateCurren;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                        "hh:mm-yyyy:MM:dd");
                try {
                    Date date_SelecteDate = simpleDateFormat
                            .parse(timeSelected);
                    Date date_Curren = simpleDateFormat.parse(timeCurren);
                    if (date_SelecteDate.compareTo(date_Curren) > 0) {

                        DatabaseHelper mDatabaseHelper = new DatabaseHelper(getContext());
                        if(MainActivity.flag_alarm==1){
                            Alarm alarm = new Alarm(timeSelected, EditPreferences.id_book);
                            mDatabaseHelper.createAlarm(alarm);
                        }
                        else if(MainActivity.flag_alarm==2){
                            Alarm alarm  = mDatabaseHelper.getAlarm(EditPreferences.id_book);
                            alarm.setmTime(timeSelected);
                            Log.d("Sua Alarm", alarm.getmIdAlarm()+"-"+
                                    alarm.getmIdBook()+"-"+alarm.getmTime());
                            mDatabaseHelper.updateAlarm(alarm);
                        }
                        ArrayList<Alarm> alarms= (ArrayList<Alarm>)mDatabaseHelper.getAllAlarms();
                        AlarmComparetor alarmComparetor = new AlarmComparetor();
                        Collections.sort(alarms, alarmComparetor);
                        Alarm alarm2 = alarms.get(0);
                        OnBootReceiver.cancelAlarm(getContext());
                        OnBootReceiver.setAlarm(getContext(), alarm2.getmTime());
                        Toast.makeText(getContext(), "Time accept",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Time Not accept",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getContext(), "Time Not Selected!!!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
