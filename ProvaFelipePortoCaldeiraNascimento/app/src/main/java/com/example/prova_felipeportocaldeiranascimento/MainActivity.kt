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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson

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


    NavHost(navController = navController, startDestination = "telaCadastroProduto") {
        composable("telaCadastroProduto") {
            TelaCadastroProduto(navController)
        }
        composable("listaDeProdutos/{produtoJSON}") {
            backStacKEntry ->
            val produtoJSON = backStacKEntry.arguments?.getString("produtoJSON")
            val produtos = Gson().fromJson(produtoJSON, Array<Produto>::class.java).toList() //espera receber um json e transforma em um objeto
            //passar json traduzido para objeto para ListaDeProdutos para listar
            ListaDeProdutos(navController, produtos)
        }

        composable("detalhesDoProduto/{produtoJSON}") {
            backStacKEntry ->
            val produtoJSON = backStacKEntry.arguments?.getString("produtoJSON")
            val produto = Gson().fromJson(produtoJSON, Produto::class.java) //espera receber um json e transforma em um objeto

            DetalhesDoProduto(navController, produto)
        }

        composable("estatisticasDosProdutos/{valorTotal}/{quantidadeTotal}") {

            EstatisticasDosProdutos(navController, it.arguments?.getString("valorTotal")!!.toDouble(), it.arguments?.getString("quantidadeTotal")!!.toInt())


        }
    }
}



@Composable
fun TelaCadastroProduto(navController: NavController) {
    var nome by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") } //Double
    var qtd by remember { mutableStateOf("") } //Int
    val context = LocalContext.current //só pra usar o toast

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Cadastro de Produtos")
        TextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome Produto") })
        TextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoria") })
        TextField(value = preco, onValueChange = { preco = it }, label = { Text("Preço") })
        TextField(value = qtd, onValueChange = { qtd = it }, label = { Text("Quantidade") })

        Button(onClick = {
            if (nome.isEmpty() || categoria.isEmpty() || preco.isEmpty() || qtd.isEmpty()) {
                Toast.makeText(context, "Algum campo não foi preenchido corretamente", Toast.LENGTH_SHORT).show()
            } else if (qtd.toInt() < 1) {
                Toast.makeText(context, "Quantidade não pode ser menor do que 1", Toast.LENGTH_SHORT).show()
            } else if (preco.toDouble() <= 0.0) { //prof disse que pode ser <= ao inves de <
                Toast.makeText(context, "Preço não pode ser 0", Toast.LENGTH_SHORT).show()
            } else {
                val produto = Produto(nome, categoria, preco.toDouble(), qtd.toInt())
                Estoque.adicionarProduto(produto)

                Toast.makeText(context, "Produto Cadastrado", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Cadastrar")
        }

        Spacer(modifier = Modifier.padding(30.dp))

        Button(onClick = {

            val produtoJSON = Estoque.getListaProdutosJson()

            navController.navigate("listaDeProdutos/$produtoJSON")

        }) {
            Text("ir para Lista de Produtos")
        }
    }
}





@Composable
fun ListaDeProdutos(navController: NavController, produtos: List<Produto>) {



    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        LazyColumn {
            items(produtos) { produto ->
                Row(Modifier.padding(5.dp)) {
                    Text("${produto.nome} (${produto.qtd} unidades)")
                    val produtoJSON = Gson().toJson(produto)
                    Button(onClick = { navController.navigate("detalhesDoProduto/${produtoJSON}") }) {
                        Text("Detalhes")
                    }
                }
            }
        }

        Button(onClick = {

            val valorTotal = Estoque.calcularValorTotalEstoque()
            val quantidadeTotal = Estoque.calcularQuantidadeTotalProdutos()

            navController.navigate("estatisticasDosProdutos/$valorTotal/$quantidadeTotal")


        }) {
            Text("Estatísticas")
        }

        Spacer(Modifier.padding(30.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Voltar para Cadastro")
        }

    }
}


@Composable
fun DetalhesDoProduto(navController: NavController, produto: Produto){


    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {


        Text("Nome: ${produto.nome}\nCategoria: ${produto.categoria}\nPreço: R$ ${produto.preco}\n${produto.qtd} unidades")


        Button(onClick = {
            navController.popBackStack()
        }) {
            Text("Voltar")
        }
    }

}

@Composable
fun EstatisticasDosProdutos(navController: NavController, totalEstoque: Double, quantidadeTotalProdutos: Int) {

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Valor Total do Estoque: R$ $totalEstoque \n Quantidade Total de Produtos: $quantidadeTotalProdutos")
        Button(onClick = {
            navController.popBackStack()
        }){
            Text("Voltar")
        }
    }
}