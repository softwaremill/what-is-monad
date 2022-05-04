package com.softwaremill.monad

object _20_Type_constructor {
  // List is a type constructor

  // List[String] and List[Int] are types:
  val x: List[String] = List("a", "b", "c")
  val y: List[Int] = List(1, 2, 3)

  // Option and Future are other examples of type constructors
}
