package ru.mikolaych.childscorekot.features

import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random
import kotlin.random.nextInt

interface RandomNumbers {



    fun getNumberLevel(level:Int): Int {
        var number: Int = 0
            when (level){
            1 -> number = rand(0, 9)
            2 -> number = rand(10, 19)
            3 -> number = rand(20, 50)
            4 -> number = rand(50, 99)

        }
        return number

    }

    fun rand(start: Int, end: Int): Int {
        require(start <= end) { "Illegal Argument" }
        return (start..end).shuffled().first()
    }

}