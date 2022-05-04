package com.softwaremill.monad

import scala.concurrent.Future

object _40_Monad_interface:
  trait Monad[F[_]]:
    def pure[T](t: T): F[T]
