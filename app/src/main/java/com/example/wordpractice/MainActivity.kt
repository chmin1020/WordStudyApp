package com.example.wordpractice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wordpractice.databinding.ActivityMainBinding
import com.example.wordpractice.room.Word

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}


    private val viewModelFactory by lazy{ ViewModelProvider.AndroidViewModelFactory(this.application) }
    private val wordViewModel by lazy { ViewModelProvider(this, viewModelFactory)[WordViewModel::class.java] }

    private val wordsObserver = Observer<List<Word>> { wordAdapter.update(it) }
    private val selectObserver = Observer<Word?> { updateSelectCheckViews(it) }

    private val wordAdapter = WordAdapter()

    //-----------------------------------
    // 생명주기 callbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initListeners()
        initRecyclerView()

        wordViewModel.words.observe(this, wordsObserver)
        wordAdapter.selectedWord.observe(this, selectObserver)
    }

    override fun onStart() {
        super.onStart()
        wordViewModel.getAll()
    }

    //--------------------------------------
    // 초기화 함수

    private fun initListeners(){
        binding.addButton.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }

        binding.deleteImageView.setOnClickListener{
            //선택된 단어가 있다면 삭제
            wordAdapter.selectedWord.value?.let { wordViewModel.delete(it) }

            with(binding){
                textTextView.text = ""
                meanTextView.text = ""
            }
        }

        binding.editImageView.setOnClickListener{
            val edit = Intent(this, AddActivity::class.java)
            edit.putExtra("originalWord", wordAdapter.selectedWord.value)
            startActivity(edit)
        }
    }

    private fun initRecyclerView(){
        with(binding.wordRecyclerView){
            adapter = wordAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            val dividerItemDecoration = DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL)
            addItemDecoration(dividerItemDecoration)
        }
    }

    //------------------------------------------
    // 옵저빙

    private fun updateSelectCheckViews(word: Word?){
        with(binding){
            textTextView.text = word?.text
            meanTextView.text = word?.mean
        }
    }
}