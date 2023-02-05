package com.brainstation23.contactbook.ui.pro

import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import com.brainstation23.contactbook.data.model.ContactModel
import com.brainstation23.contactbook.databinding.FragmentProBinding
import com.brainstation23.contactbook.ui.home.HomeActivity
import com.brainstation23.contactbook.ui.home.adapter.ContactListAdapter

class ProFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private var _binding: FragmentProBinding? = null
    var PROJECTION_NUMBERS = arrayOf(
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
        ContactsContract.CommonDataKinds.Phone.NUMBER
    )
    var PROJECTION_DETAILS = arrayOf(
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.PHOTO_URI
    )
    var phoneMap: Map<Long, ArrayList<String?>> = HashMap()
    private var adapter: ContactListAdapter? = null
    private val mViewBinding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProBinding.inflate(inflater, container, false)
        initView()
        return mViewBinding.root
    }

    private fun initView() {
        LoaderManager.getInstance(this).initLoader(0, null, this)
        activity.let {
            adapter = ContactListAdapter(requireActivity(), ArrayList())
            mViewBinding.rvContact.layoutManager = LinearLayoutManager(it)
            mViewBinding.rvContact.adapter = adapter
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        (activity as HomeActivity?)?.startLoading()
        return when (id) {
            0 -> CursorLoader(
                requireActivity(),
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PROJECTION_NUMBERS,
                null,
                null,
                null
            )
            else -> CursorLoader(
                requireActivity(),
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION_DETAILS,
                null,
                null,
                null
            )
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        mViewBinding.loader.visibility=View.GONE
        when (loader.id) {
            0 -> {
                phoneMap = HashMap()
                if (data != null) {
                    while (!data.isClosed && data.moveToNext()) {
                        val contactId: Long = data.getLong(0)
                        val phone: String = data.getString(1)
                        var list: ArrayList<String?> = ArrayList()
                        if (phoneMap.containsKey(contactId)) {
                            phoneMap[contactId]?.let { list.addAll(it) }
                        } else {
                            list = ArrayList()
                            (phoneMap as HashMap<Long, ArrayList<String?>>)[contactId] = list
                        }
                        list.add(phone)
                    }
                    data.close()
                }
                LoaderManager.getInstance(requireActivity())
                    .initLoader(1, null, this)
            }
            1 -> if (data != null) {
                while (!data.isClosed && data.moveToNext()) {
                    val contactId: Long = data.getLong(0)
                    val name: String = data.getString(1)
                    val contactPhones: ArrayList<String?>? = phoneMap[contactId]
                    if (contactPhones != null) {
                        for (phone in contactPhones) {
                            adapter?.setData(ContactModel(name, phone))
                        }
                    }
                }
                data.close()
                (activity as HomeActivity?)?.getLoadingTime("Pro approach execution time: ")
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {

    }

}