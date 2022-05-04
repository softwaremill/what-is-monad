package com.softwaremill.monad

import scala.concurrent.Future

object _60_Monad_example:
  trait Monad[F[_]]:
    def pure[T](t: T): F[T]
    def flatMap[T, U](a: F[T])(f: T => F[U]): F[U]

  val listMonad: Monad[List] = new Monad[List]:
    def pure[T](t: T): List[T] = List(t)
    def flatMap[T, U](a: List[T])(f: T => List[U]): List[U] = a.flatMap(f)

@main def demo1(): Unit =
  import _60_Monad_example.*
  println(listMonad.flatMap(List(1, 2, 3))((element: Int) => List(element, element)))
