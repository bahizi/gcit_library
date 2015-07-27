package com.gcit.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.lms.domain.Genre;

public class GenreDAO extends BaseDAO<Genre>{
	public void create(Genre genre) {
		int id = saveWithID("INSERT INTO tbl_genre (genre_name) VALUES(?)", new Object[] {genre.getGenreName()});
		if(id!=-1){
			genre.setGenreId(id);
		}
	}
	public void update(Genre genre) {
		template.update("UPDATE tbl_genre SET genre_name = ? WHERE genre_id = ?", new Object[] {genre.getGenreName(),genre.getGenreId()});

	}
	public void delete(Genre genre) {
		template.update("DELETE FROM tbl_genre WHERE genre_id = ?", new Object[] {genre.getGenreId()});
	}
	public List<Genre> readAll() {
		return  template.query("SELECT * FROM tbl_genre", new Object[] {}, this);
	}

	public Genre readOne(int genreId) {
		List<Genre> genres =  template.query("SELECT * FROM tbl_genre WHERE genre_id = ?", new Object[] {genreId}, this);
		if(genres!=null && genres.size()>0){
			return genres.get(0);
		}
		return null;
	}

	@Override
	public List<Genre> extractData(ResultSet rs) throws SQLException {
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
	public List<Genre> search(String query, int pageNum){
		int start = 0;
		if(pageNum>1){
			start = (pageNum*10)+1;			 
		}
		String s = "%"+query+"%";
		List<Genre> genres =  template.query("SELECT * FROM tbl_genre WHERE genre_name LIKE ? ORDER BY genre_name ASC LIMIT ?,10", new Object[] {s,start}, this);
		return genres;
	}
	
	public int getPageCount(String query) {
		String s = "%"+query+"%";
		List<Genre> genres =  template.query("SELECT * FROM tbl_genre WHERE genre_name LIKE ?", new Object[] {s}, this);
		return Math.abs((genres.size()-1)/10 +1);
	}
	@Override
	public List<Genre> readFirstLevel(String query, Object[] vals) {
		// TODO Auto-generated method stub
		return null;
	}

}
