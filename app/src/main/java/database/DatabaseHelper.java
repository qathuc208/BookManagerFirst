package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by sev_user on 12/12/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    // Logcat tag
    // private static final String LOG = "DatabaseHelper";

    // Database Version
    public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "booksManager";

    // Table Names
    private static final String TABLE_BOOK = "book";
    private static final String TABLE_ALARM = "alarm";

    // BOOK table collumn name
    private static final String ID = "id";// same id table Alarm
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String PUBLISHER = "publisher";
    private static final String CATEGORY = "category";
    private static final String PRICE = "price";
    private static final String DESCRIBE = "describe";
    private static final String IMAGE_PATH = "image";

    // ALARM table collumn name
    private static final String ID_BOOK = "id_book";
    private static final String TIME = "time";

    private static final String CREATE_TABLE_BOOK = "create table "
            + TABLE_BOOK + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TITLE + " char(100) ," + AUTHOR + " char(50), " + PUBLISHER
            + " char(50)," + CATEGORY + " char(50)," + PRICE + " char(20),"
            + IMAGE_PATH + " char(100)," + DESCRIBE + " char(200))";
    private static final String CREATE_TABLE_ALARM = "create table "
            + TABLE_ALARM
            + "("
            + ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TIME
            + " char(16),"
            + ID_BOOK
            + " integer, foreign key(id_book) references book(id) ON DELETE CASCADE ON UPDATE CASCADE)";



    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_BOOK);
        sqLiteDatabase.execSQL(CREATE_TABLE_ALARM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOK);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARM);

        onCreate(sqLiteDatabase);
    }

    // Insert new book
    public long createBook(Book book) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TITLE, book.getmTitle());
        values.put(AUTHOR, book.getmAuthor());
        values.put(PUBLISHER, book.getmPublisher());
        values.put(CATEGORY, book.getmCategory());
        values.put(PRICE, book.getmPrice());
        values.put(DESCRIBE, book.getmDescribe());
        values.put(IMAGE_PATH, book.getmImage());

        return sqLiteDatabase.insert(TABLE_BOOK, null, values);
    }

    // Insert a Alarm
    public long createAlarm(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TIME, alarm.getmTime());
        contentValues.put(ID_BOOK, alarm.getmIdBook());

        return db.insert(TABLE_ALARM, null, contentValues);
    }

    // Get book

    public Book getBook(int id_book) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Command Query
        String selectQuery = "SELECT  * FROM " + TABLE_BOOK + " WHERE " + ID
                + " = " + id_book;

        Cursor c = db.rawQuery(selectQuery, null);
        if(c != null)
            c.moveToFirst();

        Book book = new Book();
        book.setmAuthor(c.getString(c.getColumnIndex(AUTHOR)));
        book.setmCategory(c.getString(c.getColumnIndex(CATEGORY)));
        book.setmDescribe(c.getString(c.getColumnIndex(DESCRIBE)));
        book.setmImage(c.getString(c.getColumnIndex(IMAGE_PATH)));
        book.setmPrice(c.getString(c.getColumnIndex(PRICE)));
        book.setmPublisher(c.getString(c.getColumnIndex(PUBLISHER)));
        book.setmIdBook(id_book);

        return  book;
    }

    // Get All book
    public ArrayList<Book> getAllBook() {
        // Khai bao 1 arraylist chua tat ca book
        ArrayList<Book> arrayListBook = new ArrayList<Book>();

        String selectQuery = "SELECT  * FROM " + TABLE_BOOK;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if(c.moveToFirst()) {
            do {
                Book book = new Book();
                book.setmAuthor(c.getString(c.getColumnIndex(AUTHOR)));
                book.setmCategory(c.getString(c.getColumnIndex(CATEGORY)));
                book.setmDescribe(c.getString(c.getColumnIndex(DESCRIBE)));
                book.setmImage(c.getString(c.getColumnIndex(IMAGE_PATH)));
                book.setmPrice(c.getString(c.getColumnIndex(PRICE)));
                book.setmPublisher(c.getString(c.getColumnIndex(PUBLISHER)));
                book.setmTitle(c.getString(c.getColumnIndex(TITLE)));
                book.setmIdBook(c.getInt(c.getColumnIndex(ID)));
                /** adding to todo list */
                arrayListBook.add(book);
            } while (c.moveToNext());
        }

        return arrayListBook;
    }

    // Su dung orderBy

    public ArrayList<Book> getAllBooks(String orderBy) {
        // khai bao 1 arraylist de luu book;
        ArrayList<Book> books = new ArrayList<Book>();
        String selectQuery = "SELECT  * FROM " + TABLE_BOOK + " ORDER BY "
                + orderBy;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Book book = new Book();
                book.setmAuthor(c.getString(c.getColumnIndex(AUTHOR)));
                book.setmCategory(c.getString(c.getColumnIndex(CATEGORY)));
                book.setmDescribe(c.getString(c.getColumnIndex(DESCRIBE)));
                book.setmImage(c.getString(c.getColumnIndex(IMAGE_PATH)));
                book.setmPrice(c.getString(c.getColumnIndex(PRICE)));
                book.setmPublisher(c.getString(c.getColumnIndex(PUBLISHER)));
                book.setmTitle(c.getString(c.getColumnIndex(TITLE)));
                book.setmIdBook(c.getInt(c.getColumnIndex(ID)));
                // adding to todo list
                books.add(book);
            } while (c.moveToNext());
        }

        return books;
    }

    // Get an alarm
    public  Alarm getAlarm(int id_arlam) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_ALARM + "WHERE " + ID
                + " = " + id_arlam;

        Cursor c = db.rawQuery(selectQuery, null);
        if(c != null)
            c.moveToFirst();
        Alarm alarm = new Alarm();
        alarm.setmIdAlarm(id_arlam);
        alarm.setmIdBook(c.getInt(c.getColumnIndex(ID_BOOK)));
        alarm.setmTime(c.getString(c.getColumnIndex(TIME)));

        return alarm;
    }

    // Get all alarms
    public ArrayList<Alarm> getAllAlarms() {
        ArrayList<Alarm> alrms = new ArrayList<Alarm>();
        String selectQuery = "SELECT  * FROM " + TABLE_ALARM;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Alarm alarm = new Alarm();
                alarm.setmIdAlarm(c.getInt(c.getColumnIndex(ID)));
                alarm.setmIdBook(c.getInt(c.getColumnIndex(ID_BOOK)));
                alarm.setmTime(c.getString(c.getColumnIndex(TIME)));
                // adding to todo list
                alrms.add(alarm);
            } while (c.moveToNext());
        }

        return alrms;
    }

    // Get all alarm
    public ArrayList<Alarm> getAlarmByIdBook(int id_book) {
        ArrayList<Alarm> alarms = new ArrayList<Alarm>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_ALARM + " WHERE "
                + ID_BOOK + " = " + id_book;

        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Alarm alarm = new Alarm();
                alarm.setmIdAlarm(c.getInt(c.getColumnIndex(ID)));
                alarm.setmIdBook(c.getInt(c.getColumnIndex(ID_BOOK)));
                alarm.setmTime(c.getString(c.getColumnIndex(TIME)));
                alarms.add(alarm);
            } while (c.moveToNext());
        }
        return alarms;
    }

    // Edit a book, alarm
    public int updateBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(AUTHOR, book.getmAuthor());
        contentValues.put(CATEGORY, book.getmCategory());
        contentValues.put(DESCRIBE, book.getmDescribe());
        contentValues.put(IMAGE_PATH, book.getmImage());
        contentValues.put(PRICE, book.getmPrice());
        contentValues.put(PUBLISHER, book.getmPublisher());
        contentValues.put(TITLE, book.getmTitle());

        return db.update(TABLE_BOOK, contentValues, ID + " =? ",
                new String[] {String.valueOf(book.getmIdBook())});
    }

    public int updateAlarm(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TIME, alarm.getmTime());
        contentValues.put(ID_BOOK, alarm.getmIdBook());

        return db.update(TABLE_ALARM, contentValues, ID + " =? ",
                new String[] { String.valueOf(alarm.getmIdAlarm()) });
    }

    // Delete a book, an alarm
    public void deleteAlarm(int id_alarm) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_ALARM, ID + "=" + id_alarm, null);
        return;
    }

    public void deleteBook(int id_book) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_BOOK, ID + "=" + id_book, null);
        return;
    }
}
