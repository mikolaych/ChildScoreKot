package ru.mikolaych.childscorekot.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mikolaych.childscorekot.MainActivity
import ru.mikolaych.childscorekot.R
import ru.mikolaych.childscorekot.databinding.FragmentWrongBinding


class WrongFragment : Fragment() {
    private lateinit var binding: FragmentWrongBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWrongBinding.inflate(inflater, container, false)
        return binding.root

    }




}