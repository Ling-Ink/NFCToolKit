package com.moling.nfctoolkit.dump

class NCTDump(sectorCount: Int? = null) {
    private var dumpSectors = sectorCount?.let { arrayOfNulls<ByteArray>(it) }
    private val dumpPerms = sectorCount?.let { arrayOfNulls<ByteArray>(it) }
    private val dumpPwdAList = sectorCount?.let { arrayOfNulls<ByteArray>(it) }
    private val dumpPwdBList = sectorCount?.let { arrayOfNulls<ByteArray>(it) }

    fun setSector(index: Int, byteArray: ByteArray) {
        dumpSectors?.set(index, byteArray)
    }

    fun delSector(index: Int) {
        dumpSectors?.set(index, ByteArray(48))
    }

    fun loadFromDumps() {
        dumpSectors
    }
}