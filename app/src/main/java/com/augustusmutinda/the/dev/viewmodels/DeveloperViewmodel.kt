package com.augustusmutinda.the.dev.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class DeveloperViewmodel : ViewModel() {
    val developerOptionsState = MutableStateFlow("Default")
    val adbIP = MutableStateFlow("")

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
                if (enabled) adbIP.update { getAdbWifiInfo(context).orEmpty() }

            } catch (e: Settings.SettingNotFoundException) {
                developerOptionsState.update {
                    "restricted"
                }
            }
        }
    }

    @SuppressLint("DefaultLocale")
    fun getAdbWifiInfo(context: Context): String? {
        val result = try {
            val ip = android.net.wifi.WifiManager::class.java
                .getMethod("getConnectionInfo")
                .invoke(context.getSystemService(Context.WIFI_SERVICE)) as android.net.wifi.WifiInfo

            val ipAddress = ip.ipAddress
            val ipStr = String.format(
                "%d.%d.%d.%d",
                ipAddress and 0xff,
                ipAddress shr 8 and 0xff,
                ipAddress shr 16 and 0xff,
                ipAddress shr 24 and 0xff
            )

            // Default port for adb wireless
            "$ipStr:5555"
        } catch (e: Exception) {
            ""
        }

        return result
    }
}