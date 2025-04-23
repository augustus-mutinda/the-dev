package com.augustusmutinda.the.dev.viewmodels

import android.content.Context
import android.provider.Settings
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class DeveloperViewmodel : ViewModel() {
    val developerOptionsState = MutableStateFlow("Default")

    fun isDeveloperOptionsEnabled(context: Context) {
        if (developerOptionsState.value != "checking") {
            developerOptionsState.update { "checking" }
            try {
                val enabled = Settings.Global.getInt(
                    context.contentResolver,
                    Settings.Global.DEVELOPMENT_SETTINGS_ENABLED
                ) == 1

                developerOptionsState.update {
                    if (enabled) "enabled" else "disabled"
                }
            } catch (e: Settings.SettingNotFoundException) {
                developerOptionsState.update {
                    "restricted"
                }
            }
        }
    }
}