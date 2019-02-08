package br.com.fiap.healthtrack.medidas


import br.com.fiap.healthtrack.user.User
import org.bson.codecs.pojo.annotations.BsonId
import java.util.*

open class Medida( var user: User?, var  data: Date){
    @BsonId var _id: String? = null
    set(value) {
        field = value}
    get() {
        return field
    }
}

open class MedidaCalorico<QualificadorMedidas>( user : User, data: Date, var calorias: Int, var tipo: QualificadorMedidas, var descricao : String)
    : Medida( user, data)