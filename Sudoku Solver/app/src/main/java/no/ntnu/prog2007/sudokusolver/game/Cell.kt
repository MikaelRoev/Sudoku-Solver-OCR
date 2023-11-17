package no.ntnu.prog2007.sudokusolver.game

import android.os.Parcel
import android.os.Parcelable

/**
 * Represents a cell in a Sudoku board.
 *
 * @property row The row of the cell.
 * @property column The column of the cell.
 * @property value The value of the cell.
 * @property isInputCell Whether the cell is an input cell.
 * @property isRevealed Whether the cell is revealed on the solved board.
 */
class Cell(
    val row: Int,
    val column: Int,
    var value: Int,
    var isInputCell: Boolean = false,
    var isRevealed: Boolean = false
) : Parcelable {

    /**
     * Constructor for Parcelable.
     */
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    /**
     * Override function for Parcelable.
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(row)
        parcel.writeInt(column)
        parcel.writeInt(value)
        parcel.writeByte(if (isInputCell) 1 else 0)
        parcel.writeByte(if (isRevealed) 1 else 0)
    }

    /**
     * Override function for Parcelable.
     */
    override fun describeContents(): Int {
        return 0
    }

    /**
     * Companion object for Parcelable.
     */
    companion object CREATOR : Parcelable.Creator<Cell> {
        override fun createFromParcel(parcel: Parcel): Cell {
            return Cell(parcel)
        }

        /**
         * Override function for Parcelable companion object.
         */
        override fun newArray(size: Int): Array<Cell?> {
            return arrayOfNulls(size)
        }
    }
}