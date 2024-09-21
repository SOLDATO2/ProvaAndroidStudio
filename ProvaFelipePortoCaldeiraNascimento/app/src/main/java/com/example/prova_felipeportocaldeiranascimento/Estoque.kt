package com.example.prova_felipeportocaldeiranascimento

import androidx.compose.runtime.mutableStateListOf
import com.google.gson.Gson

class Estoque {
    companion object{
        // Dica da etapa 4 mostra a declaração dessa lista como "private"
        //então não tem como acessar diretamente fora da classe para transformar em json
        // portanto preciso adicionar um método para retornar um json de dentro dessa classe
        private val listaProdutos = mutableStateListOf<Produto>()

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

        fun calcularQuantidadeTotalProdutos(): Int {

            var total = 0
            for (produto in listaProdutos){
                total += produto.qtd
            }
            return total
        }

        fun getListaProdutosJson(): String{

            return Gson().toJson(listaProdutos)
        }
    }
}