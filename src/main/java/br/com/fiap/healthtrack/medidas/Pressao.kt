package br.com.fiap.healthtrack.medidas

import br.com.fiap.healthtrack.medidas.Medida
import br.com.fiap.healthtrack.user.User
import java.util.*

class Pressao (user : User?, data : Date) : Medida(user, data)

enum class SituacaoPressao(id: String, descricao : String){
    NORMAL("1", "Normal"),
    ELEVADA("2","Elevada"),
    ABAIXO_DO_NORMAL("3","Abaixo do normal");
}