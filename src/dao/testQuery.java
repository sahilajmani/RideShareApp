package dao;

public class testQuery {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DaoImpl db=new DaoImpl();
	System.out.println(db.findMatchedUser("b4c04048-2e3e-11e5-87d3-02b93fe38caf").size());
	}

}
