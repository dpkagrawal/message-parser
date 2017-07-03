package com.feresr.atlassianchat.DI

import com.feresr.atlassianchat.MainActivity
import com.feresr.atlassianchat.MainViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Created by feresr on 26/6/17.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class)) interface ApplicationComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(mainActivity: MainViewModel)
}