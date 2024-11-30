package com.moling.nfctoolkit.nfc.iso

import android.nfc.Tag
import android.nfc.tech.IsoDep
import com.moling.nfctoolkit.utils.byteArrayOfInts
import java.io.IOException

class Dep(tag: Tag) {
    private val selectMasterFile: ByteArray = byteArrayOfInts(0x00, 0xA4, 0x00, 0x00, 0x02, 0x3F, 0x00)
    private val selectAIDHeader : ByteArray = byteArrayOfInts(0x00, 0xA4, 0x04, 0x00)
    private var card: IsoDep = IsoDep.get(tag)

    init {
        try {
            card.connect()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun transceive(data: ByteArray): ByteArray {
        return card.transceive(data)
    }

    fun getMasterFiles(): ByteArray {
        return card.transceive(selectMasterFile)
    }

    fun selectAID(aid: ByteArray) {
        card.transceive(selectAIDHeader.plus(aid.size.toByte()).plus(aid))
    }

    fun close() {
        card.close()
    }
}