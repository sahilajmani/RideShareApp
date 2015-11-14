package rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hibernate.Session;

import pojos.RestServiceResponse;
import pojos.TransactionType;
import pojos.User;
import pojos.WalletTransactions;
import utility.RideSharingUtil;
import utility.WalletUtil;
import vo.WalletTopUp;

@Path("/wallet")
public class WalletService {
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
	

}
