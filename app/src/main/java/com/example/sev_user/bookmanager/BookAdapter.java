package com.example.sev_user.bookmanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import database.Book;
import utility.Utility;

/**
 * Created by Administrator on 12/12/2017.
 */

public class BookAdapter extends ArrayAdapter<Book>{
    private ArrayList<Book> mdata;
    private LayoutInflater mlayLayoutInflater;

    public BookAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Book> mArrayList_Book) {
        super(context, resource, mArrayList_Book);
        this.mdata = mArrayList_Book;
        this.mlayLayoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null)
            view =mlayLayoutInflater.inflate(R.layout.book_layout, null);

        Book book = mdata.get(position);
        if (book != null) {
            /**title*/
            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_name.setText("Title:"+book.getmTitle().toString());
            /**author*/
            TextView tv_author = (TextView) view.findViewById(R.id.tv_author);
            tv_author.setText("Author:"+book.getmAuthor().toString());
            /**image*/
            ImageView imageView = (ImageView)view.findViewById(R.id.im_book);
            imageView.setImageBitmap(Utility.converBitmap(book.getmImage()));
        }
        return view;
    }
}
