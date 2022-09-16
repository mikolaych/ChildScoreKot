package ru.mikolaych.childscorekot.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class DataModel : ViewModel() {

    val timerStatus:MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val multiplyStatus:MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val errorNumber:MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val timerDelta:MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val exerciseLimit:MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val timerLimit:MutableLiveData<Long> by lazy {
        MutableLiveData<Long>()
    }
    val levelNumber:MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }





}