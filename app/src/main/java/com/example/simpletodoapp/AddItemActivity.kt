package com.example.simpletodoapp

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class AddItemActivity : AppCompatActivity() {
    private lateinit var etTitle: EditText
    private lateinit var btnPickDateTime: Button
    private lateinit var btnAdd: Button
    private var dateTime: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create a LinearLayout as the root layout
        val rootLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        // Create EditText for title input
        etTitle = EditText(this).apply {
            hint = "Enter task title"
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        // Create Button for date and time picker
        btnPickDateTime = Button(this).apply {
            text = "Pick Date and Time"
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setOnClickListener {
                showDateTimePicker()
            }
        }

        // Create Button for adding the item
        btnAdd = Button(this).apply {
            text = "Add Task"
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
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

        // Add views to the root layout
        rootLayout.addView(etTitle)
        rootLayout.addView(btnPickDateTime)
        rootLayout.addView(btnAdd)

        // Set the root layout as the content view
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
