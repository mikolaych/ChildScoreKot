package ru.mikolaych.childscorekot

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.View
import android.widget.Toast
import ru.mikolaych.childscorekot.databinding.ActivityMainBinding
import ru.mikolaych.childscorekot.features.RandomNumbers
import ru.mikolaych.childscorekot.screens.WinFragment
import ru.mikolaych.childscorekot.screens.WrongFragment

private var level:Int = 1
private var result:Int? = null
private var exerciseNumber:Int = 1
private var exerciseLimit:Int = 5
private var positiveScore = 0
private var negativeScore = 0
private var errorNumber = 2
private var timer: CountDownTimer? = null


class MainActivity : AppCompatActivity(), RandomNumbers {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        starsControl(level)
        initialisation(level)
        pressStart()
        countDown(100000)

    }

    //Инициализация примера
    private fun initialisation(level:Int) {
        binding.answerWindow.text?.clear()
        val first = getNumberLevel(level)
        val second = getNumberLevel(level)
        if (first%2 == 0){
            binding.exerciseWindow.text = "$first + $second"
            result = first + second
        } else {
           if (first > second){
               binding.exerciseWindow.text = "$first - $second"
               result = first - second
           } else{
               binding.exerciseWindow.text = "$second - $first"
               result = second - first
           }
        }
    }

    //Нажатие на старт
   private fun pressStart(){
        val toast = Toast.makeText(applicationContext, "Введи число!", Toast.LENGTH_SHORT)
            binding.startButton.setOnClickListener(View.OnClickListener {
                if (binding.answerWindow.text?.isEmpty() == true){
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                } else {
                    controlAnswer()
                    countDown(100000)
                    levelChange()
                    exerciseNumber++
                }

            })

    }

    // Таймер
    private fun countDown(time:Long){
        timer?.cancel()
        timer = object: CountDownTimer(time, 1000){
            override fun onTick(p0: Long) {
                var time:Long = p0/1000
                binding.countDown.text = "Осталось ${time.toString()} секунд"
            }
            override fun onFinish() {
                binding.countDown.text = "Хана!"

            }

        }.start()
    }

    //Контроль правильности ответа
    private fun controlAnswer(){
        var resultControl:Int = binding.answerWindow.text.toString().toInt()
        if (resultControl == result){
            positiveScore++
            binding.positiveWindow.text = positiveScore.toString()
        } else {
            negativeScore++
            binding.negativeWindow.text = negativeScore.toString()
        }

    }

    //Контроль уровня
    private fun levelChange(){
        if (exerciseNumber > exerciseLimit ){
            if (positiveScore >= exerciseLimit - errorNumber){
                level++
            } else level--

            if (level>0) {

                initialisation(level)
                exerciseNumber = 1
                binding.positiveWindow.text = "0"
                binding.negativeWindow.text = "0"
                positiveScore = 0
                negativeScore = 0
            } else {
                supportFragmentManager.beginTransaction().replace(R.id.fragment, WrongFragment()).commit()
                binding.startButton.visibility = View.INVISIBLE
                timer?.cancel()
            }
        } else {
        binding.exerciseNumber.text = exerciseNumber.toString()
        starsControl(level)
        initialisation(level)
        }
    }

    //Звезды
    private fun starsControl(level: Int){
        when(level){
            1 -> {binding.levelOneStar.visibility = View.VISIBLE
                binding.levelTwoStar.visibility = View.INVISIBLE
                binding.levelThreeStar.visibility = View.INVISIBLE
            binding.levelSuperStar.visibility = View.INVISIBLE}
            2 -> {binding.levelOneStar.visibility = View.VISIBLE
                binding.levelTwoStar.visibility = View.VISIBLE
                binding.levelThreeStar.visibility = View.INVISIBLE
                binding.levelSuperStar.visibility = View.INVISIBLE}
            3 -> {binding.levelOneStar.visibility = View.VISIBLE
                binding.levelTwoStar.visibility = View.VISIBLE
                binding.levelThreeStar.visibility = View.VISIBLE
                binding.levelSuperStar.visibility = View.INVISIBLE}
            4 -> {binding.levelOneStar.visibility = View.INVISIBLE
                binding.levelTwoStar.visibility = View.INVISIBLE
                binding.levelThreeStar.visibility = View.INVISIBLE
                binding.levelSuperStar.visibility = View.VISIBLE}

        }
    }


}