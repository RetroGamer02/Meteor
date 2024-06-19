package meteor.plugin.debug

import meteor.config.Config
import meteor.config.ConfigItem
import meteor.plugin.Plugin

class DebugConfig(plugin: Plugin) : Config(plugin) {
    init {
        items.apply {
            add(ConfigItem("Debug NPCs", "isDebugNPCs".key(), false))
            add(ConfigItem("Debug Players", "isDebugPlayers".key(), false))
            add(ConfigItem("Debug Locs", "isDebugLocs".key(), false))
        }
    }
    fun isDebugNPCs() = get<Boolean>("isDebugNPCs".key())
    fun isDebugPlayers() = get<Boolean>("isDebugPlayers".key())
    fun isDebugLocs() = get<Boolean>("isDebugLocs".key())
}