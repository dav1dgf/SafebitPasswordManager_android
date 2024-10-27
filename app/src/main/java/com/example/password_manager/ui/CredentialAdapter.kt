package com.example.password_manager.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.password_manager.application.CredentialItem
import com.example.password_manager.R

class CredentialAdapter(
    private val credentialItems: List<CredentialItem>,
    private val onItemClick: (CredentialItem) -> Unit
) : RecyclerView.Adapter<CredentialAdapter.CredentialViewHolder>() {

    class CredentialViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvWebpage: TextView = itemView.findViewById(R.id.tv_detail_webpage)
        val tvEmail: TextView = itemView.findViewById(R.id.tv_detail_email)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CredentialViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.credential_item, parent, false)
        return CredentialViewHolder(view)
    }

    override fun onBindViewHolder(holder: CredentialViewHolder, position: Int) {
        val credential = credentialItems[position]
        holder.tvWebpage.text = credential.page
        holder.tvEmail.text = credential.email

        holder.itemView.setOnClickListener {
            onItemClick(credential) // Handle click event
        }
    }

    override fun getItemCount(): Int = credentialItems.size
}
