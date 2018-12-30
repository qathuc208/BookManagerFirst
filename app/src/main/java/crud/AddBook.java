package crud;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sev_user.bookmanager.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import database.Book;
import database.DatabaseHelper;
import utility.Utility;

public class AddBook extends Activity {
    private static final int MAX_IMAGE_DIMENSION = 40;
    EditText edtTitle, edtAuthor, edtPublisher, edtCategory, edtPrice,
            edtDescribe;
    ImageView imgCover;
    Button btnDone, btnCancel;
    static final int PICK_IMAGE = 1;
    String imgPath;
    private DatabaseHelper helper;
    private int SELECT_FILE = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addbook);

        helper = new DatabaseHelper(getApplicationContext());

        edtTitle = (EditText) findViewById(R.id.edtTitle);
        edtAuthor = (EditText) findViewById(R.id.edtAuthor);
        edtPublisher = (EditText) findViewById(R.id.edtPublisher);
        edtCategory = (EditText) findViewById(R.id.edtCategory);
        edtPrice = (EditText) findViewById(R.id.edtPrice);
        edtDescribe = (EditText) findViewById(R.id.edtDescribe);

        imgCover = (ImageView) findViewById(R.id.imgAddBook);

        btnDone = (Button) findViewById(R.id.btnDone);
        btnCancel = (Button) findViewById(R.id.btnCancle);

        imgCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select File"),PICK_IMAGE);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                Book book = new Book();
                book.setmAuthor(edtAuthor.getText().toString());
                book.setmCategory(edtCategory.getText().toString());
                book.setmDescribe(edtDescribe.getText().toString());
                book.setmImage(imgPath);
                book.setmPrice(edtPrice.getText().toString());
                book.setmPublisher(edtPublisher.getText().toString());
                book.setmTitle(edtTitle.getText().toString());

                helper.createBook(book);
                Utility.MakeToast(getApplicationContext(),
                        "Insert Successful!", Toast.LENGTH_SHORT);
                edtAuthor.setText("");
                edtCategory.setText("");
                edtDescribe.setText("");
                imgPath = "";
                edtPrice.setText("");
                edtPublisher.setText("");
                imgCover.setImageResource(R.drawable.icon);
                edtTitle.setText("");
            }
        });
    }

    public int getOrienttation(Context context, Uri photoUri) {
        Cursor cursor = getContentResolver()
                .query(photoUri,
                        new String[] { android.provider.MediaStore.Images.ImageColumns.DATA },
                        null, null, null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public Bitmap getCorrectlyOrientedImage(Context context, Uri photoUri) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;
        int orientation = getOrienttation(context, photoUri);

        Log.d("abc","orientation = " + orientation);

        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > MAX_IMAGE_DIMENSION || rotatedHeight > MAX_IMAGE_DIMENSION) {
            float widthRatio = ((float) rotatedWidth) / ((float) MAX_IMAGE_DIMENSION);
            float heightRatio = ((float) rotatedHeight) / ((float) MAX_IMAGE_DIMENSION);
            float maxRatio = Math.max(widthRatio, heightRatio);

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();

        /*
         * if the orientation is not 0 (or -1, which means we don't know), we
         * have to do a rotation.
         */
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                    srcBitmap.getHeight(), matrix, true);
        }

        return srcBitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case  PICK_IMAGE:
                if (data != null && data.getData() != null) {
                    Log.d("abc", "Pick Image");
                    Uri _uri = data.getData();
                    // User had pick an image.
                    Cursor cursor = getContentResolver()
                            .query(_uri,
                                    new String[] { android.provider.MediaStore.Images.ImageColumns.DATA },
                                    null, null, null);
                    cursor.moveToFirst();

                    // Link to the image
                    imgPath = cursor.getString(0);
                    Bitmap bitmap = Utility.converBitmap(imgPath);
                    imgCover.setImageBitmap(bitmap);
                    // edtTitle.setText(imgPath);
                   /* try {
                        imgCover.setImageBitmap(getCorrectlyOrientedImage(getApplicationContext(), data.getData()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    cursor.close();
                }
                return;
        }
    }
}
