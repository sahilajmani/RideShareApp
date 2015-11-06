package utility;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import pojos.TransactionType;
import pojos.User;
import pojos.WalletTransactions;

public class WalletUtil {
	
	public static void addToWallet(WalletTransactions walletRecharge,Session session){
		if(!TransactionType.CREDIT_TO_WALLET.equals(walletRecharge.getType())){
			walletRecharge.setType(TransactionType.CREDIT_TO_WALLET);
		}
		// poolOwner  is insignificant in case of add credit to wallet
		User user = RideSharingUtil.getDaoInstance().getUserDetails(walletRecharge.getPoolParticipant().getId());
		Transaction tx = session.beginTransaction();
		user.setWallet_balance(user.getWallet_balance()+walletRecharge.getAmount());
		walletRecharge.setIsSettled(true);
		walletRecharge.setTransaction_timemillis(System.currentTimeMillis());
		session.save(walletRecharge);
		session.update(user);
		tx.commit();
		
		
	}
	public static void poolRequestAccepted(WalletTransactions walletRecharge,Session session){
		if(!TransactionType.CREDIT_TO_WALLET.equals(walletRecharge.getType())){
			walletRecharge.setType(TransactionType.DEBIT_TO_WALLET);
		}
		// poolOwner  is insignificant in case of add credit to wallet
		User user = RideSharingUtil.getDaoInstance().getUserDetails(walletRecharge.getPoolParticipant().getId());
		Transaction tx = session.beginTransaction();
		user.setWallet_balance(user.getWallet_balance()-walletRecharge.getAmount());
		walletRecharge.setIsSettled(false);
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
			User poolOwner = tx.getPoolOwner();
			User poolParticipant = tx.getPoolParticipant();
			Long numberOfDays = ((System.currentTimeMillis()-tx.getTransaction_timemillis())/(24*60*60*1000));
		}
	}
}
