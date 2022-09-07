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
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveButton.setOnClickListener(View.OnClickListener {
            val exNumber = binding.numberExerciseWindow.text.toString()
            val erNumber = binding.numberErrorWindow.text.toString()
            val delTimer = binding.timerDeltaWindow.text.toString()
            dataModel.exerciseNumber.value = exNumber.toInt()
            dataModel.errorNumber.value = erNumber.toInt()
            dataModel.timerDelta.value = delTimer.toInt()

            Toast.makeText(activity?.applicationContext, "Данные сохранены", Toast.LENGTH_SHORT).show()

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            val fragment = activity?.supportFragmentManager?.findFragmentByTag("setting_fragment")
            fragment?.let {
                transaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                transaction?.remove(it)?.commitAllowingStateLoss()
            }

        })
    }



}