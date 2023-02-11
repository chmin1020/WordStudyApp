package com.example.wordpractice

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wordpractice.room.Word
import com.example.wordpractice.room.WordDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WordViewModel(application: Application): AndroidViewModel(application) {
    private val wordDB = WordDB.getInstance(application)
    private val wordDao = wordDB.wordDao()

    private val wordsInside = MutableLiveData<List<Word>>()
    val words:LiveData<List<Word>> = wordsInside

    fun getAll(){
        CoroutineScope(Dispatchers.IO).launch {
            wordsInside.postValue(wordDao.getAll())
        }
    }

    fun insert(word: Word){
        CoroutineScope(Dispatchers.IO).launch{
            wordDao.insert(word)
            getAll()
        }
    }

    fun delete(word: Word){
        CoroutineScope(Dispatchers.IO).launch{
            wordDao.delete(word)
            getAll()
        }
    }

    fun update(word: Word) {
        CoroutineScope(Dispatchers.IO).launch{
            wordDao.update(word)
            getAll()
        }
    }

}