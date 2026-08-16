package com.orderhub.driver

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: OrderDatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrderAdapter
    private var allOrders: List<OrderModel> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = OrderDatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerViewOrders)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inisialisasi adapter dengan aksi klik untuk hapus
        adapter = OrderAdapter(listOf()) { id ->
            dbHelper.deleteOrder(id)
            refreshData("SEMUA")
        }
        recyclerView.adapter = adapter

        // Tombol izin listener
        findViewById<Button>(R.id.btn_settings).setOnClickListener {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }

        // Tombol Filter
        findViewById<Button>(R.id.btnFilterAll).setOnClickListener { refreshData("SEMUA") }
        findViewById<Button>(R.id.btnFilterGrab).setOnClickListener { refreshData("GRAB") }
        findViewById<Button>(R.id.btnFilterShopee).setOnClickListener { refreshData("SHOPEE") }
        findViewById<Button>(R.id.btnFilterIndrive).setOnClickListener { refreshData("INDRIVE") }
    }

    override fun onResume() {
        super.onResume()
        // Refresh data setiap kali aplikasi dibuka kembali
        refreshData("SEMUA")
    }

    private fun refreshData(filter: String) {
        allOrders = dbHelper.getAllOrders()
        val filteredList = if (filter == "SEMUA") {
            allOrders
        } else {
            allOrders.filter { it.platform.equals(filter, true) }
        }
        adapter.updateData(filteredList)

        // Update status listener di UI
        val txtStatus = findViewById<TextView>(R.id.txtStatus)
        if (isNotificationServiceEnabled()) {
            txtStatus.text = "Status: Listener AKTIF"
            txtStatus.setTextColor(resources.getColor(android.R.color.holo_green_light))
        } else {
            txtStatus.text = "Status: Listener MATI (Klik Setelan)"
            txtStatus.setTextColor(resources.getColor(android.R.color.holo_red_light))
        }
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val pkgName = packageName
        val flat = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        if (!flat.isNullOrEmpty()) {
            val names = flat.split(":")
            for (name in names) {
                val cn = android.content.ComponentName.unflattenFromString(name)
                if (cn != null && cn.packageName == pkgName) {
                    return true
                }
            }
        }
        return false
    }
}
