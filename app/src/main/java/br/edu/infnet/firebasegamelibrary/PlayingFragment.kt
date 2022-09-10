package br.edu.infnet.firebasegamelibrary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.edu.infnet.firebasegamelibrary.databinding.FragmentPlayingBinding

class PlayingFragment : Fragment() {

    private var _binding: FragmentPlayingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayingBinding.inflate(inflater, container, false)
        return binding.root
    }
}