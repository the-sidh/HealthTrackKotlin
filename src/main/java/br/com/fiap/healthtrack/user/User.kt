package br.com.fiap.healthtrack.user

import java.util.*

data class User(  var nome: String?, var dataNasc: Date?,  var genero: Genero?, var altura : Double?, var email : String?, var password:String?){
        constructor() : this(null, null,null,null, null , null)
        var id  : String? = null
}

enum class Genero(val descricao : String){
        MASCULINO("Masculino"), FEMININO("Feminino")
}