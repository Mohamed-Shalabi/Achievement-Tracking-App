package com.mshalaby612.achievementtracker.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mshalaby612.achievementtracker.R
import com.mshalaby612.achievementtracker.data.SHARED_PREFERENCES_NAME
import com.mshalaby612.achievementtracker.data.SHARED_PREFERENCES_THREE_STARS
import com.mshalaby612.achievementtracker.data.SHARED_PREFERENCES_THREE_STARS_DEFAULT_VALUE
import com.mshalaby612.achievementtracker.data.database.ProgressDay

class StatisticsAdapter(private val days: List<ProgressDay>?) :
    RecyclerView.Adapter<StatisticsAdapter.StatisticsViewHolder>() {

    class StatisticsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val progressBar = itemView.findViewById<ProgressBar>(R.id.progress_bar)
        val textView = itemView.findViewById<TextView>(R.id.tv_statistics_day)
    }

    override fun getItemCount(): Int = days?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.statistics_item, parent, false)
        return StatisticsViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatisticsViewHolder, position: Int) {
        val day = days?.get(position)
        val context = holder.progressBar.context
        holder.apply {
            progressBar.max =
                context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                    .getInt(
                        SHARED_PREFERENCES_THREE_STARS,
                        SHARED_PREFERENCES_THREE_STARS_DEFAULT_VALUE
                    )
            progressBar.progress = day?.ingredients?.sumOf { it.timeMinutes } ?: 0
            textView.text = day?.date
        }
    }


}

class MyLinearLayoutManager(context: Context) : LinearLayoutManager(context, HORIZONTAL, false) {

    override fun canScrollHorizontally(): Boolean {
        return false
    }

    override fun canScrollVertically(): Boolean {
        return false
    }

    override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
        lp.width = width / 7
        return true
    }


}