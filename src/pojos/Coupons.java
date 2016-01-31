package pojos;

public class Coupons {

	private String couponCode;
	private String id;
	private boolean isApplicableMultipleTimes;
	private String details;
	private Long expiryDate;
	private boolean isValid;
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public boolean isApplicableMultipleTimes() {
		return isApplicableMultipleTimes;
	}
	public void setApplicableMultipleTimes(boolean isApplicableMultipleTimes) {
		this.isApplicableMultipleTimes = isApplicableMultipleTimes;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public Long getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Long expiryDate) {
		this.expiryDate = expiryDate;
	}
	public boolean isValid() {
		return isValid;
	}
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	
}
