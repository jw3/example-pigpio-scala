package pigpio.scala.example

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import com.typesafe.scalalogging.LazyLogging
import pigpio.scaladsl._

import scala.util.{Failure, Success}


object Watcher extends App with LazyLogging {
  implicit val system = ActorSystem("pigpio-example")

  implicit val lib = DefaultInitializer.gpioInitialise() match {
    case Success(Init(lib, ver)) =>
      logger.debug("initialized pigpio V{}", ver.toString)
      //run(lib)
      lib


    case Failure(ex) =>
      logger.error("failed to initialize pigpio", ex)
      system.terminate()
      throw new Error("fail")
  }

  val listener = system.actorOf(Props[Listener])
  val w = GpioAlertFunc(UserGpio(1)).subscribe(listener ! _)


  /*
   *
   *
   */

  def run(implicit lib: PigpioLibrary) = {
    val pin = UserGpio(1)
    logger.debug("starting application on {}", pin)

    //val alertbus = system.actorOf(GpioBus.props())
    logger.debug("a")
    //val gpioactor = GpioPin(pin, alertbus)
    logger.debug("b")
    val listener = system.actorOf(Props[Listener])

    //    logger.debug("c")
    //    gpioactor.tell(Listen(), listener)
    //    logger.debug("d")
    //    gpioactor ! InputPin


  }
}

class Listener extends Actor with ActorLogging {
  def receive: Receive = {
    case m => println(m)
  }
}
