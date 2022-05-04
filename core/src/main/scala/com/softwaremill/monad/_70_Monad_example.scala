package com.softwaremill.monad

import cats.effect.IO

import scala.concurrent.{ExecutionContext, Future}

object _70_Monad_example:
  trait Monad[F[_]]:
    def pure[T](t: T): F[T]
    def flatMap[T, U](a: F[T])(f: T => F[U]): F[U]

  val listMonad: Monad[List] = new Monad[List]:
    def pure[T](t: T): List[T] = List(t)
    def flatMap[T, U](a: List[T])(f: T => List[U]): List[U] = a.flatMap(f)

  val optionMonad: Monad[Option] = new Monad[Option]:
    def pure[T](t: T): Option[T] = Some(t)
    def flatMap[T, U](a: Option[T])(f: T => Option[U]): Option[U] = a.flatMap(element => f(element))

  def futureMonad(using ExecutionContext): Monad[Future] = new Monad[Future]:
    def pure[T](t: T): Future[T] = Future.successful(t)
    def flatMap[T, U](a: Future[T])(f: T => Future[U]): Future[U] = a.flatMap(element => f(element))

  val ioMonad: Monad[IO] = new Monad[IO]:
    def pure[T](t: T): IO[T] = IO.pure(t)
    def flatMap[T, U](a: IO[T])(f: T => IO[U]): IO[U] = a.flatMap(f)

  type Id[X] = X

  val idMonad: Monad[Id] = new Monad[Id]:
    def pure[T](t: T): T = t
    def flatMap[T, U](a: T)(f: T => U): U = f(a)

  val functionsFromIntMonad: Monad[Int => *] = new Monad[Int => *]:
    def pure[T](t: T): Int => T = _ => t
    def flatMap[T, U](a: Int => T)(f: T => Int => U): Int => U = (x: Int) => f(a(x))(x)
