package com.feresr.atlassianchat.DI

import com.feresr.atlassianchat.MainViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class)) interface ApplicationComponent {
    fun inject(mainActivity: MainViewModel)
}