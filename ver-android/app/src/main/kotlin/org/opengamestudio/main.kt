package org.opengamestudio

object MainComponent {
    init {
        setupEffects()
    }

    fun setupEffects() {
        // AuthContext.login -> ChatContext.login
        /*authCtrl().registerFieldCallback("login", { cc: CLDContext ->
            val c = cc as AuthContext
            chatCtrl().set("login", c.login)
        })*/
    }
}
