package rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import dao.DaoI;
import email.SendMail;
import pojos.ListWalletTransactions;
import pojos.MailNotifierThread;
import pojos.PayOutRequest;
import pojos.RestServiceResponse;
import pojos.TransactionType;
import pojos.User;
import pojos.UserBankDetails;
import pojos.WalletTransactions;
import utility.RideSharingUtil;
import utility.WalletUtil;
import vo.GenericResponse;
import vo.WalletTopUp;

@Path("/wallet")
public class WalletService {
	DaoI dao = RideSharingUtil.getDaoInstance();

	@POST
	@Path("addtowallet")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RestServiceResponse creditToWallet(WalletTopUp walletTopUp) {
		WalletTransactions tx = new WalletTransactions();
		tx.setPoolParticipant(walletTopUp.getUser());
		tx.setPoolOwner(walletTopUp.getUser());
		tx.setDetails("Recharged the wallet by "+walletTopUp.getAmount()+ "Rs");
		tx.setAmount(walletTopUp.getAmount());
		tx.setId(walletTopUp.getTransactionId()); // external id - add ext in front.
		tx.setType(TransactionType.CREDIT_TO_WALLET);
		Session session = RideSharingUtil.getSessionFactoryInstance().openSession();
		
		RestServiceResponse response = new RestServiceResponse();
		try{
		User user= WalletUtil.addToWallet(tx, session);
		session.close();
		response.setUser(user);
		response.setResponse(true);
		return response;
		}
		catch(Exception e){
			response.setResponse(false);
			e.printStackTrace();
			return response;
		}
		
	}
	
	@POST
	@Path("addtowallet")
	@Produces(MediaType.APPLICATION_JSON)
	public ListWalletTransactions getTransactionHistory(@QueryParam("userId") String userId) {
		ListWalletTransactions walletTransactions = new ListWalletTransactions();
		walletTransactions = dao.getWalletTransactionHistory(userId);
		return walletTransactions;
	}
	
	@POST
	@Path("updateBankDetails")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public GenericResponse updateBankDetails(UserBankDetails details){
		GenericResponse response = new GenericResponse();
		Transaction tx=null;
		if(details.getUserId()!=null&&details.getAcNo()!=null&&details.getIfscCode()!=null
				&&details.getAcHoldersName()!=null
				&&RideSharingUtil.getDaoInstance().getUserDetails(details.getUserId())!=null){
			Session session=null;
		try{
			 session = RideSharingUtil.getSessionFactoryInstance().openSession();
			 tx=session.beginTransaction();
			session.saveOrUpdate(details);
			System.out.println("savedd...");
			response.setSuccess(true);
			tx.commit();
		}catch(Exception e){
			e.printStackTrace();
			response.setErrorMsg(e.getMessage());
			response.setSuccess(false);
			return response;
		}
		finally{
		session.close();
		
		}
		}else{
			response.setErrorMsg("Invalid data");
			response.setSuccess(false);
		}
		
		return response;
	}

	@POST
	@Path("getUserBankDetails")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public GenericResponse getUserBankDetails(User user){
		GenericResponse response = new GenericResponse();
	if(user!=null || !"".equals(user.getId())){
		Session session = RideSharingUtil.getSessionFactoryInstance().openSession();
		Criteria cr = session.createCriteria(UserBankDetails.class).add(Restrictions.eq("userId", user.getId()));
		UserBankDetails details = (UserBankDetails) cr.uniqueResult();
		if(details!=null){
			response.setSuccess(true);
			response.setData(details);
		return response;	
		}
		session.close();
	}
		response.setSuccess(false);
	return response;
	}
	@POST
	@Path("requestPayOut")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public GenericResponse requestPayOut(PayOutRequest request){
		GenericResponse response = new GenericResponse();
		if(request!=null && request.getUserId()!=null && request.getAmount()!=0){
			Session session = RideSharingUtil.getSessionFactoryInstance().openSession();
			Transaction tx = session.beginTransaction();
			request.setRequestTime(System.currentTimeMillis());
			session.save(request);
			tx.commit();
			session.close();
			notifyRideShare(request);
			response.setSuccess(true);
		}
		return response;
		
	}

	private void notifyRideShare(PayOutRequest request) {
		// TODO Auto-generated method stub
		new MailNotifierThread("Hi,\nNew request for payout submitted by user - "+request.getUserId()
		+"\t amount - "+request.getAmount(), "rishabhgarg@nsitonline.in","New Payout request ");
	}
	
}
