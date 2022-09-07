package ru.mikolaych.childscorekot

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import ru.mikolaych.childscorekot.databinding.ActivityMainBinding
import ru.mikolaych.childscorekot.features.RandomNumbers
import ru.mikolaych.childscorekot.screens.PassInputFragment
import ru.mikolaych.childscorekot.screens.WinFragment
import ru.mikolaych.childscorekot.screens.WrongFragment
import ru.mikolaych.childscorekot.viewModel.DataModel

private var level:Int = 1
private var result:Int? = null
private var exerciseNumber:Int = 1
private var exerciseLimit:Int = 1
private var positiveScore = 0
private var negativeScore = 0
private var errorNumber:Int = 1
private var timerLevel:Long = 10000
private var timerDelta:Int = 10
private var timer: CountDownTimer? = null

private var soundStatus:Boolean = true
private var timerStatus:Boolean = true
private var multiplyStatus:Boolean = false



class MainActivity : AppCompatActivity(), RandomNumbers {
    private lateinit var binding: ActivityMainBinding
    private val dataModel: DataModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataModel.soundStatus.observe(this) {
            soundStatus = it
        }
        dataModel.timerStatus.observe(this) {
            timerStatus = it
        }
        dataModel.multiplyStatus.observe(this) {
            multiplyStatus = it
        }
        dataModel.exerciseNumber.observe(this){
            exerciseNumber = it
        }
        dataModel.errorNumber.observe(this){
            errorNumber = it
        }
        dataModel.timerDelta.observe(this){
            timerDelta = it
        }

        startLevel()
//        restartButton()
        optionButton()




    }
    
    //Старт опций
    private fun optionButton(){
        binding.optionsButton.setOnClickListener(View.OnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragment, PassInputFragment(), "pass_fragment").commit()

        })
    }

    //Рестарт приложения
    private fun restartButton(){
        binding.returnButton.setOnClickListener(View.OnClickListener {
            allClear()
           /* removeWinFragment()
            removeWrongFragment()*/
            binding.returnButton.visibility = View.INVISIBLE
            binding.startButton.visibility = View.VISIBLE
            binding.optionsButton.visibility = View.INVISIBLE
            startLevel()
        })

    }

    //закрытие фрагмента при проигрыше
    private fun removeWrongFragment():Boolean{
        val fragment = supportFragmentManager.findFragmentByTag("wrong_fragment")
        fragment?.let {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction.remove(it).commitAllowingStateLoss()

            return true
        }
        return false
    }

    //Закрытие фрагмента при выигрыше
    private fun removeWinFragment():Boolean{
        val fragment = supportFragmentManager.findFragmentByTag("win_fragment")
        fragment?.let {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction.remove(it).commitAllowingStateLoss()

            return true
        }
        return false
    }

    //Стирание UI
    private fun allClear(){
       level = 1
       exerciseNumber = 1
       positiveScore = 0
       negativeScore = 0
       timerLevel = 10000
        binding.positiveWindow.text = positiveScore.toString()
        binding.negativeWindow.text = positiveScore.toString()
        binding.exerciseNumber.text = exerciseNumber.toString()


    }

    //Запуск
    private fun startLevel() {
        starsControl(level)
        initialisation(level)
        countDown(timerLevel)
        pressStart()
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
                    exerciseNumber++
                    controlAnswer()
                    levelChange()
                }
            })
    }

    // Таймер
    private fun countDown(time:Long){
        timer?.cancel()
        timer = object: CountDownTimer(time, 1000){
            override fun onTick(p0: Long) {
                val timeLine:Long = p0/1000
                binding.countDown.text = "Осталось ${timeLine.toString()} секунд"
            }
            override fun onFinish() {
                if (level >= 1 && exerciseNumber > 1){
                    exerciseNumber--
                    positiveScore--
                    binding.positiveWindow.text = positiveScore.toString()
                    binding.exerciseNumber.text = exerciseNumber.toString()
                    countDown(timerLevel)
                } else if (level > 1 && exerciseNumber == 1){
                    level--
                    timerLevel -= timerDelta * 1000
                    positiveScore = exerciseLimit - 1
                    binding.positiveWindow.text = positiveScore.toString()
                    exerciseNumber = exerciseLimit
                    binding.exerciseNumber.text = exerciseNumber.toString()
                    initialisation(level)
                    starsControl(level)
                    countDown(timerLevel)
                } else if(level == 1 && exerciseNumber == 1) {
                    binding.countDown.text = "Время вышло!"
                    supportFragmentManager.beginTransaction().replace(R.id.fragment, WrongFragment(), "wrong_fragment").commit()
                    binding.startButton.visibility = View.INVISIBLE
                    binding.returnButton.visibility = View.VISIBLE
                    binding.optionsButton.visibility = View.VISIBLE
                }
            }
        }.start()
    }

    //Контроль правильности ответа
    private fun controlAnswer(){
        var resultControl:Int = binding.answerWindow.text.toString().toInt()
        if (resultControl == result){
            positiveScore++
            countDown(timerLevel)
            binding.positiveWindow.text = positiveScore.toString()
        } else {
            negativeScore++
            binding.negativeWindow.text = negativeScore.toString()
            countDown(timerLevel)
        }
    }

    //Контроль уровня
    private fun levelChange(){
        if (exerciseNumber > exerciseLimit ){
            if (positiveScore >= exerciseLimit - errorNumber){
                level++
                timerLevel +=  timerDelta * 1000
                countDown(timerLevel)

            } else {
                level--
                timerLevel -= timerDelta * 1000
                countDown(timerLevel)
            }

            if (level in 1..4) {
                initialisation(level)
                exerciseNumber = 1
                binding.exerciseNumber.text  = exerciseNumber.toString()
                positiveScore = 0
                negativeScore = 0
                binding.positiveWindow.text = positiveScore.toString()
                binding.negativeWindow.text = negativeScore.toString()

            } else if (level >= 5){
                supportFragmentManager.beginTransaction().replace(R.id.fragment, WinFragment(), "win_fragment").commit()
                binding.startButton.visibility = View.INVISIBLE
                binding.returnButton.visibility = View.VISIBLE
                binding.optionsButton.visibility = View.VISIBLE
                timer?.cancel()
                binding.countDown.text = "Ты выиграл!"

            }
            else if (level <= 0){
                supportFragmentManager.beginTransaction().replace(R.id.fragment, WrongFragment(), "wrong_fragment").commit()
                binding.startButton.visibility = View.INVISIBLE
                binding.returnButton.visibility = View.VISIBLE
                binding.optionsButton.visibility = View.VISIBLE
                timer?.cancel()
                binding.countDown.text = "Ты проиграл!"

            }
        } else {
            binding.exerciseNumber.text = exerciseNumber.toString()
            initialisation(level)
        }
        starsControl(level)
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