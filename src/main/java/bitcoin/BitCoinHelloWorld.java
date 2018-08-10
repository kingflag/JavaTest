package bitcoin;

import java.io.File;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionConfidence;
import org.bitcoinj.net.discovery.DnsDiscovery;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.store.SPVBlockStore;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

public class BitCoinHelloWorld implements WalletCoinsReceivedEventListener {

	public static void main(String[] args) {
		BitCoinHelloWorld demo = new BitCoinHelloWorld();
		demo.run();
	}

	public void run() {
		try {
			init();
			System.out.println("Waiting for coins...");
			while (true) {
				Thread.sleep(20);
			}
		} catch (BlockStoreException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void init() throws BlockStoreException {
		NetworkParameters params = TestNet3Params.get();

		ECKey key = new ECKey();
		System.out.println("We created a new key:\n" + key);

		Address addressFromKey = key.toAddress(params);
		System.out.println("Public Address generated: " + addressFromKey);

		System.out.println("Private key is: " + key.getPrivateKeyEncoded(params).toString());

		Wallet wallet = new Wallet(params);
		wallet.importKey(key);

		File blockFile = new File("D://temp//blocks_temp");
		SPVBlockStore blockStore = new SPVBlockStore(params, blockFile);

		BlockChain blockChain = new BlockChain(params, wallet, blockStore);
		PeerGroup peerGroup = new PeerGroup(params, blockChain);
		peerGroup.addPeerDiscovery(new DnsDiscovery(params));
		peerGroup.addWallet(wallet);

		System.out.println("Start peer group");
		peerGroup.start();

		System.out.println("Downloading block chain");
		peerGroup.downloadBlockChain();
		System.out.println("Block chain downloaded");

		wallet.addCoinsReceivedEventListener(this);
	}

	@Override
	public void onCoinsReceived(final Wallet wallet, final Transaction transaction, Coin prevBalance, Coin newBalance) {
		final Coin value = transaction.getValueSentToMe(wallet);

		System.out.println("Received tx for " + value.toFriendlyString() + ": " + transaction);

		System.out.println("Previous balance is " + prevBalance.toFriendlyString());

		System.out.println("New estimated balance is " + newBalance.toFriendlyString());

		System.out.println("Coin received, wallet balance is :" + wallet.getBalance());

		Futures.addCallback(transaction.getConfidence().getDepthFuture(1), new FutureCallback<TransactionConfidence>() {
			@Override
			public void onSuccess(TransactionConfidence result) {
				System.out.println("Transaction confirmed, wallet balance is :" + wallet.getBalance());
			}

			@Override
			public void onFailure(Throwable t) {
				t.printStackTrace();
			}
		});
	}
}
