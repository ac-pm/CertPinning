package com.acpm.certpinning;

/**
 * Created by acpm on 02/15.
 */
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public final class PubKeyManager implements X509TrustManager {

    private static String PUB_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a0282"
            + "010100d2d1b80340854182214ffcf0dd34183ed99759a1b507f881d7f285"
            + "27927b6f9f56a583c57658a54f68b05436a03a476837646115643b1b89a6"
            + "c9f0d8c5bdbaf0476e69ec18040d65721ade87de945bbff0400ea58cba4a"
            + "6a838d9842614e2d369b947dac7e3417d0b4c728fe0896db727bfc41fe45"
            + "75419255decf00af40dead3fe64aa7fb8cebdbf24f2b486501ae9a29a066"
            + "bc65dfacc963f2c5c5e90ce1b4c49d89d8715f58266bd9d38b627123d6bf"
            + "e824d44d6295fc48a8f0c382e7801c72bbbff92660059a392bbb1896cf8b"
            + "2c73cda3d1e014f277fe23933be57a03cfc30c77580c18fda749a4e3f6f2"
            + "b8de60f6a63d6a14e6ee68a73618b957d59fbd0203010001";

    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        assert (chain != null);
        if (chain == null) {
            throw new IllegalArgumentException(
                    "checkServerTrusted: X509Certificate array is null");
        }

        assert (chain.length > 0);
        if (!(chain.length > 0)) {
            throw new IllegalArgumentException(
                    "checkServerTrusted: X509Certificate is empty");
        }

        assert (null != authType && authType.equalsIgnoreCase("RSA"));
        if (!(null != authType && authType.equalsIgnoreCase("RSA"))) {
            //throw new CertificateException(
            //        "checkServerTrusted: AuthType is not RSA");
        }

        // Perform customary SSL/TLS checks
        TrustManagerFactory tmf;
        try {
            tmf = TrustManagerFactory.getInstance("X509");
            tmf.init((KeyStore) null);

            for (TrustManager trustManager : tmf.getTrustManagers()) {
                ((X509TrustManager) trustManager).checkServerTrusted(
                        chain, authType);
            }

        } catch (Exception e) {
            throw new CertificateException(e);
        }

        RSAPublicKey pubkey = (RSAPublicKey) chain[0].getPublicKey();
        String encoded = new BigInteger(1, pubkey.getEncoded()).toString(16);
        // Pin it!
        final boolean expected = PUB_KEY.equalsIgnoreCase(encoded);
        if (!expected) {
            throw new CertificateException("checkServerTrusted: Expected public key: " + PUB_KEY.substring(0,90)+"..."
                    + ", got public key:" + encoded.substring(0,90)+"...");
        }
    }

    public void checkClientTrusted(X509Certificate[] xcs, String string) {
        // throw new
        // UnsupportedOperationException("checkClientTrusted: Not supported yet.");
    }

    public X509Certificate[] getAcceptedIssuers() {
        // throw new
        // UnsupportedOperationException("getAcceptedIssuers: Not supported yet.");
        return null;
    }
}


