package com.moling.nfctoolkit.nfc.mifare

import android.nfc.Tag
import android.nfc.tech.MifareUltralight
import java.io.IOException

class UltralightC(tag: Tag) {

    private var card: MifareUltralight = MifareUltralight.get(tag)
    val MAX_PAGES: Int = 44

    init {
        try {
            card.connect()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun readPage(page: Int): ByteArray {
        if (page > MAX_PAGES) throw IllegalArgumentException("Pages out of range")
        return card.readPages(page).sliceArray(0..3)
    }

    fun close() {
        card.close()
    }
}