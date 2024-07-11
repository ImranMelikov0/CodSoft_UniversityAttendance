package com.imranmelikov.codsoft_universityattendance.presentation.courses

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imranmelikov.codsoft_universityattendance.R
import com.imranmelikov.codsoft_universityattendance.databinding.FragmentCoursesBinding

class CoursesFragment : Fragment() {
    private lateinit var binding:FragmentCoursesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentCoursesBinding.inflate(inflater,container,false)
        return binding.root
    }
}