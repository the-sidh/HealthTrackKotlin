package br.com.fiap.healthtrack.javelin

import br.com.fiap.healthtrack.javelin.controllers.*
import br.com.fiap.healthtrack.user.User
import io.javalin.Handler
import io.javalin.Javalin
import io.javalin.security.Role
import io.javalin.security.SecurityUtil.roles
import javalinjwt.JavalinJWT
import com.auth0.jwt.interfaces.DecodedJWT
import io.javalin.Context
import io.javalin.apibuilder.ApiBuilder.*


enum class ApiRole : Role { ANYONE, USER_READ, USER_WRITE }


fun main(args: Array<String>) {

    val app = Javalin.create().apply {
        accessManager(Auth::accessManager)

        exception(Exception::class.java) { e, ctx -> e.printStackTrace() }
        error(404) { ctx -> ctx.json("not found") }
    }.start(7000)

    app.routes {

        path("pesos") {
            get(PesoController::getAll)
            post(PesoController::add)
        }
        path("pressao") {
            get(PressaoController::getAll)
            post(PressaoController::add)
        }
        path("atividade-fisica") {
            get(AtividadeFisicaController::getAll)
            post(AtividadeFisicaController::add)
        }
        path("alimentacao") {
            get(AlimentacaoController::getAll)
            post(AlimentacaoController::add)
        }
        path("generate") {
            get(TokenController::generate)
        }
        path("user"){
            post(UserController::add)
        }

    }

}
