package com.c241ps319.patera.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.c241ps319.patera.R
import com.c241ps319.patera.data.model.UserModel
import com.c241ps319.patera.databinding.FragmentProfileBinding
import com.c241ps319.patera.ui.ViewModelFactory
import com.c241ps319.patera.ui.auth.login.LoginActivity
import com.c241ps319.patera.ui.main.MainViewModel
import com.c241ps319.patera.ui.profile.update.UpdateProfileActivity

class ProfileFragment : Fragment() {

    //    Use MainViewModel
    private lateinit var mainViewModel: MainViewModel

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var session: UserModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        // get MainViewModel using ViewModelProvider
        mainViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory.getInstance(requireContext())
        )[MainViewModel::class.java]

        // get Session & Set Data
        mainViewModel.getSession().observe(viewLifecycleOwner) { session ->
            if (session != null) {
                this.session = session
            }
            binding.profileName.text = session?.name
            binding.profileEmail.text = session?.email
            if (session?.picture != null && session.picture == "") {
                Glide.with(requireContext()).load(session.picture).into(binding.profileImage)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardEditProfile.setOnClickListener {
            val intent = Intent(activity, UpdateProfileActivity::class.java)
            intent.putExtra(UpdateProfileActivity.EXTRA_NAME, session.name)
            intent.putExtra(UpdateProfileActivity.EXTRA_EMAIL, session.email)
            intent.putExtra(UpdateProfileActivity.EXTRA_TOKEN, session.token)
            startActivity(intent)
        }

        binding.cardLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.confirm))
                .setMessage(getString(R.string.logout_confirmation))
                .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                    // clear session
                    mainViewModel.logout()

                    // move to Login
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    dialog.dismiss()
                }
                .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                    // Cancel the dialog
                    dialog.dismiss()
                }
                .create()
                .show()
        }

    }

    override fun onResume() {
        super.onResume()
        // get Session & Set Data
        mainViewModel.getSession().observe(viewLifecycleOwner) { session ->
            if (session != null) {
                this.session = session
            }
            binding.profileName.text = session?.name
            binding.profileEmail.text = session?.email
            if (session?.picture != null && session.picture == "") {
                Glide.with(requireContext()).load(session.picture).into(binding.profileImage)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}