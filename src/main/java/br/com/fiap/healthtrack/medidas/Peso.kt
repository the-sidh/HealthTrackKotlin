package br.com.fiap.healthtrack.medidas

import br.com.fiap.healthtrack.medidas.Medida
import br.com.fiap.healthtrack.user.User
import java.util.*

class Peso(user : User?, data : Date) : Medida(user, data)

enum class IMC(id : String, descricao : String){
    MAGREZA_GRAVE("1", "Magreza grave"),
    MAGREZA_MODERADA("2", "Magreza moderada"),
    MAGREZA_LEVE("3", "Magreza leve"),
    SAUDAVEL("4", "Saudável"),
    SOBREPESO("5", "Sobrepeso"),
    OBESIDADE_1("6","Obesidade grau 1"),
    OBESIDADE_2("7","Obesidade grau 2");
}