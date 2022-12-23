package com.example.aston_courseproject_rickmorty.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerDecorator(private val sidePadding: Int = 10, private val topPadding: Int = 10) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = topPadding
        outRect.top = topPadding

        outRect.left = sidePadding
        outRect.right = sidePadding
    }
}