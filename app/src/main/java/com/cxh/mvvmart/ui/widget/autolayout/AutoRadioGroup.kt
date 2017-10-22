package com.cxh.mvvmart.ui.widget.autolayout

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.RadioGroup

import com.zhy.autolayout.AutoLayoutInfo
import com.zhy.autolayout.utils.AutoLayoutHelper

class AutoRadioGroup : RadioGroup {
    private val mHelper = AutoLayoutHelper(this)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (!isInEditMode)
            mHelper.adjustChildren()
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return LayoutParams(context, attrs)
    }

    class LayoutParams : RadioGroup.LayoutParams, AutoLayoutHelper.AutoLayoutParams {
        private lateinit var  mAutoLayoutInfo: AutoLayoutInfo

        constructor(c: Context, attrs: AttributeSet) : super(c, attrs) {
            mAutoLayoutInfo = AutoLayoutHelper.getAutoLayoutInfo(c, attrs)
        }

        override fun getAutoLayoutInfo(): AutoLayoutInfo {
            return mAutoLayoutInfo
        }

        constructor(width: Int, height: Int) : super(width, height)

        constructor(source: ViewGroup.LayoutParams) : super(source)

        constructor(source: ViewGroup.MarginLayoutParams) : super(source)

    }
}
