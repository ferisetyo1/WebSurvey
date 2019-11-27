package feri.com.websurvey

import androidx.annotation.Keep
import com.google.firebase.database.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
class KomoditasModel (
    var id:String?=null,
    var nama:String?=null,
    var harga:Long?=0,
    var tanggal:String?=null,
    var status:Int=0,
    var uid:String?=null
)