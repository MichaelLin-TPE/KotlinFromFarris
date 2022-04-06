package com.example.extention

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import java.lang.Runnable
import kotlin.coroutines.CoroutineContext

class MainActivity() : AppCompatActivity() {

    private lateinit var ivPhoto: ImageView

    private val handler = Handler(Looper.getMainLooper())

    private var isBack = false

    private var moveX = 100f
    private var moveY = 100f

    private val repository by lazy { TasksRepository() }

    private val viewModel : TaskViewModel by viewModels{
        TaskViewModel.TaskViewModelFactory(repository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ivPhoto = findViewById(R.id.img)

        val btn = findViewById<Button>(R.id.btn)
        val btn1 = findViewById<Button>(R.id.btn1)
        val btn2 = findViewById<Button>(R.id.btn2)

        viewModel.startMoving()

        viewModel.moveDataLiveData.observe(this) {
            ivPhoto.x = it.x
        }

//        val list = mutableListOf<Int>(1,2,3)
//        list.swap(5,6)


        btn.setOnClickListener {
            viewModel.getTask(TaskType.ALL)
        }
        btn1.setOnClickListener {
            viewModel.getTask(TaskType.ACTIVATED)
        }
        btn2.setOnClickListener {
            viewModel.getTask(TaskType.COMPLETED)
        }

        viewModel.allDataLiveData.observe(this) {
            Toast.makeText(this@MainActivity, it.toString(), Toast.LENGTH_LONG).show()
        }

    }

    fun MutableList<Int>.swap(index1: Int, index2: Int) {
        val tmp = this[index1]
        this[index1] = this[index2]
        this[index2] = tmp
    }

}