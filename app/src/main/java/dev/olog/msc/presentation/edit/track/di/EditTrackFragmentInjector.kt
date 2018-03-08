package dev.olog.msc.presentation.edit.track.di

import android.support.v4.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap
import dev.olog.msc.presentation.edit.track.EditTrackFragment

@Module(subcomponents = arrayOf(EditTrackFragmentSubComponent::class))
abstract class EditTrackFragmentInjector {

    @Binds
    @IntoMap
    @FragmentKey(EditTrackFragment::class)
    internal abstract fun injectorFactory(builder: EditTrackFragmentSubComponent.Builder)
            : AndroidInjector.Factory<out Fragment>

}