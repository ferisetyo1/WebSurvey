package feri.com.websurvey

import android.util.Log
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.item_komoditas.view.*

class KomoditasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var btn_var: Button?=null

    fun bindViewHolder(komoditasModel: KomoditasModel) {
        btn_var=itemView.btn_ver

        itemView.nama_komoditas.text = komoditasModel.nama
        val dbuser = FirebaseDatabase.getInstance()
            .getReference("user")
        dbuser?.child(komoditasModel.uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val userModel = p0.getValue(UserModel::class.java)
                    itemView.nama_user.text = userModel?.fullname
                }

            })
        itemView.harga.text = "Rp ${komoditasModel.harga}"
        itemView.tanggal.text = komoditasModel.tanggal
        when (komoditasModel.status) {
            0 -> {
                itemView.status.text = "unverified"
                itemView.btn_ver.visibility = View.GONE
                val currentUser = FirebaseAuth.getInstance().currentUser
                dbuser?.child(currentUser?.uid.toString()).child("role")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            val role = p0.getValue(String::class.java)
                            if (role.equals("admin")) {
                                itemView.btn_ver.visibility = View.VISIBLE
                            }
                        }
                    })
            }
            1 -> {
                itemView.status.text = "verified"
                itemView.btn_ver.visibility = View.GONE
            }
        }
    }
}