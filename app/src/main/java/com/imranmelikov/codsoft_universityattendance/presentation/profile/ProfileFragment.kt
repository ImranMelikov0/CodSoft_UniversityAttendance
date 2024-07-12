package com.imranmelikov.codsoft_universityattendance.presentation.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.imranmelikov.codsoft_universityattendance.R
import com.imranmelikov.codsoft_universityattendance.constants.ErrorMsgConstants
import com.imranmelikov.codsoft_universityattendance.databinding.FragmentProfileBinding
import com.imranmelikov.codsoft_universityattendance.presentation.LoginActivity
import com.imranmelikov.codsoft_universityattendance.presentation.MainActivity
import com.imranmelikov.codsoft_universityattendance.util.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
  private lateinit var binding:FragmentProfileBinding
  private lateinit var viewModel: ProfileViewModel
  @Inject
  lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentProfileBinding.inflate(inflater,container,false)
        viewModel=ViewModelProvider(requireActivity())[ProfileViewModel::class.java]

        viewModel.getUser()
        observeUser()
        signOut()
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

    private fun observeUser(){
        viewModel.userLiveData.observe(viewLifecycleOwner){result->
            when(result.status){
                Status.SUCCESS->{
                    result.data?.let { user->
                        binding.progress.visibility=View.GONE
                        binding.noResultText.visibility=View.GONE
                        binding.email.text=user.email
                        binding.studentText.text=user.student
                        binding.academicDegreeText.text=user.academic_degree
                        binding.collageNameText.text=user.collage_name
                        binding.startDateText.text=user.start_date
                    }
                }
                Status.LOADING->{
                    binding.progress.visibility=View.VISIBLE
                    binding.noResultText.visibility=View.GONE
                }
                Status.ERROR->{
                    Toast.makeText(requireContext(), ErrorMsgConstants.errorForUser, Toast.LENGTH_SHORT).show()
                    binding.progress.visibility=View.GONE
                    binding.noResultText.visibility=View.VISIBLE
                }
            }
        }
    }
    private fun signOut(){
        binding.accountLogOutLinear.setOnClickListener {
            auth.signOut()
            val intent= Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            (activity as MainActivity).finish()
        }
    }

}