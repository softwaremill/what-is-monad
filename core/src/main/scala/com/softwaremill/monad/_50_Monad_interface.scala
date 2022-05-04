package com.softwaremill.monad

import scala.concurrent.Future

object _50_Monad_interface:
  trait Monad[F[_]]:
    def pure[T](t: T): F[T]
    def flatMap[T, U](a: F[T])(f: T => F[U]): F[U]
