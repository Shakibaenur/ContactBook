package com.brainstation23.contactbook.ui.noob

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.brainstation23.contactbook.data.model.ContactModel
import com.brainstation23.contactbook.databinding.FragmentNoobBinding
import com.brainstation23.contactbook.ui.home.HomeActivity
import com.brainstation23.contactbook.ui.home.adapter.ContactListAdapter
import kotlinx.coroutines.delay

class NoobFragment : Fragment() {

    private var _binding: FragmentNoobBinding? = null

    private val mViewBinding get() = _binding!!
    private var adapter: ContactListAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNoobBinding.inflate(inflater, container, false)
        initView()
        callContact()
        return mViewBinding.root
    }

    private fun initView(){
        activity.let {
            adapter = ContactListAdapter(requireActivity(), ArrayList())
            mViewBinding.rvContact.layoutManager = LinearLayoutManager(it)
            mViewBinding.rvContact.adapter = adapter
        }
        (activity as HomeActivity?)?.startLoading()
    }
    @SuppressLint("Range")
    private fun callContact(){
        val phones = context?.contentResolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        while (phones!!.moveToNext()) {
            val name =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

            val contactModel = ContactModel(name, phoneNumber)

            adapter?.setData(contactModel)

        }
        (activity as HomeActivity?)?.getLoadingTime("Noob approach execution time: ")
        mViewBinding.loader.visibility=View.GONE
        phones.close()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}