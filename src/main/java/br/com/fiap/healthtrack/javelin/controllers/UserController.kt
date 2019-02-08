package br.com.fiap.healthtrack.javelin.controllers

import br.com.fiap.healthtrack.medidas.Peso
import br.com.fiap.healthtrack.medidas.dao.mongodb.PesoMongoDBDao
import br.com.fiap.healthtrack.user.Genero
import br.com.fiap.healthtrack.user.User
import br.com.fiap.healthtrack.user.dao.UserDao
import br.com.fiap.healthtrack.user.dao.mongodbao.UserMongoDBDao
import io.javalin.Context
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object UserController {

    val dao = UserMongoDBDao()


    fun add(ctx: Context) {
        val email = ctx.formParam("email")!!.toString()
        var nome: String = ctx.formParam("nome")!!.toString()
        var dataNasc = LocalDate.parse(ctx.formParam("data")!!.toString(), DateTimeFormatter.ISO_DATE)
        var genero: Genero = if (ctx.formParam("genero")!!.toString() == "feminino") Genero.FEMININO else Genero.MASCULINO
        var altura = ctx.formParam("altura")!!.toDouble()
        var password = ctx.formParam("password")!!.toString()
        val user = User(nome, dataNasc, genero, altura, email, password)
        dao.addUser(user)
    }
}
