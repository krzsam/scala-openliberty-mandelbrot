package mandelbrot.calculation

import javax.enterprise.context.RequestScoped
import javax.ws.rs.core.{MediaType, Response}
import javax.ws.rs.{GET, Path, Produces, QueryParam}
import mandelbrot.{Calculate, DataPoint}
import net.liftweb.json.{DefaultFormats, Serialization}
import org.apache.commons.math3.complex.Complex
import org.slf4j.{Logger, LoggerFactory}

@RequestScoped
@Path("/iteration")
class IterationService {
  private val LOG: Logger = LoggerFactory.getLogger( this.getClass )

  implicit val formats = DefaultFormats

  @Path("/info")
  @GET
  @Produces(Array(MediaType.TEXT_PLAIN))
  def getInfo: Response = Response.ok("This is Iteration Service" ).build

  @Path("/iterate")
  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  def iterate(
               @QueryParam( "posx" )       posX: Int,
               @QueryParam( "posy" )       posY: Int,
               @QueryParam( "datax" )      dataX: Double,
               @QueryParam( "datay" )      dataY: Double,
               @QueryParam( "iterations")  iterations: Int
             ) : Response = {

    //LOG.info( s"Invoking Iteration service with parameters posX: ${posX} posY: ${posY} dataX: ${dataX} dataY: ${dataY} iterations: ${iterations}" )

    val result: DataPoint = Calculate.calculateOne( DataPoint( posX, posY, new Complex( dataX, dataY ), iterations ) )
    val jsonStr: String = Serialization.write( result )

    //LOG.info( s"Iteration service result: ${result}" )
    //LOG.info( s"Iteration service returning: ${jsonStr}" )

    Response.ok(jsonStr).build
  }
}
