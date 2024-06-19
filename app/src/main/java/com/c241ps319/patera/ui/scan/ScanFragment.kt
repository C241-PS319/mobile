package com.c241ps319.patera.ui.scan

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Resources
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
import com.c241ps319.patera.utils.getImageUri
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
            displayImage()
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

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //        Req Permission
        if (!allPermissionGranted()) {
            reqPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        val btnGallery = binding.btnGallery
        val btnCamera = binding.btnCamera
        val btnEditImage = binding.btnEditImage
        val btnDeleteImage = binding.btnDeleteImage
        val btnScan = binding.btnScan
        val tv = binding.textView

        btnGallery.setOnClickListener {
            openGallery()
            if (binding.ivPlant.drawable != null) {
                btnScan.visibility = View.VISIBLE
                btnEditImage.visibility = View.VISIBLE
                btnDeleteImage.visibility = View.VISIBLE

                btnGallery.visibility = View.GONE
                btnCamera.visibility = View.GONE
                tv.visibility = View.GONE
            }
        }


        btnCamera.setOnClickListener {
            Toast.makeText(requireContext(), "Camera", Toast.LENGTH_SHORT).show()

            openCamera()

            btnScan.visibility = View.VISIBLE
            btnEditImage.visibility = View.VISIBLE
            btnDeleteImage.visibility = View.VISIBLE

            btnGallery.visibility = View.GONE
            btnCamera.visibility = View.GONE
            tv.visibility = View.GONE
        }

        btnEditImage.setOnClickListener {
            Toast.makeText(requireContext(), "Edit Image", Toast.LENGTH_SHORT).show()

            btnScan.visibility = View.GONE
            btnEditImage.visibility = View.GONE
            btnDeleteImage.visibility = View.GONE

            btnGallery.visibility = View.VISIBLE
            btnCamera.visibility = View.VISIBLE
            tv.visibility = View.VISIBLE
        }

        btnDeleteImage.setOnClickListener {
            Toast.makeText(requireContext(), "Delete Image", Toast.LENGTH_SHORT).show()

            binding.ivPlant.setImageDrawable(Resources.getSystem().getDrawable(android.R.drawable.ic_menu_gallery))

            btnScan.visibility = View.GONE
            btnEditImage.visibility = View.GONE
            btnDeleteImage.visibility = View.GONE

            btnGallery.visibility = View.VISIBLE
            btnCamera.visibility = View.VISIBLE
            tv.visibility = View.VISIBLE
        }

        btnScan.setOnClickListener {
//            analyzeImage(currentImageUri!!)
        }


    }

    private fun openCamera() {
        currentImageUri = getImageUri(requireContext())
        cameraLauncher.launch(currentImageUri!!)
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) displayImage()
    }

    private fun openGallery() {
        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            currentImageUri = uri
            displayImage()
        } else {
            Toast.makeText(requireContext(), "No media selected!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayImage() {
        Log.d(TAG, "displayImage: $currentImageUri")
        if (currentImageUri != null) {
            binding.ivPlant.setImageURI(currentImageUri)
        } else {
            Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
        }
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
//                                binding.tvResult.text = it.toString()
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