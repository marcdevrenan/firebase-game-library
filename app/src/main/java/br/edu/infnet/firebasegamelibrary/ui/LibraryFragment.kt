package br.edu.infnet.firebasegamelibrary.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.infnet.firebasegamelibrary.adapter.GameAdapter
import br.edu.infnet.firebasegamelibrary.databinding.FragmentLibraryBinding
import br.edu.infnet.firebasegamelibrary.model.Game
import br.edu.infnet.firebasegamelibrary.util.FirebaseUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class LibraryFragment : Fragment() {

    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!
    private lateinit var gameAdapter: GameAdapter
    private val gameList = mutableListOf<Game>()
    private val REQUEST_CODE = 8080
    private lateinit var ivGameCover: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInteractions()
        getGames()
    }

    private fun initInteractions() {
        binding.fabAddGame.setOnClickListener {
            val action = HomeFragmentDirections
                .actionHomeFragmentToFormFragment(null)
            findNavController().navigate(action)
        }

        binding.fabSavedFiles.setOnClickListener {
            val action = HomeFragmentDirections
                .actionHomeFragmentToFilesFragment()
            findNavController().navigate(action)
        }
    }

    private fun getGames() {
        FirebaseUtils.getDatabase()
            .child("game")
            .child(FirebaseUtils.getUserId() ?: "")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        gameList.clear()
                        for (snap in snapshot.children) {
                            val game = snap.getValue(Game::class.java) as Game
                            if (game.status == 0) {
                                gameList.add(game)
                            }
                        }
                        gameList.reverse()
                        initAdapter()
                        binding.txtInfo.isVisible = false
                    } else {
                        binding.txtInfo.text = "You have no games in the library"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Error...", Toast.LENGTH_LONG).show()
                }

            })
    }

    private fun initAdapter() {
        binding.rvLibrary.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLibrary.setHasFixedSize(true)
        gameAdapter = GameAdapter(requireContext(), gameList) { game, int ->
            selectedOption(game, int)
        }

        binding.rvLibrary.adapter = gameAdapter
    }

    private fun selectedOption(game: Game, select: Int) {
        when (select) {
            GameAdapter.SELECT_ACHIEVED -> {
                game.status = 1
                updateGame(game)
            }
            GameAdapter.SELECT_EDIT -> {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToFormFragment(game)
                findNavController().navigate(action)
            }
            GameAdapter.SELECT_REMOVE -> {
                deleteGame(game)
            }
            GameAdapter.SELECT_PICTURE -> {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePictureIntent, REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val takenImage = data?.extras?.get("data") as Bitmap
            ivGameCover.setImageBitmap(takenImage)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun updateGame(game: Game) {
        FirebaseUtils.getDatabase()
            .child("game")
            .child(FirebaseUtils.getUserId() ?: "")
            .child(game.id)
            .setValue(game)
            .addOnCompleteListener { game ->
                if (game.isSuccessful) {
                    Toast.makeText(requireContext(), "Saved successfully", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "Error saving", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun deleteGame(game: Game) {
        FirebaseUtils.getDatabase()
            .child("game")
            .child(FirebaseUtils.getUserId() ?: "")
            .child(game.id)
            .removeValue()
            .addOnCompleteListener { game ->
                if (game.isSuccessful) {
                    Toast.makeText(requireContext(), "Successfully deleted", Toast.LENGTH_LONG)
                        .show()
                }
            }

        gameList.remove(game)
        gameAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}