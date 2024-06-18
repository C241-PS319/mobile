package com.c241ps319.patera.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import com.c241ps319.patera.ml.Detect
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class ImageClassifierHelper(
    var threshold: Float = 0.1f,
    var maxResult: Int = 3,
    val context: Context,
    val classifierListener: ClassifierListener?
) {
    private var imageClassifier: Detect? = null

    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(
            results: List<Category>?,
            inferenceTime: Long
        )
    }

    private fun setupImageClassifier() {
        try {
            imageClassifier = Detect.newInstance(context)
        } catch (e: IllegalStateException) {
            classifierListener?.onError("Image classifier failed to initialize: ${e.message}")
            Log.e(TAG, e.message.toString())
        }
    }

    fun classifyStaticImage(uri: Uri) {
        if (imageClassifier == null) {
            setupImageClassifier()
        }

        val bitmap = loadImage(uri)
        val tensorImage = preprocessImage(bitmap)
        val byteBuffer = tensorImage.buffer

        // Membuat TensorBuffer untuk input model
        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, 320, 320, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        val startTime = SystemClock.uptimeMillis()

        // Menjalankan inferensi model dan mengambil hasil
        val outputs = imageClassifier?.process(inputFeature0)
        val outputFeature0 = outputs?.outputFeature0AsTensorBuffer
        val outputFeature1 = outputs?.outputFeature1AsTensorBuffer
        val outputFeature2 = outputs?.outputFeature2AsTensorBuffer
        val outputFeature3 = outputs?.outputFeature3AsTensorBuffer

        val inferenceTime = SystemClock.uptimeMillis() - startTime

        // Konversi outputFeature0, outputFeature1, outputFeature2, outputFeature3 ke List<Category>
        val results = processOutput(outputFeature0, outputFeature1, outputFeature2, outputFeature3)
        Log.d(TAG, outputFeature1?.floatArray?.toList().toString())
        Log.d(TAG, outputFeature1?.floatArray?.toList()?.size.toString())

        classifierListener?.onResults(
            results,
            inferenceTime
        )

        // Pastikan untuk menutup model saat tidak lagi digunakan
        imageClassifier?.close()
    }

    private fun preprocessImage(bitmap: Bitmap): TensorImage {
        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bitmap)

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(320, 320, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(0.0f, 1.0f))  // Normalisasi jika diperlukan
            .build()

        return imageProcessor.process(tensorImage)
    }

    private fun processOutput(
        vararg outputs: TensorBuffer?
    ): List<Category> {
        val categories = mutableListOf<Category>()
        outputs.forEach { output ->
            output?.let {
                // Asumsikan output adalah daftar skor
                val scores = it.floatArray
                scores.forEachIndexed { index, score ->
                    if (score > threshold) {
                        categories.add(Category(index.toString(), score))
                    }
                }
                categories.sortByDescending { it.score }
            }
        }
        return categories.take(maxResult)
    }

    private fun loadImage(uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }.copy(Bitmap.Config.ARGB_8888, true)
    }

    companion object {
        private const val TAG = "ImageClassifierHelper"
    }
}