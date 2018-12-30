package crud;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sev_user.bookmanager.R;

import database.Book;
import database.DatabaseHelper;
import utility.Utility;

public class DetailBook extends Activity {
    TextView txtTitle, txtAuthor, txtPublisher, txtPrice, txtCategory,
            txtDescribe;
    ImageView img;
    Button btnEdit, btnDelete;
    int id_book;
    String path;
    private static int CHANGED = 2;
    DatabaseHelper helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        helper = new DatabaseHelper(getApplicationContext());
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnEdit = (Button) findViewById(R.id.edit);
        txtPublisher = (TextView) findViewById(R.id.textview_publisher);
        txtCategory = (TextView) findViewById(R.id.textview_type);
        txtDescribe = (TextView) findViewById(R.id.edittext_des);
        txtTitle = (TextView) findViewById(R.id.textview_namebook);
        txtAuthor = (TextView) findViewById(R.id.textview_author);
        txtPrice = (TextView) findViewById(R.id.textview_price);
        img = (ImageView) findViewById(R.id.image_view);

        txtCategory.setText(getIntent().getStringExtra("Category"));
        txtDescribe.setText(getIntent().getStringExtra("Describe"));
        txtPublisher.setText(getIntent().getStringExtra("Publisher"));
        txtPrice.setText("" + getIntent().getStringExtra("Price"));

        txtTitle.setText(getIntent().getStringExtra("Title"));
        txtAuthor.setText(getIntent().getStringExtra("Author"));
        path = getIntent().getStringExtra("Image");
        id_book = getIntent().getIntExtra("Id", 1);
        Bitmap bitmap = Utility.converBitmap(path);
        img.setImageBitmap(bitmap);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteBook(id_book);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),
                        EditBook.class);
                intent.putExtra("Title", txtTitle.getText().toString());
                intent.putExtra("Author", txtAuthor.getText().toString());
                intent.putExtra("Publisher", txtPublisher.getText().toString());
                intent.putExtra("Category", txtCategory.getText().toString());
                intent.putExtra("Price", txtPrice.getText().toString());
                intent.putExtra("Describe", txtDescribe.getText().toString());
                intent.putExtra("Image", path);
                intent.putExtra("Id", id_book);

                startActivityForResult(intent, CHANGED);
            }
        });
    }

    private void DeleteBook(int id_book) {
        final int id = id_book;
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Delete: " + txtTitle.getText().toString());
        dialog.setMessage("Delete this book and all related alarms!!!");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                helper.deleteBook(id);
                Utility.MakeToast(getApplicationContext(), "Delete Succesful",
                        Toast.LENGTH_SHORT);

                finish();
            }
        });
        dialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CHANGED) {
            Book book = new Book();
            book = helper.getBook(id_book);
            txtAuthor.setText(book.getmAuthor());
            txtCategory.setText(book.getmCategory());
            txtDescribe.setText(book.getmDescribe());
            txtPrice.setText(book.getmPrice());
            txtPublisher.setText(book.getmPublisher());
            txtTitle.setText(book.getmTitle());
            path = book.getmImage();
            Bitmap bitmap = Utility.converBitmap(path);
            img.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int id_book = getIntent().getIntExtra("id_book", 0);
        if (id_book != 0) {
            Book book = helper.getBook(id_book);
            txtAuthor.setText(book.getmAuthor());
            txtCategory.setText(book.getmCategory());
            txtDescribe.setText(book.getmDescribe());
            txtPrice.setText(book.getmPrice());
            txtPublisher.setText(book.getmPublisher());
            txtTitle.setText(book.getmTitle());

            path = book.getmImage();
            Bitmap bitmap = Utility.converBitmap(path);
            img.setImageBitmap(bitmap);
        }
    }
}
