package ir.adak.Vect.UI.Activities.NewMainActivity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import ir.adak.Vect.Data.Models.FilterProjectModel
import ir.adak.Vect.Data.Models.ProjectStep
import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.AddProjectActivity.AddProjectActivity
import ir.adak.Vect.UI.Activities.BackLogActivity.BackLogActivity
import ir.adak.Vect.UI.Activities.BaseActivity
import ir.adak.Vect.UI.Activities.MainActivity.MainActivity
import ir.adak.Vect.UI.Activities.UsersActivity.UsersActivity
import ir.adak.Vect.Utils.Constants
import kotlinx.android.synthetic.main.activity_main__new.*
import kotlinx.android.synthetic.main.new_main_activity.*
import pl.looksoft.shadowlib.ShadowLayout


class NewMainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            val window = window
//            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
//        }

        setContentView(R.layout.new_main_activity)

        txt_username_new.text = userInfo?.fullName
        txt_semat_new.text = userInfo?.orgLevelTitle

        if (!userInfo?.profileImage.isNullOrEmpty()) {
            Log.i("dfmlsvadv",Constants.AVATAR_BASE_URL + userInfo?.profileImage)
            Glide.with(this).load(Constants.AVATAR_BASE_URL + userInfo?.profileImage)
                .into(roundedImageView)
        }


        btn_add_project_new.setOnClickListener {
            startActivity(Intent(this, AddProjectActivity::class.java))
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
        }

        btn_users_new.setOnClickListener {
            startActivity(Intent(this, UsersActivity::class.java))
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
        }
        lyt_backlog_new.setOnClickListener {
            val filterProjectModel = FilterProjectModel()
            filterProjectModel.step = ProjectStep.BackLog.value
            startActivity(Intent(this, BackLogActivity::class.java).apply {
                putExtra("selectedFilterProjectModel", filterProjectModel)
            })
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
        }
        lyt_todo_new.setOnClickListener {
            val filterProjectModel = FilterProjectModel()
            filterProjectModel.step = ProjectStep.ToDo.value
            startActivity(Intent(this, MainActivity::class.java).apply {
                putExtra("selectedFilterProjectModel", filterProjectModel)
            })
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
        }
        lyt_doing_new.setOnClickListener {
            val filterProjectModel = FilterProjectModel()
            filterProjectModel.step = ProjectStep.Doing.value
            startActivity(Intent(this, MainActivity::class.java).apply {
                putExtra("selectedFilterProjectModel", filterProjectModel)
            })
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
        }
        lyt_done_new.setOnClickListener {
            val filterProjectModel = FilterProjectModel()
            filterProjectModel.step = ProjectStep.Done.value
            startActivity(Intent(this, MainActivity::class.java).apply {
                putExtra("selectedFilterProjectModel", filterProjectModel)
            })
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
        }
        btn_expand_new.setOnClickListener {
            if (btn_add_project_new.visibility == View.VISIBLE) {
                collapseMenu()
            } else {
                expandMenu()
            }
        }

        tab_layout.getTabAt(0)?.setCustomView(R.layout.bottombar_chat_view)
        tab_layout.getTabAt(1)?.setCustomView(R.layout.bottombar_graph_view)
        tab_layout.getTabAt(2)?.setCustomView(R.layout.bottombar_chart_view)
        tab_layout.getTabAt(3)?.setCustomView(R.layout.bottombar_home_view)
        tab_layout.getTabAt(3)?.select()
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        tab.customView?.findViewById<ImageView>(R.id.img_chat)
                            ?.setImageResource(R.drawable.ic_unselected_chat)
                        tab.customView?.findViewById<ShadowLayout>(R.id.chat_shadow)?.shadowColor =
                            Color.TRANSPARENT

                    }
                    1 -> {
                        tab.customView?.findViewById<ImageView>(R.id.img_graph)
                            ?.setImageResource(R.drawable.ic_unselected_graph)
                        tab.customView?.findViewById<ShadowLayout>(R.id.graph_shadow)?.shadowColor =
                            Color.TRANSPARENT

                    }
                    2 -> {
                        tab.customView?.findViewById<ImageView>(R.id.img_chart)
                            ?.setImageResource(R.drawable.ic_unselected_chart)
                        tab.customView?.findViewById<ShadowLayout>(R.id.chart_shadow)?.shadowColor =
                            Color.TRANSPARENT

                    }
                    3 -> {
                        tab.customView?.findViewById<ImageView>(R.id.img_home)
                            ?.setImageResource(R.drawable.ic_unselected_home)
                        tab.customView?.findViewById<ShadowLayout>(R.id.home_shadow)?.shadowColor =
                            Color.TRANSPARENT
                    }
                }
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        tab.customView?.findViewById<ImageView>(R.id.img_chat)
                            ?.setImageResource(R.drawable.ic_selected_chat)
                        tab.customView?.findViewById<ShadowLayout>(R.id.chat_shadow)?.shadowColor =
                            Color.parseColor("#400068FF")

                    }
                    1 -> {
                        tab.customView?.findViewById<ImageView>(R.id.img_graph)
                            ?.setImageResource(R.drawable.ic_selected_graph)
                        tab.customView?.findViewById<ShadowLayout>(R.id.graph_shadow)?.shadowColor =
                            Color.parseColor("#400068FF")

                    }
                    2 -> {
                        tab.customView?.findViewById<ImageView>(R.id.img_chart)
                            ?.setImageResource(R.drawable.ic_selected_chart)
                        tab.customView?.findViewById<ShadowLayout>(R.id.chart_shadow)?.shadowColor =
                            Color.parseColor("#400068FF")

                    }
                    3 -> {
                        tab.customView?.findViewById<ImageView>(R.id.img_home)
                            ?.setImageResource(R.drawable.ic_selected_home)
                        tab.customView?.findViewById<ShadowLayout>(R.id.home_shadow)?.shadowColor =
                            Color.parseColor("#400068FF")
                    }
                }
            }
        })
    }

    fun expandMenu() {
        TransitionManager.beginDelayedTransition(constraint_slide_menu)
        btn_users_new.visibility = View.VISIBLE
        btn_add_project_new.visibility = View.VISIBLE
        btn_expand_new.rotation = 90f
    }

    fun collapseMenu() {
        TransitionManager.beginDelayedTransition(constraint_slide_menu)
        btn_users_new.visibility = View.GONE
        btn_add_project_new.visibility = View.GONE
        btn_expand_new.rotation = -90f
    }
}
