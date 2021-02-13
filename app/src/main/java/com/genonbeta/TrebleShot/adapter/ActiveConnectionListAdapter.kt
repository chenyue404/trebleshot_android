/*
 * Copyright (C) 2019 Veli Tasalı
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.genonbeta.TrebleShot.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.genonbeta.TrebleShot.R
import com.genonbeta.TrebleShot.adapter.ActiveConnectionListAdapter.EditableNetworkInterface
import com.genonbeta.TrebleShot.app.IEditableListFragment
import com.genonbeta.TrebleShot.config.AppConfig
import com.genonbeta.TrebleShot.dataobject.Editable
import com.genonbeta.TrebleShot.util.Networks
import com.genonbeta.TrebleShot.util.TextUtils
import com.genonbeta.TrebleShot.widget.EditableListAdapter
import com.genonbeta.android.framework.widget.RecyclerViewAdapter.ViewHolder
import java.net.NetworkInterface
import java.util.*

/**
 * created by: veli
 * date: 4/7/19 10:35 PM
 */
class ActiveConnectionListAdapter(
    fragment: IEditableListFragment<EditableNetworkInterface, ViewHolder>,
) : EditableListAdapter<EditableNetworkInterface, ViewHolder>(fragment) {
    override fun onLoad(): MutableList<EditableNetworkInterface> {
        val resultList: MutableList<EditableNetworkInterface> = ArrayList()
        val interfaceList: List<NetworkInterface> = Networks.getInterfaces(
            true,
            AppConfig.DEFAULT_DISABLED_INTERFACES
        )
        for (addressedInterface in interfaceList) {
            val editableInterface = EditableNetworkInterface(
                addressedInterface,
                TextUtils.getAdapterName(context, addressedInterface)
            )
            if (filterItem(editableInterface)) resultList.add(editableInterface)
        }
        return resultList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = ViewHolder(layoutInflater.inflate(R.layout.list_active_connection, parent, false))
        fragment.registerLayoutViewClicks(holder)
        holder.itemView.findViewById<View>(R.id.visitView)
            .setOnClickListener { v: View? -> fragment.performLayoutClickOpen(holder) }
        holder.itemView.findViewById<View>(R.id.selector)
            .setOnClickListener { v: View? -> fragment.setItemSelected(holder, true) }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: EditableNetworkInterface = getItem(position)
        val text1: TextView = holder.itemView.findViewById(R.id.text)
        val text2: TextView = holder.itemView.findViewById(R.id.text2)
        val firstAddress = Networks.getFirstInet4Address(item)
        text1.text = item.getSelectableTitle()
        text2.text = firstAddress?.let { TextUtils.makeWebShareLink(context, it.hostAddress) }
    }

    class EditableNetworkInterface(private val netInterface: NetworkInterface, private val name: String) : Editable {
        override var id: Long = netInterface.hashCode().toLong()

        override fun applyFilter(filteringKeywords: Array<String>): Boolean {
            for (word in filteringKeywords) {
                val wordLC = word.toLowerCase()
                if (netInterface.displayName.toLowerCase().contains(wordLC) || name.toLowerCase().contains(wordLC))
                    return true
            }
            return false
        }

        override fun comparisonSupported(): Boolean {
            return false
        }

        override fun getComparableName(): String {
            return name
        }

        override fun getComparableDate(): Long {
            return 0
        }

        override fun getComparableSize(): Long {
            return 0
        }

        fun getInterface(): NetworkInterface {
            return netInterface
        }

        fun getName(): String {
            return name
        }

        override fun getSelectableTitle(): String {
            return name
        }

        override fun isSelectableSelected(): Boolean {
            return false
        }

        override fun setSelectableSelected(selected: Boolean): Boolean {
            return false
        }
    }
}