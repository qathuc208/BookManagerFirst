package crud;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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

import database.Book;
import database.DatabaseHelper;
import utility.Utility;

public class AddBook extends Activity {
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
                    Log.d("abc", "Pickture path = " + imgPath);

                    Bitmap bitmap = Utility.converBitmap(imgPath);
                    // edtTitle.setText(imgPath);
                    imgCover.setImageBitmap(bitmap);
                    cursor.close();
                }
                return;
        }
    }
}
