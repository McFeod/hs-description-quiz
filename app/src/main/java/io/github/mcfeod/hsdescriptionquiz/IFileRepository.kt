package io.github.mcfeod.hsdescriptionquiz

import java.lang.Exception

class FileRepoError: Exception()

interface IFileRepository {
    @Throws(FileRepoError::class)
    fun writeImage(path: String, image: ByteArray)

    @Throws(FileRepoError::class)
    fun readImage(path: String): ByteArray

    fun generatePath(): String
}