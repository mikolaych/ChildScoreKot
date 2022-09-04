package ru.mikolaych.childscorekot.features

import kotlin.random.Random
import kotlin.random.nextInt

interface RandomNumbers {

    fun getNumberLevel(level:Int):Int{
        var number:Int = 0
        when (level){
            1 -> number = Random.nextInt(0..9)
            2 -> number = Random.nextInt(10..19)
            3 -> number = Random.nextInt(20..50)
            4 -> number = Random.nextInt(50..99)

        }
        return number

    }

}