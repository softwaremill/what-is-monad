package com.softwaremill.monad

import scala.concurrent.Future

object _30_Monad_interface:
  trait Monad[F[_]]

  // usage:
  new Monad[List] {}
  new Monad[Option] {}
  new Monad[Future] {}
