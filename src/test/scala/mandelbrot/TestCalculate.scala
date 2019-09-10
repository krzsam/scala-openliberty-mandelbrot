package mandelbrot

import org.apache.commons.math3.complex.Complex
import org.scalatest.FlatSpec

class TestCalculate extends FlatSpec {
  "Escaped data point #1" should "have black colour" in {
    val initialIterations = 100
    val data = DataPoint( 0, 0, new Complex(0,0), initialIterations )

    val ret = Calculate.mapColour( data, initialIterations )

    assert(ret === 0)
  }

  "Escaped data point #2" should "have black colour" in {
    val initialIterations = 100
    val data = DataPoint( 0, 0, new Complex(0,0), initialIterations/2 )

    val ret = Calculate.mapColour( data, initialIterations )

    assert(ret === 8388607)
  }

  "Escaped data point #3" should "have black colour" in {
    val initialIterations = 100
    val data = DataPoint( 0, 0, new Complex(0,0), initialIterations+5 )

    val ret = Calculate.mapColour( data, initialIterations )

    assert(ret === 0)
  }

  "Non-Escaped data point #1" should "have white colour" in {
    val initialIterations = 100
    val data = DataPoint( 0, 0, new Complex(0,0), 0 )

    val ret = Calculate.mapColour( data, initialIterations )

    assert(ret === 0xFFFFFF)
  }

  "Non-Escaped data point #2" should "have white colour" in {
    val initialIterations = 100
    val data = DataPoint( 0, 0, new Complex(0,0), -1 )

    val ret = Calculate.mapColour( data, initialIterations )

    assert(ret === 0xFFFFFF)
  }
}
