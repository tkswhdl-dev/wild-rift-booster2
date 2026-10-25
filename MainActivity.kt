package com.wildrift.booster

import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val VPN_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnBoost).setOnClickListener {
            prepareVpn()
        }
    }

    private fun prepareVpn() {
        val intent = VpnService.prepare(this)
        if (intent != null) {
            startActivityForResult(intent, VPN_REQUEST_CODE)
        } else {
            onActivityResult(VPN_REQUEST_CODE, RESULT_OK, null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == VPN_REQUEST_CODE && resultCode == RESULT_OK) {
            startService(Intent(this, WildRiftVpnService::class.java))
            GameOptimizer(this).applyHighPerformanceMode()
            Toast.makeText(this, "터보 부스트 및 1.1.1.1 DNS 작동 중! 🚀", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "VPN 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}
