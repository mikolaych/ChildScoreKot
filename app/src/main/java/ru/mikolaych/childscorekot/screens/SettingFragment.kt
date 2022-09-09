package ru.mikolaych.childscorekot.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.google.android.material.slider.Slider
import ru.mikolaych.childscorekot.R
import ru.mikolaych.childscorekot.databinding.FragmentSettingBinding
import ru.mikolaych.childscorekot.viewModel.DataModel
import java.util.*
import kotlin.math.roundToInt


class SettingFragment : Fragment() {

    private val dataModel : DataModel by activityViewModels()
    private lateinit  var binding: FragmentSettingBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveButton.setOnClickListener(View.OnClickListener {
            if (binding.timerDeltaWindow.text.toString().isEmpty()){
                dataModel.timerDelta.value = 10
            } else if (binding.timerLimitWindow.text.toString().isEmpty()){
                dataModel.timerLimit.value = 10
            } else {
                val delTimer = binding.timerDeltaWindow.text.toString()
                val limTimer = binding.timerLimitWindow.text.toString()
                dataModel.timerLimit.value = limTimer.toLong()
                dataModel.timerDelta.value = delTimer.toInt()

            }
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            val fragment =
                activity?.supportFragmentManager?.findFragmentByTag("setting_fragment")
            fragment?.let {
                transaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                transaction?.remove(it)?.commitAllowingStateLoss()
            }

        })


        binding.numberLevelSlider.addOnChangeListener{slider, value, fromUser ->
            var n:String = "уровень"
            when(value.roundToInt()){
                1 -> n = "уровень"
                else -> n = "уровня"
            }
            binding.numberLevelTittleSlider.text = "${value.roundToInt()} $n"
            dataModel.levelNumber.value = value.roundToInt()
        }

        binding.numberExerciseSlider.addOnChangeListener{slider, value, fromUser ->
            binding.numberExerciseTittleSlider.text = "${value.roundToInt()} примеров"
            dataModel.exerciseLimit.value = value.roundToInt()
        }

        binding.numberErrorSlider.addOnChangeListener{slider, value, fromUser ->
            var n:String = "ошибка"
            when(value.roundToInt()){
                1 -> n = "ошибка"
                in 2..4 -> n = "ошибки"
                5 -> n = "ошибок"
            }
            binding.numberErrorTittleSlider.text = "${value.roundToInt()} $n"
            dataModel.errorNumber.value = value.roundToInt()
        }


        binding.timerSwitch.setOnCheckedChangeListener{_, isChecked->
            dataModel.timerStatus.value = isChecked
        }
        binding.multiplySwitch.setOnCheckedChangeListener{_, isChecked->
            if (isChecked){
                dataModel.multiplyStatus.value = true
                binding.numberLevelSlider.value = 1f
                binding.numberLevelSlider.visibility = View.INVISIBLE
            } else {
                dataModel.multiplyStatus.value = false
                binding.numberLevelSlider.visibility = View.VISIBLE
            }

        }


    }



}