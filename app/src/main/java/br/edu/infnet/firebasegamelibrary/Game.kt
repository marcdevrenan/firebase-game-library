package br.edu.infnet.firebasegamelibrary

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Game(
    var id: String = "",
    var title: String = "",
    var publisher: String = "",
    var platform: String = "",
    var status: Int = 0
) : Parcelable {
    init {
        this.id = FirebaseUtils.getDatabase().push().key ?: ""
    }
}