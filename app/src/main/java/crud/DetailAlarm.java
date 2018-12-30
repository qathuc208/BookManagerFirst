package crud;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sev_user.bookmanager.R;

import database.Alarm;
import database.Book;
import database.DatabaseHelper;

/**
 * Created by Administrator on 12/12/2017.
 */

public class DetailAlarm extends AppCompatActivity {

    TextView title, author, time, desTextView;
    ImageView imageView = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detai_alarm);

        DatabaseHelper mDatabaseHelper = new DatabaseHelper(DetailAlarm.this);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("packet");
        int id_alarm = bundle.getInt("id_alarm");
        Log.d("ID Book ...........", id_alarm+"");
        title = (TextView) findViewById(R.id.tv_title);
        author = (TextView) findViewById(R.id.tv_author);
        time = (TextView) findViewById(R.id.tv_timetoread);
        desTextView = (TextView) findViewById(R.id.tv_description);
        imageView = (ImageView) findViewById(R.id.imageView1);
        try {
            Alarm alarm = mDatabaseHelper.getAlarm(id_alarm);
            time.setText("Time : " + alarm.getmTime());
            Book book = mDatabaseHelper.getBook(alarm.getmIdBook());
            title.setText(book.getmTitle());
            author.setText(book.getmAuthor());
            desTextView.setText(book.getmDescribe());
            imageView.setImageURI(Uri.parse(book.getmImage()));
        } catch (Exception e) {
            Log.e("Detail Alarm Error", e.toString());
        }
    }
// Need to refactoring code
    @Override
    protected void onResume() {
        super.onResume();
        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra("packet");
            int id_alarm = bundle.getInt("id_alarm");
            DatabaseHelper mDatabaseHelper = new DatabaseHelper(DetailAlarm.this);
            Alarm alarm = mDatabaseHelper.getAlarm(id_alarm);
            time.setText("Time : " + alarm.getmTime());
            Book book = mDatabaseHelper.getBook(alarm.getmIdBook());
            title.setText(book.getmTitle());
            author.setText(book.getmAuthor());
            desTextView.setText(book.getmDescribe());
            imageView.setImageURI(Uri.parse(book.getmImage()));
        } catch (Exception e) {
            Log.e("Detail Alarm Error", e.toString());
        }
    }
}
