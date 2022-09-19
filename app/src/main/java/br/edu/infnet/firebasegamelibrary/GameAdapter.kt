package br.edu.infnet.firebasegamelibrary

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import br.edu.infnet.firebasegamelibrary.databinding.GameAdapterBinding

class GameAdapter(
    private val context: Context,
    private val gameList: List<Game>,
    val gameSelected: (Game, Int) -> Unit
) : RecyclerView.Adapter<GameAdapter.MyViewHolder>() {

    companion object {
        const val SELECT_LIBRARY: Int = 1
        const val SELECT_EDIT: Int = 2
        const val SELECT_REMOVE: Int = 3
        const val SELECT_ACHIEVED: Int = 4
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            GameAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class MyViewHolder(val binding: GameAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val game = gameList[position]

        holder.binding.txtGameTitle.text = "Title:"
        holder.binding.lblGameTitle.text = game.title

        holder.binding.txtGamePublisher.text = "Publisher:"
        holder.binding.lblGamePublisher.text = game.publisher

        holder.binding.txtGamePlatform.text = "Platform:"
        holder.binding.lblGamePlatform.text = game.platform

        holder.binding.icGameEdit.setOnClickListener {
            gameSelected(game, SELECT_EDIT)
        }
        holder.binding.icGameEdit.setColorFilter(
            ContextCompat.getColor(context, R.color.purple_500)
        )
        holder.binding.icGameDelete.setOnClickListener {
            gameSelected(game, SELECT_REMOVE)
        }
        holder.binding.icGameDelete.setColorFilter(
            ContextCompat.getColor(context, R.color.purple_500)
        )

        when (game.status) {
            0 -> {
                holder.binding.icGameLibrary.isVisible = false
                holder.binding.icGameAchieved.setOnClickListener {
                    gameSelected(game, SELECT_ACHIEVED)
                }
                holder.binding.icGameAchieved.setColorFilter(
                    ContextCompat.getColor(context, R.color.purple_500)
                )
            }
            1 -> {
                holder.binding.icGameLibrary.setOnClickListener {
                    gameSelected(game, SELECT_LIBRARY)
                }
                holder.binding.icGameLibrary.setColorFilter(
                    ContextCompat.getColor(context, R.color.purple_500)
                )
                holder.binding.icGameAchieved.setOnClickListener {
                    gameSelected(game, SELECT_ACHIEVED)
                }
                holder.binding.icGameAchieved.setColorFilter(
                    ContextCompat.getColor(context, R.color.purple_500)
                )
            }
            else -> {
                holder.binding.icGameAchieved.isVisible = false
                holder.binding.icGameLibrary.setOnClickListener {
                    gameSelected(game, SELECT_LIBRARY)
                }
                holder.binding.icGameLibrary.setColorFilter(
                    ContextCompat.getColor(context, R.color.purple_500)
                )
            }
        }
    }

    override fun getItemCount() = gameList.size
}
