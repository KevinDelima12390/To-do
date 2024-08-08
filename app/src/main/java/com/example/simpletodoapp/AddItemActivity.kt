package com.example.simpletodoapp

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.Calendar

class AddItemActivity : AppCompatActivity() {
    private lateinit var etTitle: EditText
    private lateinit var btnPickDateTime: Button
    private lateinit var btnAdd: Button
    private var dateTime: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setPadding(32, 32, 32, 32)  // Add padding to the root layout
        }

        etTitle = EditText(this).apply {
            hint = "Enter task title"
            textSize = 18f
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            background = ContextCompat.getDrawable(context, android.R.drawable.editbox_background_normal)  // Set background
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 16)  // Margin below the EditText
            }
        }

        btnPickDateTime = Button(this).apply {
            text = "Pick Date and Time"
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            background = ContextCompat.getDrawable(context, android.R.drawable.btn_default)  // Set button background
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 16)  // Margin below the button
            }
            setOnClickListener {
                showDateTimePicker()
            }
        }

        btnAdd = Button(this).apply {
            text = "Add Task"
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            background = ContextCompat.getDrawable(context, android.R.drawable.btn_default)  // Set button background
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setOnClickListener {
                val title = etTitle.text.toString()
                if (title.isNotEmpty() && dateTime.isNotEmpty()) {
                    val resultIntent = Intent().apply {
                        putExtra("title", title)
                        putExtra("dateTime", dateTime)
                    }
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                } else {
                    Toast.makeText(this@AddItemActivity, "Please enter title and select date/time", Toast.LENGTH_SHORT).show()
                }
            }
        }

        rootLayout.addView(etTitle)
        rootLayout.addView(btnPickDateTime)
        rootLayout.addView(btnAdd)

        setContentView(rootLayout)
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                val timePickerDialog = TimePickerDialog(
                    this,
                    { _, hourOfDay, minute ->
                        dateTime = String.format("%02d/%02d/%d %02d:%02d", dayOfMonth, monthOfYear + 1, year, hourOfDay, minute)
                        btnPickDateTime.text = dateTime
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )
                timePickerDialog.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}
