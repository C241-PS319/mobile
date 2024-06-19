package com.c241ps319.patera.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.c241ps319.patera.R
import com.c241ps319.patera.databinding.FragmentDashboardBinding
import com.c241ps319.patera.ui.ViewModelFactory
import com.c241ps319.patera.ui.main.MainViewModel
import com.c241ps319.patera.utils.getCurrentDateAndDay

class DashboardFragment : Fragment() {

    //    Use MainViewModel
    private lateinit var mainViewModel: MainViewModel
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // get MainViewModel using ViewModelProvider
        mainViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory.getInstance(requireContext())
        ).get(MainViewModel::class.java)

        // get Session
        mainViewModel.getSession().observe(viewLifecycleOwner) { session ->
            session.let {
                val formattedName = String.format(getString(R.string.halo_nama), it?.name)
                binding.haloNama.text = formattedName
            }
        }

        binding.dashboardDate.text = getCurrentDateAndDay()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}