package com.c241ps319.patera.ui.scan

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.c241ps319.patera.databinding.FragmentScanBinding
import com.c241ps319.patera.helper.ImageClassifierHelper
import com.c241ps319.patera.ui.ViewModelFactory
import org.tensorflow.lite.support.label.Category

class ScanFragment : Fragment() {
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    private var currentImageUri: Uri? = null

    private val viewModel: ScanViewModel by viewModels() {
        ViewModelFactory.getInstance(requireContext())
    }

    //    Launcher Permission
    private val reqPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) {
        if (it != null) {
            currentImageUri = it
            Log.d(TAG, "URI: $currentImageUri")
//            showImage()
        } else {
            Toast.makeText(requireContext(), "No media selected!", Toast.LENGTH_SHORT).show()
        }
    }

    //    Check Permission
    private fun allPermissionGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //        Req Permission
        if (!allPermissionGranted()) {
            reqPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.btnAdd.setOnClickListener {
            startGallery()


        }

        binding.btnScan.setOnClickListener {
            analyzeImage(currentImageUri!!)
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showLoading(b: Boolean) {
        binding.progressIndicator.visibility = if (b) View.VISIBLE else View.GONE
    }

    private fun analyzeImage(uri: Uri) {
        imageClassifierHelper = ImageClassifierHelper(
            context = requireContext(),
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    activity?.runOnUiThread {
                        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResults(results: List<Category>?, inferenceTime: Long) {
                    Log.d(TAG, results.toString())
                    activity?.runOnUiThread {
                        results?.let {
                            if (it.isNotEmpty() ) {
//                                val resultLabel = it[0].label
//                                val resultScore = it[0].score
                                binding.tvResult.text = it.toString()
                            }
                        }
                    }
                }

            }

        )

        imageClassifierHelper.classifyStaticImage(currentImageUri!!)

    }


    companion object {
        private val TAG = "ScanFragment"
        private const val REQUIRED_PERMISSION = android.Manifest.permission.CAMERA
    }


}