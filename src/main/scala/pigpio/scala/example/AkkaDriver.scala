package pigpio.scala.example

import akka.actor.{Actor, ActorSystem, Props}
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import pigpio.scaladsl.GpioPin.Listen
import pigpio.scaladsl.PigpioLibrary.gpioAlertFunc_t
import pigpio.scaladsl._
import rx.lang.scala.Observable
import rx.lang.scala.subjects.PublishSubject

import scala.concurrent.{ExecutionContext, Future}
import scala.io.StdIn
import scala.util.control.NonFatal

object AkkaDriver  {
  def main(args: Array[String]) {
    println("a")
    implicit val system = ActorSystem("driver")
    println("b")
    implicit val jnalib = PigpioLibrary.INSTANCE
    println("c")
    jnalib.gpioInitialise()
    println("d")


    val listener = system.actorOf(Props(new AkkaCallback))
    val pin = GpioPin(UserGpio(26))
    pin.tell(Listen(), listener)
    println("e")
    pin ! InputPin

    println("f")

    println("Press Enter to exit")
    StdIn.readLine()
  }
}


class AkkaCallback extends Actor {
  var last = 0L

  def receive: Receive = {
    case a: GpioAlert =>
      val tick = a.tick
      println(s"received GpioAlert($tick) -> diff ${tick - last}")
      last = tick
  }
}


/*

object AkkaDriver extends App {
  println("a")
  implicit val system = ActorSystem("driver")
  println("b")
  implicit val jnalib = PigpioLibrary.INSTANCE
  println("c")
  jnalib.gpioInitialise()
  println("d")
  val listener = system.actorOf(Props[Listener])
  println("e")

  implicit val mat = ActorMaterializer()
  println("f")
  val src = Source.queue[GpioAlert](100, OverflowStrategy.dropTail)
  println("g")

  val x = src.toMat(Sink.ignore)(Keep.left).run()
  println("h")
  val h = new AlertHandler(x.offer(_))
  println("i")
  jnalib.gpioSetAlertFunc(1, h)
  println("j")

  src.runWith(Sink.foreach(a => println(s"${a.tick}")))
  println("k")
  println("Press Enter to exit")
  StdIn.readLine()
}


class AkkaCallback extends Actor {
  var last = 0L

  def receive: Receive = {
    case a: GpioAlert =>
      val tick = a.tick
      println(s"received GpioAlert($tick) -> diff ${tick - last}")
      last = tick
  }
}

object AlertHandler {
  implicit val ec = ExecutionContext.global
}

class AlertHandler(handler: GpioAlert => Unit) extends gpioAlertFunc_t {
  import AlertHandler._

  def callback(gpio: Int, level: Int, tick: Int /*UINT32*/): Unit = {
    Future {handler(GpioAlert(gpio, level, tick))}
  }
}

 */
