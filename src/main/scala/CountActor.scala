import CountActor.{Command, Increase, InternalIncrease, NotThreadSafeIncrease, Terminate, mockLongComputation}
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Success

object CountActor {

  sealed trait Command
  case object Increase extends Command
  case object NotThreadSafeIncrease extends Command
  private case object InternalIncrease extends Command
  case object Terminate extends Command

  def apply(): Behavior[Command] = new CountActor().online


  def mockLongComputation(f: () => Unit = () => {}): Future[Unit] = {
    Future{
      f()
      Thread.sleep(2000)
    }
  }
}

class CountActor{
  var count = 0
  val online: Behavior[Command] = Behaviors.setup{ctx =>
    Behaviors.receiveMessagePartial{
      case InternalIncrease =>
        this.count = count + 1 // thread-safe
        println(s"count is $count")
        Behaviors.same

    case Increase =>
      ctx.pipeToSelf(mockLongComputation()) {
        case Success(_) =>
          InternalIncrease
      }
      Behaviors.same

      case NotThreadSafeIncrease =>
        mockLongComputation{ () =>
          this.count = this.count + 1
          println(s"current count is ${this.count}")
        }
        Behaviors.same

      case Terminate =>
        println("done :D")
        Behaviors.stopped

  }}
}


