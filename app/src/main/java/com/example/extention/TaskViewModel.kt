package com.example.extention

import androidx.lifecycle.*
import kotlinx.coroutines.*

class TaskViewModel(private val repository: TasksRepository) : ViewModel() {

    private val _moveDataLiveData = MutableLiveData(MainViewState())
    private val currentMoveData get() = _moveDataLiveData.value!!
    val moveDataLiveData: LiveData<MainViewState> = _moveDataLiveData

    private val _allDataLiveData = MutableLiveData<List<String>>()
    val allDataLiveData: LiveData<List<String>> = _allDataLiveData


    fun getTask(type: TaskType = TaskType.ALL) {
        viewModelScope.launch {
            when(type){
                TaskType.ALL ->{
                    repository.getTasksFromRoom().getOrNull()?.let {
                        _allDataLiveData.value = it
                    }
                }
                TaskType.ACTIVATED ->{
                    repository.getActivatedTasksFromRoom().getOrNull()?.let {
                        _allDataLiveData.value = it
                    }
                }
                else ->{
                    repository.getCompletedTasksFromRoom().getOrNull()?.let {
                        _allDataLiveData.value = it
                    }
                }
            }

        }
    }

    fun startMoving() {
        viewModelScope.launch() {
            while (isActive) {
                val nextX = if (currentMoveData.isRight) {
                    currentMoveData.x + 1f
                } else {
                    currentMoveData.x - 1f
                }

                val isRight = if (currentMoveData.x == 0f && !currentMoveData.isRight) {
                    true
                } else if (currentMoveData.x == 1400f && currentMoveData.isRight) {
                    false
                } else {
                    currentMoveData.isRight
                }


                _moveDataLiveData.value = currentMoveData.copy(x = nextX, isRight = isRight)

                delay(1)
            }
        }
    }

    class TaskViewModelFactory(private val repository: TasksRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TaskViewModel(repository) as T
        }

    }

}