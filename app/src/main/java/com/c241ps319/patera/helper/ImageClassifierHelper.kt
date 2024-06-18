package com.c241ps319.patera.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.c241ps319.patera.R
import com.c241ps319.patera.ml.Detect
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier


class ImageClassifierHelper(
    private var threshold: Float = 0.1f,
    private var maxResults: Int = 3,
//    private val modelName: String = "cancer_classification.tflite",
    private val modelName: String = "detect.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?,
) {

//    private var imageClassifier: ImageClassifier? = null
    private var imageClassifier: Detect? = null

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(threshold)
            .setMaxResults(maxResults)
        val baseOptionsBuilder = BaseOptions.builder()
            .setNumThreads(4)
        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

        try {
//            imageClassifier = ImageClassifier.createFromFileAndOptions(
//                context,
//                modelName,
//                optionsBuilder.build()
//            )
            imageClassifier = Detect.newInstance(context)
        } catch (e: IllegalStateException) {
            classifierListener?.onError(context.getString(R.string.image_classifier_failed))
            Log.e(TAG, e.message.toString())
        }
    }

    fun classifyStaticImage(imageUri: Uri) {

        if (imageClassifier == null) {
            setupImageClassifier()
        }

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(320, 320, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(NormalizeOp(0.0f, 1.0f))  // Normalisasi jika diperlukan
//            .add(CastOp(DataType.UINT8))
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }.copy(Bitmap.Config.ARGB_8888, true)?.let { bitmap ->
//            val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))
            var tensorImage = TensorImage(DataType.FLOAT32)
            tensorImage.load(bitmap)
            tensorImage = imageProcessor.process(tensorImage)

            val byteBuffer = tensorImage.buffer
            val inputFeature = TensorBuffer.createFixedSize(intArrayOf(1, 320, 320, 3), DataType.FLOAT32)
            inputFeature.loadBuffer(byteBuffer)

//            val result = imageClassifier?.classify(tensorImage)
            val output = imageClassifier?.process(inputFeature)

            val outputFeature0 = output?.outputFeature0AsTensorBuffer
            val outputFeature1 = output?.outputFeature1AsTensorBuffer
            val outputFeature2 = output?.outputFeature2AsTensorBuffer
            val outputFeature3 = output?.outputFeature3AsTensorBuffer

            Log.d(TAG, outputFeature0?.floatArray?.toList().toString())
            Log.d(TAG, outputFeature1?.floatArray?.toList().toString())
            Log.d(TAG, outputFeature2?.floatArray?.toList().toString())
            Log.d(TAG, outputFeature3?.floatArray?.toList().toString())

            val result = processOutput(outputFeature0, outputFeature1, outputFeature2, outputFeature3)

            Log.d(TAG, result.toString())

            classifierListener?.onResults(result)
        }
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

//        iterate the first output and the fourth output at the same time
        outputs[0]?.let { output0 ->
            outputs[3]?.let { output3 ->
                val scores = output0.floatArray
                val labels = output3.floatArray
//              iterate scores and labels at the same time
                for (i in scores.indices) {
                    val score = scores[i]
                    val indexLabel = labels[i]
                    if (score > threshold) {
                        val (displayName, label) = getDiseaseAndLabelByIndex(indexLabel.toInt())
                        val category = Category.create(label, displayName, score, labels[i].toInt())
                        categories.add(category)
                    }
//                    val (displayName, label) = getDiseaseAndLabelByIndex(indexLabel.toInt())
//                    val category = Category.create(label, displayName, score, labels[i].toInt())
//                    categories.add(category)
                }
                categories.sortByDescending { it.score }
            }
        }

//        outputs.forEach { output ->
//            output?.let {
//                // Asumsikan output adalah daftar skor
//                val scores = it.floatArray
//                scores.forEachIndexed { index, score ->
//                    if (score > threshold) {
//                        categories.add(Category(index.toString(), score))
//                    }
//                }
//                categories.sortByDescending { it.score }
//            }
//        }
        return categories
    }

    fun getDiseaseAndLabelByIndex(index: Int): Pair<String, String> {
        val diseases = listOf(
            "Lettuce_Bacterial",
            "Lettuce_Fungal_Downy_Mildew",
            "Lettuce_Fungal_Powdery_Mildew",
            "Lettuce_Fungal_Septoria_Blight",
            "Spinach_Curling_Virus",
            "Spinach_Manganese_Deficiency",
            "Spinach_White_Rust",
            "Tomato_Bacterial_Spot",
            "Tomato_Early_Blight",
            "Tomato_Late_Blight",
            "Tomato_Leaf_Mold",
            "Tomato_Mosaic_Virus",
            "Tomato_Septoria_Leaf_Spot",
            "Tomato_Spider_Mites",
            "Tomato_Target_Spot",
            "Tomato_Yellow_Leaf_Curl_Virus"
        )

        return if (index in 0 until diseases.size) {
            val disease = diseases[index]
            val label = when {
                disease.startsWith("Lettuce") -> "Lettuce"
                disease.startsWith("Spinach") -> "Spinach"
                disease.startsWith("Tomato") -> "Tomato"
                else -> "Unknown" // Added for safety, shouldn't happen
            }
            Pair(disease, label)
        } else {
            Pair("Invalid index. Please enter a number between 0 and ${diseases.size - 1}.", "Unknown")
        }
    }



    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(
//            results: List<Classifications>?
            results: List<Category>?
        )
    }

    companion object {
        private const val TAG = "ImageClassifierHelper"
    }

}