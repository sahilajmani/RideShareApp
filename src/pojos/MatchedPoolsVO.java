package pojos;

public class MatchedPoolsVO {
private Pool pool;
private String distance;
private String name;
private Integer poolCost;

public Pool getPool() {
	return pool;
}
public void setPool(Pool pool) {
	this.pool = pool;
}
public String getDistance() {
	return distance;
}
public void setDistance(String distance) {
	this.distance = distance;
}

public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public Integer getPoolCost() {
	return poolCost;
}
public void setPoolCost(Integer poolCost) {
	this.poolCost = poolCost;
}
}
