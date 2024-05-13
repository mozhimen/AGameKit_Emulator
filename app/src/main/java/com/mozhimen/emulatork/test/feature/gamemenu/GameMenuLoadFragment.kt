package com.mozhimen.emulatork.test.feature.gamemenu

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.mozhimen.emulatork.basic.library.SystemCoreConfig
import com.mozhimen.emulatork.basic.library.db.mos.Game
import com.mozhimen.emulatork.basic.saves.StatesManager
import com.mozhimen.emulatork.basic.saves.StatesPreviewManager
import com.mozhimen.emulatork.test.R
import com.mozhimen.emulatork.test.shared.GameMenuContract
import com.mozhimen.emulatork.test.shared.gamemenu.GameMenuHelper
import com.mozhimen.emulatork.util.coroutines.launchOnState
import com.mozhimen.emulatork.util.preferences.DummyDataStore
import dagger.android.support.AndroidSupportInjection
import java.security.InvalidParameterException
import javax.inject.Inject

/**
 * @ClassName GameMenuLoadFragment
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/5/13
 * @Version 1.0
 */
class GameMenuLoadFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var statesManager: StatesManager
    @Inject
    lateinit var statesPreviewManager: StatesPreviewManager

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.preferenceDataStore = DummyDataStore
        addPreferencesFromResource(R.xml.empty_preference_screen)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val extras = activity?.intent?.extras

        val game = extras?.getSerializable(GameMenuContract.EXTRA_GAME) as Game?
            ?: throw InvalidParameterException("Missing EXTRA_GAME")

        val systemCoreConfig = extras?.getSerializable(GameMenuContract.EXTRA_SYSTEM_CORE_CONFIG) as SystemCoreConfig?
            ?: throw InvalidParameterException("Missing EXTRA_SYSTEM_CORE_CONFIG")

        launchOnState(Lifecycle.State.CREATED) {
            setupLoadPreference(game, systemCoreConfig)
        }
    }

    private suspend fun setupLoadPreference(game: Game, systemCoreConfig: SystemCoreConfig) {
        val slotsInfo = statesManager.getSavedSlotsInfo(game, systemCoreConfig.coreID)

        slotsInfo.forEachIndexed { index, saveInfo ->
            val bitmap = GameMenuHelper.getSaveStateBitmap(
                requireContext(),
                statesPreviewManager,
                saveInfo,
                game,
                systemCoreConfig.coreID,
                index
            )

            GameMenuHelper.addLoadPreference(
                preferenceScreen,
                index,
                saveInfo,
                bitmap
            )
        }
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        return GameMenuHelper.onPreferenceTreeClicked(activity, preference)
    }

    @dagger.Module
    class Module
}