package com.pss.presentation.widget.utils

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object Utils {
    //@Throws(IOException::class)
    fun assetFilePath(context: Context, assetName: String): String? {
        val file = File(context.filesDir, assetName)

        try {
            context.assets.open(assetName).use { `is` ->
                FileOutputStream(file).use { os ->
                    val buffer = ByteArray(4 * 1024)
                    while (true) {
                        val length = `is`.read(buffer)
                        if (length <= 0)
                            break
                        os.write(buffer, 0, length)
                    }
                    os.flush()
                    os.close()
                }
                return file.absolutePath
            }
        } catch (e: IOException) {
            Log.e("pytorchandroid", "Error process asset $assetName to file path")
        }

        return null
    }

    fun outputResultComparison(outputs: FloatArray): Int {
        var maxScore = -Float.MAX_VALUE
        var maxScoreIdx = -1
        for (i in outputs.indices) {
            if (outputs[i] > maxScore) {
                maxScore = outputs[i]
                maxScoreIdx = i
            }
        }
        return maxScoreIdx
    }
}
