package App;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.*;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ejbs.Calculation;

@Stateless
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CalculationService {
	@PersistenceContext(unitName="hello")
	private EntityManager em;
	
	 private List<Calculation> calculations = new ArrayList<>();
	 Calculation calc;
	 private int performCalculation(Calculation calculation) {
	        int number1 = calculation.getNumber1();
	        int number2 = calculation.getNumber2();
	        String operation = calculation.getOperation();

	        switch (operation) {
	            case "+":
	                return number1 + number2;
	            case "-":
	                return number1 - number2;
	            case "*":
	                return number1 * number2;
	            case "/":
	                if (number2 != 0) {
	                    return number1 / number2;
	                } else {
	                    throw new IllegalArgumentException("Division by zero");
	                }
	            default:
	                throw new IllegalArgumentException("Invalid operation");
	        }
	    }
	 @POST
	    @Path("calc")
	    @Produces(MediaType.APPLICATION_JSON)
	    @Consumes(MediaType.APPLICATION_JSON)
	 public Response createCalculation(Calculation calculation) {
	        try {
	           
	            int result = performCalculation(calculation);

	            calculation.setNumber1(calculation.getNumber1());
	            calculation.setNumber2(calculation.getNumber2());
	            calculation.setOperation(calculation.getOperation());
	            calculations.add(calculation);
	            em.persist(calculation);

	            
	            return Response.status(Response.Status.OK)
	                    .entity("{\"Result\":" + result + "}")
	                    .build();
	        } catch (Exception e) {
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                    .entity("Error occurred: " + e.getMessage())
	                    .build();
	        }
	    }
	 
	
	 @GET
	 @Path("calculations")
	 @Produces(MediaType.APPLICATION_JSON)
	 public List<Calculation> getAllCalculationsListResource() {
	List<Calculation>query=em.createQuery("SELECT c FROM Calculation c",Calculation.class).getResultList();
		 return query;
	 }


}

