package meteor.config

class ConfigItem<T>(val name: String, val key: String, val defaultValue: T) {
    inline fun <reified T> get(): T {
        return ConfigManager.get(key, defaultValue as Any) as T
    }

    fun set(value: Any) {
        ConfigManager.set(key, value)
    }

    fun toggle() {
        if (defaultValue is Boolean) {
            ConfigManager.set(key, !get<Boolean>())
        }
    }
}