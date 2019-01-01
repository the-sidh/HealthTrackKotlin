package br.com.fiap.healthtrack.javelin.controllers

import br.com.fiap.healthtrack.medidas.Peso
import br.com.fiap.healthtrack.medidas.dao.mongodb.PesoMongoDBDao
import br.com.fiap.healthtrack.user.User
import io.javalin.Context
import java.util.*


object PesoController {

    val dao = PesoMongoDBDao()

    fun getAll(ctx: Context) {
        val user = ctx.attribute<User>("user")
        ctx.json(dao.getListaMedidas(user!!.email))
    }

    fun add(ctx: Context) {
        val user = ctx.attribute<User>("user")
        val pesoEmKg = ctx.formParam("peso")!!.toDouble()
         var peso = Peso(user!!, Date(),pesoEmKg)
         dao.insertMedida(peso)
    }
}


