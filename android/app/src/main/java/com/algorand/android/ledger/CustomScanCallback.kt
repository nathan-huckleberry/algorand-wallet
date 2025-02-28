/*
 * Copyright 2019 Algorand, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.algorand.android.ledger

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import androidx.annotation.StringRes

abstract class CustomScanCallback : ScanCallback() {

    var filteredAddress: String? = null

    override fun onScanResult(callbackType: Int, result: ScanResult?) {
        super.onScanResult(callbackType, result)
        result?.device?.let { foundedDevice ->
            if (filteredAddress == null) {
                onLedgerScanned(foundedDevice)
            } else {
                if (foundedDevice.address == filteredAddress) {
                    onLedgerScanned(foundedDevice)
                }
            }
        }
    }

    abstract fun onLedgerScanned(device: BluetoothDevice)
    abstract fun onScanError(@StringRes errorMessageResId: Int, @StringRes titleResId: Int)
}
