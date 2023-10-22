package com.binar.foodorder.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.binar.foodorder.R
import com.binar.foodorder.databinding.ItemFoodCartBinding
import com.binar.foodorder.databinding.ItemFoodCartOrderBinding
import com.binar.foodorder.databinding.ItemFoodListviewBinding
import com.binar.foodorder.model.Cart
import com.binar.foodorder.model.CartFood
import com.binar.foodorder.util.doneEditing

class CartListAdapter(private val cartListener: CartListener? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataDiffer =
        AsyncListDiffer(this, object : DiffUtil.ItemCallback<Cart>() {
            override fun areItemsTheSame(
                oldItem: Cart,
                newItem: Cart
            ): Boolean {
                return oldItem.foodId == newItem.foodId && oldItem.foodId == newItem.foodId
            }

            override fun areContentsTheSame(
                oldItem: Cart,
                newItem: Cart
            ): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        })

    fun submitData(data: List<Cart>) {
        dataDiffer.submitList(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CartOrderViewHolder(
            ItemFoodCartBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), cartListener
        )

    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolderBinder<Cart>).bind(dataDiffer.currentList[position])
    }


}

class CartOrderViewHolder(
    private val binding: ItemFoodCartBinding,
    private val cartListener: CartListener?
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Cart> {
    override fun bind(item: Cart) {
        setCartData(item)
        setCartNotes(item)
        setClickListeners(item)
    }

    private fun setCartData(item: Cart) {
        with(binding) {
            binding.tvFoodimage.load(item.foodImgUrl) {
                crossfade(true)
            }
            tvQuantity.text =
                itemView.rootView.context.getString(
                    R.string.total_quantity,
                    item.itemQuantity.toString()
                )
            tvFoodname.text = item.foodName
            tvFoodprice.text = (item.itemQuantity * item.foodPrice).toString()
        }
    }

    private fun setClickListeners(item: Cart) {
        with(binding) {
            tvDecrement.setOnClickListener { cartListener?.onMinusTotalItemCartClicked(item) }
            tvIncrement.setOnClickListener { cartListener?.onPlusTotalItemCartClicked(item) }
            icDelete.setOnClickListener { cartListener?.onRemoveCartClicked(item) }
        }
    }

    private fun setCartNotes(item: Cart) {
        binding.etNotesItem.setText(item.itemNotes)
        binding.etNotesItem.doneEditing {
            binding.etNotesItem.clearFocus()
            val newItem = item.copy().apply {
                itemNotes = binding.etNotesItem.text.toString().trim()
            }
            cartListener?.onUserDoneEditingNotes(newItem)
        }
    }

}


interface CartListener {
    fun onPlusTotalItemCartClicked(cart: Cart)
    fun onMinusTotalItemCartClicked(cart: Cart)
    fun onRemoveCartClicked(cart: Cart)
    fun onUserDoneEditingNotes(cart: Cart)
}