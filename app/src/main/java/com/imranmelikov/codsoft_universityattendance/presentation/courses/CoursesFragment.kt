package com.imranmelikov.codsoft_universityattendance.presentation.courses

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.imranmelikov.codsoft_universityattendance.R
import com.imranmelikov.codsoft_universityattendance.constants.ErrorMsgConstants
import com.imranmelikov.codsoft_universityattendance.databinding.FragmentCoursesBinding
import com.imranmelikov.codsoft_universityattendance.presentation.profile.ProfileViewModel
import com.imranmelikov.codsoft_universityattendance.util.Resource
import com.imranmelikov.codsoft_universityattendance.util.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CoursesFragment : Fragment() {
    private lateinit var binding:FragmentCoursesBinding
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var courseViewModel: CoursesViewModel
    private lateinit var courseAdapter: CourseAdapter
    private var userName=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentCoursesBinding.inflate(inflater,container,false)
        profileViewModel=ViewModelProvider(requireActivity())[ProfileViewModel::class.java]
        courseViewModel=ViewModelProvider(requireActivity())[CoursesViewModel::class.java]

        courseAdapter=CourseAdapter()
        profileViewModel.getUser()
        courseViewModel.getCourses()
        observeUser()
        observeCourses()
        binding.profile.setOnClickListener {
            findNavController().navigate(R.id.action_coursesFragment_to_profileFragment)
        }
        return binding.root
    }

    private fun observeUser(){
        profileViewModel.userLiveData.observe(viewLifecycleOwner){result->
            handleResult(result){user->
                "Academic Degree".also { binding.academicDegreeCardView.totalTitle.text = it }
                binding.academicDegreeCardView.total.text=user.academic_degree
                binding.studentNameView.studentName.text=user.student
                userName=user.student
            }
        }
    }
    private fun observeCourses(){
        courseViewModel.coursesLiveData.observe(viewLifecycleOwner){result->
            handleResult(result){courseList->
                binding.transactionRv.layoutManager=LinearLayoutManager(requireContext())
                courseAdapter.courseList=courseList
                courseAdapter.userName=userName
                binding.transactionRv.adapter=courseAdapter
                binding.totalCoursesCardView.total.text=courseList.size.toString()
            }
        }
    }
    private fun <T> handleResult(result: Resource<T>, actionOnSuccess: (T) -> Unit) {
        when (result.status) {
            Status.ERROR -> {
                errorResult()
            }
            Status.SUCCESS -> {
                result.data?.let(actionOnSuccess)
                successResult()
            }
            Status.LOADING -> {
                loadingResult()
            }
        }
    }
    private fun errorResult(){
        Toast.makeText(requireContext(),ErrorMsgConstants.errorForUser,Toast.LENGTH_SHORT).show()
        binding.emptyStateLayout.progress.visibility=View.GONE
        binding.emptyStateLayout.noResultText.visibility=View.VISIBLE
    }
    private fun loadingResult(){
        binding.emptyStateLayout.progress.visibility=View.VISIBLE
        binding.emptyStateLayout.noResultText.visibility=View.GONE
    }
    private fun successResult(){
        binding.emptyStateLayout.progress.visibility=View.GONE
        binding.emptyStateLayout.noResultText.visibility=View.GONE
    }
}