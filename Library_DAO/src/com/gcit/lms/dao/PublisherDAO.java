package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.gcit.lms.domain.Publisher;
@SuppressWarnings("unchecked")
public class PublisherDAO extends BaseDAO<Publisher> {

	public PublisherDAO(Connection conn) throws Exception {
		super(conn);
	}

	public void create(Publisher pub) throws Exception {
		int pubId = saveWithID("INSERT INTO tbl_publisher (publisherName, publisherAddress, publisherPhone) VALUES(?, ?, ?)",
				new Object[] { pub.getPublisherName(), pub.getPublisherAddress(), pub.getPublisherPhone() });
		if(pubId != -1){
			pub.setPublisherId(pubId);
		}
	}
	
	public void update(Publisher publisher) throws Exception {
		save("UPDATE tbl_publisher SET publisherName = ?, publisherAddress = ?, publisherPhone = ? WHERE publisherId = ?",
				new Object[] { publisher.getPublisherName(), publisher.getPublisherAddress(), publisher.getPublisherPhone(), publisher.getPublisherId() });
	}

	public void delete(Publisher publisher) throws Exception {
		save("DELETE FROM tbl_publisher WHERE publisherId = ?",
				new Object[] { publisher.getPublisherId() });
	}
	
	public List<Publisher> readAll() throws Exception{
		return (List<Publisher>) read("SELECT * FROM tbl_publisher", null);		
	}

	public Publisher readOne(int pubId) throws Exception {
		List<Publisher> publishers = (List<Publisher>) read("SELECT * FROM tbl_publisher WHERE publisherId = ?", new Object[] {pubId});
		if(publishers!=null && publishers.size()>0){
			return publishers.get(0);
		}
		return null;		
	}

	@Override
	public List<Publisher> extractData(ResultSet rs) throws Exception {
		return extractDataFirstLevel(rs);
	}

	@Override
	public List<Publisher> extractDataFirstLevel(ResultSet rs) throws Exception {
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
}
