package pojos;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
public class Pool implements Serializable{
private String id;
private boolean is_active;
private String hostUserId;
private Address sourceAddress;
private Address destinationAddress;
private Date reachDestinationTime; 
private Date leaveDestinationTime; 
private Integer numberOfMembers;
private Integer max_members;
private Integer costPerMonth;
private Car car;
private Boolean isAvailable;
private Timestamp modifyDate; //nt req
//private List<User> participants;
//public List<User> getParticipants() {
//	return participants;
//}
//public void setParticipants(List<User> participants) {
//	this.participants = participants;
//}
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
public String getHostUserId() {
	return hostUserId;
}
public void setHostUserId(String hostUserId) {
	this.hostUserId = hostUserId;
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
public Date getReachDestinationTime() {
	return reachDestinationTime;
}
public void setReachDestinationTime(Date reachDestinationTime) {
	this.reachDestinationTime = reachDestinationTime;
}
public Date getLeaveDestinationTime() {
	return leaveDestinationTime;
}
public void setLeaveDestinationTime(Date leaveDestinationTime) {
	this.leaveDestinationTime = leaveDestinationTime;
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
