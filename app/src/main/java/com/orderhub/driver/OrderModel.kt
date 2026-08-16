package com.orderhub.driver

data class OrderModel(
    val id: Int,
    val platform: String,
    val nominal: String,
    val pickup: String,
    val tujuan: String,
    val time: String,
    val rawText: String
)
