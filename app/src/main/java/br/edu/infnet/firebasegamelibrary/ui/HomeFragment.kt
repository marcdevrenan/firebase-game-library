package br.edu.infnet.firebasegamelibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.edu.infnet.firebasegamelibrary.R
import br.edu.infnet.firebasegamelibrary.adapter.ViewPagerAdapter
import br.edu.infnet.firebasegamelibrary.api.Endpoint
import br.edu.infnet.firebasegamelibrary.databinding.FragmentHomeBinding
import br.edu.infnet.firebasegamelibrary.service.Store
import br.edu.infnet.firebasegamelibrary.util.NetworkUtils
import com.google.android.gms.ads.AdRequest
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private var userName: String = ""
    private var onlineUsers: String = ""
    private var ingameUsers: String = ""
    private lateinit var store: Store

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        store = Store(requireContext() as AppCompatActivity)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        configTabLayout()
        initInteractions()
        showUserInfo()
        adTest()
    }

    private fun adTest() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    private fun initInteractions() {
        binding.ibLogout.setOnClickListener {
            logoutUser()
        }
        binding.ibFreeGames.setOnClickListener {
            getOnlineUsers()
        }
        binding.ibStore.setOnClickListener {
            val product = store.products[0]
            store.makePurchase(product)
        }
    }

    private fun configTabLayout() {
        val adapter = ViewPagerAdapter(requireActivity())
        binding.viewPager.adapter = adapter

        adapter.addFragment(LibraryFragment(), "Library")
        adapter.addFragment(PlayingFragment(), "Playing")
        adapter.addFragment(AchievedFragment(), "Achieved")

        binding.viewPager.offscreenPageLimit = adapter.itemCount

        TabLayoutMediator(
            binding.tabs, binding.viewPager
        ) { tab, position ->
            tab.text = adapter.getTitle(position)
        }.attach()

    }

    private fun showUserInfo() {
        userName = auth.currentUser?.email.toString()
        binding.txtUserLogin.text = "Logged as: $userName"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun logoutUser() {
        auth.signOut()
        findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
    }

    private fun getOnlineUsers() {
        val retrofitClient = NetworkUtils.getRetrofitInstance("https://www.valvesoftware.com")
        val endpoint = retrofitClient.create(Endpoint::class.java)

        endpoint.getFreeGames().enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = mutableListOf<String>()
                response.body()?.keySet()?.iterator()?.forEach {
                    data.add(it)
                }

                onlineUsers = response.body()?.entrySet()?.find {
                    it.key == "users_online"
                }.toString()
                ingameUsers = response.body()?.entrySet()?.find {
                    it.key == "users_ingame"
                }.toString()

                Toast.makeText(
                    requireContext(),
                    "Found $onlineUsers and $ingameUsers",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(requireContext(), "Request failed", Toast.LENGTH_LONG).show()
            }

        })
    }
}