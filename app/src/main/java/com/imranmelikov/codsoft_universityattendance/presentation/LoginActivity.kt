package com.imranmelikov.codsoft_universityattendance.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.imranmelikov.codsoft_universityattendance.R
import com.imranmelikov.codsoft_universityattendance.constants.EditTextEmptyConstants
import com.imranmelikov.codsoft_universityattendance.constants.ErrorMsgConstants
import com.imranmelikov.codsoft_universityattendance.databinding.ActivityLoginBinding
import com.imranmelikov.codsoft_universityattendance.util.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    @Inject
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        signIn()
        observeCRUD()
        if (auth.currentUser!=null){
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }

    private fun signIn(){
        val email=binding.emailEdittext
        val password=binding.passwordEdittext
        binding.saveBtn.setOnClickListener {
            when{
                email.text.isNotEmpty()&&password.text.isNotEmpty()->{
                    viewModel.signIn(email.text.toString(), password.text.toString())
                }
                email.text.isEmpty()->{
                    Toast.makeText(this, EditTextEmptyConstants.emailEmpty, Toast.LENGTH_SHORT).show()
                }
                password.text.isEmpty()->{
                    Toast.makeText(this, EditTextEmptyConstants.passwordEmpty, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun observeCRUD(){
        viewModel.msgLiveData.observe(this){result->
            when(result.status){
                Status.SUCCESS->{
                    result.data?.let {
                        val intent= Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                        Log.d(it.message,it.success.toString())
                    }
                }
                Status.LOADING->{
                }
                Status.ERROR->{
                    Toast.makeText(this, ErrorMsgConstants.errorForUser, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}