package com.orderhub.driver

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class OrderAdapter(
    private var orderList: List<OrderModel>,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cardOrder)
        val txtPlatform: TextView = itemView.findViewById(R.id.txtPlatform)
        val txtTime: TextView = itemView.findViewById(R.id.txtTime)
        val txtNominal: TextView = itemView.findViewById(R.id.txtNominal)
        val txtPickup: TextView = itemView.findViewById(R.id.txtPickup)
        val txtTujuan: TextView = itemView.findViewById(R.id.txtTujuan)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]
        
        // Set warna dan teks platform
        when (order.platform) {
            "GRAB" -> {
                holder.txtPlatform.text = "🟢 GRABFOOD"
                holder.txtPlatform.setTextColor(holder.itemView.context.resources.getColor(android.R.color.holo_green_dark))
            }
            "SHOPEE" -> {
                holder.txtPlatform.text = "🟠 SHOPEEFOOD"
                holder.txtPlatform.setTextColor(holder.itemView.context.resources.getColor(android.R.color.holo_orange_dark))
            }
            "INDRIVE" -> {
                holder.txtPlatform.text = "🔵 INDRIVE"
                holder.txtPlatform.setTextColor(holder.itemView.context.resources.getColor(android.R.color.holo_blue_dark))
            }
            else -> {
                holder.txtPlatform.text = order.platform
                holder.txtPlatform.setTextColor(holder.itemView.context.resources.getColor(android.R.color.white))
            }
        }

        holder.txtTime.text = order.time
        holder.txtNominal.text = order.nominal
        holder.txtPickup.text = "Pickup: ${order.pickup}"
        holder.txtTujuan.text = "Tujuan: ${order.tujuan}"

        // Aksi tombol hapus
        holder.btnDelete.setOnClickListener {
            onDeleteClick(order.id)
        }
        
        // Aksi klik card (opsional: bisa untuk detail atau buka app lain nanti)
        holder.cardView.setOnClickListener {
            // Tambahan fitur: Bisa buka aplikasi ojol terkait jika diklik
            // Untuk sekarang dikosongkan dulu agar fokus MVP
        }
    }

    override fun getItemCount(): Int = orderList.size

    fun updateData(newList: List<OrderModel>) {
        orderList = newList
        notifyDataSetChanged()
    }
}
