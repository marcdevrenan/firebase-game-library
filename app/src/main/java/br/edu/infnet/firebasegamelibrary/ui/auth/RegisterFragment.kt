package br.edu.infnet.firebasegamelibrary.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.edu.infnet.firebasegamelibrary.R
import br.edu.infnet.firebasegamelibrary.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        initInteractions()
    }

    private fun initInteractions() {
        binding.btnRegisterUser.setOnClickListener {
            validator()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun validator() {
        val email = binding.lblEmail.text.toString().trim()
        val password = binding.lblPwd.text.toString().trim()

        if (email.isNotEmpty()) {
            if (password.isNotEmpty()) {
                createUser(email, password)
            } else {
                Toast.makeText(requireContext(), "Enter a password", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(requireContext(), "Enter a email", Toast.LENGTH_LONG).show()
        }
    }
}