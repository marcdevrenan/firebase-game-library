package br.edu.infnet.firebasegamelibrary.model

import com.android.billingclient.api.SkuDetails


class Product(var sku: String, var description: String?, var price: String?) {
    var skuDetails: SkuDetails? = null
}