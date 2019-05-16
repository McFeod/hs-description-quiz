package io.github.mcfeod.hsdescriptionquiz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.localeRadioGroup
import kotlinx.android.synthetic.main.activity_settings.qualityRadioGroup
import kotlinx.android.synthetic.main.activity_settings.questionCount

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val preferences = Preferences(this)

        IntInputWrapper(questionCount).setup(preferences.itemCount,10, 1..100) { preferences.itemCount = it }

        RadioGroupWrapper<String>(localeRadioGroup)
            .add(R.id.ruRU, "ruRU")
            .add(R.id.enUS, "enUS")
            .add(R.id.deDE, "deDE")
            .add(R.id.frFR, "frFR")
            .setup(preferences.locale) { preferences.locale = it }

        RadioGroupWrapper<String>(qualityRadioGroup)
            .add(R.id.quality256x, "256x")
            .add(R.id.quality512x, "512x")
            .setup(preferences.quality) { preferences.quality = it }
    }
}
