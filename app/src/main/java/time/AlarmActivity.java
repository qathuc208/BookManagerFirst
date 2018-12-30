package time;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sev_user.bookmanager.R;

import java.util.ArrayList;
import java.util.Collections;

import crud.DetailBook;
import database.Alarm;
import database.AlarmComparetor;
import database.Book;
import database.DatabaseHelper;

public class AlarmActivity extends AppCompatActivity {
    DatabaseHelper mDatabaseHelper = null;
    TextView time;
    String alarm;
    ImageView imageView = null;
    Ringtone r;
    int id_book=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_display);

        time = (TextView) findViewById(R.id.tv_time);
        imageView = (ImageView) findViewById(R.id.clock);

        imageView.setOnTouchListener(new OnSwipeTouchListener());

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(AlarmActivity.this);

        alarm = prefs.getString("alertringtone", "DEFAULT_RINGTONE_URI");
        Uri notification = Uri.parse(alarm);
        r = RingtoneManager.getRingtone(AlarmActivity.this,
                notification);
        r.play();
        Vibrator vibrator = (Vibrator) AlarmActivity.this
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(5000);

        try {
            mDatabaseHelper = new DatabaseHelper(AlarmActivity.this);
            ArrayList<Alarm> alarms = (ArrayList<Alarm>) mDatabaseHelper
                    .getAllAlarms();
            AlarmComparetor alarmComparetor = new AlarmComparetor();
            Collections.sort(alarms, alarmComparetor);

            Book book = mDatabaseHelper.getBook(alarms.get(0).getmIdBook());
            time.setText(alarms.get(0).getmTime() +" - "+ book.getmTitle());
            id_book = book.getmIdBook();
            Log.d("Alarm Activity", alarms.get(0).getmTime() + book.getmTitle());
            Alarm alarm2 = alarms.get(1);
            OnBootReceiver.cancelAlarm(AlarmActivity.this);
            OnBootReceiver.setAlarm(AlarmActivity.this, alarm2.getmTime());
            mDatabaseHelper.deleteAlarm(alarms.get(0).getmIdAlarm());
        } catch (Exception e) {
            mDatabaseHelper = new DatabaseHelper(AlarmActivity.this);
            ArrayList<Alarm> alarms = (ArrayList<Alarm>) mDatabaseHelper
                    .getAllAlarms();
            mDatabaseHelper.deleteAlarm(alarms.get(0).getmIdAlarm());
            OnBootReceiver.cancelAlarm(getApplicationContext());
        }

        Thread myThread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(20000);
                    r.stop();
                    Log.d("Stop", "Stop");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        myThread.start();
    }

    private class OnSwipeTouchListener implements View.OnTouchListener {
        @SuppressWarnings("deprecation")
        private final GestureDetector gestureDetector = new GestureDetector(
                new GestureListener());

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return gestureDetector.onTouchEvent(motionEvent);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                onTouch(e);
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                   float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD
                                && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                        }
                    } else {
                        // onTouch(e);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }

        public void onTouch(MotionEvent e) {
        }

        public void onSwipeRight() {
            Log.d("Swipe right", " thangggggggg");
            r.stop();
            Intent intent = new Intent(AlarmActivity.this, DetailBook.class);
            intent.putExtra("id_book", id_book);
            startActivity(intent);
        }

        public void onSwipeLeft() {
            Log.d("Swipe Left", "thangggggggggggggggggg");
            r.stop();
            AlarmActivity.this.finish();
        }

        public void onSwipeTop() {
        }
    }
}
