package br.edu.infnet.firebasegamelibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.edu.infnet.firebasegamelibrary.R
import br.edu.infnet.firebasegamelibrary.util.FirebaseUtils
import br.edu.infnet.firebasegamelibrary.databinding.FragmentFormBinding
import br.edu.infnet.firebasegamelibrary.model.Game


class FormFragment : Fragment() {

    private var _binding: FragmentFormBinding? = null
    private val binding get() = _binding!!
    private lateinit var game: Game
    private var newGame: Boolean = true
    private var gameStatus: Int = 0
    private val args: FormFragmentArgs by navArgs()

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

        getArgs()

        binding.rgStatus.setOnCheckedChangeListener { _, id ->
            gameStatus = when (id) {
                R.id.rbLibrary -> 0
                R.id.rbPlaying -> 1
                else -> 2
            }
        }
    }

    private fun getArgs() {
        args.game.let {
            if (it != null) {
                game = it
                gameConfig()
            }
        }
    }

    private fun gameConfig() {
        newGame = false
        gameStatus = game.status
        binding.txtToolbar.text = "Editing game"

        binding.lblTitle.setText(game.title)
        binding.lblPublisher.setText(game.publisher)
        binding.lblPlatform.setText(game.platform)
        setStatus()
    }

    private fun setStatus() {
        binding.rgStatus.check(
            when (game.status) {
                0 -> {
                    R.id.rbLibrary
                }
                1 -> {
                    R.id.rbPlaying
                }
                else -> {
                    R.id.rbAchieved
                }
            }
        )
    }

    private fun cardIsValid() {
        val title = binding.lblTitle.text.toString().trim()
        val publisher = binding.lblPublisher.text.toString().trim()
        val platform = binding.lblPlatform.text.toString().trim()

        if (title.isNotEmpty()) {
            if (newGame) game = Game()
            game.title = title
            game.publisher = publisher
            game.platform = platform
            game.status = gameStatus
            saveCard()
        } else {
            Toast.makeText(requireContext(), "Empty field", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveCard() {
        FirebaseUtils.getDatabase()
            .child("game")
            .child(FirebaseUtils.getUserId() ?: "")
            .child(game.id)
            .setValue(game)
            .addOnCompleteListener { card ->
                if (card.isSuccessful) {
                    if (newGame) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}