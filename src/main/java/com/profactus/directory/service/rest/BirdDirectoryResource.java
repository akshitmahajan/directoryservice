package com.profactus.directory.service.rest;
 
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.exc.UnrecognizedPropertyException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.profactus.directory.configuration.AppConfig;
import com.profactus.directory.exception.BirdDirectoyServiceException;
import com.profactus.directory.model.Bird;
import com.profactus.directory.service.BirdDirectoyService;
 
@Path("/birds")
public class BirdDirectoryResource {
 

	AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
	private BirdDirectoyService service = (BirdDirectoyService) context.getBean("birdDirectoryService");

	public void setService(BirdDirectoyService service) {
		this.service = service;
	}

	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public Response createBird(Bird bird) throws UnrecognizedPropertyException {
		Bird addedBird = service.addBird(bird);
		return Response.status(Response.Status.CREATED).entity(addedBird).build();
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
    public Response getBirds() {
		Bird[] birds = service.getAllVisibleBirds();
		if(birds != null) {
			return Response.status(Response.Status.OK).entity(birds).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@GET	
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getBirdDetailsById(@PathParam("id") String id) throws UnrecognizedPropertyException {
		Bird bird = service.getBirdById(id);
		if(bird != null) {
			if(bird.isVisible()) {
				return Response.status(Response.Status.OK).entity(bird).build();
			} else return Response.status(Response.Status.OK).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@DELETE
	@Path("/{id}")
	public Response getMsg(@PathParam("id") String id) throws UnrecognizedPropertyException {
		if(service.deleteBird(id)){
			return Response.status(Response.Status.NO_CONTENT).build();
		} else return Response.status(Response.Status.NOT_FOUND).build();
	}
 
}