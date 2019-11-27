package feri.com.websurvey

import androidx.annotation.Keep
import com.google.firebase.database.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
class UserModel(
    var id: String? = null,
    var fullname: String? = null,
    var email: String? = null,
    var role: String? = null,
    var lastlogin: String? = null
)