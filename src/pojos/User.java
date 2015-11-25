package pojos;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class User implements Serializable {
	private String id;
	private String name;
	private String companyName;
	private String email;
	private String contact;
	private Date reachDestinationTime;
	private Date leaveDestinationTime;
	private String reachDestinationTimeInMilliseconds;
	private String leaveDestinationTimeInMilliseconds;
	private Integer poolCost;
	
	// private Date officeTime_pickup;
	// private Date officeTime_drop;
	// private String company_joined_month;
	// private String company_joined_year;
	private boolean hasCar;
	
	private boolean isMobileVerified;
	private boolean isEmailVerified;
	private boolean isActive;
	private Date registerDate;
	
	private float distance;
	private Address homeAddress;
	private Address officeAddress;
	private int duration;
	private Pool pool;
	private Integer wallet_balance=0;

	public Integer getWallet_balance() {
		return wallet_balance;
	}

	public void setWallet_balance(Integer wallet_balance) {
		this.wallet_balance = wallet_balance;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
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

	public Date getReachDestinationTime() {
		return reachDestinationTime;
	}

	public void setReachDestinationTime(Date reachDestinationTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		try {
			if(this.reachDestinationTime!=null){
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

	public boolean isHasCar() {
		return hasCar;
	}

	public void setHasCar(boolean hasCar) {
		this.hasCar = hasCar;
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

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
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

	public Pool getPool() {
		return pool;
	}

	public void setPool(Pool pool) {
		this.pool = pool;
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

	public Integer getPoolCost() {
		return poolCost;
	}

	public void setPoolCost(Integer poolCost) {
		this.poolCost = poolCost;
	}
	
}
