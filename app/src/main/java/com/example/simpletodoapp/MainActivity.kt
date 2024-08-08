package com.example.simpletodoapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

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
            val newItem = ToDoItem(title, dateTime)
            toDoList.add(newItem)
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    inner class ToDoAdapter(private val toDoList: MutableList<ToDoItem>) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

        inner class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvTitle: TextView = itemView.findViewById(1)
            val tvDateTime: TextView = itemView.findViewById(2)
            val ivPhoto: ImageView = itemView.findViewById(3)
            val cbComplete: CheckBox = itemView.findViewById(4)
            val btnDelete: Button = itemView.findViewById(5)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
            val itemView = LinearLayout(parent.context).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                addView(TextView(parent.context).apply {
                    id = 1
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                })
                addView(TextView(parent.context).apply {
                    id = 2
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                })
                addView(ImageView(parent.context).apply {
                    id = 3
                    layoutParams = LinearLayout.LayoutParams(
                        100,
                        100
                    )
                    scaleType = ImageView.ScaleType.CENTER_CROP
                })
                addView(CheckBox(parent.context).apply {
                    id = 4
                    text = "Completed"
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            val position = parent.indexOfChild(this@apply.parent as View)
                            toDoList.removeAt(position)
                            notifyDataSetChanged()
                        }
                    }
                })
                addView(Button(parent.context).apply {
                    id = 5
                    text = "Delete"
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    setOnClickListener {
                        val position = parent.indexOfChild(this@apply.parent as View)
                        toDoList.removeAt(position)
                        notifyDataSetChanged()
                    }
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
            } else {
                holder.ivPhoto.visibility = View.GONE
            }
        }

        override fun getItemCount(): Int = toDoList.size
    }
}
