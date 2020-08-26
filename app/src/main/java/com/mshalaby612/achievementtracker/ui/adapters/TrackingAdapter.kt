package com.mshalaby612.achievementtracker.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mshalaby612.achievementtracker.R
import com.mshalaby612.achievementtracker.data.database.ProgressDay
import com.mshalaby612.achievementtracker.data.SHARED_PREFERENCES_NAME
import com.mshalaby612.achievementtracker.data.SHARED_PREFERENCES_THREE_STARS
import com.mshalaby612.achievementtracker.data.SHARED_PREFERENCES_THREE_STARS_DEFAULT_VALUE

class TrackingAdapter(
    private val days: MutableList<ProgressDay>,
    private val clickAction: (position: Int) -> Unit
) : RecyclerView.Adapter<TrackingAdapter.TrackingViewHolder>() {

    class TrackingViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var textView: TextView = itemView.findViewById(R.id.tv_date)
        var textView1: TextView = itemView.findViewById(R.id.tv_progress)
        var starOne: ImageView = itemView.findViewById(R.id.start_one)
        var starTwo: ImageView = itemView.findViewById(R.id.start_two)
        var starThree: ImageView = itemView.findViewById(R.id.start_three)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackingViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.tracking_item, parent, false)
        return TrackingViewHolder(view)
    }

    override fun getItemCount(): Int = days.size

    override fun onBindViewHolder(holder: TrackingViewHolder, position: Int) {
        val progressDay = days[position]
        var i = 0L
        for (ingredient in progressDay.ingredients) {
            i += ingredient.timeMinutes
        }
        val context = holder.textView.context
        val sharedPreferences =
            context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val threeStars = sharedPreferences.getInt(
            SHARED_PREFERENCES_THREE_STARS,
            SHARED_PREFERENCES_THREE_STARS_DEFAULT_VALUE
        )

        holder.apply {
            textView.text = context.getString(R.string.date, progressDay.date)
            textView1.text = context.getString(R.string.progress, i)
            if (progressDay.ingredients.sumOf { it.timeMinutes } >= threeStars) {
                starOne.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_baseline_star_24
                    )
                )
            }
            if (progressDay.ingredients.sumOf { it.timeMinutes } >= threeStars * 2 / 3) {
                starTwo.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_baseline_star_24
                    )
                )
            }
            if (progressDay.ingredients.sumOf { it.timeMinutes } >= threeStars / 3) {
                starThree.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_baseline_star_24
                    )
                )
            }
            itemView.setOnClickListener {
                clickAction.invoke(position)
            }
        }
    }
}

