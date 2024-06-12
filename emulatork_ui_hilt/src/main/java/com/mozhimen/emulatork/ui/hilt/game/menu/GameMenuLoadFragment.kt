package com.mozhimen.emulatork.ui.hilt.game.menu
import com.mozhimen.emulatork.common.save.SaveStateManager
import com.mozhimen.emulatork.common.save.SaveStatePreviewManager
import com.mozhimen.emulatork.ui.game.menu.AbsGameMenuLoadFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @ClassName GameMenuLoadFragment
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/5/20 22:15
 * @Version 1.0
 */
@AndroidEntryPoint
class GameMenuLoadFragment: AbsGameMenuLoadFragment() {
    @Inject
    lateinit var saveStateManager: SaveStateManager
    @Inject
    lateinit var saveStatePreviewManager: SaveStatePreviewManager

    override fun statesManager(): SaveStateManager {
        return saveStateManager
    }

    override fun statesPreviewManager(): SaveStatePreviewManager {
        return saveStatePreviewManager
    }

//    @dagger.Module
//    class Module
}