@file:Suppress("DEPRECATION")

package com.moling.nfctoolkit.utils

import android.content.Intent
import android.content.res.AssetManager
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.nfc.tech.MifareClassic
import android.nfc.tech.MifareUltralight
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.nfc.tech.NfcA
import android.nfc.tech.NfcB
import android.nfc.tech.NfcF
import android.nfc.tech.NfcV
import android.os.Build
import android.util.Log
import java.io.InputStream
import java.util.Locale

private const val LOG_TAG = "NFCToolKit_Common"

@OptIn(ExperimentalUnsignedTypes::class) // just to make it clear that the experimental unsigned types are used
fun ByteArray.toHexString() = asUByteArray().joinToString("") {
    it.toString(16)
        .padStart(2, '0')
        .uppercase(Locale.getDefault())
}

@OptIn(ExperimentalUnsignedTypes::class) // just to make it clear that the experimental unsigned types are used
fun ByteArray.toHexStringRev() = asUByteArray().reversedArray().joinToString("") {
    it.toString(16)
        .padStart(2, '0')
        .uppercase(Locale.getDefault())
}

fun Intent.getTag(): Tag {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getParcelableExtra(NfcAdapter.EXTRA_TAG, Tag::class.java)!!
    } else {
        this.getParcelableExtra(NfcAdapter.EXTRA_TAG)!!
    }
}

fun Tag.getUID() = id.toHexString()

fun Tag.getTech(): Array<String> {
    val techList = mutableListOf<String>()
    for (tech in this.techList) {
        when (tech) {
            IsoDep::class.java.name -> techList.add("IsoDep")
            NfcA::class.java.name -> techList.add("NfcA")
            NfcB::class.java.name -> techList.add("NfcB")
            NfcF::class.java.name -> techList.add("NfcF")
            NfcV::class.java.name -> techList.add("NfcV")
            Ndef::class.java.name -> techList.add("Ndef")
            NdefFormatable::class.java.name -> techList.add("NdefFormatable")
            MifareClassic::class.java.name -> techList.add("MifareClassic")
            MifareUltralight::class.java.name -> {
                val ultralight: MifareUltralight = MifareUltralight.get(this)
                when (ultralight.type) {
                    MifareUltralight.TYPE_ULTRALIGHT -> techList.add("MifareUltralight")
                    MifareUltralight.TYPE_ULTRALIGHT_C -> techList.add("MifareUltralight_C")
                }
            }
        }
    }
    return techList.toTypedArray()
}

fun Tag.getATQA(): String {
    if (this.techList.contains(NfcA::class.java.name)) {
        return NfcA.get(this).atqa.toHexStringRev()
    }
    return "-"
}

@OptIn(ExperimentalStdlibApi::class)
fun Tag.getSAK(): String {
    if (this.techList.contains(NfcA::class.java.name)) {
        return NfcA.get(this).sak.toHexString()
    }
    return "-"
}

fun Tag.getMifareSize(): String {
    if (this.techList.contains(MifareClassic::class.java.name)) {
        return "%s bytes".format(MifareClassic.get(this).size.toString())
    }
    return "-"
}

fun Tag.getMifareBlockCount(): String {
    if (this.techList.contains(MifareClassic::class.java.name)) {
        return MifareClassic.get(this).blockCount.toString()
    }
    return "-"
}

fun Tag.getMifareSectorCount(): String {
    if (this.techList.contains(MifareClassic::class.java.name)) {
        return MifareClassic.get(this).sectorCount.toString()
    }
    return "-"
}

fun AssetManager.getKeyFile(fileName: String): String {
    try {
        val input: InputStream = this.open(fileName)
        val stringBuilder: StringBuilder = StringBuilder()
        var n: Int = input.read()
        while (n != -1) {
            stringBuilder.append(n.toChar())
            n = input.read()
        }
        input.close()
        Log.d(LOG_TAG, "Get key file: $fileName")
        return stringBuilder.toString()
    }
    catch (_: Exception) {
        return ""
    }
}
