package com.softwaremill.monad

import scala.concurrent.{ExecutionContext, Future}

object _80_Monad_laws:
  trait Monad[F[_]]:
    def pure[T](t: T): F[T]
    def flatMap[T, U](a: F[T])(f: T => F[U]): F[U]

  val listMonad: Monad[List] = new Monad[List]:
    def pure[T](t: T): List[T] = List(t)
    def flatMap[T, U](a: List[T])(f: T => List[U]): List[U] = a.flatMap(f)

  // laws
  class MonadLaws[F[_]](m: Monad[F]):
    // pure(t).flatMap(f) == f(t)
    def leftIdentity[T, U](t: T, f: T => F[U]): Boolean = m.flatMap(m.pure(t))(f) == f(t)

    // v.flatMap(pure) == v
    def rightIdentity[T](v: F[T]): Boolean = m.flatMap(v)(m.pure) == v

    // v.flatMap(f).flatMap(g) == v.flatMap(t => f(t).flatMap(g))
    def associativity[T, U, V](v: F[T], f: T => F[U], g: U => F[V]): Boolean =
      m.flatMap(m.flatMap(v)(f))(g) == m.flatMap(v)(t => m.flatMap(f(t))(g))

@main def demo2(): Unit =
  // on a List
  val f: Int => List[Int] = element => List(element, element)
  println(List(10).flatMap(f) == f(10))
