package no.ntnu.prog2007.sudokusolver.game

class Cell(
    val row: Int,
    val column: Int,
    var value: Int,
    var isInputCell: Boolean = false
)