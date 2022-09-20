package br.edu.infnet.firebasegamelibrary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.edu.infnet.firebasegamelibrary.databinding.FragmentFormBinding


class FormFragment : Fragment() {

    private var _binding: FragmentFormBinding? = null
    private val binding get() = _binding!!
    private lateinit var game: Game
    private var newCard: Boolean = true
    private var cardStatus: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        binding.btnSaveGame.setOnClickListener {
            cardIsValid()
        }

        binding.rgStatus.setOnCheckedChangeListener { _, id ->
            cardStatus = when (id) {
                R.id.rbLibrary -> 0
                R.id.rbPlaying -> 1
                else -> 2
            }
        }
    }

    private fun cardIsValid() {
        val title = binding.lblTitle.text.toString().trim()
        val publisher = binding.lblPublisher.text.toString().trim()
        val platform = binding.lblPlatform.text.toString().trim()

        if (title.isNotEmpty()) {
            if (newCard) game = Game()
            game.title = title
            game.publisher = publisher
            game.platform = platform
            game.status = cardStatus
            saveCard()
        } else {
            Toast.makeText(requireContext(), "Empty field", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveCard() {
        FirebaseUtils
            .getDatabase()
            .child("game")
            .child(FirebaseUtils.getUserId() ?: "")
            .child(game.id)
            .setValue(game)
            .addOnCompleteListener { card ->
                if (card.isSuccessful) {
                    if (newCard) {
                        findNavController().popBackStack()
                        Toast.makeText(requireContext(), "Saved successfully", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        Toast.makeText(requireContext(), "Saved successfully", Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Error saving", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener {

            }
    }

    private fun initAdapter() {
        binding
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}