package pojos;

public class RedeemedCoupons {
	
	private String userId;
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	private String couponCode;
	private Long timeOfRedemption;
	private String metaData;
	private boolean isSetlled;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public Long getTimeOfRedemption() {
		return timeOfRedemption;
	}
	public void setTimeOfRedemption(Long timeOfRedemption) {
		this.timeOfRedemption = timeOfRedemption;
	}
	public String getMetaData() {
		return metaData;
	}
	public void setMetaData(String metaData) {
		this.metaData = metaData;
	}
	public boolean isSetlled() {
		return isSetlled;
	}
	public void setSetlled(boolean isSetlled) {
		this.isSetlled = isSetlled;
	}
	
	

}
