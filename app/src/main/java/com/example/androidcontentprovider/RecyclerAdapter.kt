package com.example.androidcontacts.database.service

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcontentprovider.Contact
import com.example.androidcontentprovider.R

class RecyclerAdapter(
    private val context: Context,
    private val listener: ContactClickListener,
    private val contacts: ArrayList<Contact>
) : RecyclerView.Adapter<RecyclerAdapter.ContactViewHolder>() {


    inner class ContactViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        private val nameTV = view.findViewById<TextView>(R.id.nameTV)
        private val numPhoneTV = view.findViewById<TextView>(R.id.numPhoneTV)
        val callPhoneBTN = view.findViewById<ImageButton>(R.id.callPhoneBTN)
        val sendMessageBTN = view.findViewById<ImageButton>(R.id.sendMessageBTN)

        fun bind(contact: Contact) {
            nameTV.text = contact.name
            numPhoneTV.text = contact.numPhone
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val viewHolder =
            ContactViewHolder(
                LayoutInflater
                    .from(context)
                    .inflate(
                        R.layout.recycler_item,
                        parent,
                        false
                    )
            )
        viewHolder.callPhoneBTN.setOnClickListener{
            listener.onItemClickedCall(contacts[viewHolder.adapterPosition])
        }
        viewHolder.sendMessageBTN.setOnClickListener{
            listener.onItemClickedSend(contacts[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val currentContact = contacts[position]
        holder.bind(currentContact)

    }
    interface ContactClickListener{
        fun onItemClickedCall(contact: Contact)
        fun onItemClickedSend(contact: Contact)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Contact>){
        contacts.clear()
        contacts.addAll(newList)
        notifyDataSetChanged()
    }
}