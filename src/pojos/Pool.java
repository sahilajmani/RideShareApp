package pojos;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
public class Pool implements Serializable{
private String id;
private boolean is_active;
private Address sourceAddress;
private Address destinationAddress;
private Date reachDestinationTime;
private String reachDestinationTimeInMilliseconds;
private String leaveDestinationTimeInMilliseconds;
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
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	try {
		if(reachDestinationTime!=null){
		this.reachDestinationTime = sdf.parse(sdf
				.format(reachDestinationTime));
		}else{
			this.reachDestinationTime = reachDestinationTime;
		}
	} catch (ParseException e) {
		e.printStackTrace();
	}
}
public Date getLeaveDestinationTime() {
	return leaveDestinationTime;
}
public void setLeaveDestinationTime(Date leaveDestinationTime) {
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	try {
		if(leaveDestinationTime!=null){
		this.leaveDestinationTime = sdf.parse(sdf
				.format(leaveDestinationTime));
		}else{
			this.leaveDestinationTime = leaveDestinationTime;
		}
	} catch (ParseException e) {
		e.printStackTrace();
	}
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
public String getLeaveDestinationTimeInMilliseconds() {
	return leaveDestinationTimeInMilliseconds;
}
public void setLeaveDestinationTimeInMilliseconds(
		String leaveDestinationTimeInMilliseconds) {
	this.leaveDestinationTimeInMilliseconds = leaveDestinationTimeInMilliseconds;
}
public String getReachDestinationTimeInMilliseconds() {
	return reachDestinationTimeInMilliseconds;
}
public void setReachDestinationTimeInMilliseconds(
		String reachDestinationTimeInMilliseconds) {
	this.reachDestinationTimeInMilliseconds = reachDestinationTimeInMilliseconds;
}
}
