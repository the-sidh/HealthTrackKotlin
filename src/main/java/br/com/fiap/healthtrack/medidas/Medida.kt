package br.com.fiap.healthtrack.medidas

import br.com.fiap.healthtrack.user.User
import java.util.*

open class Medida(var user: User?, var  data: Date){
     //var id  : String? = null
}

open class MedidaCalorico<QualificadorMedidas>(user : User?, data: Date, var calorias: Int, var tipo: QualificadorMedidas, var descricao : String) : Medida(user, data)