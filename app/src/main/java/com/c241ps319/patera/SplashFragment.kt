package com.c241ps319.patera

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Splash
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.splashFragment_to_mainActivity)
        }, 3000)

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

}