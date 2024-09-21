package com.example.prova_felipeportocaldeiranascimento

import androidx.compose.runtime.mutableStateListOf

class Estoque {
    companion object{
        var listaProdutos = mutableStateListOf<Produto>()

        fun adicionarProduto(produto: Produto){
            listaProdutos.add(produto)
        }

        fun calcularValorTotalEstoque(): Double{

            var total = 0.0
            for (produto in listaProdutos){
                total += produto.preco * produto.qtd
            }
            return total

        }
    }
}