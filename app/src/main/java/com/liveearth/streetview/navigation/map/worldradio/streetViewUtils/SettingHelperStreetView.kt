package  com.liveearth.streetview.navigation.map.worldradio.streetViewUtils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.liveearth.streetview.navigation.map.worldradio.R
import java.util.*

class SettingHelperStreetView {
    companion object {

        fun rateUsApp(context: Context) {
            try {
                val intentRateApp = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(context.resources.getString(R.string.rate_app))
                )
                context.startActivity(intentRateApp)
            } catch (e: Exception) {
            }
        }

        fun shareUsApp(context: Context) {
            try {
                val shareAppIntent = Intent(Intent.ACTION_SEND)
                shareAppIntent.type = "text/plain"
                val shareSub = "Check out this application on play store!"
                val shareBody: String = context.resources.getString(R.string.share_app_link)
                shareAppIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub)
                shareAppIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
                context.startActivity(Intent.createChooser(shareAppIntent, "Share App using..."))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun moreOurApp(context: Context) {
            try {
                val intentRateApp = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(context.resources.getString(R.string.more_app))
                )
                context.startActivity(intentRateApp)
            } catch (e: Exception) {
            }
        }

        fun appPrivacyPolicy(context: Context) {
            try {
                val intentPrivacyApps =
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(context.resources.getString(R.string.privacy_policy_link))
                    )
                context.startActivity(intentPrivacyApps)
            } catch (e: Exception) {
            }
        }

        fun appFeedbackFun(context: Context) {
            try {
                val myIntentFeedBack = Intent(Intent.ACTION_SEND)
                myIntentFeedBack.type = "message/rfc822"
                myIntentFeedBack.setPackage("com.google.android.gm")
                myIntentFeedBack.putExtra(Intent.EXTRA_EMAIL,"thevissionstudio@gmail.com")
                myIntentFeedBack.putExtra(Intent.EXTRA_SUBJECT, "All Document Reader")
                context.startActivity(myIntentFeedBack)
            } catch (e: Exception) {
            }
        }
/*

        fun setUpLanguage(context: Context){
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(AppConstants.sharedPrefName, Context.MODE_PRIVATE)
            val languageSelected = sharedPreferences.getInt(AppConstants.SELECT_LANGUAGE,0)

            Log.d("setUpLanguage22", "setUpLanguage: $languageSelected")
            when(languageSelected){
                0->{
                    setAppLocale(context,"en")
                }
                1->{
                    setAppLocale(context,"sq")
                }
                2->{
                    setAppLocale(context,"es")
                }
                3->{
                    setAppLocale(context,"fr")
                }
                4->{
                    setAppLocale(context,"hi")
                }
                5->{
                    setAppLocale(context,"no")
                }
                6->{
                    setAppLocale(context,"nl")
                }
                7->{
                    setAppLocale(context,"ar")
                }
                8->{
                    setAppLocale(context,"pt")
                }
                9->{
                    setAppLocale(context,"de")
                }
            }

        }
*/

        fun setAppLocale(context: Context, language: String) {
            val locale = Locale(language)
            Locale.setDefault(locale)
            val config = context.resources.configuration
            config.setLocale(locale)
            context.createConfigurationContext(config)
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
        }
    }
}