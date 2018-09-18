package me.dangoslen.pathz.bandwidth.client.apis.messages;

import feign.Response;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

@Path("v1/users/{userId}/messages")
public interface BandwidthMessagingApiSpec {

    @POST
    @Consumes("application/json")
    Response sendMessage(@PathParam("userId") String userId, Message message);

    @GET
    @Produces("application/json")
    List<Message> getAllMessages(@PathParam("userId") String userId);
}
