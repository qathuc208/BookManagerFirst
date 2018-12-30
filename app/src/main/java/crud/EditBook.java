package crud;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sev_user.bookmanager.R;

import database.Book;
import database.DatabaseHelper;
import utility.Utility;

public class EditBook extends AppCompatActivity {
    EditText edtTitle, edtAuthor, edtPublisher, edtCategory, edtPrice,
            edtDescribe;
    ImageView imgCover;
    Button btnDone, btnCancel;
    static final int PICK_IMAGE = 1;
    private static int CHANGED = 2;
    String imgPath;
    private DatabaseHelper helper;
    int id_book;

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

        edtCategory.setText(getIntent().getStringExtra("Category"));
        edtDescribe.setText(getIntent().getStringExtra("Describe"));
        edtPublisher.setText(getIntent().getStringExtra("Publisher"));
        edtPrice.setText("" + getIntent().getStringExtra("Price"));

        edtTitle.setText(getIntent().getStringExtra("Title"));
        edtAuthor.setText(getIntent().getStringExtra("Author"));
        imgPath = getIntent().getStringExtra("Image");
        id_book = getIntent().getIntExtra("Id", 1);
        Bitmap bitmap = Utility.converBitmap(imgPath);
        imgCover.setImageBitmap(bitmap);
        btnDone = (Button) findViewById(R.id.btnDone);
        btnCancel = (Button) findViewById(R.id.btnCancle);

        imgCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(intent, "Select Picture"),
                        PICK_IMAGE);
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
                book.setmIdBook(id_book);
                helper.updateBook(book);
                setResult(CHANGED);
                Utility.MakeToast(getApplicationContext(), "Updated!",
                        Toast.LENGTH_SHORT);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_IMAGE:
                if (data != null && data.getData() != null) {
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
                    // edtTitle.setText(imgPath);
                    imgCover.setImageBitmap(bitmap);
                    cursor.close();
                }
                return;
        }
    }
}
