package de.doridian.yiffcraft;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SSLConnector {
    public static SSLSocketFactory allTrustingSocketFactory;

    static {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {

                    }
                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {

                    }
                }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            allTrustingSocketFactory = sc.getSocketFactory();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
