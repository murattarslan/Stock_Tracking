package com.marslan.stocktracking.ui.product.component

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marslan.stocktracking.core.extension.toast
import com.marslan.stocktracking.database.table.Product
import com.marslan.stocktracking.databinding.ItemViewProductBinding

class ProductRecyclerView : RecyclerView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, style: Int) : super(context, attrs, style) {
        layoutManager = LinearLayoutManager(context)
        adapter = ProductAdapter()
    }

    companion object {

        @SuppressLint("NotifyDataSetChanged")
        @JvmStatic
        @BindingAdapter("product:loadData")
        fun loadData(v: ProductRecyclerView, list: ArrayList<Product>?) {
            v.currentList = list ?: arrayListOf()
            v.adapter?.notifyDataSetChanged()
        }

    }

    private var currentList = arrayListOf<Product>()

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(product: Product, flag: Boolean?) {
        val index = currentList.indexOf(product)
        when (flag) {
            // edit
            null -> {
                if (index >= 0) {
                    currentList[index] = product
                    adapter?.notifyItemChanged(index)
                }
            }
            // add
            true -> {
                currentList.add(product)
                currentList.sortBy { it.name }
                adapter?.notifyDataSetChanged()
            }
            // remove
            false -> {
                if (index >= 0) {
                    currentList.remove(product)
                    adapter?.notifyItemRemoved(index)
                }
            }
        }
    }

    inner class ProductAdapter : Adapter<ProductViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            val view = ItemViewProduct(parent.context)
            view.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            return ProductViewHolder(view)
        }

        override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
            holder.view.binding.apply {
                product = currentList[position]
                executePendingBindings()
                itemViewProductConf.setOnClickListener {
                    context.toast("coming soon...")
                    // todo ürün içerik değişikliği
                }
            }
        }

        override fun getItemCount() = currentList.size

    }

    inner class ProductViewHolder(val view: ItemViewProduct) : ViewHolder(view)
}