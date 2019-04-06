package io.github.mcfeod.hsdescriptionquiz

import android.graphics.Bitmap
import java.lang.Exception

class FileRepoError: Exception()

interface IFileRepository {
    @Throws(FileRepoError::class)
    fun writeImage(path: String, bitmap: Bitmap)

    @Throws(FileRepoError::class)
    fun readImage(path: String): Bitmap

    fun generatePath(): String
}