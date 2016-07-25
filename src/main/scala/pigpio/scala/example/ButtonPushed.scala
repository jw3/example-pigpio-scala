package pigpio.scala.example

import pigpio.scaladsl._

import scala.io.StdIn
import scala.util.Success

/**
 *
 */
object ButtonPushed extends App {

  DefaultInitializer.gpioInitialise() match {
    case Success(Init(lib, ver)) =>
      println(s"initialized pigpio:$ver")
      lib
    case _ =>
      println("failed")
      System.exit(1)
  }
  implicit val pigpio = PigpioLibrary.INSTANCE

  val l = new Listener2

  RxGpio.installAll()
  DefaultDigitalIO.gpioSetMode(UserGpio(1), InputPin)
  RxGpio(1).map(_.tick).subscribe(l.tick(_))

  println("Press Enter to exit")
  StdIn.readLine()
}


class Listener2 {
  var last = 0L
  def tick(a: Long) = {
    println(s"received GpioAlert(${a}) -> diff ${a - last}")
    last = a
  }
}
