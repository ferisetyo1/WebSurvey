package feri.com.websurvey

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        btn_login.setOnClickListener(this)
        link_login.setOnClickListener(this)
        link_dashboard.setOnClickListener(this)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.link_login -> {
                startActivity(Intent(this, RegisterActivity::class.java))
                finish()
            }
            R.id.link_dashboard -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            R.id.btn_login -> {
                login()
            }
        }
    }

    private fun login() {
        mAuth?.signInWithEmailAndPassword(
            et_email.text.toString().trim(),
            et_password.text.toString().trim()
        )
            ?.addOnCompleteListener(OnCompleteListener {
                if (it.isSuccessful) {
                    var currUser = mAuth?.currentUser
                    val sdf = SimpleDateFormat("dd/M/yyyy")
                    val currentDate = sdf.format(Date())
                    val setdata = FirebaseDatabase.getInstance()
                        .getReference("user")
                        ?.child(currUser?.uid.toString())
                        .child("lastlogin")
                        ?.setValue(currentDate)?.addOnSuccessListener {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }?.addOnFailureListener {
                            Log.w("failed","error update data",it.cause)
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


}
