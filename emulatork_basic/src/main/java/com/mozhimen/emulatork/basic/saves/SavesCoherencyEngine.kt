package com.mozhimen.emulatork.basic.saves

import com.mozhimen.emulatork.basic.library.CoreID
import com.mozhimen.emulatork.basic.library.db.entities.Game

/**
 * @ClassName SavesCoherencyEngine
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/5/13
 * @Version 1.0
 */
/*
   Why does this class exist? Because shit happens and we want to make sure we are prepared.
   This is the issue:

   User enables auto-save, plays, disables auto-save, plays for 10h, saves in game, re-enables
   auto-save and loses 10h worth of game.

   If we detect a more recent SRAM file, we basically avoid loading the state. This is also handy,
   if different cores share the same SRAM file. */
class SavesCoherencyEngine(val savesManager: SavesManager, val statesManager: StatesManager) {

    suspend fun shouldDiscardAutoSaveState(game: Game, coreID: CoreID): Boolean {
        val autoSRAM = savesManager.getSaveRAMInfo(game)
        val autoSave = statesManager.getAutoSaveInfo(game, coreID)
        return autoSRAM.exists && autoSave.exists && autoSRAM.date > autoSave.date + TOLERANCE
    }

    companion object {
        private const val TOLERANCE = 30L * 1000L
    }
}
