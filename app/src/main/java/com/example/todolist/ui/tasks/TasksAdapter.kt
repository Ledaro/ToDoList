package com.example.todolist.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.data.Task
import com.example.todolist.databinding.ItemTaskBinding

class TasksAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Task, TasksAdapter.TasksVieHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksVieHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TasksVieHolder(binding)
    }

    override fun onBindViewHolder(holder: TasksVieHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class TasksVieHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onItemClick(task)
                    }
                }

                checkboxCompleted.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onCheckBoxClick(task, checkboxCompleted.isChecked)
                    }
                }
            }
        }

        fun bind(task: Task) {
            binding.apply {
                textViewTask.text = task.name
                textViewTask.paint.isStrikeThruText = task.completed
                checkboxCompleted.isChecked = task.completed
                labelPriority.isVisible = task.important
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(task: Task)
        fun onCheckBoxClick(task: Task, isChecked: Boolean)
    }

    class DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task) =
            oldItem == newItem
    }
}
