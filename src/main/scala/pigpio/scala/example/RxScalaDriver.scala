package pigpio.scala.example

import akka.actor.{ActorRef, ActorSystem, Props}
import pigpio.scala.example.DriverConstants._
import pigpio.scaladsl._

import scala.io.StdIn


object RxScalaDriver extends App {
  implicit val system = ActorSystem("driver")

  implicit val jnalib = PigpioLibrary.INSTANCE
  jnalib.gpioInitialise()

  RxGpio.installAll()
  DefaultDigitalIO.gpioSetMode(UserGpio(pin), InputPin)


  val listener = system.actorOf(Props(new AkkaCallback))

  val cb = new RxScalaCallback

  val call = new Call(listener)

  RxGpio(pin).subscribe(call.call(_))

  println("Press Enter to exit")
  StdIn.readLine()
}


class Call(ref: ActorRef) {
  def call(a: GpioAlert) = {
    ref ! a
  }
}

class RxScalaCallback {
  var last = 0L
  def tick(a: GpioAlert) = {
    val tick = a.tick
    println(s"received GpioAlert($tick) -> diff ${tick - last}")
    last = tick
  }
}
