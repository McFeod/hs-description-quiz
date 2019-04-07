package io.github.mcfeod.hsdescriptionquiz

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["id", "locale"])
data class Card(
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "locale") val locale: String,
    @ColumnInfo(name = "description") val description: String? = null,
    @ColumnInfo(name = "url") val imageURL: String? = null,
    @ColumnInfo(name = "path") val imageLocalPath: String? = null,
    @ColumnInfo(name = "shown") val shown: Boolean = false
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