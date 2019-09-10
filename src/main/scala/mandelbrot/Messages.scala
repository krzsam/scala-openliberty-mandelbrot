package mandelbrot

import org.apache.commons.math3.complex.Complex

case class DataPoint( posX: Int, posY: Int, c: Complex, iterations: Int )
//case class Marker( begin: Boolean, sizeX: Int, sizeY: Int )   // begin: True-begin, False-end
//case class Message(data: Option[DataPoint], marker: Option[Marker] )
