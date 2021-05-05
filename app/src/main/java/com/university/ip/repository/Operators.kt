package com.university.ip.repository

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc


class Operators {

    fun increaseBrightness(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        src.convertTo(src, -1, 1.0, value.toDouble())
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun increaseContrast(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        src.convertTo(src, -1, value.toDouble() / 255, 0.0)
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun grayScale(bitmap: Bitmap): Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)

        Utils.bitmapToMat(bitmap, src)

        Imgproc.cvtColor(src, src, Imgproc.COLOR_RGB2GRAY);

        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun sepia(bitmap: Bitmap): Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)

        val mSepiaKernel = Mat(4, 4, CvType.CV_32F)
        mSepiaKernel.put(0, 0,  /* R */0.189, 0.769, 0.393, 0.0)
        mSepiaKernel.put(1, 0,  /* G */0.168, 0.686, 0.349, 0.0)
        mSepiaKernel.put(2, 0,  /* B */0.131, 0.534, 0.272, 0.0)
        mSepiaKernel.put(3, 0,  /* A */0.000, 0.000, 0.000, 1.0)

        Core.transform(src, src, mSepiaKernel)

        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun binary(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)

        val threshold = value.toDouble();

        Imgproc.cvtColor(src, src, Imgproc.COLOR_RGB2GRAY);
        Imgproc.cvtColor(src, src, Imgproc.COLOR_GRAY2RGB, 4);
        Imgproc.threshold(
                src,
                src,
                threshold,
                255.0,
                0
        )

        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun medianBlur(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)

        var threshold = value / 2
        if (threshold % 2 == 0)
            threshold++;

        Imgproc.medianBlur(src, src, threshold);

        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun gaussianBlur(bitmap: Bitmap, value: Int, sigma: Int): Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)


        var threshold = value
        if (threshold % 2 == 0)
            threshold++;

        val size = Size()
        size.height = threshold.toDouble()
        size.width = threshold.toDouble()

        Imgproc.GaussianBlur(src, src, size, sigma.toDouble());

        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun Laplacian(bitmap: Bitmap): Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)

        val src_gray = Mat()
        val dst = Mat()
        Utils.bitmapToMat(bitmap, src)

        val ddepth = CvType.CV_16S
        val scale = 1.0
        val kernelSize = 3
        val delta = 0.0

        Imgproc.GaussianBlur(src, src, Size(3.0, 3.0), 0.0, 0.0, Core.BORDER_DEFAULT)
        Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_RGB2GRAY)
        Imgproc.Laplacian(src_gray, dst, ddepth, kernelSize, scale, delta, Core.BORDER_DEFAULT);

        val abs_dst = Mat()
        Core.convertScaleAbs(dst, abs_dst)


        val result = Bitmap.createBitmap(abs_dst.cols(), abs_dst.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(abs_dst, result)
        return result
    }

    fun unsharpMask(bitmap: Bitmap): Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        val dst = Mat(src.rows(), src.cols(), src.type())
        val res = Mat(src.rows(), src.cols(), src.type())
        Utils.bitmapToMat(bitmap, src)

        Imgproc.GaussianBlur(src, dst, Size(11.0, 11.0), 10.0)
        Core.addWeighted(src, 4.0, dst, -3.0, 0.0, res)
        val result = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(res, result)
        return result;
    }

    fun zoomIn(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)

        Imgproc.resize(src, src, Size((value + src.cols()).toDouble(), (value + src.rows()).toDouble()))
        val roi = Rect(0, 0, bitmap.width, bitmap.height)
        val cropped = Mat(src, roi)
        val result = Bitmap.createBitmap(cropped.cols(), cropped.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(cropped, result)
        return result
    }

    fun flip(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)

        if (value > 0)
            Core.flip(src, src, 1)
        else if (value == 0)
            Core.flip(src, src, 0)
        else if (value < 0)
            Core.flip(src, src, -1)

        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun rotate(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)

        Core.transpose(src, src);
        Core.flip(src, src, value);

        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun adaptiveBinary(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        var newValue = value
        while (!(newValue % 2 == 1 && newValue > 1))
            newValue++
        Imgproc.cvtColor(src, src, Imgproc.COLOR_RGB2GRAY)

        Imgproc.adaptiveThreshold(src, src, 255.0, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, newValue, 2.0)

        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun bilateralFilter(bitmap: Bitmap, val1: Int): Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)

        Imgproc.cvtColor(src, src, Imgproc.COLOR_BGRA2BGR);

        val dst = Mat(src.height(), src.width(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, dst)

        if (val1 % 2 == 0)
            Imgproc.bilateralFilter(src, dst, val1 + 3, 80.0, 80.0)
        else
            Imgproc.bilateralFilter(src, dst, val1 + 2, 80.0, 80.0)

        val result = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(dst, result)
        return result
    }

    fun rotateAtAnyAngle(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)

        val dsize = Size(src.rows().toDouble(), src.cols().toDouble())
        val center = Point((src.cols() / 2).toDouble(), (src.rows() / 2).toDouble())

        val matrix = Imgproc.getRotationMatrix2D(center, value.toDouble() * 1.411, 1.0)
        Imgproc.warpAffine(src, src, matrix, dsize, Imgproc.INTER_LINEAR)

        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }
}