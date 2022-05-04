package com.softwaremill.monad

import com.softwaremill.monad._70_Monad_example.Monad

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global

import _70_Monad_example.*

object _90_Monad_program:
  class MonadUsage[F[_]](m: Monad[F]):
    def monadProgram(
        userId: Long,
        lookupUser: Long => F[String],
        sendMessage: String => F[Int]
    ): F[Int] =
      m.flatMap(lookupUser(userId)) { u =>
        if u.startsWith("A") then sendMessage("Hello, user A!")
        else m.pure(1)
      }

@main def demo3(): Unit =
  import _90_Monad_program.*

  println("Id:")
  println(
    new MonadUsage(idMonad)
      .monadProgram(10, _ => "Jack", (s: String) => 0)
  )
  println()
  println("List:")
  println(
    new MonadUsage(listMonad)
      .monadProgram(11, _ => List("Adam", "Mary"), (s: String) => List(0))
  )
  println()
  println("Future:")
  println(
    new MonadUsage(futureMonad)
      .monadProgram(12, _ => Future("Sophie"), (s: String) => Future(0))
  )
