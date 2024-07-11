package com.imranmelikov.codsoft_universityattendance.presentation.coursesdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imranmelikov.codsoft_universityattendance.databinding.FragmentCoursesDetailBinding


class CoursesDetailFragment : Fragment() {
    private lateinit var binding:FragmentCoursesDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentCoursesDetailBinding.inflate(inflater,container,false)
        return binding.root
    }
}