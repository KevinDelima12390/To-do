package com.example.simpletodoapp

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
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
    private val DETAIL_REQUEST_CODE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createMainView()
    }

    private fun createMainView() {
        val rootLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.LTGRAY)
            setPadding(16, 16, 16, 16)
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
            setPadding(8, 8, 8, 8)
        }

        fab = FloatingActionButton(this).apply {
            setImageResource(android.R.drawable.ic_input_add)
            backgroundTintList = ContextCompat.getColorStateList(this@MainActivity, android.R.color.holo_blue_dark)
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.END or Gravity.BOTTOM
                setMargins(0, 0, 16, 16)
            }
            setOnClickListener {
                val intent = Intent(this@MainActivity, AddItemActivity::class.java)
                startActivityForResult(intent, ADD_ITEM_REQUEST_CODE)
                ObjectAnimator.ofFloat(this, "rotation", 0f, 360f).apply {
                    duration = 500
                    interpolator = DecelerateInterpolator()
                    start()
                }
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
            val itemView = CardView(parent.context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    if (this is ViewGroup.MarginLayoutParams) {
                        setMargins(8, 8, 8, 8)
                    }
                }
                radius = 16f
                cardElevation = 8f
                setCardBackgroundColor(Color.WHITE)
                setPadding(16, 16, 16, 16)

                addView(LinearLayout(parent.context).apply {
                    orientation = LinearLayout.VERTICAL
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )

                    addView(TextView(parent.context).apply {
                        id = 1001
                        textSize = 18f
                        setTextColor(Color.BLACK)
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        ).apply {
                            setMargins(0, 0, 0, 8)
                        }
                    })
                    addView(TextView(parent.context).apply {
                        id = 1002
                        textSize = 16f
                        setTextColor(Color.DKGRAY)
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        ).apply {
                            setMargins(0, 0, 0, 8)
                        }
                    })
                    addView(ImageView(parent.context).apply {
                        id = 1003
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            200
                        ).apply {
                            setMargins(0, 8, 0, 8)
                        }
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    })
                    addView(CheckBox(parent.context).apply {
                        id = 1004
                        text = "Completed"
                        setTextColor(Color.BLACK)
                        setOnCheckedChangeListener { _, isChecked ->
                            // Handle completion status
                        }
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        ).apply {
                            setMargins(0, 8, 0, 8)
                        }
                    })
                    addView(Button(parent.context).apply {
                        id = 1005
                        text = "Delete"
                        setBackgroundColor(Color.RED)
                        setTextColor(Color.WHITE)
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        ).apply {
                            setMargins(0, 8, 0, 8)
                        }
                    })
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
                ObjectAnimator.ofFloat(holder.itemView, "alpha", 0f, 1f).apply {
                    duration = 500
                    start()
                }
            }

            holder.btnDelete.setOnClickListener {
                val position = holder.adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    toDoList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, toDoList.size)
                    ObjectAnimator.ofFloat(holder.itemView, "translationX", 0f, -1000f).apply {
                        duration = 500
                        start()
                    }
                }
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
            setBackgroundColor(Color.LTGRAY)
        }

        val tvTitle = TextView(this).apply {
            text = item.title
            textSize = 24f
            setTextColor(Color.BLACK)
        }

        val tvDateTime = TextView(this).apply {
            text = item.dateTime
            textSize = 18f
            setTextColor(Color.DKGRAY)
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

        val backButton = Button(this).apply {
            text = "Back"
            setBackgroundColor(Color.BLUE)
            setTextColor(Color.WHITE)
            setOnClickListener {
                createMainView()
            }
        }

        detailLayout.addView(tvTitle)
        detailLayout.addView(tvDateTime)
        detailLayout.addView(ivPhoto)
        detailLayout.addView(backButton)

        setContentView(detailLayout)
    }
}
