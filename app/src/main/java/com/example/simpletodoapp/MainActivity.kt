package com.example.simpletodoapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.core.content.ContextCompat

data class ToDoItem(
    val title: String,
    val dateTime: String,
    val photoUri: String? = null
)

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private val toDoList = mutableListOf<ToDoItem>()
    private val ADD_ITEM_REQUEST_CODE = 1
    private val DETAIL_REQUEST_CODE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        recyclerView = RecyclerView(this).apply {
            id = View.generateViewId()
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ToDoAdapter(toDoList)
        }

        fab = FloatingActionButton(this).apply {
            setImageResource(android.R.drawable.ic_input_add)
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setOnClickListener {
                val intent = Intent(this@MainActivity, AddItemActivity::class.java)
                startActivityForResult(intent, ADD_ITEM_REQUEST_CODE)
            }
        }

        rootLayout.addView(recyclerView)
        rootLayout.addView(fab)

        setContentView(rootLayout)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_ITEM_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val title = data?.getStringExtra("title") ?: ""
            val dateTime = data?.getStringExtra("dateTime") ?: ""
            val photoUri = data?.getStringExtra("photoUri")
            val newItem = ToDoItem(title, dateTime, photoUri)
            toDoList.add(newItem)
            recyclerView.adapter?.notifyDataSetChanged()
        } else if (requestCode == DETAIL_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val position = data?.getIntExtra("position", -1) ?: -1
            if (position != -1) {
                toDoList.removeAt(position)
                recyclerView.adapter?.notifyItemRemoved(position)
                recyclerView.adapter?.notifyItemRangeChanged(position, toDoList.size)
            }
        }
    }

    inner class ToDoAdapter(private val toDoList: MutableList<ToDoItem>) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

        inner class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvTitle: TextView = itemView.findViewById(1001)
            val tvDateTime: TextView = itemView.findViewById(1002)
            val ivPhoto: ImageView = itemView.findViewById(1003)
            val cbComplete: CheckBox = itemView.findViewById(1004)
            val btnDelete: Button = itemView.findViewById(1005)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
            val itemView = LinearLayout(parent.context).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setPadding(16, 16, 16, 16)

                addView(TextView(parent.context).apply {
                    id = 1001
                    textSize = 18f
                    setTextColor(ContextCompat.getColor(parent.context, android.R.color.black))
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                })
                addView(TextView(parent.context).apply {
                    id = 1002
                    textSize = 16f
                    setTextColor(ContextCompat.getColor(parent.context, android.R.color.darker_gray))
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                })
                addView(ImageView(parent.context).apply {
                    id = 1003
                    layoutParams = LinearLayout.LayoutParams(
                        100,
                        100
                    )
                    scaleType = ImageView.ScaleType.CENTER_CROP
                })
                addView(CheckBox(parent.context).apply {
                    id = 1004
                    text = "Completed"
                    setOnCheckedChangeListener { _, isChecked ->
                        // Handle completion status
                    }
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                })
                addView(Button(parent.context).apply {
                    id = 1005
                    text = "Delete"
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                })
            }
            return ToDoViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
            val item = toDoList[position]
            holder.tvTitle.text = item.title
            holder.tvDateTime.text = item.dateTime
            if (item.photoUri != null) {
                holder.ivPhoto.setImageURI(Uri.parse(item.photoUri))
                holder.ivPhoto.visibility = View.VISIBLE
            } else {
                holder.ivPhoto.visibility = View.GONE
            }

            holder.itemView.setOnClickListener {
                showDetailView(item, position)
            }

            holder.btnDelete.setOnClickListener {
                toDoList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, toDoList.size)
            }
        }

        override fun getItemCount(): Int = toDoList.size
    }

    private fun showDetailView(item: ToDoItem, position: Int) {
        val detailLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setPadding(32, 32, 32, 32)
            setBackgroundColor(ContextCompat.getColor(this@MainActivity, android.R.color.white))
        }

        val tvTitle = TextView(this).apply {
            text = item.title
            textSize = 24f
            setTextColor(ContextCompat.getColor(this@MainActivity, android.R.color.black))
        }

        val tvDateTime = TextView(this).apply {
            text = item.dateTime
            textSize = 18f
            setTextColor(ContextCompat.getColor(this@MainActivity, android.R.color.darker_gray))
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 8, 0, 8)
            }
        }

        val ivPhoto = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                200
            ).apply {
                setMargins(0, 16, 0, 16)
            }
            scaleType = ImageView.ScaleType.CENTER_CROP
            if (item.photoUri != null) {
                setImageURI(Uri.parse(item.photoUri))
                visibility = View.VISIBLE
            } else {
                visibility = View.GONE
            }
        }

        detailLayout.addView(tvTitle)
        detailLayout.addView(tvDateTime)
        detailLayout.addView(ivPhoto)

        setContentView(detailLayout)
    }
}
