package com.softwaremill.monad

import scala.concurrent.{ExecutionContext, Future}

trait Monad[F[_]]:
  def pure[T](t: T): F[T]
  def flatMap[T, U](a: F[T])(f: T => F[U]): F[U]

object Monad:
  val listMonad: Monad[List] = new Monad[List]:
    def pure[T](t: T): List[T] = List(t)
    // List(1, 2, 3).flatMap(x => List(x, x)) --> List(1, 1, 2, 2, 3, 3)
    def flatMap[T, U](a: List[T])(f: T => List[U]): List[U] = a.flatMap(element => f(element))

  val optionMonad: Monad[Option] = new Monad[Option]:
    def pure[T](t: T): Option[T] = Some(t)
    def flatMap[T, U](a: Option[T])(f: T => Option[U]): Option[U] = a.flatMap(element => f(element))

  def futureMonad(using ExecutionContext): Monad[Future] = new Monad[Future]:
    def pure[T](t: T): Future[T] = Future.successful(t)
    def flatMap[T, U](a: Future[T])(f: T => Future[U]): Future[U] = a.flatMap(element => f(element))

  type Id[X] = X

  val idMonad: Monad[Id] = new Monad[Id]:
    def pure[T](t: T): T = t
    def flatMap[T, U](a: T)(f: T => U): U = f(a)

  val functionsFromIntMonad: Monad[Int => *] = new Monad[Int => *]:
    def pure[T](t: T): Int => T = _ => t
    def flatMap[T, U](a: Int => T)(f: T => Int => U): Int => U = (x: Int) => f(a(x))(x)

  // common code
  class MonadUsage[F[_]](m: Monad[F]):
    def repeat[T](n: Int, v: F[T], f: T => F[T]): F[T] =
      if n == 0 then v else repeat(n - 1, m.flatMap(v)(f), f)

    def monadProgram(user: F[String], sendMessage: String => F[Int]): F[Int] =
      m.flatMap(user) { u =>
        if u.startsWith("A") then sendMessage("Hello, user A!")
        else m.pure(1)
      }

  // laws
  class MonadLaws[F[_]](m: Monad[F]):
    // pure(t).flatMap(f) == f(t)
    def leftIdentity[T, U](t: T, f: T => F[U]): Boolean = m.flatMap(m.pure(t))(f) == f(t)

    // v.flatMap(pure) == v
    def rightIdentity[T](v: F[T]): Boolean = m.flatMap(v)(m.pure) == v

    // v.flatMap(f).flatMap(g) == v.flatMap(t => f(t).flatMap(g))
    def associativity[T, U, V](v: F[T], f: T => F[U], g: U => F[V]): Boolean =
      m.flatMap(m.flatMap(v)(f))(g) == m.flatMap(v)(t => m.flatMap(f(t))(g))

@main def demo =
  import Monad.*
  println(new MonadUsage(listMonad).repeat(3, listMonad.pure("x"), v => List(v, s"${v}_$v")))

  println(new MonadUsage(listMonad).monadProgram(List("Adam", "Mary"), (s: String) => List(0)))
  println(new MonadUsage(idMonad).monadProgram("Adam", (s: String) => 0))
