package io.github.lanlacope.maytomato.clazz.propaty

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

const val ERROR = "ERROR"

object AppGitHost {
    const val SOURCE: String = "https://github.com/lanlacope/Maytomato"
    const val LICENSE: String = "https://github.com/lanlacope/Maytomato/blob/master/README.MD#license"
    const val LATEST: String = "https://github.com/lanlacope/Maytomato/releases/latest"
    const val LATEST_API: String = "https://api.github.com/repos/Maytomato/NXShare/releases/latest"
    const val LATEST_TAG: String = "tag_name"
}

object ThemeJsonPropaty {
    const val APP_THEME: String = "ThemeType"
    const val THEME_SYSTEM: String = "SystemTheme"
    const val THEME_LIGHT: String = "LightTheme"
    const val THEME_DARK: String = "DarkTheme"
    val APP_THEME_LIST: PersistentList<String> = persistentListOf(
        THEME_SYSTEM,
        THEME_LIGHT,
        THEME_DARK
    )
}