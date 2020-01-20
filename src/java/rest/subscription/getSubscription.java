/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest.subscription;

import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author jonathan
 */
@Path("/subscription")
public class getSubscription {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/getSubscription")
    public Response getSubscription(Subscription s) {

        try {
            Response.ResponseBuilder response = Response.ok();
            Subscription sus= new Subscription();
            sus.setAuth(s.getAuth());
            sus.setEndpoint(s.getEndpoint());
            sus.setKey(s.getKey());
            String mess="HOLA PROBANDO 123";
           PublicKey pk= sus.getUserPublicKey();
           System.out.println(pk.toString());
            Subscription.sendPushMessage(sus,pk,mess.getBytes());
            return response.build();
        } 
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
}