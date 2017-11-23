package dev.olog.presentation.fragment_player

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dev.olog.domain.interactor.player.GetMiniPlayingQueueUseCase
import dev.olog.presentation.music_service.RxMusicServiceControllerCallback
import javax.inject.Inject

class PlayerFragmentViewModelFactory @Inject constructor(
        private val controllerCallback: RxMusicServiceControllerCallback,
        private val getMiniPlayingQueueUseCase: GetMiniPlayingQueueUseCase

) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlayerFragmentViewModel(
                controllerCallback,
                getMiniPlayingQueueUseCase
        ) as T
    }

}