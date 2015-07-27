package com.gcit.lms.domain;

public class Publisher {
	private Integer publisherId;
	private String publisherName;
	private String publisherAddress;
	private String publisherPhone;
	//may need a variable for Publisher's books
	
	public Integer getPublisherId() {
		return publisherId;
	}
	public void setPublisherId(Integer publisherId) {
		this.publisherId = publisherId;
	}
	public String getPublisherName() {
		return publisherName;
	}
	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}
	public String getPublisherAddress() {
		return publisherAddress;
	}
	public void setPublisherAddress(String publisherAddress) {
		this.publisherAddress = publisherAddress;
	}
	public String getPublisherPhone() {
		return publisherPhone;
	}
	public void setPublisherPhone(String publisherPhone) {
		this.publisherPhone = publisherPhone;
	}
	@Override 
	public String toString(){
		return publisherName;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((publisherAddress == null) ? 0 : publisherAddress.hashCode());
		result = prime * result + publisherId;
		result = prime * result
				+ ((publisherName == null) ? 0 : publisherName.hashCode());
		result = prime * result
				+ ((publisherPhone == null) ? 0 : publisherPhone.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Publisher other = (Publisher) obj;
		if (publisherAddress == null) {
			if (other.publisherAddress != null)
				return false;
		} else if (!publisherAddress.equals(other.publisherAddress))
			return false;
		if (publisherId != other.publisherId)
			return false;
		if (publisherName == null) {
			if (other.publisherName != null)
				return false;
		} else if (!publisherName.equals(other.publisherName))
			return false;
		if (publisherPhone == null) {
			if (other.publisherPhone != null)
				return false;
		} else if (!publisherPhone.equals(other.publisherPhone))
			return false;
		return true;
	}
	
}
