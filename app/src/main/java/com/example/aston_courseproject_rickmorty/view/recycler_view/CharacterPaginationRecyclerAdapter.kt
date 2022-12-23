package com.example.aston_courseproject_rickmorty.view.recycler_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.aston_courseproject_rickmorty.R
import com.example.aston_courseproject_rickmorty.model.dto.CharacterForListDto
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import java.lang.Exception

class CharacterPaginationRecyclerAdapter(private val itemClickListener: CharacterViewHolder.ItemClickListener) :
    PagingDataAdapter<CharacterForListDto, CharacterPaginationRecyclerAdapter.CharacterViewHolder>(
        DiffUtilCallback()
    ) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.cell_character, parent, false)
        return CharacterViewHolder(itemView, itemClickListener)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val listItem = getItem(position)
        holder.bind(listItem)
        Picasso.get()
            .load(listItem?.image)
            .transform(CropCircleTransformation())
            .into(holder.imageView, object : Callback {
                override fun onSuccess() {
                    holder.imageProgressBar.visibility = View.GONE
                    holder.cellProgressBar.visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                }
            })
        with (holder) {
            txtViewName.text = listItem?.name ?: ""
            txtViewSpecies.text = listItem?.species ?: ""
            txtViewStatus.text = listItem?.status ?: ""
            txtViewGender.text = listItem?.gender ?: ""
        }
        if (holder.txtViewName.text == "") {
            holder.cellProgressBar.visibility = View.VISIBLE
        }
    }

    class CharacterViewHolder(itemView: View, private val itemClickListener: ItemClickListener): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView_avatar)
        val txtViewName: TextView = itemView.findViewById(R.id.textView_name)
        val txtViewSpecies: TextView = itemView.findViewById(R.id.textView_species)
        val txtViewStatus: TextView = itemView.findViewById(R.id.textView_status)
        val txtViewGender: TextView = itemView.findViewById(R.id.textView_gender)
        val imageProgressBar: ProgressBar = itemView.findViewById(R.id.image_progressbar)
        val cellProgressBar: ProgressBar = itemView.findViewById(R.id.cell_progressbar)

        fun bind(listItem: CharacterForListDto?) {
            itemView.setOnClickListener {
                itemClickListener.onItemClick(listItem)
            }
        }

        interface ItemClickListener {

            fun onItemClick(character: CharacterForListDto?)
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<CharacterForListDto>() {
        override fun areItemsTheSame(oldItem: CharacterForListDto, newItem: CharacterForListDto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CharacterForListDto, newItem: CharacterForListDto): Boolean {
            return (oldItem.name == newItem.name
                    && oldItem.species == newItem.species
                    && oldItem.status == newItem.status
                    && oldItem.gender == newItem.gender)
        }

    }
}