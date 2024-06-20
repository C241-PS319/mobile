package com.c241ps319.patera.ui.scan

import android.annotation.SuppressLint
import android.content.Intent
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
import com.c241ps319.patera.ui.result.ResultActivity
import com.c241ps319.patera.utils.getImageUri
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.support.label.Category
import android.app.Activity.RESULT_OK
import android.database.Cursor
import android.provider.OpenableColumns

class ScanFragment : Fragment() {
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private lateinit var _binding: FragmentScanBinding
    private val binding get() = _binding

    private var currentImageUri: Uri? = null

    private val viewModel: ScanViewModel by viewModels() {
        ViewModelFactory.getInstance(requireContext())
    }

    //    Launcher Permission
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Permission request granted")
            } else {
                showToast("Permission request denied")
            }
        }

    private fun allPermissionsGranted() =
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
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        _binding.apply {
            btnScan.setOnClickListener{
                currentImageUri?.let {
                    analyzeImage(it)
                }?: showToast("No image selected")
            }
            btnGallery.setOnClickListener {
                openGallery()
            }
            btnCamera.setOnClickListener {
                openCamera()
            }
            btnEditImage.setOnClickListener {
                btnScan.visibility = View.GONE
                btnEditImage.visibility = View.GONE
                btnDeleteImage.visibility = View.GONE

                btnGallery.visibility = View.VISIBLE
                btnCamera.visibility = View.VISIBLE
                textView.visibility = View.VISIBLE
            }
            btnDeleteImage.setOnClickListener {
                binding.ivPlant.setImageDrawable(Resources.getSystem().getDrawable(android.R.drawable.ic_menu_gallery))

                textTitleImg.text = "Belum ada foto yang dipilih"
                btnScan.visibility = View.GONE
                btnEditImage.visibility = View.GONE
                btnDeleteImage.visibility = View.GONE

                btnGallery.visibility = View.VISIBLE
                btnCamera.visibility = View.VISIBLE
                textView.visibility = View.VISIBLE
            }
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
            val cacheDir = requireContext().cacheDir
            UCrop.of(uri, Uri.fromFile(cacheDir.resolve("${System.currentTimeMillis()}.jpg")))
                .withMaxResultSize(2000, 2000)
                .start(requireContext(), this)
        } else {
            Toast.makeText(requireContext(), "No media selected!", Toast.LENGTH_SHORT).show()
        }
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "super.onActivityResult(requestCode, resultCode, data)",
        "androidx.appcompat.app.AppCompatActivity"
    )
    )
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            currentImageUri = resultUri
            displayImage()
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            Log.e("Crop Error", "onActivityResult: $cropError")
        } else {
            Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayImage() {
        Log.d(TAG, "displayImage: $currentImageUri")
        if (currentImageUri != null) {
            val fileName = getFileName(currentImageUri!!)
            binding.textTitleImg.text = fileName
            binding.ivPlant.setImageURI(currentImageUri)
            binding.btnScan.visibility = View.VISIBLE
            binding.btnEditImage.visibility = View.VISIBLE
            binding.btnDeleteImage.visibility = View.VISIBLE

            binding.btnGallery.visibility = View.GONE
            binding.btnCamera.visibility = View.GONE
            binding.textView.visibility = View.GONE
        } else {
            Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor: Cursor? = requireContext().contentResolver.query(uri, null, null, null, null)
            cursor.use { c ->
                if (c != null && c.moveToFirst()) {
                    result = c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != null && cut != -1) {
                result = result?.substring(cut + 1)
            }
        }
        return result ?: "Unknown"
    }

    private fun showLoading(b: Boolean) {
        binding.progressIndicator.visibility = if (b) View.VISIBLE else View.GONE
    }

    private fun analyzeImage(image: Uri) {
        val imageHelper = ImageClassifierHelper(
            context = requireContext(),
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    showToast(error)
                }

                override fun onResults(results: List<Category>?) {
                    val resultString = results?.joinToString("\n") {
                        val threshold = (it.score * 100).toInt()
                        "${it.displayName} : ${threshold}%"
                    }
                    if (resultString != null) {
                        Log.d(TAG, "onResults: $resultString")
                        navigateToResult(image, resultString)
                    }
//                    TODO: Save to database
//                    if (resultString != null) {
//                        val data = HistoryEntity(date = convertMillisToDateString(System.currentTimeMillis()), uri = image.toString(), result = resultString)
//                        lifecycleScope.launch(Dispatchers.IO) {
//                            this@MainActivity.runOnUiThread {
//                                viewModel.addHistory(data)
//                                moveToResult(image, resultString)
//                            }
//                        }
//                    }
                }
            }
        )
        imageHelper.classifyStaticImage(image)
    }

    private fun navigateToResult(image:Uri, result: String){
        val intent = Intent(requireContext(), ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, image.toString())
        intent.putExtra(ResultActivity.EXTRA_RESULT, result)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private val TAG = "ScanFragment"
        private const val REQUIRED_PERMISSION = android.Manifest.permission.CAMERA
    }
}