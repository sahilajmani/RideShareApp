package pojos;

public class WalletTransactions {
	private String type;
	private User poolOwner;
	private User poolParticipant;
	private String id;
	private Boolean isSettled;
	private Long transaction_timemillis;
	private Integer amount;
	private String details;
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Long getTransaction_timemillis() {
		return transaction_timemillis;
	}
	public void setTransaction_timemillis(Long transaction_timemillis) {
		this.transaction_timemillis = transaction_timemillis;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public User getPoolOwner() {
		return poolOwner;
	}
	public void setPoolOwner(User poolOwner) {
		this.poolOwner = poolOwner;
	}
	public User getPoolParticipant() {
		return poolParticipant;
	}
	public void setPoolParticipant(User poolParticipant) {
		this.poolParticipant = poolParticipant;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Boolean getIsSettled() {
		return isSettled;
	}
	public void setIsSettled(Boolean isSettled) {
		this.isSettled = isSettled;
	}
	

}
