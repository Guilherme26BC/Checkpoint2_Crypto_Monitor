# Checkpoint 2 Kotlin development
Aluno: Guilherme Bezerra Carvalho
RM: 550282
Explicação das classes .kt do projeto android_crypto_monitor

### Descrição do projeto
O projeto consiste em um app para monitaramento de cryptomoeda, expecificamente do Bitcoin, para isso utilizando-se de uma API.

### Ferramentas utilizadas
- API do mercadoBitcoin.net

### Orderm de explicação
* TicketResponse.kt
* MercadoBitcoinService.kt
* MercadoBitcoinServiceFactory.kt
* MainActivity.kt


## TicketResponse.kt

### Descrição:
Essa classe é utilizada para criar o modelo de conversão do Json retornado pela API rest utilizada para consultar o valor das criptomoedas. Para que ele possa ser convertido.
Dentro dela criamos duas classes:
```
class TicketResponse (  
    val ticker: Ticker  
)
```
* A priemeira classe que criamos dentro desse arquivo é a classe TicketResponse, que será a responsável por ser a chave do Json. Nessa classe temos um atributo ticker do tipo Ticker, que é nossa outra classe desse arquivo. Importante destacar que a classe foi criada já como um construtor
```
class Ticker(  
    val high: String,  
    val low: String,  
    val vol: String,  
    val last: String,  
    val buy: String,  
    val sell: String,  
    val date: Long  
)
```
* A outra classe criada representa os dados do Json, o conteúdo própriamente dito, que é obtido pela API. Portanto seus atributos são todos os dados que estão presentes no Json de retorno da API.

## MercadoBitcoinService.kt
```
interface MercadoBitcoinService {
```
* Esse arquivo consiste em uma interface que servirá para fazer as nossas requisições HTTP.

```
 @GET("api/BTC/ticker/")  
   
```
* Primeiro importante destacar que estamos utilizando do Retrofit e do coroutines; o retrofit é uma biblioteca do java que é utilizada para requisições HTTP, e o coroutines é uma ferramenta que garante a funcionalidade assíncrona no Android.
* Estamos usando a tag do Retrofit @GET, que é o verbo que vamos utilizar, a nossa requisição. Junto da tag estamos passando como parâmetro o complemento de url: "api/BTC/ticker/" que no caso é o complemento necessário para a API que queremos.

```
 suspend fun getTicker(): Response<TicketResponse>  
}
```
* Na linha em baixo temos uma função, antes de declarar a função temos a palavra "suspend", que faz parte do coroutines, que garante assincronicidade para o método.
* A linha também indica o retorno de um objeto Response, do retrofit, que é do tipo TicketResponse, que é a nossa classe criada.

## MercadoBitcoinServiceFactory.kt
```
fun create(): MercadoBitcoinService {  
    val retrofit = Retrofit.Builder()  
        .baseUrl("https://www.mercadobitcoin.net/")  
        .addConverterFactory(GsonConverterFactory.create())  
        .build()  
  
    return retrofit.create(MercadoBitcoinService::class.java)  
}
```
* Foi criado uma função "create( )" que implementa a nossa interface "MercadoBitcoinService"
* Em seguida criamos um objeto "retrofit" que recebe como valor o nosso builder( )
```
.baseUrl("https://www.mercadobitcoin.net/")  
```
* Após o builder adicionou-se o método baseUrl recebendo de parâmetro a URL base da nossa API, que será concatenada com o resto da URL presente no GET da nossa interface.
```
.addConverterFactory(GsonConverterFactory.create())
.build()  
```
* Em seguida estamos adicionando o método addConverterFactory( ), passando como parametro o GsonConverterFactory.create, ou seja esse código será responsável por converter o JSON no nosso objeto Kotlin TiketResponse.
* Para finalizar esse comando usamos o build( ) para criar a instância do retrofit.

```
return retrofit.create(MercadoBitcoinService::class.java)  
```
* Esse return é o que vai conectar de fato essa classe com nossa interface. Ela utiliza do retrofit que foi instanciado e utiliza o método create para implementar automaticamente a interface, passada como parametro, MercadoBitcoinService. O ::class.java é a forma de referênciar como classe no kotlin.

## MainActivity.kt
```
class MainActivity : AppCompatActivity()
```
* Para esse projeto mudamos a herança da classe MainActivity para herdar de AppCompatActivity( ), que por sua vez herda de ComponentActivity( ), mas possui outros atributos como a toolbar.

```
override fun onCreate(savedInstanceState: Bundle?) {  
    super.onCreate(savedInstanceState)
```
* Nessa parte do código não teve nenhuma alteração, mantivemos a sobrescrita do método onCreate, passando o super com savedInstanceState da activity;

```
setContentView(R.layout.activity_main);
```
* Com essa linha linkamos a classe MainActivity.kt com o arquivo de layout activity_main.xml, usando da classe R, do próprio Android, para acessar o layout.

```
val toolbarMain :Toolbar = findViewById(R.id.toolbar_main);  
configureToolbar(toolbarMain)
``` 
* Nesse trecho de código, estamos criando um objeto "toolbarMain" da classe Toolbar para que possamos manipular configurar a nossa toolbar.
* O método findViewById( ) é um método que busca um objeto xml pelo id efaz um parse para o kotlin, ou seja converte o xml em um objeto manipulável kotlin.
* No nosso caso ele busca o objeto xml "toolbar_main" e faz o parse para utiliza-lo no kotlin, passando-o como conteúdo no objeto toolbarMain.
* Em seguida aplicamos o método que nós criamos, "configureToolBar( )" para configurar a toolbar, passando o objeto toolbarMain como parâmetro.

```
val btnRefresh : Button = findViewById(R.id.btn_refresh);
```
* Aqui igual feito antes, estamos utilizando o findViewById para buscar um objeto .xml e converte-lo em um objeto kotlin, nesse caso, estamos buscando o objeto "bnt_refresh"
```
btnRefresh.setOnClickListener{  
  makeRestCall()  
}
```
* Em seguida estamos configurando a ação do botão, nesse caso estamos configurando um listener para click (setOnClickListener), que de forma resumida toda vez que clicar no btnRefresh, ele executará o conteúdo.
* O conteúdo desse listener é um método criado no arquivo: o "makeRestCall( )"
--- 
### Explicação do método configureToolbar( )
```
private fun configureToolbar(toolbar: Toolbar){  
    setSupportActionBar(toolbar)  
    toolbar.setTitleTextColor(getColor(R.color.white))  
    supportActionBar?.setTitle(getText(R.string.app_title))  
    supportActionBar?.setBackgroundDrawable(getDrawable(R.color.primary))  
}
```

* Primeiro criamos um método privado para ser usado apenas nessa classe para configurar a toolbar.
* O primeiro comando dentro do método serve para colocar a toolbar criada como a padrão e utilizada dentro da nossa activity.
* Em seguida passamos a configurar esteticamente a nossa activity =, colocando a cor do texto como branca e o background como a cor primary, criada no xml colors.
* Além disso também definimos o texto de titulo que também foi criado dentro do xml Strings.
* Importante destacar que para puxar características criadas em outras partes do código utilizando o método getColor, getText, getDrawable.
 ---
### Explicação do método makeRestCall( )
```
 CoroutineScope(Dispatchers.Main).launch {
```
* Primeiro cria-se o escopo da coroutines focado na main.
* Depois usou-se o launch para poder lançar de fato esse escopo, criando um bloco de instruções que serão executadas de forma assíncrona.

```
try{  
    val service = MercadoBitcoinServiceFactory().create();  
    val response = service.getTicker();
```
* Após o termos lançado o escopo do coroutines, iniciamos um bloco tryCatch para que possamos tratar possíveis exceções.
* Em seguida criamos um objeto "service" que recebe como valor o retorno do método create( ) da classe MercadoBitcoinServiceFactory( ), que criamos.
* Também criamos um objeto "response" que recebe como valor a resposta do método, ou seja, ele vai completar a  url cadastrada no MercadoBitcoinServiceFactory, com o resto no método GET da interface MercadoBitcoinService, que por sua vez retorna o nosso ticker de resposta. Importante destacar que esse objeto receberá um Response< ticketResponse > como valor

```
if(response.isSuccessful){  
    val tickerResponse = response.body();
```
* Em seguida damos inicio à um bloco "if" que será executado caso o response seja um sucesso (código http na faixa de 200), ou seja, a requisição get seja executada com successo.
* A primeira instrução do bloco é justamente a criação de um objeto que armazenará o response.body( ), ou seja, armazenará o TicketResponse propriamente dito.
```
val lblValue: TextView = findViewById(R.id.lbl_value);  
val lblDate: TextView = findViewById(R.id.lbl_date);
``` 
* Após isso, criamos outros dois objetos do tipo TextView que recebem respectivamente o lbl_value e o lbl_date como valores,, após passarem por uma conversão de objeto .xml para kotlin pelo findViewById( ).
``` 
 val lastValue = tickerResponse?.ticker?.last?.toDoubleOrNull();  
if(lastValue!=null){  
    val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt","BR"));  
    lblValue.text = numberFormat.format(lastValue);  
}
```
* Nesse trecho criamos um objeto lastValue que receberá o nosso valor desejado, last. Para isso utilizamos do tickerResponse?.tickerw?last?.toDoubleOrNull( ), essa sequência é o caminho para chegar até o valor do last, finalizando com uma conversão para double ou nulo, pelo fato de termos dado essa possibilidade quando colocamos interrogação em frente aos objetos.
* Após esse objeto, demos início à mais um bloco "if", dessa vez ele será executado caso o nosso objeto lastValue seja diferente de nulo.
* Dentro dele criamos um objeto "numberFormat" para a formatação do nosso lastValue para os padrões brasileiros. Utilizando a classe NumberFormat e o método getCurrencyInstance, passando como parâmetro o Locale com as informações brasileiras, o que trará as informações para converter para o real brasileiro.
* Em seguida utilizamos o objeto lblValue e atribuimos a ele como texto, usando o .text, o lastValue passado pela nossa mascara de formatação criada logo acima.

```
val date  = tickerResponse?.ticker?.date?.let { Date(it*1000L) }  
val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());  
lblDate.text = sdf.format(date);
```
* Em seguida fizemos algo semelhante porém com a data. Criamos um objeto "date" que vai receber o timestamp pelo tickerResponse?.ticker?.date?.let, porém aqui estamos utilizando o let, pois precisamos fazer uma conversão na data, e o let só fará caso a data não seja nula, caso contrário retorna null.
* A data será retornada no padrão Unix timestamp, para converter de uma forma que possa ser formatado, usamos o Date( ) e passamos como parâmetro o tempo multiplicado por 1000 Long, para assim transformar em milisegundos.
* Em seguida criamos um outro objeto que será nossa mascara de formatação, utilizando o SimpleDateFormat no nosso padrão brasileiro.
* Para finalizar esse trecho atualizamos o texto da lblDate passando a data formatada.
```
}else{  
    val errorMessage = when(response.code()){  
        400 -> "Bad Request"  
  401 -> "Unauthorized"  
  403 -> "Forbidden"  
  404 -> "Not Found"  
  else -> "Unknown error"  
  }  
    Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()  
}
```
* Esse bloco else é em relação ao nosso primeiro if, ou seja o que verifica se o response foi um sucesso.
* Nesse caso então criamos um objeto que vai pegar o código do response.code e atribuir à um erro escrito, e caso seja um erro diferente dos apresentados apresenta como unknown error.
* Finalizando o bloco utilizamos de um alerta visual na tela, o Toast, para poder criar e exibir  a mensagem de erro para o usuário.
```
} catch (e: Exception) {  
  Toast.makeText(this@MainActivity, "Falha na chamada: ${e.message}", Toast.LENGTH_LONG).show()  
    }  
}
```
* Finalizando o bloco try, com o nosso catch tratando as exceções, também utilizando do toast exibimos a mensagem de falha para o usuário.
