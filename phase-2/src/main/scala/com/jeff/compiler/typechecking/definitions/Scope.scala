package com.jeff.compiler.typechecking.definitions

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
    * Method to get the initialised symbols for a scope.
    * @return A list containing all initialised variables.
    */
  def initialisedSymbols():List[Symbole]

  def isInitialised(name:String):Boolean

}
