package dev.olog.domain.interactor.playlist

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import dev.olog.domain.entity.PlaylistType.PODCAST
import dev.olog.domain.entity.PlaylistType.TRACK
import dev.olog.domain.gateway.podcast.PodcastPlaylistGateway
import dev.olog.domain.gateway.track.PlaylistGateway
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class MoveItemInPlaylistUseCaseTest {

    private val playlistGateway = mock<PlaylistGateway>()
    private val podcastGateway = mock<PodcastPlaylistGateway>()
    private val sut = MoveItemInPlaylistUseCase(playlistGateway, podcastGateway)

    @Test
    fun testInvokePodcast() = runBlockingTest {
        // given
        val id = 1L
        val from = 10
        val to = 11
        val moves = listOf(from to to)
        val input = MoveItemInPlaylistUseCase.Input(id, moves, PODCAST)

        // when
        sut(input)

        verify(podcastGateway).moveItem(id, moves)
        verifyZeroInteractions(playlistGateway)
    }

    @Test
    fun testInvokeTrack() = runBlockingTest {
        // given
        val id = 1L
        val from = 10
        val to = 11
        val moves = listOf(from to to)
        val input = MoveItemInPlaylistUseCase.Input(id, moves, TRACK)

        // when
        sut(input)

        verify(playlistGateway).moveItem(id, moves)
        verifyZeroInteractions(podcastGateway)
    }

}