package pojos;

public class Image {

	private String id;
	private User user;
	private byte[] imageBinary;
	private String description;
	private String imageType;
	private Boolean isVerified;
	Long updated;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public byte[] getImageBinary() {
		return imageBinary;
	}
	public void setImageBinary(byte[] imageBinary) {
		this.imageBinary = imageBinary;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImageType() {
		return imageType;
	}
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	public Boolean getIsVerified() {
		return isVerified;
	}
	public void setIsVerified(Boolean isVerified) {
		this.isVerified = isVerified;
	}
	public Long getUpdated() {
		return updated;
	}
	public void setUpdated(Long updated) {
		this.updated = updated;
	}
	
	
	
}
