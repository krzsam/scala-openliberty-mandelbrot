package mandelbrot.system

import java.awt.image.BufferedImage

import javax.enterprise.context.RequestScoped
import javax.ws.rs.core.{MediaType, Response}
import javax.ws.rs.{GET, Path, Produces}
import org.eclipse.microprofile.metrics.annotation.{Counted, Timed}
import org.slf4j.{Logger, LoggerFactory}

import scala.util.{Failure, Random, Success, Try}

@RequestScoped
@Path("/system")
class SystemService {
  private val LOG: Logger = LoggerFactory.getLogger( this.getClass )

  @Path("/info")
  @GET
  @Produces(Array(MediaType.TEXT_PLAIN))
  @Counted(absolute = true, description = "Number of times info is requested")
  def getInfo: Response = Response.ok("Mandelbrot Example Application using OpenLiberty Microservices" ).build

  @Path("/properties")
  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Timed(name = "getPropertiesTime", description = "Time needed to get the JVM system properties")
  @Counted(absolute = true, description = "Number of times the JVM system properties are requested")
  def getProperties: Response = Response.ok(System.getProperties).build

  @Path("/testimage")
  @GET
  @Produces(Array("image/png"))
  @Timed(name = "getImageTime", description = "Time needed to get image")
  @Counted(absolute = true, description = "Number of times image is requested")
  def getImage: Response = {
    val size = 320
    val img = new BufferedImage( size, size, BufferedImage.TYPE_INT_RGB )

    LOG.info( s"Image buffer created ${img.getWidth} x ${img.getHeight}")

    1 to 100 foreach{
      x =>
        val coordX = Random.nextInt( size-1 )
        val coordY = Random.nextInt( size-1 )
        LOG.info( s"Setting pixel #${x} at ${coordX} x ${coordY}")
        img.setRGB( coordX, coordY, 0xff0000 )
    }

    val status = Try(  Response.ok(img).build )
    status match {
      case Success( response ) =>
        LOG.info( s"Returning correct response: ${response.getStatus}")
        response
      case Failure( exception ) =>
        val res = Response.Status.INTERNAL_SERVER_ERROR
        LOG.error( s"Returning error response: ${res.getStatusCode}, error: ${exception.getLocalizedMessage}", exception )
        Response.status( res.getStatusCode, exception.getLocalizedMessage ).build()
    }
  }
}
