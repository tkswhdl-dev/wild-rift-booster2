package com.wildrift.booster

import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import java.io.FileInputStream
import java.nio.ByteBuffer

class WildRiftVpnService : VpnService() {
    private var vpnInterface: ParcelFileDescriptor? = null
    private var isRunning = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isRunning) {
            isRunning = true
            startVpn()
        }
        return START_STICKY
    }

    private fun startVpn() {
        vpnInterface = Builder()
            .setSession("WildRift Gaming DNS")
            .addAddress("10.0.0.2", 24)
            .addDnsServer("1.1.1.1") // Cloudflare DNS
            .addRoute("0.0.0.0", 0)
            .establish()

        Thread {
            try {
                val fd = vpnInterface?.fileDescriptor ?: return@Thread
                val inputStream = FileInputStream(fd)
                val buffer = ByteBuffer.allocate(32767)
                while (isRunning) {
                    if (inputStream.read(buffer.array()) > 0) buffer.clear()
                    Thread.sleep(10)
                }
            } catch (e: Exception) { e.printStackTrace() }
        }.start()
    }

    override fun onDestroy() {
        isRunning = false
        vpnInterface?.close()
        super.onDestroy()
    }
}
