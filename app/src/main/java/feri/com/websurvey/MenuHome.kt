package feri.com.websurvey


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_menu_home.view.*

/**
 * A simple [Fragment] subclass.
 */
class MenuHome : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v=inflater.inflate(R.layout.fragment_menu_home, container, false)

        var query= FirebaseDatabase.getInstance()
            .getReference("komoditas")
            .orderByChild("status")
            .equalTo("1".toDoubleOrNull()!!)
        query.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                v.jml_komoditas.text=p0.childrenCount.toString()
            }

        })

        query= FirebaseDatabase.getInstance()
            .getReference("user")
            .orderByChild("role")
            .equalTo("surveyor")

        query.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                v.jml_surveyor.text=p0.childrenCount.toString()
            }

        })
        return v
    }


}
