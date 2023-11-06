package ir.adak.Vect.Data.Models

import android.graphics.Color
import android.view.View
import com.bumptech.glide.Glide
import ir.adak.Vect.R
import ir.adak.Vect.Utils.Constants
import kotlinx.android.synthetic.main.group_item_layout.view.*

data class GroupItem(
    var fullName: String? = null,
    var orgLevelTitle: String? = null,
    var profileImage: String? = null,
    var orgLevelId: Int = -1,
    var isChild: Boolean = false,
    val type: Int,
    val access: Int,
    var selected: Boolean = false
) : com.xwray.groupie.kotlinandroidextensions.Item() {
    override fun bind(
        viewHolder: com.xwray.groupie.kotlinandroidextensions.ViewHolder,
        position: Int
    ) {
        if (!profileImage.isNullOrEmpty()) {
            Glide.with(viewHolder.itemView.context)
                .load(Constants.AVATAR_BASE_URL + profileImage)
                .into(viewHolder.itemView.img_avatar)
        }
        viewHolder.itemView.txt_users_name.text = fullName
        viewHolder.itemView.txt_semat.text = orgLevelTitle
        if (selected) {
            selected = true
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#F4F6FF"))
            viewHolder.itemView.selected_icon.visibility = View.VISIBLE
        } else {
            selected = false
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
            viewHolder.itemView.selected_icon.visibility = View.GONE
        }

    }

    override fun getLayout(): Int = R.layout.group_item_layout


}