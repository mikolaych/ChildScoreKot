package ru.mikolaych.childscorekot.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import ru.mikolaych.childscorekot.R
import ru.mikolaych.childscorekot.databinding.FragmentPassInputBinding
import java.util.*


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

        val a = rand(3, 9)
        val b = rand(3, 9)
        val c = rand(3, 9)
        var d = a + b * c
        binding.example.text = "$a + $b * $c"



        binding.confirmButton.setOnClickListener(View.OnClickListener {
            val readPassword:String = binding.inputPassWindow.text.toString()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            if (readPassword == d.toString()){
                transaction?.replace(R.id.fragment, SettingFragment(), "setting_fragment")
                transaction?.disallowAddToBackStack()
                transaction?.commit()

            }
            else{
                Toast.makeText(context, "Активирован режим для ребенка", Toast.LENGTH_SHORT).show()
                val fragment = activity?.supportFragmentManager?.findFragmentByTag("pass_fragment")
                fragment?.let {
                    transaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    transaction?.remove(it)?.commitAllowingStateLoss()
                }

            }
        })
    }

    fun rand(start: Int, end: Int): Int {
        require(start <= end) { "Illegal Argument" }
        return (start..end).shuffled().first()
    }





}