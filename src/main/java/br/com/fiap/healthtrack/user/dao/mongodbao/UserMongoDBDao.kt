package br.com.fiap.healthtrack.user.dao.mongodbao

import br.com.fiap.healthtrack.mongodb.BasicMongoDBDao
import br.com.fiap.healthtrack.mongodb.NoSQLClientManagerMongoDB
import br.com.fiap.healthtrack.user.Genero
import br.com.fiap.healthtrack.user.Teste
import br.com.fiap.healthtrack.user.User
import br.com.fiap.healthtrack.user.dao.UserDao
import com.mongodb.client.MongoCollection
import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil.close
import com.mongodb.client.model.Filters
import java.util.*

class UserMongoDBDao : BasicMongoDBDao<User>(), UserDao {
    override fun getMongoCollection(): MongoCollection<User> = getDatabase().getCollection("userss", User::class.java)


        override fun getUserById(id: String): User {
        val mongoCollection = getMongoCollection()
        var user : User?
        try {
            user = mongoCollection.find(Filters.eq("id", id)).first();
        } finally {
            NoSQLClientManagerMongoDB.closeClient()
        }

        return  user!!
    }

    override fun addUser(user: User) {
        val mongoCollection = getMongoCollection()
        try {
            mongoCollection.insertOne(user)
        } finally {
            NoSQLClientManagerMongoDB.closeClient()
        }
    }


    override fun updateUser(user: User) {
        val mongoCollection = getMongoCollection()
        try {
            mongoCollection.replaceOne(Filters.eq("id", user._id), user)
        } finally {
            NoSQLClientManagerMongoDB.closeClient()
        }
    }


    override fun getUser(): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserByEmail(email: String): User {
        val mongoCollection = getMongoCollection()
        var user : User?
        try {
            user = mongoCollection.find(Filters.eq("email", email)).first();
        } finally {
            NoSQLClientManagerMongoDB.closeClient()
        }
        return  user!!
    }
}


