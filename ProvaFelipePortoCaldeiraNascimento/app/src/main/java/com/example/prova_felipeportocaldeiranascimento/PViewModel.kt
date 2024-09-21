package com.example.prova_felipeportocaldeiranascimento

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class ProdutoViewModel : ViewModel() {
    val listaProdutos = mutableStateListOf<Produto>()
}