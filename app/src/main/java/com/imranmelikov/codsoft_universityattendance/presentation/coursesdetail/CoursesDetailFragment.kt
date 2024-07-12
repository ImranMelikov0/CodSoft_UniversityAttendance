package com.imranmelikov.codsoft_universityattendance.presentation.coursesdetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.imranmelikov.codsoft_universityattendance.constants.ErrorMsgConstants
import com.imranmelikov.codsoft_universityattendance.databinding.FragmentCoursesDetailBinding
import com.imranmelikov.codsoft_universityattendance.util.Resource
import com.imranmelikov.codsoft_universityattendance.util.Status
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CoursesDetailFragment : Fragment() {
    private lateinit var binding:FragmentCoursesDetailBinding
    private lateinit var viewModel: CoursesDetailViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentCoursesDetailBinding.inflate(inflater,container,false)
        viewModel=ViewModelProvider(requireActivity())[CoursesDetailViewModel::class.java]

        binding.transactionDetails.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        val courseId=arguments?.getString("courseId")
        val student=arguments?.getString("user")
        courseId?.let {
            viewModel.getCourse(it)
        }
        student?.let {
            observeCourse(it)
        }
        observeMessage()
        return binding.root
    }

    private fun observeCourse(student:String){
        viewModel.courseLiveData.observe(viewLifecycleOwner){result->
            handleResult(result){
                binding.transactionDetails.date.text=it.duration
                binding.transactionDetails.name.text=it.course_name
                val joinedString=it.student_list.joinToString(", ")
                binding.transactionDetails.studentList.text=joinedString

               val availableStudent= it.student_list.find {name->
                 name==student
                }
                if(availableStudent==student){
                    binding.transactionDetails.btnEnrollCourse.visibility=View.GONE
                }else{
                    binding.transactionDetails.btnEnrollCourse.visibility=View.VISIBLE
                    binding.transactionDetails.btnEnrollCourse.setOnClickListener {_->
                        it.student_list.add(student)
                        viewModel.updateCourse(it)
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }
    private fun observeMessage(){
        viewModel.msgLiveData.observe(viewLifecycleOwner){result->
            handleResult(result){
                Log.d(it.message,it.success.toString())
            }
        }
    }
    private fun <T> handleResult(result: Resource<T>, actionOnSuccess: (T) -> Unit) {
        when (result.status) {
            Status.ERROR -> {
                Toast.makeText(requireContext(), ErrorMsgConstants.errorForUser, Toast.LENGTH_SHORT).show()
            }
            Status.SUCCESS -> {
                result.data?.let(actionOnSuccess)
            }
            Status.LOADING -> {
            }
        }
    }
}