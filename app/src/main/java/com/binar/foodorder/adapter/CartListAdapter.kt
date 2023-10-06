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
        AsyncListDiffer(this, object : DiffUtil.ItemCallback<CartFood>() {
            override fun areItemsTheSame(
                oldItem: CartFood,
                newItem: CartFood
            ): Boolean {
                return oldItem.cart.id == newItem.cart.id && oldItem.food.id == newItem.food.id
            }

            override fun areContentsTheSame(
                oldItem: CartFood,
                newItem: CartFood
            ): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        })

    fun submitData(data: List<CartFood>) {
        dataDiffer.submitList(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  CartOrderViewHolder(
            ItemFoodCartBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),cartListener
        )

        }

    override fun getItemCount(): Int =dataDiffer.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolderBinder<CartFood>).bind(dataDiffer.currentList[position])
    }


}

class CartOrderViewHolder(
    private val binding: ItemFoodCartBinding,
    private val cartListener: CartListener?
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<CartFood> {
    override fun bind(item: CartFood) {
        setCartData(item)
        setCartNotes(item)
        setClickListeners(item)
    }

    private fun setCartData(item: CartFood) {
        with(binding) {
            binding.tvFoodimage.load(item.food.Image) {
                crossfade(true)
            }
            tvQuantity.text =
                itemView.rootView.context.getString(
                    R.string.total_quantity,
                    item.cart.itemQuantity.toString()
                )
            tvFoodname.text = item.food.name
            tvFoodprice.text = (item.cart.itemQuantity * item.food.Price).toString()
        }
    }
    private fun setClickListeners(item: CartFood) {
        with(binding) {
            tvDecrement.setOnClickListener { cartListener?.onMinusTotalItemCartClicked(item.cart) }
            tvIncrement.setOnClickListener { cartListener?.onPlusTotalItemCartClicked(item.cart) }
            icDelete.setOnClickListener { cartListener?.onRemoveCartClicked(item.cart) }
        }
    }

    private fun setCartNotes(item: CartFood) {
        binding.etNotesItem.setText(item.cart.itemNotes)
        binding.etNotesItem.doneEditing {
            binding.etNotesItem.clearFocus()
            val newItem = item.cart.copy().apply {
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