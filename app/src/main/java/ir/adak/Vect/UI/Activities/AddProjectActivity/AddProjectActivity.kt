package ir.adak.Vect.UI.Activities.AddProjectActivity

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import ir.adak.Vect.Data.Models.*
import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.AddUsersViewModel
import ir.adak.Vect.UI.Activities.BaseActivity
import ir.adak.Vect.UI.Fragments.AddNewTaskFragment
import kotlinx.android.synthetic.main.activity_add_project.*


class AddProjectActivity : BaseActivity() {
    var parentId: String? = null
    var meetingId: String? = null
    var project: Project? = null
    var position = -1
    var isEdit: Boolean = false

    lateinit var viewModel: AddUsersViewModel

    val addNewTaskFragment = AddNewTaskFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_project)
        viewModel = ViewModelProviders.of(this)
            .get(AddUsersViewModel::class.java)
        parentId = intent.extras?.getString("parentId")
        meetingId = intent.extras?.getString("meetingId")
//        project = intent.extras?.getSerializable("project") as Project?
        project = intent.extras?.getParcelable("project")
        Log.i("egwygifwas",project.toString())
        position = intent.extras?.getInt("position")?:-1
        isEdit = intent.extras?.getBoolean("isEdit") ?: false
        txt_new_project_title.text = if (isEdit) "ویرایش وظیفه" else "وظیفه جدید"
        val bundle = Bundle().apply {
            putString("parentId", parentId)
            putString("meetingId", meetingId)
            putParcelable("project", project)
            putInt("position", position)
            putBoolean("isEdit", isEdit)
        }
        addNewTaskFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(add_project_fragment_container.id, addNewTaskFragment).commit()
    }
}
