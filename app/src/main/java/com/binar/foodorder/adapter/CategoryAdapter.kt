package com.binar.foodorder.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.binar.foodorder.databinding.ItemCategoryBinding
import com.binar.foodorder.model.Category

/**
 * Created by Rahmat Hidayat on 02/10/2023.
 */
class CategoryAdapter(private val onItemClick: (Category) -> Unit) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    private var items: MutableList<Category> = mutableListOf()
    private val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.name == newItem.name &&
                    oldItem.categoryImgUrl == newItem.categoryImgUrl
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindView(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitListCategory(item: List<Category>) {
        differ.submitList(item)
        Log.d("CategoryAdapter", "Data masuk ke adapter: $item")
    }


    class CategoryViewHolder(
        val binding: ItemCategoryBinding,
        onItemClick: (Category) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: Category) {
            binding.tvFoodimage.load(item.categoryImgUrl)
            binding.tvFoodname.text = item.name
        }
    }
}


