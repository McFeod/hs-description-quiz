package io.github.mcfeod.hsdescriptionquiz

import android.os.Parcel
import android.os.Parcelable

data class Card(
    val id: String,
    val name: String,
    val locale: String,
    val description: String? = null,
    val imageURL: String? = null,
    val imageLocalPath: String? = null,
    val shown: Boolean = false
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() as String,
        parcel.readString() as String,
        parcel.readString() as String,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(locale)
        parcel.writeString(description)
        parcel.writeString(imageURL)
        parcel.writeString(imageLocalPath)
        parcel.writeByte(if (shown) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Card> {
        override fun createFromParcel(parcel: Parcel): Card {
            return Card(parcel)
        }

        override fun newArray(size: Int): Array<Card?> {
            return arrayOfNulls(size)
        }
    }

    fun shouldFetchDetails(): Boolean = this.description == null || this.imageURL == null
}