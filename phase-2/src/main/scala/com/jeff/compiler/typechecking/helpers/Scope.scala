package com.jeff.compiler.typechecking.helpers

/**
  * Trait to define a simple scope
  */
trait Scope {

  val name: String

  /**
    * Method to get the optional enclosing scope.
    *
    * @return The optional scope enclosing this scope.
    */
  def enclosingScope: Option[Scope]

  /**
    * Method to search for a symbol locally in a given scope.
    *
    * @param name The name of the symbol.
    * @return An option that may enclose the symbol.
    */
  def findSymbolLocally(name: String): Option[Symbole]

  /**
    * Method to search for a symbol globally in relation to the scope.
    *
    * @param name The name of the symbol.
    * @return An option that may enclose the symbol.
    */
  def findSymbolDeeply(name: String): Option[Symbole]

  /**
    * Method to search for an initialised symbol. Checks the current and any parent scope available.
    *
    * @param name The name of the symbol.
    * @return An option that may enclose the symbol.
    */
  def findInitialisedSymbol(name: String): Option[Symbole]

  /**
    * Method to add a symbol to a scope.
    *
    * @param symbol The symbol to add.
    * @return A try signaling success or failure.
    */
  def addSymbol(symbol: Symbole): Unit

  /**
    * Method to initialise a symbol.
    *
    * @param symbol The symbol to initialise.
    * @return A try.
    */
  def initialiseSymbol(symbol: Symbole): Unit

  /**
    * Method to check if a given symbol has been initialised.
    *
    * @param symbol The symbol to check.
    * @return a boolean.
    */
  def isInitialised(symbol: Symbole): Boolean

}
