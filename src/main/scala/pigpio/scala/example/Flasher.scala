package pigpio.scala.example

import akka.actor.ActorSystem
import com.typesafe.scalalogging.LazyLogging
import pigpio.scaladsl._

import scala.concurrent.Await
import scala.concurrent.duration.{Duration, DurationInt}
import scala.util.{Failure, Success}


object Flasher extends App with LazyLogging {
  implicit val system = ActorSystem("pigpio-example")
  implicit val lpigpio = PigpioLibrary.INSTANCE

  DefaultInitializer.gpioInitialise() match {
    case Success(Init(lib, ver)) =>
      logger.debug("initialized pigpio V{}", ver.toString)
      run()

    case Failure(ex) =>
      logger.error("failed to initialize pigpio", ex)
      system.terminate()
  }

  Await.ready(system.whenTerminated, Duration.Inf)
  logger.debug("gpio terminating")
  DefaultInitializer.gpioTerminate()
  logger.debug("application terminating")

  /*
   *
   *
   */

  def run() = {
    val pin = UserGpio(3)
    logger.debug("starting application on {}", pin)

    val gpioactor = GpioPin(pin)
    gpioactor ! OutputPin

    import system.dispatcher
    system.scheduler.schedule(0.seconds, 10.seconds, gpioactor, Levels.on)
    system.scheduler.schedule(5.seconds, 10.seconds, gpioactor, Levels.off)
  }
}
