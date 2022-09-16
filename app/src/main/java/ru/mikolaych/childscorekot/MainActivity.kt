package ru.mikolaych.childscorekot

import android.content.Context
import android.media.MediaPlayer
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
private var exerciseLimit:Int = 5
private var positiveScore = 0
private var negativeScore = 0
private var levelNumber = 1
private var errorNumber:Int = 1
private var timerFirst:Long = 10
private var timerLevel:Long = timerFirst * 1000
private var timerDelta:Int = 10
private var timer: CountDownTimer? = null
private var timerStatus:Boolean = false
private var multiplyStatus:Boolean = false
private var bgMusic: MediaPlayer? = null




class MainActivity : AppCompatActivity(), RandomNumbers {
    private lateinit var binding: ActivityMainBinding

    private val dataModel: DataModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.startAppButton.visibility = View.INVISIBLE
        dataModelInitialisation()
        startUpp()
        optionButton()
        music()

    }

    private fun music(){
       binding.soundCheck.setOnCheckedChangeListener { _, isChecked ->

           if (bgMusic == null) {
               bgMusic = MediaPlayer.create(this, R.raw.music)
               if (isChecked) {
                   bgMusic?.start()
                   bgMusic?.isLooping = true
               }

           } else {

               bgMusic?.stop()
               bgMusic?.release()
               bgMusic = null
           }


       }
    }

    //Передача данных
    private fun dataModelInitialisation(){

        dataModel.timerStatus.observe(this) {
            timerStatus = it
        }
        dataModel.multiplyStatus.observe(this) {
            multiplyStatus = it
        }
        dataModel.exerciseLimit.observe(this){
            exerciseLimit = it
        }
        dataModel.errorNumber.observe(this){
            errorNumber = it
        }
        dataModel.timerDelta.observe(this){
            timerDelta = it
        }
        dataModel.timerLimit.observe(this){
            timerFirst = it
        }
        dataModel.levelNumber.observe(this){
            levelNumber = it
        }
        dataModel.multiplyStatus.observe(this){
            multiplyStatus = it
        }
        dataModel.timerStatus.observe(this){
            timerStatus = it
        }

    }
    
    //Старт опций
    private fun optionButton(){



        binding.optionsButton.setOnClickListener(View.OnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragment, PassInputFragment(), "pass_fragment").commit()
            binding.startAppButton.visibility = View.VISIBLE

        })
    }

    //Старт приложения
    private fun startUpp(){
        binding.startAppButton.setOnClickListener(View.OnClickListener {
            dataModelInitialisation()
            allClear()
            startLevel()
            binding.startAppButton.visibility = View.INVISIBLE
            binding.startButton.visibility = View.VISIBLE
            binding.optionsButton.visibility = View.INVISIBLE

        })

    }



    //Стирание UI
    private fun allClear(){
       level = 1
       exerciseNumber = 1
       positiveScore = 0
       negativeScore = 0
       timerLevel = timerFirst * 1000
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
        if (!multiplyStatus){
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
        } else {
            levelNumber = 1
                binding.exerciseWindow.text = "$first X $second"
                result = first * second

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
        val mediaLevelDown = MediaPlayer.create(baseContext, R.raw.level_down)
        val mediaWrong = MediaPlayer.create(baseContext, R.raw.wrong)
        val mediaFalseAnswer = MediaPlayer.create(baseContext, R.raw.false_answer)
        if (timerStatus){
        timer?.cancel()
        timer = object: CountDownTimer(time, 1000){
            override fun onTick(p0: Long) {
                val timeLine:Long = p0/1000
                binding.countDown.text = "Осталось ${timeLine.toString()} секунд"
            }
            override fun onFinish() {
                if (level >= 1 && exerciseNumber > 1){
                    if (mediaFalseAnswer.isPlaying){
                        mediaFalseAnswer.stop()
                        mediaFalseAnswer.release()
                    }
                    exerciseNumber--
                    positiveScore--

                    mediaFalseAnswer.start()
                    binding.positiveWindow.text = positiveScore.toString()
                    binding.exerciseNumber.text = exerciseNumber.toString()
                    countDown(timerLevel)
                } else if (level > 1 && exerciseNumber == 1){
                    if (mediaLevelDown.isPlaying){
                        mediaLevelDown.stop()
                        mediaLevelDown.release()
                    }
                    level--
                    mediaLevelDown.start()
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
                    if (mediaWrong.isPlaying){
                        mediaWrong.stop()
                        mediaWrong.release()
                    }
                    supportFragmentManager.beginTransaction().replace(R.id.fragment, WrongFragment(), "wrong_fragment").commit()

                    mediaWrong.start()
                    binding.startButton.visibility = View.INVISIBLE
                    binding.startAppButton.visibility = View.VISIBLE
                    binding.optionsButton.visibility = View.VISIBLE
                    allClear()
                }
            }
        }.start()
        } else binding.countDown.text = ""
    }

    //Контроль правильности ответа
    private fun controlAnswer(){
        val mediaTrueAnswer = MediaPlayer.create(baseContext, R.raw.true_answer)
        val mediaFalseAnswer = MediaPlayer.create(baseContext, R.raw.false_answer)
        var resultControl:Int = binding.answerWindow.text.toString().toInt()
        if (resultControl == result){
            if (mediaTrueAnswer.isPlaying){
                mediaTrueAnswer.stop()
                mediaTrueAnswer.release()
            }
            positiveScore++
            countDown(timerLevel)
            if (exerciseNumber != exerciseLimit + 1){
                mediaTrueAnswer.start()
            }
            binding.positiveWindow.text = positiveScore.toString()
        } else {
            negativeScore++
            if (exerciseNumber != exerciseLimit + 1) {
                mediaFalseAnswer.start()
            }
            binding.negativeWindow.text = negativeScore.toString()
            countDown(timerLevel)
        }
    }

    //Контроль уровня
    private fun levelChange(){
        val mediaLevelUp = MediaPlayer.create(baseContext, R.raw.level_up)
        val mediaLevelDown = MediaPlayer.create(baseContext, R.raw.level_down)
        val mediaWin = MediaPlayer.create(baseContext, R.raw.win)
        val mediaWrong = MediaPlayer.create(baseContext, R.raw.wrong)
        if (exerciseNumber > exerciseLimit ){
            if (positiveScore >= exerciseLimit - errorNumber){
                if (mediaLevelUp.isPlaying){
                    mediaLevelUp.stop()
                    mediaLevelUp.release()
                }
                level++

                if (level < levelNumber) {
                    mediaLevelUp.start()
                }
                timerLevel +=  timerDelta * 1000
                countDown(timerLevel)

            } else {
                if (mediaLevelDown.isPlaying){
                    mediaLevelDown.stop()
                    mediaLevelDown.release()
                }
                level--

                if (level > 0){
                    mediaLevelDown.start()
                }
                timerLevel -= timerDelta * 1000
                countDown(timerLevel)
            }

            if (level in 1..levelNumber) {
                initialisation(level)
                exerciseNumber = 1
                binding.exerciseNumber.text  = exerciseNumber.toString()
                positiveScore = 0
                negativeScore = 0
                binding.positiveWindow.text = positiveScore.toString()
                binding.negativeWindow.text = negativeScore.toString()

            } else if (level >= levelNumber){
                if (mediaWin.isPlaying){
                    mediaWin.stop()
                    mediaWin.release()
                }
                supportFragmentManager.beginTransaction().replace(R.id.fragment, WinFragment(), "win_fragment").commit()

                mediaWin.start()
                binding.startButton.visibility = View.INVISIBLE
                binding.startAppButton.visibility = View.VISIBLE
                binding.optionsButton.visibility = View.VISIBLE
                timer?.cancel()
                binding.countDown.text = "Ты выиграл!"
                allClear()

            }
            else if (level <= 0){
                if (mediaWrong.isPlaying){
                    mediaWrong.stop()
                    mediaWrong.release()
                }
                supportFragmentManager.beginTransaction().replace(R.id.fragment, WrongFragment(), "wrong_fragment").commit()

                mediaWrong.start()
                binding.startButton.visibility = View.INVISIBLE
                binding.startAppButton.visibility = View.VISIBLE
                binding.optionsButton.visibility = View.VISIBLE
                timer?.cancel()
                binding.countDown.text = "Ты проиграл!"
                allClear()

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

    override fun onPause() {
        super.onPause()
        bgMusic?.stop()
        bgMusic?.release()
        bgMusic = null

    }


}