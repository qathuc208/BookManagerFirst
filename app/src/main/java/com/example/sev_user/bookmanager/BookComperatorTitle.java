package com.example.sev_user.bookmanager;

import java.util.Comparator;

import database.Book;

public class BookComperatorTitle implements Comparator<Book>{

	@Override
	public int compare(Book b1, Book b2) {
		// TODO Auto-generated method stub
		return b1.getmTitle().compareToIgnoreCase(b2.getmTitle());
	}
}
