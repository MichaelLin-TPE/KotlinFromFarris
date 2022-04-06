package com.example.extention

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope

class ControlledCoroutines<T> {

    private var cachedTasks: Deferred<T>? = null

    private var activeTask: Deferred<T>? = null


    //取消之前的請求，只接受後來的請求
    suspend fun cancelPreviousThenRun(block: suspend () -> T): T {
        // 如果當前有正在執行的 cachedTasks，可以直接取消並改成執行最新的請求
        cachedTasks?.cancelAndJoin()

        return coroutineScope {
            // 建立一個 async 並且 suspend
            val newTask = async {
                block()
            }
            // newTask 執行完畢時清除舊的 cachedTasks 任務
            newTask.invokeOnCompletion {
                cachedTasks = null
            }
            // newTask 完成後交給 cachedTasks
            cachedTasks = newTask
            // newTask 恢復狀態並開始執行
            newTask.await()

        }
    }

    //使用之前的請求，拒絕後來的請求
    suspend fun joinPreviousOrRun(block: suspend () -> T): T {
        // 如果當前有正在執行的 cachedTasks ，直接返回
        activeTask?.let {
            return it.await()
        }

        // 否則建立一個新的 async
        return coroutineScope {
            val newTask = async {
                block()
            }

            newTask.invokeOnCompletion {
                activeTask = null
            }

            activeTask = newTask
            newTask.await()
        }
    }

}