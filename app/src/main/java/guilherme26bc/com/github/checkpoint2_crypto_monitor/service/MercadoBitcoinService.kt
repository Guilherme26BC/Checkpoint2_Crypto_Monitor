package guilherme26bc.com.github.checkpoint2_crypto_monitor.service

import guilherme26bc.com.github.checkpoint2_crypto_monitor.model.TicketResponse
import retrofit2.Response
import retrofit2.http.GET

interface MercadoBitcoinService {

    @GET("api/BTC/ticker/")
    suspend fun getTicker(): Response<TicketResponse>
}