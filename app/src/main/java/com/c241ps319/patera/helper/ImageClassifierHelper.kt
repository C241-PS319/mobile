package com.c241ps319.patera.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.c241ps319.patera.ml.Detect
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.InputStream
import java.nio.ByteBuffer

class ImageClassifierHelper(val context: Context) {
    private val model = Detect.newInstance(context)

    fun createInputBuffer(byteBuffer: ByteBuffer): TensorBuffer {
        val inputFeature =
            TensorBuffer.createFixedSize(intArrayOf(1, 320, 320, 3), DataType.FLOAT32)
        inputFeature.loadBuffer(byteBuffer)
        return inputFeature
    }

    fun processModel(inputBuffer: TensorBuffer): List<TensorBuffer> {
        val outputs = model.process(inputBuffer)
        return listOf(
            outputs.outputFeature0AsTensorBuffer,
            outputs.outputFeature1AsTensorBuffer,
            outputs.outputFeature2AsTensorBuffer,
            outputs.outputFeature3AsTensorBuffer
        )
    }

    fun uriToByteBuffer(uri: Uri, width: Int, height: Int): ByteBuffer {
        // Load bitmap from URI
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)

        // Convert bitmap to ByteBuffer
        val byteBuffer =
            ByteBuffer.allocateDirect(4 * width * height * 3) // 4 bytes per float, 3 channels (RGB)
        resizedBitmap.copyPixelsToBuffer(byteBuffer)
        byteBuffer.rewind() // Reset buffer position

        return byteBuffer
    }

    fun processImageUri(uri: Uri): List<TensorBuffer> {
        val inputWidth = 320
        val inputHeight = 320
        val byteBuffer = uriToByteBuffer(uri, inputWidth, inputHeight)
        val inputBuffer = createInputBuffer(byteBuffer)
        return processModel(inputBuffer)
    }


    fun close() {
        model.close()
    }


    companion object {
        private val TAG = ImageClassifierHelper::class.java.simpleName
    }

}