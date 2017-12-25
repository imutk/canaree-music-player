package dev.olog.presentation.fragment_mini_queue

import android.arch.lifecycle.Lifecycle
import android.databinding.ViewDataBinding
import android.view.MotionEvent
import dev.olog.presentation.BR
import dev.olog.presentation.R
import dev.olog.presentation._base.BaseListAdapterDraggable
import dev.olog.presentation._base.DataBoundViewHolder
import dev.olog.presentation.dagger.FragmentLifecycle
import dev.olog.presentation.model.DisplayableItem
import dev.olog.presentation.navigation.Navigator
import dev.olog.presentation.service_music.MusicController
import dev.olog.presentation.utils.extension.setOnClickListener
import dev.olog.presentation.utils.extension.setOnLongClickListener
import kotlinx.android.synthetic.main.item_playing_queue.view.*
import javax.inject.Inject

class MiniQueueFragmentAdapter @Inject constructor(
        @FragmentLifecycle lifecycle: Lifecycle,
        private val musicController: MusicController,
        private val navigator: Navigator

): BaseListAdapterDraggable<DisplayableItem>(lifecycle) {

    override fun initViewHolderListeners(viewHolder: DataBoundViewHolder<*>, viewType: Int) {
        viewHolder.setOnClickListener(dataController) { item, _ ->
            if (viewHolder.itemViewType == R.layout.item_playing_queue){
                musicController.skipToQueueItem(item.mediaId)
            }
        }
        viewHolder.setOnLongClickListener(dataController) { item, _ ->
            if (viewHolder.itemViewType == R.layout.item_playing_queue){
                navigator.toDialog(item, viewHolder.itemView)
            }
        }
        viewHolder.setOnClickListener(R.id.more, dataController) { item, _, view ->
            navigator.toDialog(item, view)
        }

        viewHolder.itemView.dragHandle?.setOnTouchListener { _, event ->
            if(event.actionMasked == MotionEvent.ACTION_DOWN) {
                touchHelper?.startDrag(viewHolder)
                true
            } else false
        }
    }

    override fun bind(binding: ViewDataBinding, item: DisplayableItem, position: Int) {
        binding.setVariable(BR.item, item)
    }

    override val hasGranularUpdate: Boolean = false

    override fun getItemViewType(position: Int): Int = dataController[position].type

    override fun areItemsTheSame(oldItem: DisplayableItem, newItem: DisplayableItem): Boolean {
        return oldItem.mediaId == newItem.mediaId
    }

    override fun isViewTypeDraggable(): Int = R.layout.item_playing_queue

}