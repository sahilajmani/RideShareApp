package pojos;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Address implements Serializable{
private String id;
private String addressLine1;
private String addressLine2;
private String fullAddress;
private String city;
private String state;
private String pincode;
private double lattitude;
private double longitude;

public String getFullAddress() {
	return fullAddress;
}
public void setFullAddress(String fullAddress) {
	this.fullAddress = fullAddress;
}

public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getAddressLine1() {
	return addressLine1;
}
public void setAddressLine1(String addressLine1) {
	this.addressLine1 = addressLine1;
}
public String getAddressLine2() {
	return addressLine2;
}
public void setAddressLine2(String addressLine2) {
	this.addressLine2 = addressLine2;
}
public String getCity() {
	return city;
}
public void setCity(String city) {
	this.city = city;
}
public String getState() {
	return state;
}
public void setState(String state) {
	this.state = state;
}
public String getPincode() {
	return pincode;
}
public void setPincode(String pincode) {
	this.pincode = pincode;
}
public double getLattitude() {
	return lattitude;
}
public void setLattitude(double lattitude) {
	this.lattitude = lattitude;
}
public double getLongitude() {
	return longitude;
}
public void setLongitude(double longitude) {
	this.longitude = longitude;
}

}
