package com.lulu.surfaceviewdemo

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

/**
 * @author zhanglulu on 2020/6/15.
 * for
 */
private const val TAG = "TextSurfaceView"
class TextSurfaceView(context: Context): SurfaceView(context), SurfaceHolder.Callback, Runnable {

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
//        var lockCanvas = holder.lockCanvas()
//        lockCanvas.drawColor(Color.WHITE)
//        holder.unlockCanvasAndPost(lockCanvas)
        while (mIsDrawing) {
            draw()
        }
    }

    //----------------------------------------------------------------------------------------------
    // 绘制 start

    public var text = ""
    private var curX = 0f
    private var curY = 0f
    private var textPaint = TextPaint()
    private var cW = 0F
    private var cY = 0F
    private var curIndex = 0
    private var isFirst = true
    private var dragY = 0F//拖拽距离
    private val fontMetrics: Paint.FontMetrics

    init {
        textPaint.color = Color.RED
        //paint.style = Paint.Style.STROKE
        textPaint.textSize = 50F
        textPaint.textAlign = Paint.Align.LEFT//x 就是文字最左侧到当前 view 左边距的距离
        //paint.strokeWidth = 30F
        fontMetrics = textPaint.fontMetrics
        curY = -fontMetrics.top
        cY = fontMetrics.bottom - fontMetrics.top
        cW = textPaint.measureText("中")
    }
    private fun draw() {
        try {
            val widthMaxNum = (width/cW).toInt()
            mCanvas = holder.lockCanvas()
            mCanvas?.let {
                if (isFirst) {
                    it.drawColor(Color.WHITE)
                    isFirst = false
                    return
                }

                //画个背景
                it.drawColor(Color.WHITE)
                while (curIndex < text.length) {
                    val endIndex =  if (curIndex+widthMaxNum >= text.length) {
                        text.length
                    } else{
                        curIndex+widthMaxNum
                    }
                    val subText = text.subSequence(curIndex, endIndex).toString()
                    it.drawText(subText, curX, curY, textPaint)
                    curIndex += widthMaxNum
                    curX = 0F
                    curY += cY + 10
//                    if (curIndex >= text.length) {
//                        mIsDrawing = false
//                    }
                    //Log.d(TAG, "draw: $curX $curY width:$width curIndex:$curIndex text.length:${text.length}")
                }
                curY = -fontMetrics.top + dragY
                curX = 0F
                curIndex = 0
                Log.d(TAG, "draw: dragY: $dragY")
                //Thread.sleep(15)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            //提交画布
            mCanvas?.let {
                holder.unlockCanvasAndPost(mCanvas)
            }
        }

    }
    private var dragDownY = 0F
    private var downY = 0F
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                dragDownY = event.y - dragY
                downY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                dragY = event.y - dragDownY
            }
            MotionEvent.ACTION_UP -> {
                if (event.y - downY > 0) {
                    //正在向上滑动
                    flingAnim(-100F)
                } else {
                    //向下滑动
                    flingAnim(100F)
                }
            }
            else -> {
            }
        }
        return true
    }

    private fun flingAnim(endValue: Float) {
        val ofFloat = ValueAnimator.ofFloat(0F, endValue)
        ofFloat.duration = 100
        ofFloat.addUpdateListener {
            dragY -= it.animatedValue as Float
        }
        ofFloat.start()
    }
}