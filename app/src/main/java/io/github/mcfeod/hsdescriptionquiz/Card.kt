package io.github.mcfeod.hsdescriptionquiz

import java.io.Serializable

data class Card (
    val id: String,
    val name: String,
    val locale: String,
    val description: String? = null,
    val imageURL: String? = null,
    val imageLocalPath: String? = null,
    val shown: Boolean = false
): Serializable {
    fun shouldFetchDetails(): Boolean = this.description == null || this.imageURL == null
}