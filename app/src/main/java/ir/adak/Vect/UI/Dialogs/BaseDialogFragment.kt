package ir.adak.Vect.UI.Dialogs

import android.util.Log
import androidx.fragment.app.DialogFragment
import ir.adak.Vect.Data.Retrofit.StandardServerResponse
import ir.adak.Vect.R
import org.json.JSONObject
import retrofit2.Response

open class BaseDialogFragment : DialogFragment() {

    fun <T> isResponseValid(response: Response<StandardServerResponse<T>>): Int {

        try {
            if (!response.isSuccessful) {

                if (response.code() == 500) {
                    val errorBody = response.errorBody()?.string()
                    val jsonObject = JSONObject(errorBody?.trim() ?: "")
                    val statusCode = jsonObject.getInt("statusCode")
                    if (statusCode == 2) {
                        val error = jsonObject.getString("Message")

                        showAlertDialog(
                            R.drawable.ic_close_black_24dp,
                            "خطا",
                            error,
                            object : MyAlertDialog.OnButtonClickedListener {
                                override fun OnButtonClicked() {

                                }
                            }

                        )
                        return 0
                    }

                }
                if (response.code() == 401) {

                    return 1

                }

                showAlertDialog(
                    R.drawable.ic_close_black_24dp,
                    "خطا",
                    "خطایی رخ داده است؛ لطفا بعدا تلاش نمایید.",
                    object : MyAlertDialog.OnButtonClickedListener {
                        override fun OnButtonClicked() {

                        }
                    }

                )

                return 0

            }

        } catch (e: java.lang.Exception) {
            Log.i("MyTagg", e.message ?: "")
            showAlertDialog(
                R.drawable.ic_close_black_24dp,
                "خطا",
                "خطایی رخ داده است؛ لطفا بعدا تلاش نمایید.",
                object : MyAlertDialog.OnButtonClickedListener {
                    override fun OnButtonClicked() {

                    }
                }

            )

            return 0
        }

        val myResponse = response.body() ?: return 0

        if (!myResponse.isSuccess) {
            if (myResponse.message != "Not Fount")
                showAlertDialog(
                    R.drawable.ic_close_black_24dp,
                    "خطا",
                    myResponse.message ?: "خطایی رخ داده است لطفا بعدا تلاش نمایید.",
                    object : MyAlertDialog.OnButtonClickedListener {
                        override fun OnButtonClicked() {

                        }
                    }

                )

            return 0
        }

        if (myResponse.data == null) {

            return 0
        }

        return 2
    }


    fun showAlertDialog(
        icon: Int,
        title: String,
        text: String,
        onButtonClickedListener: MyAlertDialog.OnButtonClickedListener
    ) {
        val alertDialog =
            MyAlertDialog(icon, title, text, onButtonClickedListener)
        alertDialog.show(childFragmentManager, "")
    }


}