package com.gcit.lms.domain;

import java.util.List;

public class Book {
	private int bookId;
	private String title;
	private Publisher publisher;	
	private List<Author> authors;
	private List<Genre> genres;
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Publisher getPublisher() {
		return publisher;
	}
	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}
	public List<Author> getAuthors() {
		return authors;
	}
	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}
	public List<Genre> getGenres() {
		return genres;
	}
	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}
	@Override
	public String toString(){
		String result ="";
		result+=title;
		if (authors.size()>0){
			result += " by " + authors.get(0).toString();
			for(int i = 1; i< authors.size();i++){
				if(i==authors.size()-1){
					result+= ", ";
				}
				else{
					result+= " & ";
				}
				result+= authors.get(i).toString();			
			}
		}
		return result;
	}
}