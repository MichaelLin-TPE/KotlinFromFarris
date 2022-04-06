package com.example.extention

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

enum class TaskType{
    ALL,ACTIVATED,COMPLETED
}

class TasksRepository {

    var controlledCoroutines = ControlledCoroutines<List<String>>()

    suspend fun getTasksFromRoom() : Result<List<String>>{
        return withContext(Dispatchers.IO){
            kotlin.runCatching {
                controlledCoroutines.cancelPreviousThenRun {
                    fakeGetTasks()
                }
            }
        }
    }
    suspend fun getActivatedTasksFromRoom(): Result<List<String>> {
        return withContext(Dispatchers.IO){
            kotlin.runCatching {
                controlledCoroutines.cancelPreviousThenRun {fakeGetActivatedTasks()}
            }
        }
    }

    suspend fun getCompletedTasksFromRoom(): Result<List<String>> {
        return withContext(Dispatchers.IO){
            kotlin.runCatching {
                controlledCoroutines.cancelPreviousThenRun {fakeGetCompletedTasks()}
            }
        }
    }

    private suspend fun fakeGetTasks():List<String>{
        delay(1500L)
        return mutableListOf("Task1","Task2","Task3")
    }

    private suspend fun fakeGetActivatedTasks():List<String>{
        delay(1000L)
        return mutableListOf("Task1")
    }

    private suspend fun fakeGetCompletedTasks():List<String>{
        delay(500L)
        return mutableListOf("Tasks2","Task3")
    }


}