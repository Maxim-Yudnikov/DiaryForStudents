package com.maxim.diaryforstudents.editDiary.edit.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.maxim.diaryforstudents.databinding.EditGradeBinding
import com.maxim.diaryforstudents.databinding.EditGradeDateBinding

class EditGradesAdapter : RecyclerView.Adapter<EditGradesAdapter.ItemViewHolder>() {
    private val list = mutableListOf<GradeUi>()

    abstract class ItemViewHolder(binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(item: GradeUi)
    }

    class BaseViewHolder(private val binding: EditGradeBinding) : ItemViewHolder(binding) {
        override fun bind(item: GradeUi) {
            item.show(binding.editGradeEditText)
        }
    }

    class DateViewHolder(private val binding: EditGradeDateBinding) : ItemViewHolder(binding) {
        override fun bind(item: GradeUi) {
            item.show(binding.lessonDateTextView)
        }
    }

    override fun getItemViewType(position: Int) = if (list[position] is GradeUi.Base) 0 else 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (viewType == 0) BaseViewHolder(
            EditGradeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ) else DateViewHolder(
            EditGradeDateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun update(newList: List<GradeUi>) {
        val diff = EditGradesDiff(list, newList)
        val result = DiffUtil.calculateDiff(diff)
        list.clear()
        list.addAll(newList)
        result.dispatchUpdatesTo(this)
    }
}

class EditGradesDiff(
    private val oldList: List<GradeUi>,
    private val newList: List<GradeUi>,
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = false
}