package com.ad.brainshopchatbot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ad.brainshopchatbot.databinding.MessageItemBinding
import com.ad.brainshopchatbot.model.Message

class MessageAdapter :
    ListAdapter<Message, MessageAdapter.MessageViewHolder>(Diff) {

    inner class MessageViewHolder(private val binding: MessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: Message) {
            binding.apply {
                if (model.isFromBot) {
                    tvBotMessage.visibility = View.VISIBLE
                    tvMessage.visibility = View.GONE

                    tvBotMessage.text = model.message
                } else {
                    tvBotMessage.visibility = View.GONE
                    tvMessage.visibility = View.VISIBLE

                    tvMessage.text = model.message
                }
            }

        }

    }

    object Diff : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean =
            oldItem.hashCode() == newItem.hashCode()

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            MessageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        getItem(position).let {
            holder.bind(it)
        }
    }
}