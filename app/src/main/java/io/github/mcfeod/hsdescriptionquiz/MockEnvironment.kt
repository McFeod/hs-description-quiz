package io.github.mcfeod.hsdescriptionquiz


class MockEnvironment: IFileRepository {
    override fun readImage(path: String): ByteArray {
        throw FileRepoError()
    }

    override fun writeImage(path: String, image: ByteArray) {}

    override fun generatePath(): String = ""

    override fun deleteImage(path: String) {}
}