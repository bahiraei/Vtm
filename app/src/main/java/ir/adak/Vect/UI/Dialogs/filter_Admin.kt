package ir.adak.Vect.UI.Dialogs

import android.annotation.SuppressLint
import android.os.Bundle
import ir.adak.Vect.R
import android.view.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.filter_tasks_bottom_sheet_2.*


class filter_Admin(var Array:ArrayList<Data_Admin>) : BottomSheetDialogFragment()  {
    var adapter:Adapter_Admin ?=null
    var ff:data_7 ?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL,R.style.SheetDialog)
    }



    fun Clicl(gg:data_7)
    {
        ff=gg
    }




    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var vv= inflater.inflate(R.layout.filter_tasks_bottom_sheet_2, container, false)
        return  vv
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter= Adapter_Admin(context!!,Array)
        recy_admin.adapter=adapter

        adapter?.click(object  :Adapter_Admin.DATA_2{
            override fun data_3(S: String) {
                if (S.equals("A"))
                {
                    dismiss()
                    ff?.DATA("A")
                }else if (S.equals("B")){
                    dismiss()
                    ff?.DATA("B")
                }
            }

        })


    }


    interface  data_7{
        fun DATA (S:String)
    }



}