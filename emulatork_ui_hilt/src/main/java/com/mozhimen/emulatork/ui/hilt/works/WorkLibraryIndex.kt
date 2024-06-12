package com.mozhimen.emulatork.ui.hilt.works

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.WorkerParameters
import com.mozhimen.emulatork.common.EmulatorKCommon
import com.mozhimen.emulatork.ui.works.AbsWorkLibraryIndex
import com.mozhimen.emulatork.ui.hilt.game.GameActivity
import com.mozhimen.emulatork.ui.game.AbsGameActivity
import com.mozhimen.emulatork.ui.works.AbsWorkCoreUpdate
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * @ClassName LibraryIndexWork
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/5/20 22:29
 * @Version 1.0
 */
@HiltWorker
class WorkLibraryIndex @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val emulatorKCommon: EmulatorKCommon,
) : AbsWorkLibraryIndex(context, workerParams) {

    ///////////////////////////////////////////////////////////////////////////

    override fun emulatorKCommon(): EmulatorKCommon {
        return emulatorKCommon
    }

    override fun gameActivityClazz(): Class<out AbsGameActivity> {
        return GameActivity::class.java
    }

    override fun workCoreUpdateClazz(): Class<out AbsWorkCoreUpdate> {
        return WorkCoreUpdate::class.java
    }

    ///////////////////////////////////////////////////////////////////////////

//    @dagger.Module(subcomponents = [Subcomponent::class])
//    abstract class Module {
//        @Binds
//        @IntoMap
//        @WorkerKey(WorkLibraryIndex::class)
//        abstract fun bindMyWorkerFactory(builder: Subcomponent.Builder): AndroidInjector.Factory<out ListenableWorker>
//    }
//
//    @dagger.Subcomponent
//    interface Subcomponent : AndroidInjector<WorkLibraryIndex> {
//        @dagger.Subcomponent.Builder
//        abstract class Builder : AndroidInjector.Builder<WorkLibraryIndex>()
//    }
}