package ru.mikolaych.childscorekot.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import ru.mikolaych.childscorekot.R
import ru.mikolaych.childscorekot.databinding.FragmentSettingBinding
import ru.mikolaych.childscorekot.viewModel.DataModel


class SettingFragment : Fragment() {

    private val dataModel : DataModel by activityViewModels()
    private lateinit  var binding: FragmentSettingBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveButton.setOnClickListener(View.OnClickListener {
            if (binding.numberExerciseWindow.text.toString().isEmpty() ||
                binding.numberErrorWindow.text.toString().isEmpty() ||
                    binding.timerDeltaWindow.text.toString().isEmpty() ||
                    binding.timerLimitWindow.text.toString().isEmpty()||
                    binding.numberLevelWindow.text.toString().isEmpty()){
                Toast.makeText(context, "Заполните все поля!", Toast.LENGTH_SHORT).show()
        } else {
                var soundStat:Boolean = false
                binding.soundSwitch.setOnCheckedChangeListener{_, isChecked ->
                    soundStat = true


                }

                val exNumber = binding.numberExerciseWindow.text.toString()
                val erNumber = binding.numberErrorWindow.text.toString()
                val delTimer = binding.timerDeltaWindow.text.toString()
                val limTimer = binding.timerLimitWindow.text.toString()
                val limLevel = binding.numberLevelWindow.text.toString()
                dataModel.exerciseLimit.value = exNumber.toInt()
                dataModel.errorNumber.value = erNumber.toInt()
                dataModel.timerLimit.value = limTimer.toLong()
                dataModel.timerDelta.value = delTimer.toInt()
                dataModel.levelNumber.value = limLevel.toInt()
                dataModel.soundStatus.value = soundStat


                Toast.makeText(activity?.applicationContext, "Данные сохранены", Toast.LENGTH_SHORT)
                    .show()

                val transaction = activity?.supportFragmentManager?.beginTransaction()
                val fragment =
                    activity?.supportFragmentManager?.findFragmentByTag("setting_fragment")
                fragment?.let {
                    transaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    transaction?.remove(it)?.commitAllowingStateLoss()
                }
            }

        })


    }



}