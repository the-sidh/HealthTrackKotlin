package br.com.fiap.healthtrack.javelin.controllers

import br.com.fiap.healthtrack.javelin.TokenProvider
import br.com.fiap.healthtrack.user.User
import br.com.fiap.healthtrack.user.dao.mongodbao.UserMongoDBDao
import io.javalin.Context
import javalinjwt.JavalinJWT
import javalinjwt.examples.JWTResponse


object TokenController {
   private val userDao = UserMongoDBDao()

    fun generate(ctx: Context) {
        val provider = TokenProvider.getProvider()
        val credentials = ctx.basicAuthCredentials()
        val email = credentials?.username
        val password = credentials?.password
        val user = userDao.getUserByEmail(email!!)
        if (validateUser(user, password)) {
            val token = provider.generateToken(user);
            ctx.json(JWTResponse(token));
        }else{
            ctx.status(401).json("Unauthorized")
        }
    }

    private fun validateUser(user: User, password: String?): Boolean {
        return user.password == password
    }

    fun validate(ctx: Context) {
        val provider = TokenProvider.getProvider()
        val token = JavalinJWT.getTokenFromHeader(ctx).get()
        val decodedJWT = provider.validateToken(token).get()
        val email = decodedJWT.getClaim("email").asString()
        val user = userDao.getUserByEmail(email!!)
        ctx.attribute("user", user)
    }
}