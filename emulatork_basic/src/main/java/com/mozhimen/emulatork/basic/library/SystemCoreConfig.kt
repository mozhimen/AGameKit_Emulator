package com.mozhimen.emulatork.basic.library

import com.mozhimen.emulatork.basic.controller.ControllerConfig
import com.mozhimen.emulatork.basic.core.CoreVariable
import java.io.Serializable

/**
 * @ClassName SystemCoreConfig
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/5/11
 * @Version 1.0
 */
data class SystemCoreConfig(
    val coreID: CoreID,
    val controllerConfigs: HashMap<Int, ArrayList<ControllerConfig>>,
    val exposedSettings: List<ExposedSetting> = listOf(),
    val exposedAdvancedSettings: List<ExposedSetting> = listOf(),
    val defaultSettings: List<CoreVariable> = listOf(),
    val statesSupported: Boolean = true,
    val rumbleSupported: Boolean = false,
    val requiredBIOSFiles: List<String> = listOf(),
    val regionalBIOSFiles: Map<String, String> = mapOf(),
    val statesVersion: Int = 0,
    val supportsLibretroVFS: Boolean = false,
    val skipDuplicateFrames: Boolean = true,
    val supportedOnlyArchitectures: Set<String>? = null
) : Serializable