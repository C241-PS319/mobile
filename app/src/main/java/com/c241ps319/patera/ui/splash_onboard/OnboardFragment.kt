package com.c241ps319.patera.ui.splash_onboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.c241ps319.patera.R
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class OnboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_onboard, container, false)

        val fragmentList = arrayListOf(
            FirstScreenFragment(),
            SecondScreenFragment()
        )

        val adapter =
            ViewPagerAdapter(fragmentList, requireActivity().supportFragmentManager, lifecycle)
        val viewPager = view.findViewById<ViewPager2>(R.id.view_pager_onboard)
        viewPager.adapter = adapter

//        Indicator
        val indicator = view.findViewById<DotsIndicator>(R.id.dots_indicator)
        indicator.attachTo(viewPager)

        return view
    }

}