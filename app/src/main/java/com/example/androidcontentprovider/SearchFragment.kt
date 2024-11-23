package com.example.androidcontentprovider

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a16_3_ContentProvider.SendSmsFragment
import com.example.androidcontacts.database.service.RecyclerAdapter
import com.example.androidcontentprovider.databinding.FragmentMainBinding
import com.example.androidcontentprovider.databinding.FragmentSearchBinding
import java.util.ArrayList

class SearchFragment : Fragment(), RecyclerAdapter.ContactClickListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerAdapter: RecyclerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val allContacts =
            requireArguments().getParcelableArrayList<Contact>(Contact::class.java.simpleName)!!// as ArrayList<Contact>
        var contacts = allContacts
        recyclerAdapter = RecyclerAdapter(requireContext(), this, contacts)
        binding.recyclerRV.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerRV.adapter = recyclerAdapter
        binding.searchBTN.setOnClickListener {
            val buf = binding.inputSearchET.text
            if (binding.inputSearchET.text.isNotEmpty()) {
                contacts = allContacts
                    .filter {
                        it.name!!.contains(buf, true) ||
                                it.numPhone!!.contains(buf, true)
                    } as ArrayList<Contact>
            } else {
                contacts = allContacts
            }
            recyclerAdapter = RecyclerAdapter(requireContext(), MainFragment(), contacts)
            binding.recyclerRV.adapter = recyclerAdapter
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

    private fun callNum(number: String?) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$number")
        startActivity(intent)
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