package ir.adak.Vect.Utils

import android.graphics.Rect
import android.view.View

import androidx.recyclerview.widget.RecyclerView

class RecyclerViewOverlapDecoration(
    private val left: Int,
    private val top: Int,
    private val right: Int,
    private val bottom: Int
) : RecyclerView.ItemDecoration() {


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemPosition = parent.getChildAdapterPosition(view)
        outRect.set(left, top, right, bottom)
    }
}