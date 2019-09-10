package mandelbrot

import scala.util.Try

object Parse {
  def toInt( str: String, default: Int = 0 ):Int = Try( str.toInt ).getOrElse( default )
  def toDouble( str: String, default: Double = 0.0 ):Double = Try( str.toDouble ).getOrElse( default )
}
