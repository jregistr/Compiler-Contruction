package com.jeff.compiler.bool


class Parser {
  val store:Map[Char, Boolean] = (for(e <- 'a' to 'z') yield e -> false).toMap

}
