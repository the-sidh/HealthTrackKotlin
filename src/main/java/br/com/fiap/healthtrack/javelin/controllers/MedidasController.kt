package br.com.fiap.healthtrack.javelin.controllers

import br.com.fiap.healthtrack.medidas.*
import br.com.fiap.healthtrack.medidas.dao.MedidasDao
import br.com.fiap.healthtrack.medidas.dao.mongodb.*
import br.com.fiap.healthtrack.user.User
import io.javalin.Context
import java.util.*


abstract class MedidaController<T>{

    abstract fun retrieveMedida(ctx: Context) : T

    abstract val dao : MedidasDao<T>

    fun getAll(ctx: Context) {
        val user = ctx.attribute<User>("user")
        ctx.json(dao.getListaMedidas(user!!.email))
    }

    fun add(ctx: Context) {
        val medida = retrieveMedida(ctx)
        dao.insertMedida(medida)
    }
}

object PesoController : MedidaController<Peso>() {

    override val dao = PesoMongoDBDao()

    override fun retrieveMedida(ctx: Context) : Peso {
        val user = ctx.attribute<User>("user")
        val pesoEmKg = ctx.formParam("peso")!!.toDouble()
        var peso = Peso(user!!, Date(), pesoEmKg)
        return peso
    }
}


object PressaoController : MedidaController<Pressao>() {

    override val dao = PressaoMongoDBDao()

    override fun retrieveMedida(ctx: Context) : Pressao {
        val user = ctx.attribute<User>("user")
        val sistolica = ctx.formParam("sistolica")!!.toInt()
        val diastolica = ctx.formParam("diastolica")!!.toInt()
        var pressao = Pressao(user!!, Date(), sistolica, diastolica)
        return pressao
    }
}

object AtividadeFisicaController : MedidaController<AtividadeFisica>() {

    override val dao = AtividadeFisicaMongoDBDao()

    override fun retrieveMedida(ctx: Context) : AtividadeFisica {
        val user = ctx.attribute<User>("user")
        val calorias= ctx.formParam("calorias")!!.toInt()
        val codTipo= ctx.formParam("tipo")!!.toInt()
        val descricao =  ctx.formParam("descricao")!!.toString()
        val tipo = retrieveTipo(codTipo)

        var atividadeFisica = AtividadeFisica(user!!, Date(), calorias, tipo, descricao)
        return atividadeFisica
    }

    private fun retrieveTipo(codTipo: Int): TipoAtividadeFisica {
        val tipoAtividadeFisica : TipoAtividadeFisica
        when(codTipo){
            1 -> tipoAtividadeFisica = TipoAtividadeFisica.CAMINHADA
            2-> tipoAtividadeFisica =  TipoAtividadeFisica.CORRIDA
            3-> tipoAtividadeFisica = TipoAtividadeFisica.MUSCULACAO
            4-> tipoAtividadeFisica = TipoAtividadeFisica.PEDALADA
            else -> tipoAtividadeFisica = TipoAtividadeFisica.CAMINHADA
        }
        return tipoAtividadeFisica
    }
}

object AlimentacaoController : MedidaController<Alimentacao>() {

    override val dao = AlimentacaoMongoDBDao()

    override fun retrieveMedida(ctx: Context) : Alimentacao {
        val user = ctx.attribute<User>("user")
        val calorias= ctx.formParam("calorias")!!.toInt()
        val codTipo= ctx.formParam("tipo")!!.toInt()
        val descricao =  ctx.formParam("descricao")!!.toString()
        val tipo = retrieveTipo(codTipo)
        var alimentacao = Alimentacao(user!!, Date(), calorias, tipo, descricao)
        return alimentacao
    }

    private fun retrieveTipo(codTipo: Int): TipoAlimentacao {
        val tipoAlimentacao : TipoAlimentacao
        when(codTipo){
            1 -> tipoAlimentacao = TipoAlimentacao.CAFE_DA_MANHA
            2-> tipoAlimentacao =  TipoAlimentacao.ALMOCO
            3-> tipoAlimentacao = TipoAlimentacao.JANTAR
            else -> tipoAlimentacao = TipoAlimentacao.CAFE_DA_MANHA
        }
        return tipoAlimentacao
    }
}
