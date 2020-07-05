package com.ismail.todolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_main.view.*


class MainFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        view.fabBtn.setOnClickListener(){
            findNavController().navigate(R.id.action_mainFragment_to_detailFragment)
            Toast.makeText(requireContext(), "Navigation successful", Toast.LENGTH_SHORT).show()

        }

        return  view
    }


}