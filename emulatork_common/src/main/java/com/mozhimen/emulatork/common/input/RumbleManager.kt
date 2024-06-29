package com.mozhimen.emulatork.common.input

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.InputDevice
import com.mozhimen.basick.utilk.kotlinx.coroutines.collectSafe
import com.mozhimen.emulatork.basic.setting.SettingManager
import com.mozhimen.emulatork.common.core.CoreBundle
import com.mozhimen.emulatork.input.unit.InputUnitManager
import com.swordfish.libretrodroid.RumbleEvent
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.newSingleThreadContext
import kotlin.math.roundToInt

/**
 * @ClassName RumbleManager
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/5/13
 * @Version 1.0
 */
@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
class RumbleManager(
    applicationContext: Context,
    private val settingManager: SettingManager,
    private val inputUnitManager: InputUnitManager
) {
    private val deviceVibrator = applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    private val rumbleContext = newSingleThreadContext("Rumble")

    suspend fun collectAndProcessRumbleEvents(
        coreBundle: CoreBundle,
        rumbleEventsObservable: Flow<RumbleEvent>
    ) {
        val enableRumble = settingManager.enableRumble()
        val isSupportRumble = coreBundle.isSupportRumble

        if (!enableRumble && isSupportRumble) {
            return
        }

        inputUnitManager.getEnabledInputsObservable()
            .map { getVibrators(it) }
            .flatMapLatest { vibrators ->
                rumbleEventsObservable
                    .onEach { kotlin.runCatching { vibrate(vibrators[it.port], it) } }
                    .onStart { stopAllVibrators(vibrators) }
                    .onCompletion { stopAllVibrators(vibrators) }
                    .flowOn(rumbleContext)
            }
            .collectSafe { }
    }

    private fun stopAllVibrators(vibrators: List<Vibrator>) {
        vibrators.forEach {
            kotlin.runCatching { it.cancel() }
        }
    }

    private suspend fun getVibrators(gamePads: List<InputDevice>): List<Vibrator> {
        val enableDeviceRumble = settingManager.enableDeviceRumble()

        return if (gamePads.isEmpty() && enableDeviceRumble) {
            listOf(deviceVibrator)
        } else {
            gamePads.map { it.vibrator }
        }
    }

    private fun vibrate(vibrator: Vibrator?, rumbleEvent: RumbleEvent) {
        if (vibrator == null) return

        vibrator.cancel()

        val amplitude = computeAmplitude(rumbleEvent)

        if (amplitude == 0) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && vibrator.hasAmplitudeControl()) {
            vibrator.vibrate(VibrationEffect.createOneShot(MAX_RUMBLE_DURATION_MS, amplitude))
        } else if (amplitude > LEGACY_MIN_RUMBLE_STRENGTH) {
            vibrator.vibrate(MAX_RUMBLE_DURATION_MS)
        }
    }

    private fun computeAmplitude(rumbleEvent: RumbleEvent): Int {
        val strength = rumbleEvent.strengthStrong * 0.66f + rumbleEvent.strengthWeak * 0.33f
        return (DEFAULT_RUMBLE_STRENGTH * (strength) * 255).roundToInt()
    }

    companion object {
        const val MAX_RUMBLE_DURATION_MS = 1000L
        const val DEFAULT_RUMBLE_STRENGTH = 0.5f
        const val LEGACY_MIN_RUMBLE_STRENGTH = 100
    }
}