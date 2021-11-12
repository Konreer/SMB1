package com.example.smb1.Activity

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.smb1.R

class SettingsActivity : AppCompatActivity() {
    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = this.getSharedPreferences("prefs_file1", Context.MODE_PRIVATE)
        setContentView(R.layout.activity_settings)
        findViewById<EditText>(R.id.fontSizeNumber).setText(preferences.getFloat("fontSize", 14f).toInt().toString())
        val spinner: Spinner = findViewById(R.id.fontSpinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.Fonts,
            android.R.layout.simple_spinner_item
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = arrayAdapter
            spinner.setSelection(preferences.getInt("selectedFontFamilyIndex", 0))
        }

    }

    fun updateFontAndFontFamily(){
        val preferences = this.getSharedPreferences("prefs_file1", Context.MODE_PRIVATE)
        val fontSize = preferences.getFloat("fontSize", 0f)
        val fontFamily = preferences.getString("fontFamily", "")
        if (fontSize != 0f){
            findViewById<EditText>(R.id.fontSizeNumber).textSize = fontSize
            findViewById<TextView>(R.id.fontSizeText).textSize = fontSize
            findViewById<TextView>(R.id.fontText).textSize = fontSize
            findViewById<Button>(R.id.button2).textSize = fontSize
        }

        if (!fontFamily.equals("")){
            findViewById<EditText>(R.id.fontSizeNumber).typeface = Typeface.createFromAsset(this.assets, fontFamily)
            findViewById<TextView>(R.id.fontSizeText).typeface = Typeface.createFromAsset(this.assets, fontFamily)
            findViewById<TextView>(R.id.fontText).typeface = Typeface.createFromAsset(this.assets, fontFamily)
            findViewById<Button>(R.id.button2).typeface = Typeface.createFromAsset(this.assets, fontFamily)
        }
    }

    fun save(view: android.view.View) {
        val editor = preferences.edit()
        val fontSize = findViewById<EditText>(R.id.fontSizeNumber).text.toString()
        if (fontSize != "") {
            editor.putFloat("fontSize", fontSize.toFloat())
        }

        val spinner: Spinner = findViewById<Spinner>(R.id.fontSpinner)

        editor.putString("fontFamily", "font/" + spinner.selectedItem.toString() + ".ttf")
        editor.putInt("selectedFontFamilyIndex", spinner.selectedItemPosition)
        editor.apply()

        finish()
    }

    override fun onResume() {
        updateFontAndFontFamily()
        super.onResume()
    }
}