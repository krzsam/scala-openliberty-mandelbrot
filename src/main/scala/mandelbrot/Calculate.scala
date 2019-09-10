package mandelbrot

import java.awt.image.BufferedImage

import org.apache.commons.math3.complex.Complex
import org.slf4j.{Logger, LoggerFactory}

import scala.annotation.tailrec

object Calculate {
  private val LOG: Logger = LoggerFactory.getLogger( this.getClass )

  @tailrec
  def calculateOneIteration( cn: Complex, c0: Complex, iterations: Integer ) : (Complex,Int) = {
    if( cn.isInfinite || cn.isNaN || cn.abs() >= 4 || iterations <= 0 )
      (cn,iterations)
    else
      calculateOneIteration( cn.multiply( cn ).add( c0 ), c0, iterations-1 )
  }

  def calculateOne( d: DataPoint ) : DataPoint = {
    val ret = calculateOneIteration( Complex.ZERO, d.c, d.iterations )
    DataPoint( d.posX, d.posY, ret._1, ret._2 )
  }

  def mapColour( result: DataPoint, initialIterations: Int ): Int = {
    // 0 remaining iterations - did not escape - white, 0xFFFFFF
    // initialIterations - black, 0x000000
    val inverted = (initialIterations-result.iterations) * 1.0
    val initial = (initialIterations * 1.0)
    val grad = inverted / initial
    val v = (0xFF * grad * 0x10101).toInt

    Math.min( Math.max( 0, v ), 0xFFFFFF )
  }

  def processResult( image: BufferedImage, result: DataPoint, initialIterations: Int ): Unit = {
    image.setRGB( result.posX, result.posY, mapColour( result, initialIterations ) )
  }
}
