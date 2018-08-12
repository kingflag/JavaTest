package bitcoin;

import java.security.SecureRandom;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.wallet.Wallet;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BitCoinJunit {

	private static final Logger log = LoggerFactory.getLogger(BitCoinJunit.class);

	NetworkParameters params = TestNet3Params.get();

	@Before
	public void createNet() {
		params = TestNet3Params.get();
	}

	@Test
	public void sendBTC() {
		try {
			/**
			 * We created a new key: ECKey{pub
			 * HEX=02dacbaf9f67518bd6175ce18545bbe63ecbe37f4f1354ef45cf78790f2bcfb8fa,
			 * creationTimeSeconds=1534046942, isEncrypted=false,
			 * isPubKeyOnly=false} Public Address generated:
			 * mrB95r2Ugcxe1ky94Mky5AmejewYLXe7HE Private key is:
			 * cT9XgWVopC7uvQcFuGmrgQ4PTd4uv4dxHmSyTnSTUU8yC8VWi9Ha Private Hex
			 * key is:
			 * a613355ab06092b3cf784350f8c13485bb8d75a048d48655ad3abf9bf8dce6f5
			 * Start peer group Downloading block chain Block chain downloaded
			 * Waiting for coins...
			 */
			String hexPriKey = "0xa613355ab06092b3cf784350f8c13485bb8d75a048d48655ad3abf9bf8dce6f5";

			String priKey = "cT9XgWVopC7uvQcFuGmrgQ4PTd4uv4dxHmSyTnSTUU8yC8VWi9Ha";
			ECKey ecKey = BitCoinUtil.getECKeyFromHexPriKey(hexPriKey);

			ECKey ecKey2 = BitCoinUtil.getECkey(priKey);
			String address = BitCoinUtil.getPubKeyFrom(ecKey);
			String address2 = BitCoinUtil.getPubKeyFrom(ecKey2);
			System.out.println(address);
			System.out.println(address2);

			// 构建钱包
			Wallet wallet = new Wallet(params);
			wallet.importKey(ecKey);
			String recipientAddress = "mvWz4q7uVsAX5h1LVah6EnXDVTAME8hS39";
			String amount = "0.01";
			BitCoinUtil.send(wallet, recipientAddress, amount);
			// final Wallet.SendResult sendResult = wallet.sendCoins(peerGroup,
			// toAddress, amountToSend);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
