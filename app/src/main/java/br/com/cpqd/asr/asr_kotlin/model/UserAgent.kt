package br.com.cpqd.asr.asr_kotlin.model

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.usb.UsbDevice.getDeviceId
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import br.com.cpqd.asr.asr_kotlin.BuildConfig

class UserAgent {

    companion object {

        private val TAG: String = UserAgent::class.java.simpleName

        private val model: String = Build.MODEL

        private val manufacturer: String = Build.MANUFACTURER

        private val os: String = "android"

        private val osVersion: String = Build.VERSION.RELEASE

        private val appName: String = BuildConfig.APPLICATION_ID

        private val appVersion: String = BuildConfig.VERSION_NAME

        fun toMap(): Map<String, String> {
            return mutableMapOf<String, String>("User-Agent" to stringify())
        }

        private fun stringify(): String {
            return "model=$model;manufacturer=$manufacturer;os=$os;os_version=$osVersion;app_name=$appName;app_version=$appVersion"
        }

    }


}