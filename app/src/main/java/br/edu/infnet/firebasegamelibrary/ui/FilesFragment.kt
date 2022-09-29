package br.edu.infnet.firebasegamelibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import br.edu.infnet.firebasegamelibrary.databinding.FragmentFilesBinding
import java.io.File


class FilesFragment : Fragment(), AdapterView.OnItemClickListener {

    private var _binding: FragmentFilesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadFiles()
    }

    private fun loadFiles() {
        val files: Array<out File>? = requireContext().filesDir?.listFiles()
        val fileName = ArrayList<String>()

        if (files != null) {
            for (file in files.indices) {
                fileName.add(files[file].name.toString())
            }
        }

        val listFiles = binding.lvFiles
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, fileName)
        listFiles.adapter = adapter
        listFiles.choiceMode = ListView.CHOICE_MODE_SINGLE
        listFiles.onItemClickListener = this
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val selectedItem: String = p0?.getItemAtPosition(p2) as String

        val bundle = bundleOf("name" to selectedItem)
        TODO()
    }
}