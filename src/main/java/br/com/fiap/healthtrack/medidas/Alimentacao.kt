package br.com.fiap.healthtrack.medidas

import br.com.fiap.healthtrack.user.User
import java.util.*


class Alimentacao(user : User?, data: Date, calorias: Int, tipo: TipoAlimentacao, descricao: String) : MedidaCalorico<TipoAlimentacao>(user,  data, calorias, tipo, descricao)  {
  constructor() : this( null, Date(), 0 , TipoAlimentacao.ALMOCO,"");
}

enum class TipoAlimentacao(val descricao: String, var id :String) {
    CAFE_DA_MANHA("Café da manhã","1"),
    ALMOCO("Almoço","2"),
    JANTAR("Jantar","3")

}



