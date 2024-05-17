package com.mozhimen.emulatork.test.feature.settings

import android.content.Context
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.mozhimen.emulatork.test.R
import com.mozhimen.emulatork.test.shared.settings.BiosPreferences
import com.mozhimen.abilityk.jetpack.preference.SafePreferenceDataStore
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * @ClassName BiosSettingsFragment
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/5/14
 * @Version 1.0
 */
class BiosSettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var biosPreferences: BiosPreferences

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.preferenceDataStore = SafePreferenceDataStore
        setPreferencesFromResource(R.xml.empty_preference_screen, rootKey)
        biosPreferences.addBiosPreferences(preferenceScreen)
    }

    @dagger.Module
    class Module
}
