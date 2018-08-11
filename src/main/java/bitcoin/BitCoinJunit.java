package bitcoin;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.TestNet3Params;
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
		String priKey = "cMoYCwpgXCXwpZvUKku8PLogxyaM8SVMvRoKf2VcxhSjmUptuxLP";
		ECKey ecKey = BitCoinUtil.getECKeyFromPriKey(priKey);
		
		String address =BitCoinUtil.getPubKeyFrom(ecKey);
		
		System.out.println(address);
	}

}
