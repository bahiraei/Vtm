package ir.adak.Vect

import android.content.SharedPreferences
import androidx.multidex.MultiDexApplication
import androidx.preference.PreferenceManager
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.firebase.messaging.FirebaseMessaging
import ir.adak.Vect.Data.Retrofit.Api
import ir.adak.Vect.Utils.Constants
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import com.koushikdutta.ion.Ion
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
//import com.facebook.flipper.plugins.inspector.DescriptorMapping
//import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
//import com.facebook.flipper.android.AndroidFlipperClient
//import com.facebook.flipper.android.utils.FlipperUtils
//import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
//import com.facebook.soloader.SoLoader


class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

//        SoLoader.init(this, false)
//
//        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
//            val client = AndroidFlipperClient.getInstance(this)
//            val descriptorMapping = DescriptorMapping.withDefaults()
//            client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
//            client.addPlugin(SharedPreferencesFlipperPlugin(this))
//            client.addPlugin(InspectorFlipperPlugin(this, descriptorMapping))
//            client.start()
//        }

        Fresco.initialize(this)
        Ion.getDefault(this).conscryptMiddleware.enable(false)


        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/irmob.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        subscribeFirebaseTopic(Constants.NOTIFICATION_TOPIC);
    }

    companion object {
        var sharedPreferences: SharedPreferences? = null
        private var api: Api? = null

    final    fun  getRetofitApi(): Api? {

            if (api == null) {

                val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY

                val client = OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .connectTimeout(Constants.CONNECT_TIME_OUT, TimeUnit.SECONDS)
                    .readTimeout(Constants.READ_TIME_OUT, TimeUnit.SECONDS)
                    .writeTimeout(Constants.WRITE_TIME_OUT, TimeUnit.SECONDS)
                    .build()
                api = Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
                    .create(Api::class.java)
            }

            return api
        }
        fun subscribeFirebaseTopic(topic : String){
            FirebaseMessaging.getInstance().subscribeToTopic(topic)
        }

        fun unsubscribeFirebaseTopic(topic : String){
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
        }
    }


}
