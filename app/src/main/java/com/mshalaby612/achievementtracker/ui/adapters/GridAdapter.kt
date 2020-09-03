package com.mshalaby612.achievementtracker.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mshalaby612.achievementtracker.data.Ingredient
import com.mshalaby612.achievementtracker.R

class GridAdapter(
    private val ingredients: ArrayList<Ingredient>,
    private val clickAction: (position: Int) -> Unit
) : RecyclerView.Adapter<GridAdapter.GridViewHolder>() {

    class GridViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var textView: TextView = itemView.findViewById(R.id.grid_text)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.grid_item, parent, false)
        println("I'm here")
        return GridViewHolder(view)
    }

    override fun getItemCount(): Int = ingredients.size

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.apply {
            textView.text = ingredient.timeMinutes.toString()
            itemView.setOnClickListener {
                clickAction.invoke(position)
            }
        }
    }
}

class MyGridLayoutManager(context: Context, spanCount: Int) : GridLayoutManager(context, spanCount) {

    override fun canScrollHorizontally(): Boolean {
        return false
    }

    override fun canScrollVertically(): Boolean {
        return false
    }

    override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
        lp.height = width * 2 / 7
        return true
    }


}