package pojos;

public class GroupChat {
 Integer id;
 Pool pool_id;
 User sender;
 String message;
 Integer fetchCount;
 Time timeStamp;
public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}
public Pool getPool_id() {
	return pool_id;
}
public void setPool_id(Pool pool_id) {
	this.pool_id = pool_id;
}
public User getSender() {
	return sender;
}
public void setSender(User sender) {
	this.sender = sender;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
public Integer getFetchCount() {
	return fetchCount;
}
public void setFetchCount(Integer fetchCount) {
	this.fetchCount = fetchCount;
}
public Time getTimeStamp() {
	return timeStamp;
}
public void setTimeStamp(Time timeStamp) {
	this.timeStamp = timeStamp;
}
 
}
