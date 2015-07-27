package com.gcit.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.lms.domain.Publisher;
public class PublisherDAO extends BaseDAO<Publisher> {

	public void create(Publisher pub) {
		int pubId = saveWithID("INSERT INTO tbl_publisher (publisherName, publisherAddress, publisherPhone) VALUES(?, ?, ?)",
				new Object[] { pub.getPublisherName(), pub.getPublisherAddress(), pub.getPublisherPhone() });
		if(pubId != -1){
			pub.setPublisherId(pubId);
		}
	}
	
	public void update(Publisher publisher){
		template.update("UPDATE tbl_publisher SET publisherName = ?, publisherAddress = ?, publisherPhone = ? WHERE publisherId = ?",
				new Object[] { publisher.getPublisherName(), publisher.getPublisherAddress(), publisher.getPublisherPhone(), publisher.getPublisherId() });
	}

	public void delete(Publisher publisher) throws Exception {
		template.update("DELETE FROM tbl_publisher WHERE publisherId = ?",
				new Object[] { publisher.getPublisherId() });
	}
	
	public List<Publisher> readAll() {
		return  template.query("SELECT * FROM tbl_publisher", this);		
	}

	public Publisher readOne(int pubId) {
		List<Publisher> publishers =  template.query("SELECT * FROM tbl_publisher WHERE publisherId = ?", new Object[] {pubId}, this);
		if(publishers!=null && publishers.size()>0){
			return publishers.get(0);
		}
		return null;		
	}

	@Override
	public List<Publisher> extractData(ResultSet rs) throws SQLException {
		List<Publisher> publishers = new ArrayList<Publisher>();		
		while(rs.next()){
			Publisher pub = new Publisher();
			pub.setPublisherId(rs.getInt("publisherId"));
			pub.setPublisherName(rs.getString("publisherName"));
			pub.setPublisherAddress(rs.getString("publisherAddress"));
			pub.setPublisherPhone(rs.getString("publisherPhone"));			
			publishers.add(pub);
		}
		return publishers;
	}
	

	@Override
	public List<Publisher> search(String query, int pageNum){
		int start = 0;
		if(pageNum>1){
			start = (pageNum*10)+1;			 
		}
		System.out.println("Searching page #" +pageNum+"[from "+start+" to "+start+10);
		String s = "%"+query+"%";
		List<Publisher> pubs =  template.query("SELECT * FROM tbl_publisher WHERE publisherName LIKE ?  ORDER BY publisherName ASC LIMIT ?,10", new Object[] {s,start}, this);
		return pubs;
	}
	
	public int getPageCount(String query) {
		String s = "%"+query+"%";
		List<Publisher> pubs =  template.query("SELECT * FROM tbl_publisher WHERE publisherName LIKE ?", new Object[] {s}, this);
		return Math.abs((pubs.size()-1)/10 +1);
	}

	@Override
	public List<Publisher> readFirstLevel(String query, Object[] vals) {
		return template.query(query, vals, this);	
	}
}
