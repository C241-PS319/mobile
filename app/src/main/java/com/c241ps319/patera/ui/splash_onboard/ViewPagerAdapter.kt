package com.c241ps319.patera.ui.splash_onboard

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    listFragment: ArrayList<Fragment>,
    fragmentManager: FragmentManager,
    lifecyle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecyle) {

    private val fragmentList = listFragment
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}