package bitcoin;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.store.SPVBlockStore;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.KeyChainGroup;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.bitcoinj.wallet.Wallet;

import com.google.common.base.Joiner;

public class BitCoinUtil {

	public static final String passphrase = "";

	public static NetworkParameters getParams() {
		return TestNet3Params.get();
	}

	/**
	 * 通过私钥获取ECKey
	 * 
	 * @param priKey
	 * @return
	 */
	public static ECKey getECKeyFromPriKey(String priKey) {
		ECKey ecKey = ECKey.fromPrivate(NumberUtils.createBigInteger(priKey));
		return ecKey;
	}
	

	public static String getPubKeyFrom(ECKey ecKey) {
		NetworkParameters params = getParams();
		return ecKey.toAddress(params).toBase58().toString();
	}

	public static void t(Wallet wallet, String recipientAddress, String password, String mount) {
		Address a = Address.fromBase58(getParams(), recipientAddress);
		SendRequest req = SendRequest.to(a, Coin.parseCoin(mount));
		req.aesKey = wallet.getKeyCrypter().deriveKey(password);
		try {
			wallet.sendCoins(req);
		} catch (InsufficientMoneyException e) {
			e.printStackTrace();
		}
	}

	public static void watchAddress() {
		Wallet toWatch = null;
		DeterministicKey watchingKey = toWatch.getWatchingKey();
		String s = watchingKey.serializePubB58(getParams());
		long creationTimeSeconds = watchingKey.getCreationTimeSeconds();

		DeterministicKey key = DeterministicKey.deserializeB58(null, "key data goes here", getParams());

		Wallet wallet = Wallet.fromWatchingKey(getParams(), key);

		NetworkParameters params = TestNet3Params.get();

		DeterministicSeed seed = new DeterministicSeed(new SecureRandom(), 128, "password", Utils.currentTimeSeconds());
		wallet = Wallet.fromSeed(params, seed);

		// tobytes
		byte[] bytes = MnemonicCode.toSeed(new ArrayList<>(), passphrase);

	}

	public static void test() {
		NetworkParameters params = TestNet3Params.get();
		DeterministicSeed seed = new DeterministicSeed(new SecureRandom(), 128, "123456", Utils.currentTimeSeconds());
		List<String> mnemonicCode = seed.getMnemonicCode();
		System.err.println("mnemonicCode" + mnemonicCode);
		// byte[] bytes = MnemonicCode.toSeed(mnemonicCode, "123456");
		Wallet wallet = Wallet.fromSeed(params, seed);
	}

	public static void test2(ECKey ceKey) {
		NetworkParameters params = getParams();
		String s = ceKey.toAddress(params).toBase58().toString();
		String privateKeyAsWiF = ceKey.getPrivateKeyAsWiF(params);// 私钥，
																	// WIF(Wallet
																	// Import
																	// Format)
		System.err.println(privateKeyAsWiF + "==========" + s);
	}

	// 通过私钥拿到eckey
	public static ECKey getECkey(String prikey) {
		ECKey key = DumpedPrivateKey.fromBase58(getParams(), prikey).getKey();
		return key;
	}

	// 通过助记词导入新钱包
	public static Wallet createWallet(String seedCode, String password) {
		KeyChainGroup kcg;
		DeterministicSeed deterministicSeed = null;
		try {
			deterministicSeed = new DeterministicSeed(seedCode, null, password, Utils.currentTimeSeconds());
		} catch (UnreadableWalletException e) {
			e.printStackTrace();
		}
		kcg = new KeyChainGroup(getParams(), deterministicSeed);
		Wallet wallet = new Wallet(getParams(), kcg);
		return wallet;
	}

	// 创建新钱包。
	public static Wallet createWallet2() {
		KeyChainGroup kcg = new KeyChainGroup(getParams());
		Wallet wallet = new Wallet(getParams(), kcg);
		wallet.getParams().getId();
		return wallet;

	}

	public static void testAddress(Wallet wallet) {
		Address a = wallet.currentReceiveAddress();
		ECKey b = wallet.currentReceiveKey();
		Address c = wallet.freshReceiveAddress();
	}

	public static void userSPeed(Wallet wallet) {

		NetworkParameters params = getParams();
		DeterministicSeed seed = wallet.getKeyChainSeed();
		System.out.println("Seed words are: " + Joiner.on(" ").join(seed.getMnemonicCode()));
		System.out.println("Seed birthday is: " + seed.getCreationTimeSeconds());

		// 通过speed 获取Wallet
		String seedCode = "yard impulse luxury drive today throw farm pepper survey wreck glass federal";
		String seedCode2 = "liberty identify erase shuffle dignity armed produce mention actual you top vendor";
		long creationtime = 1409478661L;
		DeterministicSeed seed2;
		try {
			seed2 = new DeterministicSeed(seedCode, null, "", creationtime);
			Wallet restoredWallet = Wallet.fromSeed(params, seed2);
		} catch (UnreadableWalletException e) {
			e.printStackTrace();
		}

	}

	public static void getWallet() {
		NetworkParameters params = getParams();
		Wallet wallet = new Wallet(params);

		List<ECKey> keys = new ArrayList<>();
		ECKey ecKey = new ECKey();
		// 加密eckey
		ecKey.encrypt(wallet.getKeyCrypter(), wallet.getKeyCrypter().deriveKey("123456"));
		keys.add(ecKey);

		wallet.importKeysAndEncrypt(keys, "123456");
		try {
			SPVBlockStore blockStore = new SPVBlockStore(params, getBLockFile());
			BlockChain chain = new BlockChain(params, wallet, blockStore);
			PeerGroup peerGroup = new PeerGroup(params, chain);
			peerGroup.addWallet(wallet);
			peerGroup.startAsync();
			peerGroup.downloadBlockChain();
			// startAndWait()
		} catch (BlockStoreException e) {
			e.printStackTrace();
		}
	}

	public static File getBLockFile() {
		File file = new File("/tmp/bitcoin-blocks");
		if (!file.exists()) {
			try {
				boolean newFile = file.createNewFile();
				if (newFile) {
					return file;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	// 发送交易
	public static void send(Wallet wallet, String recipientAddress, String amount) {
		NetworkParameters params = getParams();
		Address targetAddress = Address.fromBase58(params, recipientAddress);
		// Do the send of 1 BTC in the background. This could throw
		// InsufficientMoneyException.
		SPVBlockStore blockStore = null;
		try {
			blockStore = new SPVBlockStore(params, getBLockFile());
		} catch (BlockStoreException e) {
			e.printStackTrace();
		}
		BlockChain chain = null;
		try {
			chain = new BlockChain(params, wallet, blockStore);
			PeerGroup peerGroup = new PeerGroup(params, chain);
			try {
				Wallet.SendResult result = wallet.sendCoins(peerGroup, targetAddress, Coin.parseCoin(amount));
				// Save the wallet to disk, optional if using auto saving (see
				// below).
				// wallet.saveToFile(....);
				// Wait for the transaction to propagate across the P2P network,
				// indicating acceptance.
				try {
					Transaction transaction = result.broadcastComplete.get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				return;
			} catch (InsufficientMoneyException e) {
				e.printStackTrace();
			}
		} catch (BlockStoreException e) {
			e.printStackTrace();
		}

	}

	public static String send(WalletAppKit walletAppKit, String recipientAddress, String amount) {
		NetworkParameters params = getParams();
		String err = "";

		if (StringUtils.isEmpty(recipientAddress) || recipientAddress.equals("Scan recipient QR")) {
			err = "Select recipient";
			return err;
		}
		if (StringUtils.isEmpty(amount) | Double.parseDouble(amount) <= 0) {
			err = "Select valid amount";
			return err;

		}
		if (walletAppKit.wallet().getBalance().isLessThan(Coin.parseCoin(amount))) {
			err = "You got not enough coins";
			return err;
		}
		SendRequest request = SendRequest.to(Address.fromBase58(params, recipientAddress), Coin.parseCoin(amount));
		try {
			walletAppKit.wallet().completeTx(request);
			walletAppKit.wallet().commitTx(request.tx);
			walletAppKit.peerGroup().broadcastTransaction(request.tx).broadcast();
			return "";
		} catch (InsufficientMoneyException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	/**
	 * 通过Wallet 获取 助记词
	 * 
	 * @param wallet
	 * @return
	 */
	public static List<String> getSeedWordsFromWallet(Wallet wallet) {
		DeterministicSeed seed = wallet.getKeyChainSeed();
		return seed.getMnemonicCode();
	}

	// 通过speed 获取钱包
	public static Wallet getFromSpeed(String seedCode) {
		NetworkParameters params = getParams();
		DeterministicSeed seed;
		try {
			seed = new DeterministicSeed(seedCode, null, passphrase, Utils.currentTimeSeconds());

			Wallet restoredWallet = Wallet.fromSeed(params, seed);
			return restoredWallet;
		} catch (UnreadableWalletException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 通过本地文件获取Wallet
	public static Wallet getWalletFromFile(String filePath) {
		try {
			return Wallet.loadFromFile(new File(filePath));
		} catch (UnreadableWalletException e) {
			e.printStackTrace();
		}
		return null;
	}

}
