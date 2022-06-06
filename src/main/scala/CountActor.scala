import CountActor.{Command, Incr, InternalIncre, mockLongComputation}
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Success

object CountActor {

  sealed trait Command
  case object Incr extends Command
  private case object InternalIncre extends Command

  def apply(): Behavior[Command] = new CountActor(0).online


  def mockLongComputation: Future[Unit] = {
    Future(
      Thread.sleep(2000)
    )
  }
}

class CountActor(var count: Int){
  val online: Behavior[Command] = Behaviors.setup{ctx =>
    Behaviors.receiveMessagePartial{
      case InternalIncre =>
        this.count = count + 1 // thread-safe
        println(s"count is $count")
        Behaviors.same
    case Incr =>
      ctx.pipeToSelf(mockLongComputation) {
        case Success(_) =>
          InternalIncre
      }

      Behaviors.same

  }}
}


