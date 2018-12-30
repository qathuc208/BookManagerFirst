package database;

/**
 * Created by sev_user on 12/12/2017.
 */

public class Book {
    private String mAuthor;
    private String mTitle;
    private String mPublisher;
    private String mCategory;
    private String mPrice;
    private String mImage;
    private String mDescribe;
    private int mIdBook;

    public Book() {
    }

    public Book(String mAuthor, String mTitle, String mPublisher,
                String mCategory, String mPrice, String mImagePath, String mDescribe) {
        super();
        this.mAuthor = mAuthor;
        this.mTitle = mTitle;
        this.mPublisher = mPublisher;
        this.mCategory = mCategory;
        this.mPrice = mPrice;
        this.mImage = mImagePath;
        this.mDescribe = mDescribe;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmPublisher() {
        return mPublisher;
    }

    public void setmPublisher(String mPublisher) {
        this.mPublisher = mPublisher;
    }

    public String getmCategory() {
        return mCategory;
    }

    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public String getmDescribe() {
        return mDescribe;
    }

    public void setmDescribe(String mDescribe) {
        this.mDescribe = mDescribe;
    }

    public int getmIdBook() {
        return mIdBook;
    }

    public void setmIdBook(int mIdBook) {
        this.mIdBook = mIdBook;
    }
}
