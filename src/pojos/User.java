package pojos;

import java.sql.Timestamp;

public class User {
private String id;
private String name;
private String	companyName;
private String email;
private String contact;
private Time startTime_pickUp;
private Time startTime_drop;
private Time officeTime_pickup;
private Time officeTime_drop;
private String company_joined_month;
private String company_joined_year;
private boolean hasCar;
private boolean isCurrentPoolActive;
private boolean isMobileVerified;
private boolean isEmailVerified;
private boolean isActive;
private Timestamp registerDate;
private Timestamp modifyDate;
private float distance;
private Address homeAddress;
private Address officeAddress;
private int duration;
private Pool pool;
public Pool getPool() {
	return pool;
}


public void setPool(Pool pool) {
	this.pool = pool;
}


public String getName() {
	return name;
}


public String getId() {
	return id;
}


public void setId(String id) {
	this.id = id;
}


public void setName(String name) {
	this.name = name;
}
public String getCompanyName() {
	return companyName;
}
public void setCompanyName(String companyName) {
	this.companyName = companyName;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getContact() {
	return contact;
}
public void setContact(String contact) {
	this.contact = contact;
}
public Time getStartTime_pickUp() {
	return startTime_pickUp;
}
public void setStartTime_pickUp(Time startTime_pickUp) {
	this.startTime_pickUp = startTime_pickUp;
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
public String getCompany_joined_month() {
	return company_joined_month;
}
public void setCompany_joined_month(String company_joined_month) {
	this.company_joined_month = company_joined_month;
}
public String getCompany_joined_year() {
	return company_joined_year;
}
public void setCompany_joined_year(String company_joined_year) {
	this.company_joined_year = company_joined_year;
}
public boolean isHasCar() {
	return hasCar;
}
public void setHasCar(boolean hasCar) {
	this.hasCar = hasCar;
}
public boolean isCurrentPoolActive() {
	return isCurrentPoolActive;
}
public void setCurrentPoolActive(boolean isCurrentPoolActive) {
	this.isCurrentPoolActive = isCurrentPoolActive;
}
public boolean isMobileVerified() {
	return isMobileVerified;
}
public void setMobileVerified(boolean isMobileVerified) {
	this.isMobileVerified = isMobileVerified;
}
public boolean isEmailVerified() {
	return isEmailVerified;
}
public void setEmailVerified(boolean isEmailVerified) {
	this.isEmailVerified = isEmailVerified;
}
public boolean isActive() {
	return isActive;
}
public void setActive(boolean isActive) {
	this.isActive = isActive;
}
public Timestamp getRegisterDate() {
	return registerDate;
}
public void setRegisterDate(Timestamp registerDate) {
	this.registerDate = registerDate;
}
public Timestamp getModifyDate() {
	return modifyDate;
}
public void setModifyDate(Timestamp modifyDate) {
	this.modifyDate = modifyDate;
}
public float getDistance() {
	return distance;
}
public void setDistance(float distance) {
	this.distance = distance;
}
public Address getHomeAddress() {
	return homeAddress;
}
public void setHomeAddress(Address homeAddress) {
	this.homeAddress = homeAddress;
}
public Address getOfficeAddress() {
	return officeAddress;
}
public void setOfficeAddress(Address officeAddress) {
	this.officeAddress = officeAddress;
}
public int getDuration() {
	return duration;
}
public void setDuration(int duration) {
	this.duration = duration;
}
@Override
public String toString(){
	return "\n Name - "+this.getName()+"\n Company -"+this.getCompanyName()+"\n Mobile - "+this.getContact()+"\n Email -"+this.getEmail();
	
}
}
