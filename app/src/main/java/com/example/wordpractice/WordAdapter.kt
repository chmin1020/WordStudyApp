package com.example.wordpractice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.wordpractice.databinding.ItemWordBinding
import com.example.wordpractice.room.Word

class WordAdapter: RecyclerView.Adapter<WordAdapter.WordViewHolder>() {
    private val words = mutableListOf<Word>()

    private val selectedWordInside = MutableLiveData<Word?>()
    val selectedWord:LiveData<Word?> = selectedWordInside

    //-----------------------------------------------
    // 오버라이딩 영역

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val binding: ItemWordBinding = ItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(words[position])
    }

    override fun getItemCount() = words.size


    //-----------------------------------------------
    // 추가 함수

    fun update(newList: List<Word>){
        words.clear()
        words.addAll(newList)
        selectedWordInside.postValue(null)
        notifyDataSetChanged()
    }


    //-----------------------------------------------
    // 커스텀 뷰홀더

    inner class WordViewHolder(private val binding: ItemWordBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(word: Word){
            binding.apply {
                textTextView.text = word.text
                meanTextView.text = word.mean
                typeChip.text = word.type
            }

            binding.root.setOnClickListener {
                selectedWordInside.postValue(word)
            }
        }
    }

}