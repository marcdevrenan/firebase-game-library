package br.edu.infnet.firebasegamelibrary.service

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.edu.infnet.firebasegamelibrary.model.Product
import com.android.billingclient.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Store : BillingClientStateListener, SkuDetailsResponseListener, PurchasesUpdatedListener {

    private lateinit var context: AppCompatActivity
    val products: MutableList<Product> = ArrayList<Product>()
    private lateinit var clientInApp: BillingClient

    constructor(context: AppCompatActivity) {

        this.context = context
        products.add(Product("android.test.purchased", null, null))
        clientInApp = BillingClient
            .newBuilder(context)
            .enablePendingPurchases()
            .setListener(this)
            .build()
        clientInApp.startConnection(this)
    }

    override fun onBillingSetupFinished(billingResult: BillingResult?) {

        if (billingResult?.responseCode == BillingClient.BillingResponseCode.OK) {
            Log.i("PlayStore", "Connection established...")

            val skuList = ArrayList<String>()
            for (product in products) {
                skuList.add(product.sku)
            }

            val params = SkuDetailsParams
                .newBuilder()
                .setSkusList(skuList)
                .setType(BillingClient.SkuType.INAPP)
                .build()
            clientInApp.querySkuDetailsAsync(params, this)
        }
    }

    override fun onBillingServiceDisconnected() {
        Log.i("PlayStore", "Lost connection...")
    }

    override fun onSkuDetailsResponse(
        billingResult: BillingResult?,
        skuDetailsList: MutableList<SkuDetails>?
    ) {
        if (billingResult?.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList != null && skuDetailsList.size > 0) {
            for (product in products) {
                for (item in skuDetailsList) {
                    if (product.sku.equals(product.sku)) {
                        product.description = product.description
                        product.price = product.price
                        product.skuDetails = item
                        Log.i("PlayStore", "Product: ${product.description} = ${product.price}")
                    }
                }
            }
            Log.i("PlayStore", "Loaded product data")
        }
    }

    fun makePurchase(product: Product) {
        Log.i("playStore", "Making purchase...")
        val params = BillingFlowParams
            .newBuilder()
            .setSkuDetails(product.skuDetails)
            .build()
        clientInApp.launchBillingFlow(context, params)
    }

    private suspend fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            Log.i("PlayStore", "Confirmed purchase")
            if (!purchase.isAcknowledged) {
                val params = AcknowledgePurchaseParams
                    .newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
                val result = withContext(Dispatchers.IO) {
                    clientInApp.acknowledgePurchase(params)
                }
            }
        }
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult?,
        purchaseList: MutableList<Purchase>?
    ) {
        if (billingResult?.responseCode == BillingClient.BillingResponseCode.OK && purchaseList != null && purchaseList.size > 0) {
            Log.i("PlayStore", "Purchase made")
            for (purchase in purchaseList) {
                GlobalScope.launch(Dispatchers.IO) {
                    handlePurchase(purchase)
                }
            }
        } else if (billingResult?.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Log.i("PlayStore", "User canceled the purchase")
        }
    }

    fun closeStore() {
        Log.i("PlayStore", "Closing the store...")
        clientInApp.endConnection()
    }
}