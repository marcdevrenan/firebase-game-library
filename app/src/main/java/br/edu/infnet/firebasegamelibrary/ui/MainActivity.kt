package br.edu.infnet.firebasegamelibrary.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import br.edu.infnet.firebasegamelibrary.R
import br.edu.infnet.firebasegamelibrary.databinding.ActivityMainBinding
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this) {
        }

        supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    }
}