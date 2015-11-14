package utility;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

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
		tx.commit();	
	}
	
	
	public static void poolLeftByUser(WalletTransactions walletRecharge,Session session){ 
		Criteria cr = session.createCriteria(WalletTransactions.class).add(Restrictions.eq("poolOwner.id",walletRecharge.getPoolOwner().getId())).add
				(Restrictions.eq("poolParticipant.id", walletRecharge.getId())).add(Restrictions.eq("isSettled",false));
		Collection <WalletTransactions> unsettledTransaction = (Collection<WalletTransactions>) cr.list();
		if(unsettledTransaction!= null){
			WalletTransactions tx = unsettledTransaction.iterator().next();
			cr = session.createCriteria(Pool.class).add(Restrictions.eq("id",tx.getPoolOwner().getId()));
			Pool userPool = (Pool)cr.list().get(0);
			User poolOwner = tx.getPoolOwner();
			User poolParticipant = tx.getPoolParticipant();
			Long numberOfDays = ((System.currentTimeMillis()-tx.getTransaction_timemillis())/(24*60*60*1000));
			int days = numberOfDays.intValue();
			if(days>5){
			days=5;
			}
			int ownerShare = (int)((days)/5.00 *poolOwner.getPoolCost()); // cost per month ?? we should rename //havnt we assumed that there are 5 working days only?
			// this variable
			tx.setAmount(ownerShare);
			tx.setIsSettled(true);
			
			int participantRefund = poolOwner.getPoolCost()-ownerShare;
			Transaction t1 = session.beginTransaction();
			poolOwner.setWallet_balance(poolOwner.getWallet_balance()+ownerShare);
			WalletTransactions poolOwnerTx = new WalletTransactions();
			poolOwnerTx.setId(tx.getId());
			poolOwnerTx.setAmount(ownerShare);
			poolOwnerTx.setType(TransactionType.CREDIT_TO_WALLET);
			poolOwnerTx.setIsSettled(true);
			poolOwnerTx.setPoolParticipant(poolParticipant);
			poolOwnerTx.setPoolOwner(poolOwner);
			poolOwnerTx.setDetails("Pool left by user - "+poolParticipant.getName()+" Amount credited for "+days+" days");
			WalletTransactions poolParticipanttx = new WalletTransactions();
			poolParticipanttx.setType(TransactionType.REFUND);
			poolParticipanttx.setId(tx.getId());
			poolParticipanttx.setAmount(participantRefund);
			poolParticipanttx.setIsSettled(true);
			poolParticipanttx.setPoolOwner(poolParticipant);
			poolParticipanttx.setPoolParticipant(poolParticipant);
			poolParticipanttx.setDetails("Refund by Rideeasay on leaving "+poolOwner.getName()+"'s Pool");
			poolParticipant.setWallet_balance(poolParticipant.getWallet_balance()+participantRefund);
			
			session.update(tx);
			session.save(poolParticipanttx);
			session.save(poolOwnerTx);
			session.update(poolParticipant);
			session.update(poolOwner);
			t1.commit();
			
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
