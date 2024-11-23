package com.example.androidcontentprovider

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentProviderOperation
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.provider.ContactsContract.CommonDataKinds.StructuredName
import android.provider.ContactsContract.RawContacts
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a16_3_ContentProvider.SendSmsFragment
import com.example.androidcontacts.database.service.RecyclerAdapter
import com.example.androidcontentprovider.databinding.FragmentMainBinding

class MainFragment : Fragment(), RecyclerAdapter.ContactClickListener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private var recyclerAdapter: RecyclerAdapter? = null
    private var contactList: ArrayList<Contact>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerRV.layoutManager = LinearLayoutManager(requireContext())
        binding.toolbar.inflateMenu(R.menu.main_menu)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.exit -> requireActivity().finishAffinity()
                R.id.search -> {
                    val bundle = Bundle()
                    val fragment = SearchFragment()
                    bundle.putParcelableArrayList(Contact::class.java.simpleName, contactList)
                    fragment.arguments = bundle
                    fragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.container, fragment)
                        ?.addToBackStack("")
                        ?.commit()
                }
            }
            true
        }
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            permissionContact.launch(Manifest.permission.READ_CONTACTS)
            recyclerAdapter?.notifyDataSetChanged()
        } else {
            getContact()
        }


        //Добавить контакт
        binding.addBTN.setOnClickListener {
            if (binding.nameET.text.isNotEmpty() &&
                binding.numPhoneET.text.isNotEmpty()
            ) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.WRITE_CONTACTS
                    ) !=
                    PackageManager.PERMISSION_GRANTED
                ) {
                    permissionWriteContact.launch(Manifest.permission.WRITE_CONTACTS)
                } else {
                    addContact()
                    recyclerAdapter!!.notifyDataSetChanged()
                    getContact()
                }
            }
        }
    }

    private fun addContact() {
        val newContactName = binding.nameET.text.toString()
        val newContactPhone = binding.numPhoneET.text.toString()
        val listCPO = ArrayList<ContentProviderOperation>()

        listCPO.add(
            ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
                .withValue(RawContacts.ACCOUNT_TYPE, null)
                .withValue(RawContacts.ACCOUNT_NAME, null)
                .build()
        )
        listCPO.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                .withValue(StructuredName.DISPLAY_NAME, newContactName)
                .build()
        )
        listCPO.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                .withValue(Phone.NUMBER, newContactPhone)
                .withValue(Phone.TYPE, Phone.TYPE_MOBILE)
                .build()
        )

        try {
            requireActivity().contentResolver.applyBatch(ContactsContract.AUTHORITY, listCPO)
        } catch (e: Exception) {
            Log.e("Exeception ", e.message!!)
        }
    }

    @SuppressLint("Range")
    private fun getContact() {
        contactList = ArrayList()
        val phones = requireActivity().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        )
        while (phones!!.moveToNext()) {
            val name =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNum =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val contact = Contact(name, phoneNum)
            contactList?.add(contact)
        }
        phones.close()
        recyclerAdapter = RecyclerAdapter(requireContext(), this, contactList!!)
        binding.recyclerRV.adapter = recyclerAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun callNum(number: String?) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$number")
        startActivity(intent)
    }

    private val permissionContact = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(
                requireContext(),
                "Получен доступ к контактам",
                Toast.LENGTH_SHORT
            ).show()
            getContact()
        } else {
            Toast.makeText(
                requireContext(),
                "В разрешении отказано",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    private val permissionWriteContact = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(
                requireContext(),
                "Получен доступ к записи контактов",
                Toast.LENGTH_SHORT
            ).show()
            getContact()
        } else {
            Toast.makeText(
                requireContext(),
                "В разрешении отказано",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    private val permissionOfCall = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(
                requireContext(),
                "Получен доступ к звонку",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                requireContext(),
                "В разрешении отказано",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    private val permissionOfSms = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(
                requireContext(),
                "Получен доступ к сообщениям",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                requireContext(),
                "В разрешении отказано",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onItemClickedCall(contact: Contact) {
        val numPhone = contact.numPhone
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            permissionOfCall.launch(Manifest.permission.CALL_PHONE)
        } else {
            callNum(numPhone)
        }
    }

    override fun onItemClickedSend(contact: Contact) {
        if (requireContext().checkSelfPermission(Manifest.permission.SEND_SMS) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            permissionOfSms.launch(Manifest.permission.SEND_SMS)
        } else {
            val fragment = SendSmsFragment()
            val bundle = Bundle()
            bundle.putParcelable(Contact::class.java.simpleName, contact)
            fragment.arguments = bundle
            fragmentManager
                ?.beginTransaction()
                ?.add(R.id.container, fragment)
                ?.commit()
        }
    }
}