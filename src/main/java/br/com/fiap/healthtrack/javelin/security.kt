package br.com.fiap.healthtrack.javelin

import br.com.fiap.healthtrack.javelin.controllers.TokenController
import br.com.fiap.healthtrack.user.User
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.javalin.Context
import io.javalin.Handler
import io.javalin.security.Role
import javalinjwt.JWTGenerator
import javalinjwt.JWTProvider

object TokenProvider {

    fun getProvider(): JWTProvider {
        val algorithm = Algorithm.HMAC256("very_secret")

        val verifier = JWT.require(algorithm).build()

        val generator: JWTGenerator<User> = JWTGenerator(
                { user: User, alg: Algorithm ->
                    val token = JWT.create().withClaim("email", user.email)
                    token.sign(alg)
                });
        return JWTProvider(algorithm, generator, verifier)
    }
}

object Auth {

    fun accessManager(handler: Handler, ctx: Context, permittedRoles: Set<Role>) {
        if (ctx.matchedPath() == "/generate")
            handler.handle(ctx)
        else
            try {
                TokenController.validate(ctx)
                handler.handle(ctx)
            } catch (e: Exception) {
                ctx.status(401).json("Unauthorized")
            }
    }

}
