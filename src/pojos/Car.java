package pojos;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Car implements Serializable{
private String id;
private User owner;
private String name;
private Integer seater;
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public User getOwner() {
	return owner;
}
public void setOwner(User owner) {
	this.owner = owner;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public Integer getSeater() {
	return seater;
}
public void setSeater(Integer seater) {
	this.seater = seater;
}

}
