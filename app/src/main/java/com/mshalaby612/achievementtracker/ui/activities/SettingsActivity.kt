package com.mshalaby612.achievementtracker.ui.activities

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import com.mshalaby612.achievementtracker.R
import com.mshalaby612.achievementtracker.data.SHARED_PREFERENCES_AUTO_START
import com.mshalaby612.achievementtracker.data.SHARED_PREFERENCES_NAME
import com.mshalaby612.achievementtracker.data.SHARED_PREFERENCES_THREE_STARS
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        sharedPreferences =
            applicationContext.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

        buildLayout()

        makeSeekBarAction()

        setUpSwitchButton()
    }

    private fun setUpSwitchButton() {
        auto_start_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            sharedPreferences.edit().apply {
                putBoolean(SHARED_PREFERENCES_AUTO_START, isChecked)
                apply()
            }
        }
    }

    private fun makeSeekBarAction() {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            var threeStarsTime = 0
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tv_seekBar.text = getString(R.string._100_minutes, progress - progress % 5)
                threeStarsTime = progress - progress % 5
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                with(sharedPreferences.edit()) {
                    putInt(SHARED_PREFERENCES_THREE_STARS, threeStarsTime)
                    apply()
                }
            }

        })
    }

    private fun buildLayout() {
        setSupportActionBar(toolbar)
        seekBar.progress = sharedPreferences.getInt(SHARED_PREFERENCES_THREE_STARS, 240)
        tv_seekBar.text = getString(R.string._100_minutes, seekBar.progress)
        auto_start_switch.isChecked = sharedPreferences.getBoolean(SHARED_PREFERENCES_AUTO_START, true)
    }
}