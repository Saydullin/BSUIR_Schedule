package com.bsuir.bsuirschedule.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsuir.bsuirschedule.databinding.SubgroupItemBinding

class SubgroupsAdapter(
    private var subgroups: List<Int>
): RecyclerView.Adapter<SubgroupsAdapter.ViewHolder>() {

    init {
        Log.e("sady", "got is ${subgroups.size}")
    }

    class ViewHolder(private val binding: SubgroupItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Int) {
            binding.subgroupNumber.text = item.toString()
        }
    }

    fun updateList(newSubgroups: List<Int>) {
        subgroups = newSubgroups
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SubgroupItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subgroup = subgroups[position]

        holder.bind(subgroup)
    }

    override fun getItemCount() = subgroups.size

}