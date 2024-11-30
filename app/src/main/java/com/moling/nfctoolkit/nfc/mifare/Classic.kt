package com.moling.nfctoolkit.nfc.mifare

import android.nfc.Tag
import android.nfc.tech.MifareClassic
import java.io.IOException

class Classic(tag: Tag) {
    private var card: MifareClassic = MifareClassic.get(tag)
    val lastSectorIndex: Int = card.sectorCount - 1
    val keyTypeA: Int = 0
    val keyTypeB: Int = 1

    init {
        try {
            card.connect()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun authenticateSector(keyType: Int, sector: Int, key: ByteArray): Boolean {
        try {
            return when (keyType) {
                keyTypeA -> card.authenticateSectorWithKeyA(sector, key)
                keyTypeB -> card.authenticateSectorWithKeyB(sector, key)
                else -> false
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
    }

    fun getLastBlockIndex(sector: Int): Int {
        return card.getBlockCountInSector(sector) - 1
    }

    fun readBlock(sector: Int, block: Int): ByteArray {
        return card.readBlock(sector * 4 + (block % 4))
    }

    fun close() {
        card.close()
    }
}