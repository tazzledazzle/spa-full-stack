package com.northshore.models

data class Product (
    var id: Long? = null,
    var name: String? = null,
    var price: Double? = null,
    var inStock: Boolean? = null
)