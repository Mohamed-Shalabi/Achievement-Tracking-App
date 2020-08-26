package com.mshalaby612.achievementtracker.ui.activities

import android.app.ActivityOptions
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.navigation.NavigationView
import com.mshalaby612.achievementtracker.R
import com.mshalaby612.achievementtracker.calculateRemainingOfTheDayMillis
import com.mshalaby612.achievementtracker.data.*
import com.mshalaby612.achievementtracker.ui.adapters.GridAdapter
import com.mshalaby612.achievementtracker.ui.adapters.MyGridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var adapter: GridAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var toggle: ActionBarDrawerToggle
    private var todayProgress = 0

    companion object {
        var todayIngredients: MutableList<Ingredient>? = mutableListOf()
        var ingredients: ArrayList<Ingredient> = arrayListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setActionBarAndNavigationView()

        openSharedPreferences()

        setTheProgressBar()

        makeIngredientsList()

        buildTheRecyclerView()

    }

    private fun openSharedPreferences() {
        sharedPreferences =
            applicationContext.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
        todayProgress = sharedPreferences.getInt(SHARED_PREFERENCES_TODAY_PROGRESS, 0)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        //val json = sharedPreferences.getString(SHARED_PREFERENCES_TODAY_ENTITY, null)
        //todayIngredients = listFromJson(json).toMutableList()
    }

    private fun makeIngredientsList() {
        for (i in 1..6) {
            ingredients.add(Ingredient(i * 5))
        }
        ingredients.add(Ingredient(40))
        ingredients.add(Ingredient(50))
        ingredients.add(Ingredient(60))
    }

    private fun buildTheRecyclerView() {
        recycler_grid.setHasFixedSize(true)
        adapter = GridAdapter(ingredients) { position ->
            val millisLeft = calculateRemainingOfTheDayMillis()
            val milli = ingredients[position].timeMillis
            if (millisLeft > milli) {
                val intent = Intent(this, CountDownActivity::class.java)
                intent.putExtra("period", ingredients[position].timeMinutes)
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            } else {
                AlertDialog.Builder(this).apply {
                    setTitle("The length of the session is not enough.")
                    setIcon(
                        ContextCompat.getDrawable(
                            this@MainActivity,
                            R.drawable.ic_baseline_warning_24
                        )
                    )
                    setNeutralButton(
                        "OK"
                    ) { dialog, _ ->
                        dialog.dismiss()
                    }
                    create()
                    show()
                }
            }
        }
        recycler_grid.apply {
            layoutManager = MyGridLayoutManager(this@MainActivity, 3)
            adapter = this@MainActivity.adapter
        }
    }

    private fun setTheProgressBar() {
        progressBar.progressDrawable =
            ContextCompat.getDrawable(this, R.drawable.custom_progress_bar)
        progressBar.max = sharedPreferences.getInt(
            SHARED_PREFERENCES_THREE_STARS,
            SHARED_PREFERENCES_THREE_STARS_DEFAULT_VALUE
        )
        progressBar.progress = todayProgress
        tv_progress.text = getString(R.string.today_progress, todayProgress)
        updateThreeStars()
    }

    private fun updateThreeStars() {
        val threeStars = sharedPreferences.getInt(
            SHARED_PREFERENCES_THREE_STARS,
            SHARED_PREFERENCES_THREE_STARS_DEFAULT_VALUE
        )

        start_one.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                if (todayProgress >= threeStars)
                    R.drawable.ic_baseline_star_24
                else
                    R.drawable.ic_baseline_star_border_24
            )
        )

        start_two.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                if (todayProgress >= threeStars * 2 / 3)
                    R.drawable.ic_baseline_star_24
                else
                    R.drawable.ic_baseline_star_border_24
            )
        )

        start_three.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                if (todayProgress >= threeStars / 3)
                    R.drawable.ic_baseline_star_24
                else
                    R.drawable.ic_baseline_star_border_24
            )
        )

    }

    private fun setActionBarAndNavigationView() {
        setSupportActionBar(toolbar)
        toggle = ActionBarDrawerToggle(
            this,
            motion_layout,
            toolbar,
            R.string.app_name,
            R.string.app_name
        )
        toggle.drawerArrowDrawable.color = ContextCompat.getColor(this, android.R.color.white)
        motion_layout.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_item_settings -> {
                startActivity(
                    Intent(this, SettingsActivity::class.java),
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
                )
            }
            R.id.nav_item_tracking -> {
                startActivity(
                    Intent(this, TrackingActivity::class.java),
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
                )
            }
            R.id.nav_item_statistics -> {
                startActivity(
                    Intent(this, StatisticsActivity::class.java),
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
                )
            }
        }
        motion_layout.closeDrawer(navigationView)
        return true
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            SHARED_PREFERENCES_TODAY_PROGRESS -> {
                todayProgress =
                    sharedPreferences!!.getInt(SHARED_PREFERENCES_TODAY_PROGRESS, 0)
                progressBar.progress = todayProgress
                tv_progress.text = getString(R.string.today_progress, todayProgress)
                updateThreeStars()
            }
            SHARED_PREFERENCES_THREE_STARS -> {
                progressBar.max = sharedPreferences!!.getInt(
                    SHARED_PREFERENCES_THREE_STARS,
                    SHARED_PREFERENCES_THREE_STARS_DEFAULT_VALUE
                )
                updateThreeStars()
            }
        }
    }

    override fun onDestroy() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroy()
    }
}