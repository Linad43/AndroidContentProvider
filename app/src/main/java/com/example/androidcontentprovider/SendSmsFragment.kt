package com.example.a16_3_ContentProvider

import android.annotation.SuppressLint
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.androidcontentprovider.Contact
import com.example.androidcontentprovider.databinding.FragmentSendSmsBinding

class SendSmsFragment : Fragment() {

    private var _binding: FragmentSendSmsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSendSmsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получаем объект Contact из Bundle
        val contact = arguments?.getSerializable(Contact::class.java.simpleName) as Contact
        binding.nameTV.text = contact.name
        binding.phoneTV.text = contact.numPhone
        binding.sendBTN.setOnClickListener {
            val message = binding.messageET.text.toString()
            if (message.isNotEmpty()) {
                sendSms(contact.numPhone.toString(), message)
                parentFragmentManager.popBackStack()
                Toast.makeText(
                    requireContext(),
                    "Сообщение отправлено",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun sendSms(phoneNumber: String, message: String) {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
    }
}