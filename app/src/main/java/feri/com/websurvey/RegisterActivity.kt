package feri.com.websurvey

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*
import java.text.SimpleDateFormat
import java.util.*


class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private var mAuth: FirebaseAuth? = null
    private var database: FirebaseDatabase? = null

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.link_login -> {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            R.id.link_dashboard -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            R.id.btn_login -> {
                register()
            }
        }
    }

    private fun register() {
        mAuth?.createUserWithEmailAndPassword(
            et_email.text.toString().trim(),
            et_password.text.toString().trim()
        )
            ?.addOnCompleteListener(OnCompleteListener {
                if (it.isSuccessful) {
                    val curruser = mAuth?.currentUser
                    val sdf = SimpleDateFormat("dd/M/yyyy")
                    val currentDate = sdf.format(Date())
                    val user = UserModel(
                        curruser?.uid.toString(),
                        et_namauser.text.toString(),
                        et_email.text.toString(),
                        "surveyor",
                        currentDate
                    )
                    database?.getReference("user")
                        ?.child(curruser?.uid.toString())
                        ?.setValue(user)?.addOnSuccessListener {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }?.addOnFailureListener {
                            Log.w("failed","error tambah data",it.cause)
                        }
                } else {
                    Log.w("auth failed", "signInWithCustomToken:failure", it.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        link_login.setOnClickListener(this)
        link_dashboard.setOnClickListener(this)
        btn_login.setOnClickListener(this)
    }
}
