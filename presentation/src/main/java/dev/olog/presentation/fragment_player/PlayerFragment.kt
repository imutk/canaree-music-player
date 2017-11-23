package dev.olog.presentation.fragment_player

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jakewharton.rxbinding2.view.RxView
import dev.olog.presentation.GlideApp
import dev.olog.presentation.R
import dev.olog.presentation._base.BaseFragment
import dev.olog.presentation.model.CoverModel
import dev.olog.presentation.model.PlayerFragmentMetadata
import dev.olog.presentation.music_service.MusicController
import dev.olog.presentation.utils.asLiveData
import dev.olog.presentation.utils.subscribe
import dev.olog.presentation.widgets.SwipeableImageView
import kotlinx.android.synthetic.main.fragment_player.*
import kotlinx.android.synthetic.main.layout_player_toolbar.*
import javax.inject.Inject

class PlayerFragment : BaseFragment() {

    @Inject lateinit var viewModel: PlayerFragmentViewModel
    @Inject lateinit var musicController: MusicController

    lateinit var title: TextView
    lateinit var artist: TextView

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        title = activity!!.findViewById(R.id.title)
        artist = activity!!.findViewById(R.id.artist)

        viewModel.onMetadataChangedLiveData
                .subscribe(this, this::setMetadata)

        viewModel.onCoverChangedLiveData(context!!)
                .subscribe(this, this::setCover)

        viewModel.onPlaybackStateChangedLiveData
                .subscribe(this, {
                    seekBar.handleState(it)
                    nowPlaying.isActivated = it
                    cover.isActivated = it
                })

        viewModel.onRepeatModeChangedLiveData
                .subscribe(this, {
                    repeat.setImageResource(if (it == PlaybackStateCompat.REPEAT_MODE_ONE)
                        R.drawable.vd_repeat_one else R.drawable.vd_repeat)
                    repeat.setColorFilter(ContextCompat.getColor(context!!,
                            if (it == PlaybackStateCompat.REPEAT_MODE_NONE) R.color.button_primary_tint
                            else R.color.item_selected))
                })

        viewModel.onShuffleModeChangedLiveData
                .subscribe(this , {
                    shuffle.setColorFilter(ContextCompat.getColor(context!!,
                            if (it == PlaybackStateCompat.SHUFFLE_MODE_NONE) R.color.button_primary_tint
                            else R.color.item_selected))
                })

        viewModel.onMaxChangedObservable
                .subscribe(this, {
                    duration.text = it.asString
                    seekBar.max = it.asInt
                })

        seekBar.observeStopTrackingTouch()
                .map { it.progress.toLong() }
                .asLiveData()
                .subscribe(this, { musicController.seekTo(it) })

//        bookmark textView will automatically updated
        viewModel.onBookmarkChangedObservable
                .subscribe(this, { seekBar.progress = it })

        seekBar.observeChanges()
                .asLiveData()
                .subscribe(this, { bookmark.text = it })

        RxView.clicks(repeat)
                .asLiveData()
                .subscribe(this, { musicController.toggleRepeatMode() })

        RxView.clicks(shuffle)
                .asLiveData()
                .subscribe(this, { musicController.toggleShuffleMode() })

        RxView.clicks(fakeNext)
                .asLiveData()
                .subscribe(this, { musicController.skipToNext() })

        RxView.clicks(fakePrevious)
                .asLiveData()
                .subscribe(this, { musicController.skipToPrevious() })
    }

    override fun onResume() {
        super.onResume()
        cover.setOnSwipeListener(object : SwipeableImageView.SwipeListener {

            override fun onSwipedLeft() {
                musicController.skipToNext()
            }

            override fun onSwipedRight() {
                musicController.skipToPrevious()
            }

            override fun onClick() {
                musicController.playPause()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        cover.setOnSwipeListener(null)
    }

    private fun setMetadata(metadata: PlayerFragmentMetadata){
        title.text = metadata.title
        artist.text = metadata.artist
    }

    private fun setCover(coverModel: CoverModel){
        val (img, placeholder) = coverModel
        GlideApp.with(context).clear(cover)

        GlideApp.with(context)
                .load(img)
                .centerCrop()
                .placeholder(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .priority(Priority.IMMEDIATE)
                .override(800)
                .into(cover)
    }

    override fun provideView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_player, container, false)
    }


}