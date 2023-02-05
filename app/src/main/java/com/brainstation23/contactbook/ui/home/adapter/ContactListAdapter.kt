package com.brainstation23.contactbook.ui.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brainstation23.contactbook.data.model.ContactModel
import com.brainstation23.contactbook.databinding.ItemContactListBinding


class ContactListAdapter(var context: Context, var items: ArrayList<ContactModel>) :
    RecyclerView.Adapter<ContactListAdapter.ServiceListAdapterViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ServiceListAdapterViewHolder {
        val binding =
            ItemContactListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceListAdapterViewHolder(
            binding
        )

    }

    override fun getItemCount(): Int = items.size


    override fun onBindViewHolder(holder: ServiceListAdapterViewHolder, position: Int) {
        holder.bind(items[position])

    }

    fun setData(list: ContactModel) {
        items.add(list)
        notifyItemInserted(items.size)
    }

    inner class ServiceListAdapterViewHolder(
        private val binding: ItemContactListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(
            model: ContactModel
        ) {
            binding.tvName.text = getEmptyString(model.name)
            binding.tvNumber.text = getEmptyString(model.number)
        }

        private fun getEmptyString(text: String?): String {
            return if (text.isNullOrEmpty()) {
                ""
            } else {
                text
            }
        }
    }
}