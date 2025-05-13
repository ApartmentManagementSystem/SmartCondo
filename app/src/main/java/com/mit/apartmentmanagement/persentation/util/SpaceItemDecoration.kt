package com.mit.apartmentmanagement.persentation.util

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(
    context: Context,
    spaceH: Int,
    spaceV: Int,
    private val spanCount: Int
) : RecyclerView.ItemDecoration() {

    private val spaceHorizontal: Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        spaceH.toFloat(),
        context.resources.displayMetrics
    ).toInt()

    private val spaceVertical: Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        spaceV.toFloat(),
        context.resources.displayMetrics
    ).toInt()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        outRect.left = spaceHorizontal * column / spanCount
        outRect.right = spaceHorizontal * (spanCount - 1 - column) / spanCount

        if (position >= spanCount) {
            outRect.top = spaceVertical
        }
    }
}