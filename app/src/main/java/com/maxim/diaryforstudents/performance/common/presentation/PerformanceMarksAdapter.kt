package com.maxim.diaryforstudents.performance.common.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.maxim.diaryforstudents.databinding.MarkBinding

class PerformanceMarksAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<PerformanceMarksAdapter.ItemViewHolder>() {
    private val list = mutableListOf<PerformanceUi.Mark>()
    private var showDate = true

    class ItemViewHolder(private val binding: MarkBinding, private val listener: Listener) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PerformanceUi.Mark, showDate: Boolean) {
            binding.dateTextView.visibility = if (showDate) View.VISIBLE else View.GONE
            item.showName(binding.markTextView)
            item.showDate(binding.dateTextView)
            itemView.setOnClickListener {
                listener.details(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            MarkBinding.inflate(LayoutInflater.from(parent.context), parent, false), listener
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position], showDate)
    }

    fun update(newList: List<PerformanceUi.Mark>, showDate: Boolean) {
        this.showDate = showDate
        val diff = PerformanceDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(diff)
        list.clear()
        list.addAll(newList)
        result.dispatchUpdatesTo(this)
    }

    interface Listener {
        fun details(mark: PerformanceUi.Mark)
    }
}