package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.gcit.lms.domain.Borrower;
import com.gcit.lms.domain.Genre;
@SuppressWarnings("unchecked")
public class GenreDAO extends BaseDAO<Genre>{
	public GenreDAO(Connection conn) throws Exception {
		super(conn);
	}
	public void create(Genre genre) throws Exception{
		int id = saveWithID("INSERT INTO tbl_genre (genre_name) VALUES(?)", new Object[] {genre.getGenreName()});
		if(id!=-1){
			genre.setGenreId(id);
		}
	}
	public void update(Genre genre) throws Exception{
		save("UPDATE tbl_genre SET genre_name = ? WHERE genre_id = ?", new Object[] {genre.getGenreName(),genre.getGenreId()});

	}
	public void delete(Genre genre) throws Exception{
		save("DELETE FROM tbl_genre WHERE genre_id = ?", new Object[] {genre.getGenreId()});
	}
	public List<Genre> readAll() throws Exception{
		return (List<Genre>) read("SELECT * FROM tbl_genre", new Object[] {});
	}

	public Genre readOne(int genreId) throws Exception{
		List<Genre> genres = (List<Genre>) read("SELECT * FROM tbl_genre WHERE genre_id = ?", new Object[] {genreId});
		if(genres!=null && genres.size()>0){
			return genres.get(0);
		}
		return null;
	}

	@Override
	public List<Genre> extractData(ResultSet rs) throws Exception {
		return extractDataFirstLevel(rs);
	}
	@Override
	public List<Genre> extractDataFirstLevel(ResultSet rs) throws Exception {
		List<Genre> genres = new ArrayList<Genre>();

		while(rs.next()){
			Genre genre = new Genre();
			genre.setGenreId(rs.getInt("genre_id"));
			genre.setGenreName(rs.getString("genre_name"));
			genres.add(genre);
		}

		return genres;
	}
	@Override
	public List<Genre> readPage(int pageNum) throws Exception {
		int start = 0;
		if(pageNum>1){
			start = (pageNum*10)+1;			 
		}		
		List<Genre> result = (List<Genre>) read("SELECT * FROM tbl_genre ORDER BY genre_name ASC LIMIT ?,10", new Object[] {start});
		return result;
	}

	@Override
	public List<Genre> search(String query, int pageNum) throws Exception {
		int start = 0;
		if(pageNum>1){
			start = (pageNum*10)+1;			 
		}
		String s = "%"+query+"%";
		List<Genre> genres = (List<Genre>) read("SELECT * FROM tbl_genre WHERE genre_name LIKE ? ORDER BY genre_name ASC LIMIT ?,10", new Object[] {s,start});
		return genres;
	}
	
	public int getPageCount(String query) throws Exception{
		String s = "%"+query+"%";
		List<Genre> genres = (List<Genre>) read("SELECT * FROM tbl_genre WHERE genre_name LIKE ?", new Object[] {s});
		return Math.abs((genres.size()-1)/10 +1);
	}

}
