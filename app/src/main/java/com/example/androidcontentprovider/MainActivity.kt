package com.example.androidcontentprovider

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidcontacts.database.service.RecyclerAdapter
import com.example.androidcontentprovider.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private var recyclerAdapter: RecyclerAdapter? = null
    private var contactList: MutableList<Contact>? = null
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, MainFragment())
            .commit()
//        binding.recyclerRV.layoutManager = LinearLayoutManager(this)
//        binding.recyclerRV.adapter = recyclerAdapter
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.READ_CONTACTS
//            ) !=
//            PackageManager.PERMISSION_GRANTED
//        ) {
//            permissionContact.launch(Manifest.permission.READ_CONTACTS)
////            binding.recyclerRV.adapter = recyclerAdapter
//            recyclerAdapter?.notifyDataSetChanged()
//        } else {
//            getContact()
////            binding.recyclerRV.adapter = recyclerAdapter
////            recyclerAdapter?.notifyDataSetChanged()
//        }
    }
//    @SuppressLint("Range")
//    private fun getContact() {
//        contactList = ArrayList()
//        val phones = contentResolver.query(
//            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//            null,
//            null,
//            null,
//            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME// + "ASC"
//        )
//        while (phones!!.moveToNext()) {
//            val name =
//                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
//            val phoneNum =
//                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
//            val contact = Contact(name, phoneNum)
//            contactList?.add(contact)
//        }
//        phones.close()
//        recyclerAdapter = RecyclerAdapter(this, this, contactList!!)
//        binding.recyclerRV.adapter = recyclerAdapter
//    }
//    private fun callNum(number: String?) {
//        val intent = Intent(Intent.ACTION_CALL)
//        intent.data = Uri.parse("tel:$number")
//        startActivity(intent)
//    }
//
//    private val permissionContact = registerForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { isGranted ->
//        if (isGranted) {
//            Toast.makeText(
//                this,
//                "Получен доступ к контактам",
//                Toast.LENGTH_SHORT
//            ).show()
//            getContact()
//        } else {
//            Toast.makeText(
//                this,
//                "В разрешении отказано",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }
//    private val permissionOfCall = registerForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { isGranted ->
//        if (isGranted) {
//            Toast.makeText(
//                this,
//                "Получен доступ к звонку",
//                Toast.LENGTH_SHORT
//            ).show()
//        } else {
//            Toast.makeText(
//                this,
//                "В разрешении отказано",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }
//
//    override fun onItemClickedCall(contact: Contact) {
//        val numPhone = contact.numPhone
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)!=
//            PackageManager.PERMISSION_GRANTED
//        ){
//            permissionOfCall.launch(Manifest.permission.CALL_PHONE)
//        }else{
//            callNum(numPhone)
//        }
//    }
//
//    override fun onItemClickedSend(contact: Contact) {
//        TODO("Not yet implemented")
//    }
}