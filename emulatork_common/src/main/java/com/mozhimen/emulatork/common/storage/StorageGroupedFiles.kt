package com.mozhimen.emulatork.common.storage

/**
 * @ClassName GroupedStorageFiles
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/5/11
 * @Version 1.0
 */
data class StorageGroupedFiles(
    val primaryFile: StorageBaseFile,
    val dataFiles: List<StorageBaseFile>
) {
    fun allFiles() = listOf(primaryFile) + dataFiles
}