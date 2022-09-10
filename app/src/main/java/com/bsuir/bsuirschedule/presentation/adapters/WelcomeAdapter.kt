package com.bsuir.bsuirschedule.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.WelcomePageItemBinding
import com.bsuir.bsuirschedule.domain.models.WelcomeText

class WelcomeAdapter(
    private val context: Context,
    private val data: ArrayList<WelcomeText>,
    private val onGetStartedClick: () -> Unit
): RecyclerView.Adapter<WelcomeAdapter.ViewHolder>() {

    class ViewHolder(
        private val binding: WelcomePageItemBinding,
        private val welcomeTextList: ArrayList<WelcomeText>,
        private val onGetStartedClick: () -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: WelcomeText, position: Int) {
            binding.title.text = item.title
            binding.caption.text = item.caption
            binding.icon.setImageDrawable(item.image)

            if (position == welcomeTextList.size-1) {
                binding.getStartedButton.alpha = 0f
                binding.getStartedButton.animate().setStartDelay(500).alpha(1f)
                binding.getStartedButton.visibility = View.VISIBLE
                binding.getStartedButton.setOnClickListener {
                    onGetStartedClick()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = WelcomePageItemBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(view, data, onGetStartedClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]

        holder.bind(item, position)
    }

    override fun getItemCount() = data.size

}