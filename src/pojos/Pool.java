package pojos;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("serial")
public class Pool implements Serializable{
private String id;
private boolean is_active;
private User hostUser;
private Address sourceAddress;
private Address destinationAddress;
private Time startTime_pickup;
private Time startTime_drop;
private Time officeTime_pickup;
private Time officeTime_drop;
private String comments;
private Integer numberOfMembers;
private Integer max_members;
private Integer costPerMonth;
private Car car;
private Boolean isAvailable;
private Timestamp modifyDate;
private Collection <User> participants = new ArrayList();
public Collection<User> getParticipants() {
	return participants;
}
public void setParticipants(Collection<User> participants) {
	this.participants = participants;
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public boolean isIs_active() {
	return is_active;
}
public void setIs_active(boolean is_active) {
	this.is_active = is_active;
}
public User getHostUser() {
	return hostUser;
}
public void setHostUser(User hostUser) {
	this.hostUser = hostUser;
}
public Address getSourceAddress() {
	return sourceAddress;
}
public void setSourceAddress(Address sourceAddress) {
	this.sourceAddress = sourceAddress;
}
public Address getDestinationAddress() {
	return destinationAddress;
}
public void setDestinationAddress(Address destinationAddress) {
	this.destinationAddress = destinationAddress;
}
public Time getStartTime_pickup() {
	return startTime_pickup;
}
public void setStartTime_pickup(Time startTime_pickup) {
	this.startTime_pickup = startTime_pickup;
}
public Time getStartTime_drop() {
	return startTime_drop;
}
public void setStartTime_drop(Time startTime_drop) {
	this.startTime_drop = startTime_drop;
}
public Time getOfficeTime_pickup() {
	return officeTime_pickup;
}
public void setOfficeTime_pickup(Time officeTime_pickup) {
	this.officeTime_pickup = officeTime_pickup;
}
public Time getOfficeTime_drop() {
	return officeTime_drop;
}
public void setOfficeTime_drop(Time officeTime_drop) {
	this.officeTime_drop = officeTime_drop;
}
public String getComments() {
	return comments;
}
public void setComments(String comments) {
	this.comments = comments;
}
public Integer getNumberOfMembers() {
	return numberOfMembers;
}
public void setNumberOfMembers(Integer numberOfMembers) {
	this.numberOfMembers = numberOfMembers;
}
public Integer getMax_members() {
	return max_members;
}
public void setMax_members(Integer max_members) {
	this.max_members = max_members;
}
public Integer getCostPerMonth() {
	return costPerMonth;
}
public void setCostPerMonth(Integer costPerMonth) {
	this.costPerMonth = costPerMonth;
}
public Car getCar() {
	return car;
}
public void setCar(Car car) {
	this.car = car;
}
public Boolean getIsAvailable() {
	return isAvailable;
}
public void setIsAvailable(Boolean isAvailable) {
	this.isAvailable = isAvailable;
}
public Timestamp getModifyDate() {
	return modifyDate;
}
public void setModifyDate(Timestamp modifyDate) {
	this.modifyDate = modifyDate;
}


}
