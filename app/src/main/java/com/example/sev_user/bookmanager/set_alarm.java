package com.example.sev_user.bookmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class set_alarm extends Activity {
	private TextView tv_idbook, tv_idalarm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_alarm);

		tv_idalarm = (TextView) findViewById(R.id.tv_id_alarm);
		tv_idbook = (TextView) findViewById(R.id.tv_id_alarm);
		// getdata id_book;
		Intent cal_intent1 = getIntent();
		Bundle mBundle1 = cal_intent1.getBundleExtra("mypackage1");
		int id_book = mBundle1.getInt("Id_Book");
		tv_idbook.setText("id cua book la:" + id_book);


	}

}
