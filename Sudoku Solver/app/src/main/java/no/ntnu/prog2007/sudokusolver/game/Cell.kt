package no.ntnu.prog2007.sudokusolver.game

import android.os.Parcel
import android.os.Parcelable

class Cell(
    val row: Int,
    val column: Int,
    var value: Int,
    var isInputCell: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(row)
        parcel.writeInt(column)
        parcel.writeInt(value)
        parcel.writeByte(if (isInputCell) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cell> {
        override fun createFromParcel(parcel: Parcel): Cell {
            return Cell(parcel)
        }

        override fun newArray(size: Int): Array<Cell?> {
            return arrayOfNulls(size)
        }
    }
}