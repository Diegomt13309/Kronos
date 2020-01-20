/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest.subscription;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Security;
import java.util.Base64;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

/**
 *
 * @author jonathan
 */
class Subscription {
    // Getters and setters for e.g. endpoint
    private String auth;
    private String key;
    private String endpoint;
    
    public Subscription() {
  // Add BouncyCastle as an algorithm provider
  if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
      Security.addProvider(new BouncyCastleProvider());
  }
}
    
    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getAuth() {
        return auth;
    }

    /**
     * Returns the base64 encoded auth string as a byte[]
     */
    public byte[] getAuthAsBytes() {
        return Base64.getDecoder().decode(getAuth());
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    /**
     * Returns the base64 encoded public key string as a byte[]
     */
    public byte[] getKeyAsBytes() {
        return Base64.getDecoder().decode(getKey());
    }

    /**
     * Returns the base64 encoded public key as a PublicKey object
     */
    public PublicKey getUserPublicKey() throws Exception {
        KeyFactory kf = KeyFactory.getInstance("ECDH", BouncyCastleProvider.PROVIDER_NAME);
        ECNamedCurveParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256r1");
        ECPoint point = ecSpec.getCurve().decodePoint(getKeyAsBytes());
        ECPublicKeySpec pubSpec = new ECPublicKeySpec(point, ecSpec);

        return kf.generatePublic(pubSpec);
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
	return endpoint;
    }
    
    
    
    public static void sendPushMessage(Subscription sub,PublicKey pk ,byte[] payload) {


  Notification notification;
  PushService pushService;

        try {
            // Create a notification with the endpoint, userPublicKey from the subscription and a custom payload
            notification = new Notification(
                    sub.getEndpoint(),
                    pk,
                    sub.getAuthAsBytes(),
                    payload
            ); 
        
               pushService = new PushService();
               pushService.send(notification);
        
        } catch (Exception ex) {
            
                System.out.println(ex.getMessage());
        }

 
}
}