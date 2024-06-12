package com.mozhimen.emulatork.ui.dagger.game.menu

import android.content.Context
import com.mozhimen.emulatork.common.save.SaveStateManager
import com.mozhimen.emulatork.common.save.SaveStatePreviewManager
import com.mozhimen.emulatork.ui.game.menu.AbsGameMenuSaveFragment
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * @ClassName GameMenuSaveFragment
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/5/20 22:17
 * @Version 1.0
 */
class GameMenuSaveFragment : AbsGameMenuSaveFragment() {
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

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    @dagger.Module
    class Module

}