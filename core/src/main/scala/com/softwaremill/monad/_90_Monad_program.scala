package com.softwaremill.monad

import com.softwaremill.monad._70_Monad_example.Monad

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global

object _90_Monad_program:
  trait Monad[F[_]]:
    def pure[T](t: T): F[T]
    def flatMap[T, U](a: F[T])(f: T => F[U]): F[U]

  val listMonad: Monad[List] = new Monad[List]:
    def pure[T](t: T): List[T] = List(t)
    def flatMap[T, U](a: List[T])(f: T => List[U]): List[U] = a.flatMap(f)

  def futureMonad(using ExecutionContext): Monad[Future] = new Monad[Future]:
    def pure[T](t: T): Future[T] = Future.successful(t)
    def flatMap[T, U](a: Future[T])(f: T => Future[U]): Future[U] = a.flatMap(element => f(element))

  type Id[X] = X
  val idMonad: Monad[Id] = new Monad[Id]:
    def pure[T](t: T): T = t
    def flatMap[T, U](a: T)(f: T => U): U = f(a)

  class MonadUsage[F[_]](m: Monad[F]):
    def monadProgram(userId: Long, lookupUser: Long => F[String], sendMessage: String => F[Int]): F[Int] =
      m.flatMap(lookupUser(userId)) { u =>
        if u.startsWith("A") then sendMessage("Hello, user A!")
        else m.pure(1)
      }

@main def demo3(): Unit =
  import _90_Monad_program.*

  println("Id:")
  println(new MonadUsage(idMonad).monadProgram(10, _ => "Jack", (s: String) => 0))
  println()
  println("List:")
  println(new MonadUsage(listMonad).monadProgram(11, _ => List("Adam", "Mary"), (s: String) => List(0)))
  println()
  println("Future:")
  println(new MonadUsage(futureMonad).monadProgram(12, _ => Future("Sophie"), (s: String) => Future(0)))
