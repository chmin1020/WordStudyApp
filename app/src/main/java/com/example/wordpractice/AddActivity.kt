package com.example.wordpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wordpractice.databinding.ActivityAddBinding
import com.example.wordpractice.room.Word
import com.google.android.material.chip.Chip

class AddActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAddBinding.inflate(layoutInflater) }

    private val viewModelFactory by lazy{ ViewModelProvider.AndroidViewModelFactory(this.application) }
    private val wordViewModel by lazy { ViewModelProvider(this, viewModelFactory)[WordViewModel::class.java] }

    private var originalWord:Word? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        initListeners()
    }


    //----------------------------------------------------
    // 초기 설정 함수

    private fun initViews() {
        val types = listOf("명사", "동사", "대명사", "형용사", "부사", "감탄사", "전치사", "접속사")
        binding.typeChipGroup.apply {
            types.forEach { text ->
                addView(createChip(text))
            }
        }

        //수정을 위한 인텐트였다면
        originalWord = intent.getParcelableExtra("originalWord")
        originalWord?.let { word->
            with(binding){
                textInputEditText.setText(word.text)
                meanTextInputEditText.setText(word.mean)
                val selectedChip = binding.typeChipGroup.children.firstOrNull { (it as Chip).text == word.type } as? Chip
                selectedChip?.isChecked = true
            }
        }
    }

    private fun createChip(text: String): Chip {
        return Chip(this).apply {
            this.text = text
            isCheckable = true
            isClickable = true
        }
    }

    private fun initListeners(){
        binding.addButton.setOnClickListener{
            val text = binding.textInputEditText.text.toString()
            val mean = binding.meanTextInputEditText.text.toString()
            val type = findViewById<Chip>(binding.typeChipGroup.checkedChipId).text.toString()

            originalWord?.also { editWord(text, mean, type) } ?: addWord(text, mean, type)
            finish()
        }

        binding.textInputEditText.addTextChangedListener {
            it?.let { text ->
                binding.textTextInputLayout.error = when(text.length) {
                    0 -> "값을 입력해주세요"
                    1 -> "2자 이상을 입력해주세요"
                    else -> null
                }
            }
        }
    }


    //-----------------------------------------------------
    // 내부 함수

    private fun addWord(text: String, mean: String, type: String){
        val newWord = Word(text = text, mean = mean, type = type)
        wordViewModel.insert(newWord)
    }

    private fun editWord(text: String, mean: String, type: String){
        originalWord?.let {
            wordViewModel.update(it.copy(text = text, mean = mean, type = type))
        }
    }
}