package br.com.fiap.healthtrack.javelin

import br.com.fiap.healthtrack.javelin.controllers.PesoController
import br.com.fiap.healthtrack.javelin.controllers.TokenController
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
        path("generate") {
            get(TokenController::generate)
        }

//        get("/users/:user-id") { ctx ->
//            ctx.json(userDao.findById(ctx.pathParam("user-id").toInt())!!)
//        }
//
//        get("/users/email/:email") { ctx ->
//            ctx.json(userDao.findByEmail(ctx.pathParam("email"))!!)
//        }
//
//
//
      post("/users") { ctx ->
            val user = ctx.body<User>()
            userDao.save(name = user.name, email = user.email)
            ctx.status(201)
        }
//
//        patch("/users/:user-id") { ctx ->
//            val user = ctx.body<User>()
//            userDao.update(
//                    id = ctx.pathParam("user-id").toInt(),
//                    user = user
//            )
//            ctx.status(204)
//        }
//
//        delete("/users/:user-id") { ctx ->
//            userDao.delete(ctx.pathParam("user-id").toInt())
//            ctx.status(204)
//        }

    }

}
