package com.projectraphael.animegreatsage.ai

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.StatFs
import android.os.Environment
import java.io.File

data class DeviceAiCompatibility(
    val androidSdk: Int,
    val isArm64: Boolean,
    val totalRamMb: Long,
    val availableRamMb: Long,
    val freeStorageMb: Long,
    val meetsLocalRuntimeBaseline: Boolean,
    val explanation: String
)

/**
 * Conservative capability check, not a performance guarantee.
 * A compatible result does not mean a model is installed or will run smoothly.
 */
object DeviceCompatibility {
    fun inspect(context: Context): DeviceAiCompatibility {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memory = ActivityManager.MemoryInfo()
        manager.getMemoryInfo(memory)
        val totalRam = memory.totalMem / (1024L * 1024L)
        val availableRam = memory.availMem / (1024L * 1024L)

        val dataPath = context.filesDir ?: File(Environment.getDataDirectory(), "data")
        val stats = StatFs(dataPath.absolutePath)
        val freeStorage = (stats.availableBytes / (1024L * 1024L)).coerceAtLeast(0L)

        val arm64 = Build.SUPPORTED_ABIS.any { it.equals("arm64-v8a", ignoreCase = true) }
        val sdkOkay = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        val memoryOkay = totalRam >= 3072L && availableRam >= 900L
        val storageOkay = freeStorage >= 1200L
        val meets = arm64 && sdkOkay && memoryOkay && storageOkay

        val reasons = buildList {
            if (!arm64) add("ARM64 ABI not detected")
            if (!sdkOkay) add("Android 10 or newer is required by the planned local runtime")
            if (!memoryOkay) add("available memory is below the conservative baseline")
            if (!storageOkay) add("free storage is below the model-installation baseline")
        }

        return DeviceAiCompatibility(
            androidSdk = Build.VERSION.SDK_INT,
            isArm64 = arm64,
            totalRamMb = totalRam,
            availableRamMb = availableRam,
            freeStorageMb = freeStorage,
            meetsLocalRuntimeBaseline = meets,
            explanation = if (meets) {
                "Device meets the preliminary local-AI baseline. Real model performance still requires testing."
            } else {
                reasons.joinToString("; ")
            }
        )
    }
}
