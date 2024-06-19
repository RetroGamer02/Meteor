package meteor.plugin

import meteor.config.Config
import meteor.config.ConfigManager
import meteor.ui.compose.sidebar.PluginsButton.Companion.runningMap

open class Plugin(val name: String) : EventSubscriber(){
    var enabledByDefault = false
    var configuration: Config? = null
    var running = false

    open fun onStart() {

    }
    open fun onStop() {

    }

    fun start() {
        val enable = ConfigManager.get<Boolean>("plugin.$name.enabled", enabledByDefault)
        if (!enable)
            return
        onStart()
        subscribeEvents(true)
        running = true
        runningMap[this] = true
    }

    fun stop() {
        unsubscribe()
        onStop()
        running = false
        runningMap[this] = false
    }

    inline fun <reified T> configuration(): T {
        configuration = T::class.constructors.first().call(this) as Config
        return configuration as T
    }
}