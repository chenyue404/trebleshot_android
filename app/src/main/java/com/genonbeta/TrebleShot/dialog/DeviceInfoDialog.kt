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
package com.genonbeta.TrebleShot.dialog

import android.app.Activity
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.genonbeta.TrebleShot.BuildConfig
import com.genonbeta.TrebleShot.R
import com.genonbeta.TrebleShot.dataobject.Device
import com.genonbeta.TrebleShot.util.AppUtils

/**
 * Created by: veli
 * Date: 5/18/17 5:16 PM
 */
class DeviceInfoDialog(activity: Activity, device: Device) : AlertDialog.Builder(activity) {
    companion object {
        val TAG = DeviceInfoDialog::class.java.simpleName
    }

    init {
        val kuick = AppUtils.getKuick(activity)
        try {
            kuick.reconstruct(device)
        } catch (ignored: ReconstructionFailedException) {
        }
        @SuppressLint("InflateParams") val rootView =
            LayoutInflater.from(activity).inflate(R.layout.layout_device_info, null)
        val localDevice = AppUtils.getLocalDevice(activity)
        val image = rootView.findViewById<ImageView>(R.id.image)
        val text1: TextView = rootView.findViewById<TextView>(R.id.text1)
        val notSupportedText: TextView = rootView.findViewById<TextView>(R.id.notSupportedText)
        val modelText: TextView = rootView.findViewById<TextView>(R.id.modelText)
        val versionText: TextView = rootView.findViewById<TextView>(R.id.versionText)
        val accessSwitch: SwitchCompat = rootView.findViewById(R.id.accessSwitch)
        val trustSwitch: SwitchCompat = rootView.findViewById(R.id.trustSwitch)
        val isDeviceNormal = Device.Type.Normal == device.type
        if (Device.Type.Web != device.type && BuildConfig.PROTOCOL_VERSION_MIN > device.protocolVersionMin) notSupportedText.setVisibility(
            View.VISIBLE
        )
        DeviceLoader.showPictureIntoView(device, image, AppUtils.getDefaultIconBuilder(activity))
        text1.setText(device.username)
        modelText.setText(String.format("%s %s", device.brand!!.toUpperCase(), device.model!!.toUpperCase()))
        versionText.setText(device.versionName)
        accessSwitch.setChecked(!device.isBlocked)
        trustSwitch.setEnabled(!device.isBlocked)
        trustSwitch.setChecked(device.isTrusted)
        accessSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { button: CompoundButton?, isChecked: Boolean ->
            device.isBlocked = !isChecked
            kuick.publish(device)
            kuick.broadcast()
            trustSwitch.setEnabled(isChecked)
        })
        if (isDeviceNormal) trustSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { button: CompoundButton?, isChecked: Boolean ->
            device.isTrusted = isChecked
            kuick.publish(device)
            kuick.broadcast()
        }) else trustSwitch.setVisibility(View.GONE)
        setView(rootView)
        setPositiveButton(R.string.butn_close, null)
        setNegativeButton(R.string.butn_remove) { dialog: DialogInterface?, which: Int ->
            RemoveDeviceDialog(
                activity,
                device
            ).show()
        }
    }
}