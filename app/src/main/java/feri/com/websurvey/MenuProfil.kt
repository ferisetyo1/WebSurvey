package feri.com.websurvey

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.fragment_menu_profil.view.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MenuProfil : Fragment() {

    private var mAuth: FirebaseAuth? = null
    private var database: FirebaseDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v=inflater.inflate(R.layout.fragment_menu_profil,container,false)

        mAuth = FirebaseAuth.getInstance()
        database= FirebaseDatabase.getInstance()

        val user=mAuth?.currentUser
        val dbUser=database?.getReference("user")?.child(user?.uid.toString())
        //updateUI
        user?.apply {
            v.profil_details.visibility=View.VISIBLE
            v.btn_logout.text="Logout"
            dbUser?.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    Log.w("failed", "Failed to read value.", p0.toException())
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val userModel=p0.getValue(UserModel::class.java)
                    v.nama_user.text=userModel?.fullname
                    v.email.text=userModel?.email
                    v.role.text=userModel?.role
                    v.lastlogin.text=userModel?.lastlogin
                }
            })
        }

        v.btn_logout.setOnClickListener(View.OnClickListener {
            if (v.btn_logout.text.equals("Logout")){
                mAuth?.signOut();
                startActivity(Intent(context, LoginActivity::class.java))
                (context as MainActivity).finish()
            }else{
                startActivity(Intent(context, LoginActivity::class.java))
            }
        })

        return v
    }

}
