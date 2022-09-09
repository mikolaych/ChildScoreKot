package ru.mikolaych.childscorekot.features

import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random
import kotlin.random.nextInt

interface RandomNumbers {



    fun getNumberLevel(level:Int): Int {
        var number: Int = 0
            when (level){
            1 -> number = (0..9).random()
            2 -> number = (10..19).random()
            3 -> number = (20..50).random()
            4 -> number = (50..99).random()

        }
        return number

    }

}