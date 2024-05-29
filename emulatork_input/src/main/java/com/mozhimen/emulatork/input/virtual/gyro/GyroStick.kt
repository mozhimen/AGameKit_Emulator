package com.mozhimen.emulatork.input.virtual.gyro

import com.swordfish.radialgamepad.library.RadialGamePad

/**
 * @ClassName StickTiltTracker
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/5/13
 * @Version 1.0
 */
class GyroStick(val id: Int) : Gyro {

    override fun updateTracking(xTilt: Float, yTilt: Float, pads: Sequence<RadialGamePad>) {
        pads.forEach { it.simulateMotionEvent(id, xTilt, yTilt) }
    }

    override fun stopTracking(pads: Sequence<RadialGamePad>) {
        pads.forEach { it.simulateClearMotionEvent(id) }
    }

    override fun trackedIds(): Set<Int> = setOf(id)
}