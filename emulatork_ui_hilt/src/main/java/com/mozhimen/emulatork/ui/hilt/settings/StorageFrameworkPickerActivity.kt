package com.mozhimen.emulatork.ui.hilt.settings

import com.mozhimen.emulatork.basic.storage.StorageDirProvider
import com.mozhimen.emulatork.ui.hilt.works.WorkLibraryIndex
import com.mozhimen.emulatork.ui.settings.AbsStorageFrameworkPickerActivity
import com.mozhimen.emulatork.ui.works.AbsWorkLibraryIndex
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @ClassName StorageFrameworkPickerLauncher
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/5/21
 * @Version 1.0
 */
@AndroidEntryPoint
class StorageFrameworkPickerActivity :AbsStorageFrameworkPickerActivity() {
    @Inject
    lateinit var storageProvider: StorageDirProvider

    override fun directoriesManager(): StorageDirProvider {
        return storageProvider
    }

    override fun workLibraryIndexClazz(): Class<out AbsWorkLibraryIndex> {
        return WorkLibraryIndex::class.java
    }
}