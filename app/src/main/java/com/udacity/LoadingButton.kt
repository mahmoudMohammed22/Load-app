package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0



    private var valueAnimator = ValueAnimator()
    private var anmaitor = 0f


    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->


    }


    init {
        isClickable = true
        valueAnimator = ValueAnimator.ofFloat(0f,100f)
        valueAnimator.duration = 2000
        valueAnimator.interpolator = AccelerateInterpolator()
        valueAnimator.addUpdateListener {
            anmaitor = (it.animatedValue as Float)
            invalidate()
            if(anmaitor ==100f){
                onFiniched()
            }
        }


    }

    private fun onFiniched() {
        valueAnimator.cancel()
        buttonState = ButtonState.Completed
        invalidate()
    }





    private val paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        strokeWidth = 2F
        textSize = resources.getDimension(R.dimen.default_text_size)
        textAlign = Paint.Align.CENTER

    }

    override fun performClick(): Boolean {
        super.performClick()
        buttonState  = ButtonState.Loading
        valueAnimator.start()
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        addView(canvas)
    }


    private fun addView(canvas: Canvas){


        paint.strokeWidth = 0f
        paint.color = Color.GRAY
        canvas.drawRect(0f,0f,width.toFloat(),height.toFloat(),paint)
        if(buttonState == ButtonState.Loading){
            paint.color = resources.getColor(R.color.backColor)
            canvas.drawRect(
                0f,
                0f,
                (widthSize * (anmaitor / 100)).toFloat(),
                heightSize.toFloat(),
                paint
            )
            val rect = RectF(
                width*0.70f,height*.3f,width*.80f,height*0.6f
            )
            paint.color = Color.YELLOW
            canvas.drawArc(rect, 0f, (360 * anmaitor / 100).toFloat(), true, paint)

        }
        val textDisplay = if (buttonState == ButtonState.Loading){
            context.getString(R.string.button_loading)
        }else {
            context.getString(R.string.button_name)
        }

        paint.color = Color.WHITE

        canvas.drawText(textDisplay,widthSize.toFloat()/2,(heightSize.toFloat()+    30)/2,paint)


    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}