package dao;

public interface AuthenticationDaoI {

	boolean authenticate(String username, String password);
	void insertToken(String username, Long token) throws Exception;

}
