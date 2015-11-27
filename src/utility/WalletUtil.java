package utility;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import email.SendMail;
import pojos.Pool;
import pojos.TransactionType;
import pojos.User;
import pojos.WalletTransactions;

public class WalletUtil {
	
	public static User addToWallet(WalletTransactions walletRecharge,Session session){ //for payU
		if(!TransactionType.CREDIT_TO_WALLET.equals(walletRecharge.getType())){ //why this if??
			walletRecharge.setType(TransactionType.CREDIT_TO_WALLET);
		}
		// poolOwner  is insignificant in case of add credit to wallet
		User user = RideSharingUtil.getDaoInstance().getUserDetails(walletRecharge.getPoolParticipant().getId());
		Transaction tx = session.beginTransaction();
		user.setWallet_balance(user.getWallet_balance()+walletRecharge.getAmount());
		walletRecharge.setIsSettled(true);
		walletRecharge.setTransaction_timemillis(System.currentTimeMillis());
		walletRecharge.setId("EXT_"+System.currentTimeMillis()+":"+user.getId().substring(25));
		session.save(walletRecharge);
		session.update(user);
		String message = "Hi "+user.getName()+"\n You have received a credit of INR "+walletRecharge.getAmount()+" to "
				+ "your RideEasy Wallet. Your new wallet balance is "+user.getWallet_balance()+". \n Thanks. Keep Riding "
						+ "!";
		String subject = "You have received a new wallet credit of INR"+walletRecharge.getAmount();
		String[] to = { user.getEmail() };
				SendMail.sendEmail(GlobalConstants.FROM_EMAIL,
						GlobalConstants.PASSWORD_EMAIL, subject, message,
						to);
		tx.commit();
		return user;
		
	}
	
	public static User addToWalletInternal(WalletTransactions walletRecharge,Session session){ //for all crediting internal transactions
		
		User user = RideSharingUtil.getDaoInstance().getUserDetails(walletRecharge.getPoolOwner().getId());
		Transaction tx = session.beginTransaction();
		user.setWallet_balance(user.getWallet_balance()+walletRecharge.getAmount());
		walletRecharge.setIsSettled(true);
		walletRecharge.setId("INT_"+System.currentTimeMillis()+":"+user.getId().substring(25));
		walletRecharge.setTransaction_timemillis(System.currentTimeMillis());
		session.save(walletRecharge);
		session.update(user);
		String message = "Hi "+user.getName()+"\n You have received a credit of INR "+walletRecharge.getAmount()+" to "
				+ "your RideEasy Wallet. Your new wallet balance is "+user.getWallet_balance()+". \n Thanks. Keep riding, keep sharing ! "
						+ "!";
		String subject = "You have received a new wallet credit of INR"+walletRecharge.getAmount();
		String[] to = { user.getEmail() };
				SendMail.sendEmail(GlobalConstants.FROM_EMAIL,
						GlobalConstants.PASSWORD_EMAIL, subject, message,
						to);
		tx.commit();
		return user;
		
	}
	
	
	public static void poolRequestAccepted(WalletTransactions walletRecharge,String poolOwnerId,String userId){   //set an id here//internal transaction // this method will only be initiated once a new request is acceted
		Session session = RideSharingUtil.getSessionFactoryInstance().openSession();
		if(!TransactionType.CREDIT_TO_WALLET.equals(walletRecharge.getType())){
			walletRecharge.setType(TransactionType.DEBIT_TO_WALLET);  				//significance of if?
		}
	//	User user = RideSharingUtil.getDaoInstance().getUserDetails(walletRecharge.getPoolParticipant().getId());
		Transaction tx = session.beginTransaction();
		User participant =  RideSharingUtil.getDaoInstance().getUserDetails(userId);
		participant.setWallet_balance(participant.getWallet_balance()-walletRecharge.getAmount());
		int walletBalance=participant.getWallet_balance();
		String participantEmail =participant.getEmail();
		String name = participant.getName();
		session.update(participant);
		
		tx.commit();
		session.clear();
		
		Transaction tx1 = session.beginTransaction();
		walletRecharge.setIsSettled(false);
		walletRecharge.setId("INT_POOL"+System.currentTimeMillis()+":"+poolOwnerId.substring(25));
		walletRecharge.setTransaction_timemillis(System.currentTimeMillis());
		User poolOwner =RideSharingUtil.getDaoInstance().getUserDetails(poolOwnerId);
		walletRecharge.setPoolOwner(poolOwner);
		String poolOwnerName = poolOwner.getName();
		walletRecharge.setDetails("Advance paid for joining "+poolOwnerName+"'s pool");
		walletRecharge.setPoolParticipant(RideSharingUtil.getDaoInstance().getUserDetails(userId));
		session.save(walletRecharge);
	//	session.update(user);
		tx1.commit();
		String message = "Hi "+name+"\n Your request to join pool owned by  "+poolOwnerName+""
				+ "just got approved. Your new wallet balance is INR "+walletBalance+"\n Thanks for using RideEasy, Keep riding, Keep sharing !";
		String subject = "Congratulations !Your pool request has been processed, here is your updated wallet balance "+walletBalance;
		String[] to = { participantEmail };
				SendMail.sendEmail(GlobalConstants.FROM_EMAIL,
						GlobalConstants.PASSWORD_EMAIL, subject, message,
						to);
				
				session.close();
			
	}
	
	
	public static void poolLeftByUser(WalletTransactions walletRecharge,String poolOwnerId,String poolparticipantId){ 
		Session session = RideSharingUtil.getSessionFactoryInstance().openSession();
		Criteria cr = session.createCriteria(WalletTransactions.class).add(Restrictions.eq("poolOwner.id",poolOwnerId)).add
				(Restrictions.eq("poolParticipant.id", poolparticipantId)).add(Restrictions.eq("isSettled",false));
		Collection <WalletTransactions> unsettledTransaction = (Collection<WalletTransactions>) cr.list();
		String transactionId="";
		if(unsettledTransaction!= null && unsettledTransaction.size()>0){
		Transaction t1= session.beginTransaction();
			WalletTransactions tx = unsettledTransaction.iterator().next();
//			cr = session.createCriteria(Pool.class).add(Restrictions.eq("id",tx.getPoolOwner().getId()));
//			Pool userPool = (Pool)cr.list().get(0);
		//	User poolOwner = walletRecharge.getPoolOwner();
		//	User poolParticipant = walletRecharge.getPoolParticipant();
			Long numberOfDays = ((System.currentTimeMillis()-tx.getTransaction_timemillis())/(24*60*60*1000));
			transactionId=tx.getId();
		//	System.out.println(numberOfDays);
			int days = numberOfDays.intValue();
			if(days>5){
			days=5;
			}
			int ownerShare = (int)((days)/5.00 *tx.getPoolOwner().getPoolCost()); // cost per month ?? we should rename //havnt we assumed that there are 5 working days only?
			int participantRefund = tx.getPoolOwner().getPoolCost()-ownerShare;
			// this variable
//			tx.setAmount(ownerShare);
			tx.setIsSettled(true);
			session.update(tx);
			t1.commit();
			
			Transaction t2= session.beginTransaction();
			User poolOwner = RideSharingUtil.getDaoInstance().getUserDetails(poolOwnerId);
			String poolOwnerName=poolOwner.getName();
			String poolOwnerEmail=poolOwner.getEmail();
			User poolParticipant = RideSharingUtil.getDaoInstance().getUserDetails(poolparticipantId);
			WalletTransactions poolOwnerTx = new WalletTransactions();
			poolOwnerTx.setId(transactionId+"Credit");
			poolOwnerTx.setAmount(ownerShare);
			poolOwnerTx.setTransaction_timemillis(System.currentTimeMillis());
			poolOwnerTx.setType(TransactionType.CREDIT_TO_WALLET);
			poolOwnerTx.setIsSettled(true);
			poolOwnerTx.setPoolParticipant(poolParticipant);
			poolOwnerTx.setPoolOwner(poolOwner);
			poolOwnerTx.setDetails("Pool left by user - "+poolParticipant.getName()+" Amount credited for "+days+" days");
			session.saveOrUpdate(poolOwnerTx);
			t2.commit();
			session.clear();
			Transaction t4= session.beginTransaction();
			User poolOwner2 = RideSharingUtil.getDaoInstance().getUserDetails(poolOwnerId);
			User poolParticipant2 = RideSharingUtil.getDaoInstance().getUserDetails(poolparticipantId);
			poolOwner2.setWallet_balance(poolOwner2.getWallet_balance()+ownerShare);
			poolParticipant2.setWallet_balance(poolParticipant2.getWallet_balance()+participantRefund);
			session.update(poolParticipant2);
			session.update(poolOwner2);
			t4.commit();
			
			Transaction t3= session.beginTransaction();
			WalletTransactions poolParticipanttx = new WalletTransactions();
			User poolParticipant1 = RideSharingUtil.getDaoInstance().getUserDetails(poolparticipantId);
			poolParticipanttx.setType(TransactionType.REFUND);
			poolParticipanttx.setId(transactionId+"Refund");
			poolParticipanttx.setAmount(participantRefund);
			poolParticipanttx.setIsSettled(true);
			poolParticipanttx.setTransaction_timemillis(System.currentTimeMillis());
			poolParticipanttx.setPoolOwner(poolParticipant1);
			poolParticipanttx.setPoolParticipant(poolParticipant1);
			poolParticipanttx.setDetails("Refund by Rideeasay on leaving "+poolOwnerName+"'s Pool");
			
		    session.saveOrUpdate(poolParticipanttx);
			
		//	session.save(poolParticipant);
		//	session.save(poolOwner);
			//poolParticipant mail notification
			String message = "Hi "+poolParticipant1.getName()+"\n You just received a refund of INR "+participantRefund+
					" in you wallet as part of final settlement for pool owned by "+poolOwner.getName()+""
							+ ".Your updated wallet balance is INR "+poolParticipant1.getWallet_balance()
							+ ". We hope you had a pleasent experience riding and sharing ! Do share your feedback with us.";
			String subject = "You have received a refund of INR "+participantRefund+"in your Wallet";
			String[] to = { poolParticipant.getEmail() };
					SendMail.sendEmail(GlobalConstants.FROM_EMAIL,
							GlobalConstants.PASSWORD_EMAIL, subject, message,
							to);
					
					// pool Owner mail notification
					
					 message = "Hi "+poolOwnerName+"\n You just received the final settlement amount of INR "+ownerShare+
							 " as "+poolParticipant1.getName()+" decided to leave your car pool. Please share your valuable feeback with us. Happy riding, happy sharing !";
					 subject = "You have received final settlement amount with "+poolParticipant1.getName();
					String [] to1 = { poolOwnerEmail };
							SendMail.sendEmail(GlobalConstants.FROM_EMAIL,
									GlobalConstants.PASSWORD_EMAIL, subject, message,
									to1);
							
//			session.update(poolOwner);
//			session.update(poolParticipant);
		//	poolOwner = null;
	//		poolParticipant= null;
	//		tx=null;
							t3.commit();
			session.close();
			
		}
	}
	
	public static void settleTransactions(Session session){
		Criteria cr = session.createCriteria(WalletTransactions.class);
		cr.add(Restrictions.eq("isSettled", false));
		Collection <WalletTransactions> transactions = cr.list();
		Transaction tx =null;
		for(WalletTransactions transaction : transactions){
			tx = session.beginTransaction();
			transaction.setIsSettled(true);
			session.update(transaction); //optimize the code. In loop it is being called.Dont know when will it commit. If it is only interacting with cache then no need to optimise.
			tx.commit();
			WalletTransactions settlementTx= new WalletTransactions();
			settlementTx.setId(transaction.getId());
			settlementTx.setDetails(transaction.getAmount()+" Rs paid by "+transaction.getPoolParticipant());
			settlementTx.setAmount(transaction.getAmount());
			settlementTx.setPoolParticipant(transaction.getPoolParticipant());
			settlementTx.setType(TransactionType.CREDIT_TO_WALLET);
			settlementTx.setPoolOwner(transaction.getPoolOwner());
			addToWalletInternal(settlementTx, session);
		}
		session.close();
	}
}
