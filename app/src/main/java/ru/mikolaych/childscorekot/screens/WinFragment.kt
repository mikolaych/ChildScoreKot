package ru.mikolaych.childscorekot.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import ru.mikolaych.childscorekot.R
import ru.mikolaych.childscorekot.databinding.FragmentWinBinding


class WinFragment : Fragment() {
    private lateinit var binding:FragmentWinBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWinBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.returnButton.setOnClickListener(View.OnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            val fragment = activity?.supportFragmentManager?.findFragmentByTag("win_fragment")
            fragment?.let {
                transaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                transaction?.remove(it)?.commitAllowingStateLoss()
            }

        })
    }

}