package pojos;

import java.io.Serializable;
import java.util.List;

public class ListWalletTransactions implements Serializable{

	List<WalletTransactions> walletTransactions;
	
	public List<WalletTransactions> getWalletTransactions() {
		return walletTransactions;
	}
	public void setWalletTransactions(
			List<WalletTransactions> walletTransactions) {
		this.walletTransactions = walletTransactions;
	}
}
