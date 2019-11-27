package feri.com.websurvey

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create.*
import java.text.ParseException
import java.text.SimpleDateFormat


class CreateActivity : AppCompatActivity(), View.OnClickListener {

    private var mAuth: FirebaseAuth? = null
    private var database: FirebaseDatabase? = null
    private var id: String? = null

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_back -> {
                finish()
            }
            R.id.btn_tbh -> {
                create()
            }
        }
    }

    private fun create() {
        if (et_nama.text.isNullOrEmpty()){
            et_nama.error="please fill the blank"
            return
        }
        if (et_harga.text.isNullOrEmpty()){
            et_harga.error="please fill the blank"
            return
        }
        if (et_tanggal.text.isNullOrEmpty()){
            et_tanggal.error="please fill the blank"
            return
        }

        if (!isDateValid(et_tanggal.text.toString().trim())){
            et_tanggal.error="tanggal tidak valid"
            return
        }

        var komoditasModel=KomoditasModel(
            id,
            et_nama.text.toString(),
            et_harga.text.toString().toLongOrNull(),
            et_tanggal.text.toString(),
            0,
            mAuth?.currentUser?.uid
        )
        database?.getReference("komoditas")?.child(id.toString())?.setValue(komoditasModel)
            ?.addOnSuccessListener {
                Toast.makeText(this,"success tambah",Toast.LENGTH_LONG).show()
                finish()
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        supportActionBar?.hide()
        val STRING_CHARACTERS = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        id = "komoditas_" + (1..6).map { STRING_CHARACTERS.random() }.joinToString("")
        mAuth = FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance()
        btn_back.setOnClickListener(this)
        btn_tbh.setOnClickListener(this)
    }

    fun isDateValid(dateStr: String): Boolean {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        sdf.setLenient(false)
        try {
            sdf.parse(dateStr)
        } catch (e: ParseException) {
            return false
        }

        return true
    }
}
