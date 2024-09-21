package com.example.prova_felipeportocaldeiranascimento

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LayoutMain()
        }
    }
}

@Composable
fun LayoutMain(){
    val navController = rememberNavController()
    val produtoViewModel: ProdutoViewModel = viewModel()

    NavHost(navController = navController, startDestination = "telaCadastroProduto") {
        composable("telaCadastroProduto") {
            TelaCadastroProduto(navController, produtoViewModel)
        }
        composable("listaDeProdutos") {
            ListaDeProdutos(navController, produtoViewModel)
        }

        composable("detalhesDoProduto/{produtoNome}") { backStackEntry ->
            val produtoNome = backStackEntry.arguments?.getString("produtoNome")
            DetalhesDoProduto(navController, produtoViewModel, produtoNome)
        }
    }
}



@Composable
fun TelaCadastroProduto(navController: NavController, produtoViewModel: ProdutoViewModel = viewModel()){

    var nome by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") } // double
    var qtd by remember { mutableStateOf("") } //Int
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){

        Text("Cadastro de Produtos")

        TextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome Produto") })

        TextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoria") })

        TextField(value = preco, onValueChange = { preco = it }, label = { Text("Preço") })

        TextField(value = qtd, onValueChange = { qtd = it }, label = { Text("Quantidade") })

        Button(onClick = {

            if (nome.isEmpty() || categoria.isEmpty() || preco.isEmpty() || qtd.isEmpty()) {
                Toast.makeText(context, "Algum campo não foi preenchido corretamente", Toast.LENGTH_SHORT).show()
            }else if(qtd.toInt() < 1) {

                Toast.makeText(context, "quantidade não pode ser menor do que 1", Toast.LENGTH_SHORT).show()

            }else if(preco.toDouble() <= 0.0){
                Toast.makeText(context, "preço não pode ser menor do que 0", Toast.LENGTH_SHORT).show()
            }else{
                val produto = Produto(nome, categoria, preco.toDouble(), qtd.toInt())
                produtoViewModel.listaProdutos.add(produto)
                Toast.makeText(context, "Produto Cadastrado", Toast.LENGTH_SHORT).show()
            }

        }) {
            Text("Cadastrar")
        }

        Spacer(modifier = Modifier.padding(30.dp))

        Button(onClick = { navController.navigate("listaDeProdutos") }) { // Corrigir o nome da rota aqui
            Text("Ir para Lista de Produtos")
        }
    }
}





@Composable
fun ListaDeProdutos(navController: NavController, produtoViewModel: ProdutoViewModel = viewModel() ){

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

        LazyColumn {
            items(produtoViewModel.listaProdutos) { produto ->
                Row(Modifier.padding(5.dp)) {
                    Text("${produto.nome} (${produto.qtd} unidades)")
                    Button(onClick = {

                        navController.navigate("detalhesDoProduto/${produto.nome}")

                    }) {
                        Text("Detalhes")
                    }

                }


            }
        }

        Button(onClick = {
            navController.popBackStack()
        }) {
            Text("Voltar para Cadastro")
        }
    }
}

@Composable
fun DetalhesDoProduto(navController: NavController, produtoViewModel: ProdutoViewModel = viewModel(), produtoNome: String?){

    val produto = produtoViewModel.listaProdutos.find { it.nome == produtoNome }


    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {


        Text("Nome: ${produto?.nome}\nCategoria: ${produto?.categoria}\nPreço: R$ ${produto?.preco}\n${produto?.qtd} unidades")


        Button(onClick = {
            navController.popBackStack()
        }) {
            Text("Voltar")
        }
    }

}



@Composable
@Preview
fun Preview() {

}