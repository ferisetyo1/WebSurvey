package feri.com.websurvey

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_menu_list_komoditas.*
import kotlinx.android.synthetic.main.fragment_menu_list_komoditas.view.*

class MenuListKomoditas : Fragment() {

    private var mAuth: FirebaseAuth? = null
    private var rv_option: FirebaseRecyclerOptions<KomoditasModel>? = null
    private var rv_adapter: FirebaseRecyclerAdapter<KomoditasModel, KomoditasViewHolder>? = null
    private var rv_komoditas: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_menu_list_komoditas, container, false)
        rv_komoditas = v.rv_komoditas

        // Init auth
        mAuth = FirebaseAuth.getInstance();

        // Init user
        val currentUser = mAuth?.getCurrentUser()
        val dbUser =
            FirebaseDatabase.getInstance().getReference("user").child(currentUser?.uid.toString())
        // update ui
        currentUser?.apply {
            dbUser.child("role").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.w("failed", "Failed to read value.", p0.toException());
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val role = p0.getValue(String::class.java)
                    if (role.equals("surveyor")) {
                        v.fab.show()
                    }
                }
            })
        }

        v.fab.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, CreateActivity::class.java))
        })

        search("")

        v.searchView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                search(p0.toString())
            }

        })
        return v
    }

    private fun search(p0: String?) {
        val query = FirebaseDatabase.getInstance()
            .getReference("komoditas")
            .orderByChild("nama")
            .startAt(p0)
            .endAt(p0 + "\uf8ff");

        rv_option = FirebaseRecyclerOptions.Builder<KomoditasModel>()
            .setQuery(query, KomoditasModel::class.java!!)
            .build()

        rv_adapter =
            object : FirebaseRecyclerAdapter<KomoditasModel, KomoditasViewHolder>(rv_option!!) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): KomoditasViewHolder {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_komoditas, parent, false)

                    return KomoditasViewHolder(view)
                }

                override fun onBindViewHolder(
                    holder: KomoditasViewHolder,
                    position: Int,
                    model: KomoditasModel
                ) {
                    holder.bindViewHolder(model)
                    holder.btn_var?.setOnClickListener(View.OnClickListener {
                        FirebaseDatabase.getInstance().getReference("komoditas").child(model?.id.toString())
                            .child("status").setValue(1)
                        notifyDataSetChanged()
                    })
                }

            };
        rv_adapter?.startListening()
        rv_komoditas?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = rv_adapter
        }
    }

    override fun onDetach() {
        super.onDetach()
        rv_adapter?.stopListening()
    }
}
