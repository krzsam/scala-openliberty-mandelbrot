package mandelbrot.image

import java.awt.image.BufferedImage

import javax.enterprise.context.RequestScoped
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.core.Response.Status
import javax.ws.rs.core.{MediaType, Response}
import javax.ws.rs.{GET, Path, Produces, QueryParam}
import mandelbrot.{Calculate, DataPoint}
import net.liftweb.json.{DefaultFormats, Serialization}
import org.apache.commons.math3.complex.Complex
import org.slf4j.{Logger, LoggerFactory}

import scala.util.{Failure, Success, Try}

@RequestScoped
@Path("/image")
class ImageService {
  private val LOG: Logger = LoggerFactory.getLogger( this.getClass )

  val calculationService = "calculation-svc"
  val calculationServicePort = 9080

  val client = ClientBuilder.newClient()

  implicit val formats = DefaultFormats

  @Path("/info")
  @GET
  @Produces(Array(MediaType.TEXT_PLAIN))
  def getInfo: Response = Response.ok("This is Image Generation Service" ).build

  def invokeCalculationService(data: DataPoint, img: BufferedImage, initialIterations: Int ) = {
    val queryString=s"http://${calculationService}:${calculationServicePort}/mandelbrot/iteration/iterate?" +
      s"posx=${data.posX}&posy=${data.posY}&datax=${data.c.getReal}&datay=${data.c.getImaginary}&iterations=${data.iterations}"

    val response: Response = client.target( queryString ).request().get()
    //LOG.info( s"For ${queryString} got response: ${response}")
    if( response.getStatus == Status.OK.getStatusCode ) {
      val ent = response.readEntity( classOf[String] )
      val data = Serialization.read[DataPoint]( ent )
      Calculate.processResult( img, data, initialIterations )
    }
  }

  @Path("/generate")
  @GET
  @Produces(Array("image/png"))
  def getImage(
                @QueryParam( "tlx" )   topLeftX: Double,
                @QueryParam( "tly" )   topLeftY: Double,
                @QueryParam( "brx" )   bottomRightX: Double,
                @QueryParam( "bry" )   bottomRightY: Double,
                @QueryParam( "sx" )    stepsX: Int,
                @QueryParam( "sy" )    stepsY: Int,
                @QueryParam( "iterations")  iterations: Int
              ): Response = {
    val img = new BufferedImage( stepsX, stepsY, BufferedImage.TYPE_INT_RGB )

    LOG.info( s"Image buffer created ${img.getWidth} x ${img.getHeight}")

    val distRe = bottomRightX - topLeftX
    val stepRe = distRe / stepsX

    val distIm = topLeftY - bottomRightY
    val stepIm = distIm / stepsY

    val topLeft = new Complex( topLeftX, topLeftY )

    val rangeY = 0 until stepsY
    rangeY.foreach((itemY) => {
      val rangeX = 0 until stepsX
      rangeX.foreach(
        (itemX) => {
          val z0 = topLeft.add( new Complex(stepRe * itemX, -stepIm * itemY) )
          val data = DataPoint(itemX, itemY, z0, iterations )
          invokeCalculationService( data, img, iterations )
        }
      )
    })

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
