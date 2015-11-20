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
	
	
	public static void poolRequestAccepted(WalletTransactions walletRecharge,Session session){   //set an id here//internal transaction // this method will only be initiated once a new request is acceted
		if(!TransactionType.CREDIT_TO_WALLET.equals(walletRecharge.getType())){
			walletRecharge.setType(TransactionType.DEBIT_TO_WALLET);  				//significance of if?
		}
	//	User user = RideSharingUtil.getDaoInstance().getUserDetails(walletRecharge.getPoolParticipant().getId());
		User user = walletRecharge.getPoolParticipant();
		Transaction tx = session.beginTransaction();
		user.setWallet_balance(user.getWallet_balance()-walletRecharge.getAmount());
		walletRecharge.setIsSettled(false);
		walletRecharge.setDetails("Advance paid for joining "+walletRecharge.getPoolOwner().getName()+"'s pool");
		walletRecharge.setId("INT_POOL"+System.currentTimeMillis()+":"+walletRecharge.getPoolOwner().getId().substring(25));
		walletRecharge.setTransaction_timemillis(System.currentTimeMillis());
		session.save(walletRecharge);
		session.update(user);
		String message = "Hi "+user.getName()+"\n Your request to join pool owned by  "+walletRecharge.getPoolOwner().getName()+""
				+ "just got approved. Your new wallet balance is INR "+user.getWallet_balance()+"\n Thanks for using RideEasy, Keep riding, Keep sharing !";
		String subject = "Congratulations !Your pool request has been processed, here is your updated wallet balance "+user.getWallet_balance();
		String[] to = { user.getEmail() };
				SendMail.sendEmail(GlobalConstants.FROM_EMAIL,
						GlobalConstants.PASSWORD_EMAIL, subject, message,
						to);
		tx.commit();		
	}
	
	
	public static void poolLeftByUser(WalletTransactions walletRecharge){ 
		Session session = RideSharingUtil.getSessionFactoryInstance().openSession();
		Criteria cr = session.createCriteria(WalletTransactions.class).add(Restrictions.eq("poolOwner.id",walletRecharge.getPoolOwner().getId())).add
				(Restrictions.eq("poolParticipant.id", walletRecharge.getPoolParticipant().getId())).add(Restrictions.eq("isSettled",false));
		Collection <WalletTransactions> unsettledTransaction = (Collection<WalletTransactions>) cr.list();
		if(unsettledTransaction!= null && unsettledTransaction.size()>0){
		Transaction t1= session.beginTransaction();
			WalletTransactions tx = unsettledTransaction.iterator().next();
//			cr = session.createCriteria(Pool.class).add(Restrictions.eq("id",tx.getPoolOwner().getId()));
//			Pool userPool = (Pool)cr.list().get(0);
			User poolOwner = walletRecharge.getPoolOwner();
			User poolParticipant = walletRecharge.getPoolParticipant();
			Long numberOfDays = ((1447320545000L-tx.getTransaction_timemillis())/(24*60*60*1000));
			System.out.println(numberOfDays);
			int days = numberOfDays.intValue();
			if(days>5){
			days=5;
			}
			int ownerShare = (int)((days)/5.00 *poolOwner.getPoolCost()); // cost per month ?? we should rename //havnt we assumed that there are 5 working days only?
			// this variable
			tx.setAmount(ownerShare);
			tx.setIsSettled(true);
			
			int participantRefund = poolOwner.getPoolCost()-ownerShare;
			//Transaction t1 = session.beginTransaction();
			poolOwner.setWallet_balance(poolOwner.getWallet_balance()+ownerShare);
			WalletTransactions poolOwnerTx = new WalletTransactions();
			poolOwnerTx.setId(tx.getId()+"Credit");
			poolOwnerTx.setAmount(ownerShare);
			poolOwnerTx.setType(TransactionType.CREDIT_TO_WALLET);
			poolOwnerTx.setIsSettled(true);
			poolOwnerTx.setPoolParticipant(poolParticipant);
			poolOwnerTx.setPoolOwner(poolOwner);
			poolOwnerTx.setDetails("Pool left by user - "+poolParticipant.getName()+" Amount credited for "+days+" days");
			WalletTransactions poolParticipanttx = new WalletTransactions();
			poolParticipanttx.setType(TransactionType.REFUND);
			poolParticipanttx.setId(tx.getId()+"Refund");
			poolParticipanttx.setAmount(participantRefund);
			poolParticipanttx.setIsSettled(true);
			poolParticipanttx.setPoolOwner(poolParticipant);
			poolParticipanttx.setPoolParticipant(poolParticipant);
			poolParticipanttx.setDetails("Refund by Rideeasay on leaving "+poolOwner.getName()+"'s Pool");
			poolParticipant.setWallet_balance(poolParticipant.getWallet_balance()+participantRefund);
			session.update(tx);
		    session.save(poolParticipanttx);
			session.save(poolOwnerTx);
			//poolParticipant mail notification
			String message = "Hi "+poolParticipant.getName()+"\n You just received a refund of INR "+participantRefund+
					"in you wallet as part of final settlement for pool owned by "+poolOwner.getName()+""
							+ ". We hope you had a pleasent experience riding and sharing ! Do share your feedback with us.";
			String subject = "You have received a refund of INR"+tx.getAmount()+"in your Wallet";
			String[] to = { poolParticipant.getEmail() };
					SendMail.sendEmail(GlobalConstants.FROM_EMAIL,
							GlobalConstants.PASSWORD_EMAIL, subject, message,
							to);
					
					// pool Owner mail notification
					
					 message = "Hi "+poolOwner.getName()+"\n You just received the final settlement amount of INR "+ownerShare+
							 " as "+poolParticipant.getName()+" decided to leave your car pool. Please share your valuable feeback with us. Happy riding, happy sharing !";
					 subject = "You have received final settlement amount with "+poolParticipant.getName();
					String [] to1 = { poolOwner.getEmail() };
							SendMail.sendEmail(GlobalConstants.FROM_EMAIL,
									GlobalConstants.PASSWORD_EMAIL, subject, message,
									to1);
							
//			session.update(poolOwner);
//			session.update(poolParticipant);
			poolOwner = null;
			poolParticipant= null;
			tx=null;
			t1.commit();
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
