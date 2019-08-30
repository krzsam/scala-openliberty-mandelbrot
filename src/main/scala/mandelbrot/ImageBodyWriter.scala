package mandelbrot

import java.awt.image.BufferedImage
import java.io.OutputStream
import java.lang.annotation.Annotation
import java.lang.reflect.Type

import javax.imageio.ImageIO
import javax.ws.rs.Produces
import javax.ws.rs.core.{MediaType, MultivaluedMap}
import javax.ws.rs.ext.{MessageBodyWriter, Provider}

@Produces(Array("image/png"))
@Provider
class ImageBodyWriter extends MessageBodyWriter[BufferedImage] {
  override def isWriteable(`type`: Class[_], genericType: Type, annotations: Array[Annotation], mediaType: MediaType): Boolean = {
    `type` == classOf[BufferedImage]
  }

  override def writeTo(t: BufferedImage,
                       `type`: Class[_], genericType: Type, annotations: Array[Annotation],
                       mediaType: MediaType, httpHeaders: MultivaluedMap[String, Object], entityStream: OutputStream): Unit = {
    ImageIO.write( t, "PNG", entityStream )
  }

  override def getSize(t: BufferedImage, `type`: Class[_], genericType: Type, annotations: Array[Annotation], mediaType: MediaType): Long =
    -1
}
