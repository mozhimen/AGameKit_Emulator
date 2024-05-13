package com.mozhimen.emulatork.test.feature.input.inputclass

import com.mozhimen.emulatork.test.feature.input.InputKey

/**
 * @ClassName InputClassUnknown
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/5/13
 * @Version 1.0
 */
object InputClassUnknown : InputClass {
    override fun getInputKeys(): Set<InputKey> {
        return emptySet()
    }

    override fun getAxesMap(): Map<Int, Int> {
        return emptyMap()
    }
}
