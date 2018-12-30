package database;

/**
 * Created by sev_user on 12/12/2017.
 */

public class Alarm {

    private int mIdAlarm;
    private String mTime;
    private int mIdBook;

    public Alarm() {
    }

    public void setmIdAlarm(int mIdAlarm) {
        this.mIdAlarm = mIdAlarm;
    }

    public int getmIdAlarm() {
        return mIdAlarm;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public int getmIdBook() {
        return mIdBook;
    }

    public void setmIdBook(int mIdBook) {
        this.mIdBook = mIdBook;
    }

    public Alarm(String mTime, int mIdBook) {
        super();
        this.mTime = mTime;
        this.mIdBook = mIdBook;
    }
}
