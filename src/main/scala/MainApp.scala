import akka.actor.typed.ActorSystem

object MainApp extends App {

  val a = ActorSystem(CountActor.apply(), "local")
  for {
    _ <- 0 until 100
  } yield {
    a ! CountActor.NotThreadSafeIncrease
  }

}
