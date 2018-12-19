package br.com.fiap.healthtrack.medidas

import java.util.*
import br.com.fiap.healthtrack.user.User

class AtividadeFisica(user : User?, data: Date, calorias : Int, tipo : TipoAtividadeFisica, descricao : String) : MedidaCalorico<TipoAtividadeFisica>( user, data, calorias, tipo, descricao)

enum class TipoAtividadeFisica(val descricao : String){
    CORRIDA("corrida"),
    CAMINHADA("caminhada"),
    MUSCULACAO("musculação"),
    PEDALADA("pedalada")
}

