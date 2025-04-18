package guilherme26bc.com.github.checkpoint2_crypto_monitor.model

class TicketResponse (
    val ticker: Ticker
)
class Ticker(
    val high: String,
    val low: String,
    val vol: String,
    val last: String,
    val buy: String,
    val sell: String,
    val date: Long
)