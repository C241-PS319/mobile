package com.c241ps319.patera.ui.splash_onboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.c241ps319.patera.ui.main.MainActivity
import com.c241ps319.patera.R
import com.c241ps319.patera.ui.ViewModelFactory
import com.c241ps319.patera.ui.auth.login.LoginActivity
import com.c241ps319.patera.ui.main.MainViewModel

class SplashFragment : Fragment() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Splash
        Handler(Looper.getMainLooper()).postDelayed({
            if (onBoardingIsFinished()) {
                viewModel.getSession().observe(viewLifecycleOwner) { session ->
                    if (session == null) {
                        val intent = Intent(requireContext(), LoginActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    } else {
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    }
                }
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_onboardFragment)
            }
        }, 3000)

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    private fun onBoardingIsFinished(): Boolean {
        // SharedPref
        val sharedPreferences = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("finished",false)
    }

}