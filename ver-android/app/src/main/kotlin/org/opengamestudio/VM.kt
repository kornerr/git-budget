package org.opengamestudio
import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

object VM {
    var androidContext: Context? = null
    val playgroundIsVisible = mutableStateOf(false)
    val playgroundTitle = mutableStateOf("TODO-Title")

    init {
        playCtrl().set("didLaunch", true)
        // Launch main component differently since it has no ctrl.
        MainComponent.setupEffects()
    }

    fun reportFailure(
        title: String,
        message: String
    ) {
        reportFailure(androidContext!!, title, message)
    }
}
