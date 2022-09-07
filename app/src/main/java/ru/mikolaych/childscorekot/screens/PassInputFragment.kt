package ru.mikolaych.childscorekot.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import ru.mikolaych.childscorekot.R
import ru.mikolaych.childscorekot.databinding.FragmentPassInputBinding


class PassInputFragment : Fragment() {
    private lateinit var binding: FragmentPassInputBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPassInputBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.confirmButton.setOnClickListener(View.OnClickListener {
            val readPassword:String = binding.inputPassWindow.text.toString()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            if (readPassword == "0102"){
                transaction?.replace(R.id.fragment, SettingFragment(), "setting_fragment")
                transaction?.disallowAddToBackStack()
                transaction?.commit()

            }
            else{
                val fragment = activity?.supportFragmentManager?.findFragmentByTag("pass_fragment")
                fragment?.let {
                    transaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    transaction?.remove(it)?.commitAllowingStateLoss()
                }

            }
        })
    }



}