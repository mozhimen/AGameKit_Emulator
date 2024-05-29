package com.mozhimen.emulatork.common.save

import com.mozhimen.basick.utilk.java.io.file2bytes_use_ofUnzip
import com.mozhimen.basick.utilk.kotlin.UtilKResult
import com.mozhimen.basick.utilk.kotlin.bytes2file_ofGZip
import com.mozhimen.emulatork.core.ECoreId
import com.mozhimen.emulatork.basic.game.db.entities.Game
import com.mozhimen.emulatork.basic.storage.StorageProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File

/**
 * @ClassName StatesManager
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/5/13
 * @Version 1.0
 */
// TODO Since states are core related we should not put them in the same folder. This break previous versions states
// so I decided to manage a transition phase reading also the old directory. We should safely remove it in a few weeks.

class SaveStateManager(private val storageProvider: StorageProvider) {

    suspend fun getSlotSave(
        game: Game,
        coreID: com.mozhimen.emulatork.core.ECoreId,
        index: Int
    ): SaveState? = withContext(Dispatchers.IO) {
        assert(index in 0 until MAX_STATES)
        getSaveState(getSlotSaveFileName(game, index), coreID.coreName)
    }

    suspend fun setSlotSave(
        game: Game,
        saveState: SaveState,
        coreID: com.mozhimen.emulatork.core.ECoreId,
        index: Int
    ) = withContext(Dispatchers.IO) {
        assert(index in 0 until MAX_STATES)
        setSaveState(getSlotSaveFileName(game, index), coreID.coreName, saveState)
    }

    suspend fun getAutoSaveInfo(
        game: Game,
        coreID: com.mozhimen.emulatork.core.ECoreId
    ): SaveInfo = withContext(Dispatchers.IO) {
        val autoSaveFile = getStateFile(getAutoSaveFileName(game), coreID.coreName)
        val autoSaveHasData = autoSaveFile.length() > 0
        SaveInfo(autoSaveFile.exists() && autoSaveHasData, autoSaveFile.lastModified())
    }

    suspend fun getAutoSave(game: Game, coreID: com.mozhimen.emulatork.core.ECoreId) = withContext(Dispatchers.IO) {
        getSaveState(getAutoSaveFileName(game), coreID.coreName)
    }

    suspend fun setAutoSave(
        game: Game,
        coreID: com.mozhimen.emulatork.core.ECoreId,
        saveState: SaveState
    ) = withContext(Dispatchers.IO) {
        setSaveState(getAutoSaveFileName(game), coreID.coreName, saveState)
    }

    suspend fun getSavedSlotsInfo(
        game: Game,
        coreID: com.mozhimen.emulatork.core.ECoreId
    ): List<SaveInfo> = withContext(Dispatchers.IO) {
        (0 until MAX_STATES)
            .map { getStateFileOrDeprecated(getSlotSaveFileName(game, it), coreID.coreName) }
            .map { SaveInfo(it.exists(), it.lastModified()) }
            .toList()
    }

    private suspend fun getSaveState(
        fileName: String,
        coreName: String
    ): SaveState? {
        return UtilKResult.runCatching_ofRetry(FILE_ACCESS_RETRIES) {
            val saveFile = getStateFileOrDeprecated(fileName, coreName)
            val metadataFile = getMetadataStateFile(fileName, coreName)
            if (saveFile.exists()) {
                val byteArray = saveFile.file2bytes_use_ofUnzip()!!
                val stateMetadata = runCatching {
                    Json.Default.decodeFromString(
                        SaveState.SaveMetadata.serializer(),
                        metadataFile.readText()
                    )
                }
                SaveState(byteArray, stateMetadata.getOrNull() ?: SaveState.SaveMetadata())
            } else {
                null
            }
        }.getOrNull()
    }

    private suspend fun setSaveState(
        fileName: String,
        coreName: String,
        saveState: SaveState
    ) {
        UtilKResult.runCatching_ofRetry(FILE_ACCESS_RETRIES) {
            writeStateToDisk(fileName, coreName, saveState.state)
            writeMetadataToDisk(fileName, coreName, saveState.metadata)
        }
    }

    private fun writeMetadataToDisk(
        fileName: String,
        coreName: String,
        metadata: SaveState.SaveMetadata
    ) {
        val metadataFile = getMetadataStateFile(fileName, coreName)
        metadataFile.writeText(Json.encodeToString(SaveState.SaveMetadata.serializer(), metadata))
    }

    private fun writeStateToDisk(
        fileName: String,
        coreName: String,
        stateArray: ByteArray
    ) {
        val saveFile = getStateFile(fileName, coreName)
        stateArray.bytes2file_ofGZip(saveFile)
    }

    @Deprecated("Using this folder collisions might happen across different systems.")
    private fun getStateFileOrDeprecated(fileName: String, coreName: String): File {
        val stateFile = getStateFile(fileName, coreName)
        val deprecatedStateFile = getDeprecatedStateFile(fileName)
        return if (stateFile.exists() || !deprecatedStateFile.exists()) {
            stateFile
        } else {
            deprecatedStateFile
        }
    }

    private fun getStateFile(fileName: String, coreName: String): File {
        val statesDirectories = File(storageProvider.getExternalFileStates(), coreName)
        statesDirectories.mkdirs()
        return File(statesDirectories, fileName)
    }

    private fun getMetadataStateFile(
        stateFileName: String,
        coreName: String
    ): File {
        val statesDirectories = File(storageProvider.getExternalFileStates(), coreName)
        statesDirectories.mkdirs()
        return File(statesDirectories, "$stateFileName.metadata")
    }

    @Deprecated("Using this folder collisions might happen across different systems.")
    private fun getDeprecatedStateFile(fileName: String): File {
        val statesDirectories = storageProvider.getInternalFileStates()
        return File(statesDirectories, fileName)
    }

    private fun getAutoSaveFileName(game: Game) = "${game.fileName}.state"
    private fun getSlotSaveFileName(game: Game, index: Int) = "${game.fileName}.slot${index + 1}"

    companion object {
        const val MAX_STATES = 4
        private const val FILE_ACCESS_RETRIES = 3
    }
}