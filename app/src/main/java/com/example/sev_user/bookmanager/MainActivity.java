package com.example.sev_user.bookmanager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import crud.AddBook;
import crud.DetailAlarm;
import crud.DetailBook;
import database.Alarm;
import database.Book;
import database.DatabaseHelper;
import setting.SettingPreferences;
import time.EditPreferences;

public class MainActivity extends AppCompatActivity {
    // tab1_book
    private android.widget.SearchView mSearchViewBook;
    private ListView mListView_Book;
    private ArrayList<Book> mArrayList_Book;
    private BookAdapter mBookAdapter;
    // tab2_alarm;
    private AlarmAdapter mAlarmAdapter;
    private android.widget.SearchView mSearchViewAlarm;
    private ListView mListViewAlarm;
    private ArrayList<Alarm> mArrayList_Alarm;
    //
    private DatabaseHelper mDatabaseHelper;
    private ConvertString mConvertString = new ConvertString();
    public boolean result = false;

    TabHost tab = null;
    public static int flag_alarm = 0;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        loadTabs();
        init();
        tab1();
        tab2();
    }

    public void loadTabs() {
        tab = (TabHost) findViewById(android.R.id.tabhost);
        tab.setup();
        TabHost.TabSpec spec;
        // tab1 book;
        spec = tab.newTabSpec("t1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Books");
        tab.addTab(spec);
        // tab 2 alarm
        spec = tab.newTabSpec("t2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Alarm");
        tab.addTab(spec);
        // thiet lap tab khoi tao ban dau;
        tab.setCurrentTab(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    public void init() {
        mDatabaseHelper = new DatabaseHelper(this);
        // tab_book
        mSearchViewBook = (android.widget.SearchView) findViewById(R.id.sv_book);
        mListView_Book = (ListView) findViewById(R.id.lv_book);
        // tab_alarm
        mSearchViewAlarm = (android.widget.SearchView) findViewById(R.id.sv_alarm);
        mListViewAlarm = (ListView) findViewById(R.id.lv_alarm);

    }

    public void tab1() {
        //dataBook();
        result = UtilityPermission.checkPermission(MainActivity.this);
        Log.d("abc","Click tab1 = " + result);
        if (result) {
            mArrayList_Book = mDatabaseHelper.getAllBooks(pref.getString("sort_books", "Title"));
            Collections.sort(mArrayList_Book, new BookComperatorTitle());

            mBookAdapter = new BookAdapter(MainActivity.this, R.layout.book_layout, mArrayList_Book);
            mBookAdapter.notifyDataSetChanged();
            mListView_Book.setAdapter(mBookAdapter);

            // add su kien searchView;
            SearchViewBook();
            ViewDetailsBook();
        }

        //add permission using lib - Only call funtion
        //requestStoragePermission();
    }

    private void ViewDetailsBook() {
        mListView_Book.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                    Log.d("abc","Uncall it itemClick= " + result);
                    Log.d("abc","call it itemClick= ");

                    Book book = (Book) parent.getAdapter()
                            .getItem(pos);
                    Intent intent = new Intent(MainActivity.this,
                            DetailBook.class);

                    intent.putExtra("Title", book.getmTitle());
                    intent.putExtra("Author", book.getmAuthor());
                    intent.putExtra("Publisher", book.getmPublisher());
                    intent.putExtra("Category", book.getmCategory());
                    intent.putExtra("Price", book.getmPrice());
                    intent.putExtra("Describe", book.getmDescribe());
                    intent.putExtra("Image", book.getmImage());
                    intent.putExtra("Id", book.getmIdBook());

                    startActivity(intent);

            }
        });

        mListView_Book.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = (Book) parent.getAdapter()
                        .getItem(position);
                Toast.makeText(getApplicationContext(),
                        book.getmIdBook() + "--" + book.getmTitle(),
                        Toast.LENGTH_SHORT).show();
                AlertDialog myAlertDialog = BookAlertDialog(book
                        .getmIdBook());// transmit id_book;
                myAlertDialog.show();
                return true;
            }
        });
    }

    // create dialog when click onitem on listbook;
    private AlertDialog BookAlertDialog(int id_book) {
        final int Id_Book = id_book;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Lua Chon Chi Tiet");
        builder.setMessage("Chon muc");
        builder.setCancelable(false);
        // Create details book
        builder.setPositiveButton("Set Alarm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent myIntent = new Intent(MainActivity.this, EditPreferences.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("Id_Book", Id_Book);
                        flag_alarm = 1;
                        myIntent.putExtra("mypackage1", bundle);
                        startActivity(myIntent);
                    }
                });
        // tao nut chuyen sang set alarm book;
        builder.setNegativeButton("View Alarm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mArrayList_Alarm = mDatabaseHelper.getAlarmByIdBook(Id_Book);
                        Collections.sort(mArrayList_Alarm, new AlarmComparatorTime());
                        mAlarmAdapter = new AlarmAdapter(MainActivity.this, R.layout.reminder_layout, mArrayList_Alarm);
                        mListViewAlarm.setAdapter(mAlarmAdapter);
                        tab.setCurrentTab(1);
                    }
                });
        builder.setNeutralButton("Hủy", null);
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public void SearchViewBook() {
        mSearchViewBook.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String query_convert = mConvertString.convertStirng(query);
                ArrayList<Book> mBook = new ArrayList<Book>();

                for (Book book : mArrayList_Book) {
                    String authorName = mConvertString.convertStirng(book.getmAuthor());
                    String bookName = mConvertString.convertStirng(book.getmTitle());

                    if (bookName.contains(query_convert) || authorName.contains(query_convert)
                            && !mBook.contains(book)) {
                        mBook.add(book);
                    }
                }
                Collections.sort(mBook, new BookComperatorTitle());
                BookAdapter adapter = new BookAdapter(MainActivity.this, R.layout.book_layout, mBook);
                mListView_Book.setAdapter(adapter);
                return false;
            }

            //Suggesstion
            @Override
            public boolean onQueryTextChange(String newText) {
                String query_convert = mConvertString
                        .convertStirng(newText);
                ArrayList<Book> mBooks = new ArrayList<Book>();
                for (Book book : mArrayList_Book) {
                    String authorname = mConvertString
                            .convertStirng(book.getmAuthor());
                    String bookname = mConvertString.convertStirng(book
                            .getmTitle());
                    if ((bookname.contains(query_convert) || authorname
                            .contains(query_convert))
                            && (!mBooks.contains(book))) {
                        mBooks.add(book);
                    }
                }
                Collections.sort(mBooks, new BookComperatorTitle());
                BookAdapter adapter = new BookAdapter(
                        MainActivity.this, R.layout.book_layout, mBooks);
                mListView_Book.setAdapter(adapter);
                return false;
            }
        });

        mSearchViewBook.setOnCloseListener(new android.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                // load book on listview;
                mArrayList_Book = mDatabaseHelper.getAllBook();
                Collections.sort(mArrayList_Book, new BookComperatorTitle());
                mBookAdapter = new BookAdapter(MainActivity.this,
                        R.layout.book_layout, mArrayList_Book);
                mListView_Book.setAdapter(mBookAdapter);
                return false;
            }
        });
    }

    public void tab2() {
        //dataAlarm();
        // load alarm;
        mArrayList_Alarm = mDatabaseHelper.getAllAlarms();
        Collections.sort(mArrayList_Alarm, new AlarmComparatorTime());
        mAlarmAdapter = new AlarmAdapter(MainActivity.this, R.layout.reminder_layout,
                mArrayList_Alarm);
        mListViewAlarm.setAdapter(mAlarmAdapter);

        // add su kien serview;
        SearchViewAlarm();
        ViewDetailsAlarm();
    }

    private void SearchViewAlarm() {
        mSearchViewAlarm
                .setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        // TODO Auto-generated method stub
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        // TODO Auto-generated method stub
                        String newTextconvert = mConvertString
                                .convertStirng(newText);
                        ArrayList<Alarm> mAlarms = new ArrayList<Alarm>();
                        for (Alarm alarm : mArrayList_Alarm) {
                            String time = mConvertString.convertStirng(alarm
                                    .getmTime().toLowerCase());
                            String title = mConvertString
                                    .convertStirng(mDatabaseHelper.getBook(
                                            alarm.getmIdBook()).getmTitle());
                            if ((time.contains(newTextconvert) || title
                                    .contains(newTextconvert))
                                    && (!mAlarms.contains(alarm))) {
                                mAlarms.add(alarm);
                            }
                        }
                        Collections.sort(mAlarms, new AlarmComparatorTime());
                        AlarmAdapter adapter = new AlarmAdapter(
                                MainActivity.this, R.layout.reminder_layout,
                                mAlarms);
                        mListViewAlarm.setAdapter(adapter);
                        return false;
                    }
                });

        // close searchview;
        mSearchViewAlarm.setOnCloseListener(new android.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                // TODO Auto-generated method stub
                mArrayList_Alarm = mDatabaseHelper.getAllAlarms();
                mAlarmAdapter = new AlarmAdapter(MainActivity.this,
                        R.layout.reminder_layout, mArrayList_Alarm);
                mListViewAlarm.setAdapter(mAlarmAdapter);
                return false;
            }
        });
    }

    private void ViewDetailsAlarm() {
        // click on listAlarm de chon 1 alarm;
        mListViewAlarm
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Alarm mAlarm = (Alarm) parent.getAdapter().getItem(
                                position);
                        Intent intent = new Intent(MainActivity.this,
                                DetailAlarm.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("id_alarm", mAlarm.getmIdAlarm());
                        Log.d("Swipe right", mAlarm.getmIdAlarm()
                                + " thuc");
                        intent.putExtra("packet", bundle);
                        startActivity(intent);

                    }

                });
        mListViewAlarm
                .setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent,
                                                   View view, int position, long id) {
                        Alarm alarm = (Alarm) parent.getAdapter().getItem(
                                position);
                        AlertDialog myAlertDialog = AlarmAlertDialog(alarm
                                .getmIdAlarm());// transmit id_book;
                        myAlertDialog.show();
                        return false;
                    }
                });
    }

    private AlertDialog AlarmAlertDialog(int id_alarm) {
        final int IdAlarm = id_alarm;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Thiết lập tiêu đề hiển thị
        builder.setTitle("Lựa Chọn ");
        // Thiết lập thông báo hiển thị
        builder.setMessage("Chọn Mục");
        builder.setCancelable(false);
        // Tạo nút detaibook;
        builder.setPositiveButton("Xóa nhắc nhở",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mDatabaseHelper.deleteAlarm(IdAlarm);
                        mArrayList_Alarm = mDatabaseHelper.getAllAlarms();
                        Collections.sort(mArrayList_Alarm,
                                new AlarmComparatorTime());
                        mAlarmAdapter = new AlarmAdapter(MainActivity.this,
                                R.layout.reminder_layout, mArrayList_Alarm);
                        mListViewAlarm.setAdapter(mAlarmAdapter);
                        Toast.makeText(MainActivity.this, "Xóa thành công !!!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        // tao nut chuyen sang set alarm book;
        builder.setNegativeButton("Sửa nhắc nhở",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        flag_alarm = 2;
                        Intent myIntent = new Intent(MainActivity.this,
                                EditPreferences.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("Id_Book", IdAlarm);
                        myIntent.putExtra("mypackage1", bundle);
                        startActivity(myIntent);
                    }
                });
        // Tạo nút Hủy
        builder.setNeutralButton("Hủy", null);
        AlertDialog dialog = builder.create();
        return dialog;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(MainActivity.this, AddBook.class);
                startActivity(intent);
                break;
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this,
                        SettingPreferences.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // create data for book
    public void dataBook() {
        DatabaseHelper mDatabaseHelper = new DatabaseHelper(this);
        // them vai du lieu vao bang;
        mDatabaseHelper.createBook(new Book("trần văn chiều",
                "tin học đại cương", "nxb ha noi", "tin hoc", "15000d", "",
                "day lap tirnh c"));
        mDatabaseHelper.createBook(new Book(" Ngô Văn Ba", "Giáo trình PHP",
                "nxb bac ninh", "tin hoc", "45000d", "", "day lap tirnh php"));
        mDatabaseHelper.createBook(new Book("Hoàng Minh Sơn", "Android",
                "nxb ha noi", "tin hoc", "55000d", "",
                "day lap tirnh android can ban"));
        mDatabaseHelper.createBook(new Book("Nguyễn Tiến Sơn", "Vi Điều Khiển",
                "nxb giao duc", "tin hoc", "15000d", "",
                "day lap tirnh vi dieu khien"));
        mDatabaseHelper.createBook(new Book("Bguyeenx Văn Mậu",
                "toan cao cap tap 1", "nxb khoa hoc ki thuat", "tin hoc",
                "15000d", "", "day toan cao cap"));
        mDatabaseHelper.createBook(new Book("Trương Văn Ba",
                "tin hoc van phong", "nxb su that", "tin hoc", "65000d", "",
                "day tin hoc van phong"));
        mDatabaseHelper.createBook(new Book("Nguyễn Văn Can",
                "Toán Cao Cấp Tập 2", "nxb khoa hoc ki thuat", "tin hoc",
                "15000d", "", "day toan cao cap"));
        mDatabaseHelper.createBook(new Book("Hoàng Trung Hải",
                "Tin học van phòng 2", "nxb su that", "tin hoc", "65000d", "",
                "day tin hoc van phong"));
    }

    // create example data for alarm;
    public void dataAlarm() {
        DatabaseHelper mDatabaseHelper = new DatabaseHelper(this);
        mDatabaseHelper.createAlarm(new Alarm("17:35:02", 1));
        mDatabaseHelper.createAlarm(new Alarm("7:35:23", 2));
        mDatabaseHelper.createAlarm(new Alarm("6:35:07", 3));
        mDatabaseHelper.createAlarm(new Alarm("7:00:00", 4));
        mDatabaseHelper.createAlarm(new Alarm("1:35:12", 5));
        mDatabaseHelper.createAlarm(new Alarm("18:35:00", 6));
        mDatabaseHelper.createAlarm(new Alarm("19:35:09", 7));
        mDatabaseHelper.createAlarm(new Alarm("23:35:00", 8));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //boolean result = UtilityPermission.checkPermission(MainActivity.this);
        flag_alarm = 0;
        mArrayList_Alarm = mDatabaseHelper.getAllAlarms();
        Collections.sort(mArrayList_Alarm, new AlarmComparatorTime());
        mAlarmAdapter = new AlarmAdapter(MainActivity.this, R.layout.reminder_layout,
                mArrayList_Alarm);
        mListViewAlarm.setAdapter(mAlarmAdapter);
        // /Books
        mArrayList_Book = mDatabaseHelper.getAllBooks(pref.getString(
                "sort_books", "Title"));
        mBookAdapter = new BookAdapter(getApplicationContext(),
                R.layout.book_layout, mArrayList_Book);
        mListView_Book.setAdapter(mBookAdapter);
            // language
            // String lang = pref.getString("language", "en");
            // Locale locale = new Locale(lang);
            // Locale.setDefault(locale);
            // Configuration config = new Configuration();
            // config.locale = locale;
            // getBaseContext().getResources().updateConfiguration(config,
            // getBaseContext().getResources().getDisplayMetrics());
        //}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(permissions.length == 0){
            return;
        }
        boolean allPermissionsGranted = true;
        if(grantResults.length>0){
            for(int grantResult: grantResults){
                if(grantResult != PackageManager.PERMISSION_GRANTED){
                    allPermissionsGranted = false;
                    break;
                }
            }
        }

        if(!allPermissionsGranted){
            boolean somePermissionsForeverDenied = false;
            for(String permission: permissions){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, permission)){
                    //denied
                    Log.d("abc", "denied");
                }else{
                    if(ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED){
                        //allowed
                        Log.d("abc", "allow");
                    } else{
                        //set to never ask again
                        Log.d("abc", "never check");
                        somePermissionsForeverDenied = true;
                    }
                }
            }
            if (somePermissionsForeverDenied) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Permissions Required")
                        .setMessage("You have forcefully denied some of the required permissions " +
                                "for this action. Please open settings, go to permissions and allow them.")
                        .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", getPackageName(), null));
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivityForResult(intent, 100);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }
        } else {
            switch (requestCode) {
                case UtilityPermission.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        mArrayList_Book = mDatabaseHelper.getAllBooks(pref.getString("sort_books", "Title"));
                        Collections.sort(mArrayList_Book, new BookComperatorTitle());

                        mBookAdapter = new BookAdapter(MainActivity.this, R.layout.book_layout, mArrayList_Book);
                        mBookAdapter.notifyDataSetChanged();
                        mListView_Book.setAdapter(mBookAdapter);
                        SearchViewBook();
                        ViewDetailsBook();
                    } else {
                        ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SEND_SMS}, UtilityPermission.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            Log.d("Abc","test");
            mArrayList_Book = mDatabaseHelper.getAllBooks(pref.getString("sort_books", "Title"));
            Collections.sort(mArrayList_Book, new BookComperatorTitle());

            mBookAdapter = new BookAdapter(MainActivity.this, R.layout.book_layout, mArrayList_Book);
            mBookAdapter.notifyDataSetChanged();
            mListView_Book.setAdapter(mBookAdapter);
            SearchViewBook();
            ViewDetailsBook();
        }
    }

    private void requestStoragePermission() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SEND_SMS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            mArrayList_Book = mDatabaseHelper.getAllBooks(pref.getString("sort_books", "Title"));
                            Collections.sort(mArrayList_Book, new BookComperatorTitle());

                            mBookAdapter = new BookAdapter(MainActivity.this, R.layout.book_layout, mArrayList_Book);
                            mBookAdapter.notifyDataSetChanged();
                            mListView_Book.setAdapter(mBookAdapter);

                            // add su kien searchView;
                            SearchViewBook();
                            ViewDetailsBook();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    static class UtilityPermission {
        public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

        public static boolean checkPermission(final Context context) {
            int currentAPIVersion = Build.VERSION.SDK_INT;

            if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)
                        || ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.SEND_SMS)) {
                        Log.d("abc","never checked");
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true)
                                .setTitle("Permission necessary")
                                .setMessage("External storage permission is necessary")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                                    }
                                });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();
                    } else {
                        Log.d("abc","normal checked");
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }
}
