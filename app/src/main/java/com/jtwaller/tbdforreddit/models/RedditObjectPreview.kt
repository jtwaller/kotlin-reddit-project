package com.jtwaller.tbdforreddit.models

import android.os.Parcel
import android.os.Parcelable

class RedditObjectPreview(
        val images: List<PreviewImageObject>?
) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(PreviewImageObject))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(images)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RedditObjectPreview> {
        override fun createFromParcel(parcel: Parcel): RedditObjectPreview {
            return RedditObjectPreview(parcel)
        }

        override fun newArray(size: Int): Array<RedditObjectPreview?> {
            return arrayOfNulls(size)
        }
    }

}

class PreviewImageObject(
        val source: PreviewImage?
        // val resolution: List<PreviewImage>  -- Don't need for now
) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readParcelable<PreviewImage>(PreviewImage::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(source, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PreviewImageObject> {
        override fun createFromParcel(parcel: Parcel): PreviewImageObject {
            return PreviewImageObject(parcel)
        }

        override fun newArray(size: Int): Array<PreviewImageObject?> {
            return arrayOfNulls(size)
        }
    }

}

class PreviewImage(
        val url: String?,
        val width: Int,
        val height: Int
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
        parcel.writeInt(width)
        parcel.writeInt(height)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PreviewImage> {
        override fun createFromParcel(parcel: Parcel): PreviewImage {
            return PreviewImage(parcel)
        }

        override fun newArray(size: Int): Array<PreviewImage?> {
            return arrayOfNulls(size)
        }
    }

}