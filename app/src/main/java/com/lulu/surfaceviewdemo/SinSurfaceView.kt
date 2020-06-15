package com.lulu.surfaceviewdemo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.lang.Exception
import kotlin.math.sin

/**
 * @author zhanglulu on 2020/6/15.
 * for
 */
private const val TAG = "TestSurfaceView"
class SinSurfaceView(context: Context): SurfaceView(context), SurfaceHolder.Callback, Runnable {

    private var mCanvas: Canvas? = null
    private var mIsDrawing = false

    init {
        holder.addCallback(this)
        isFocusable = true
        isFocusableInTouchMode = true
        keepScreenOn = true
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        mIsDrawing = true
        Thread(this).start()
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        mIsDrawing = false
    }

    override fun run() {
        while (mIsDrawing) {
            draw()
        }
    }

    //----------------------------------------------------------------------------------------------
    // 绘制 start

    private var curX = 0f
    private var curY = 0f
    private var path = Path()
    private var paint = Paint()
    init {
        paint.color = Color.RED
        path.moveTo(0F, 0F)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 30F
    }
    private fun draw() {
        try {
            mCanvas = holder.lockCanvas()
            mCanvas?.let {
                //画个背景
                it.drawColor(Color.WHITE)
                it.drawPath(path, paint)
            }
            curX += 10
            curY = ((100* sin(curX * 2 * Math.PI/180.0) + 400).toFloat())
            if (curX > width) {
                mIsDrawing = false
            }
            Log.d(TAG, "draw: $curX $curY $width ")
            path.lineTo(curX, curY)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            //提交画布
            mCanvas?.let {
                holder.unlockCanvasAndPost(mCanvas)
            }
        }

    }
}