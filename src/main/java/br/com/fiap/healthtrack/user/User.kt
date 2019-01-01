package br.com.fiap.healthtrack.user

import org.bson.*
import org.bson.codecs.Codec
import org.bson.codecs.CollectibleCodec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import java.util.*

data class User(var nome: String?, var dataNasc: Date?, var genero: Genero?, var altura: Double?, var email: String?, var password: String?) {
    constructor() : this(null, null, null, null, null, null)

    var _id: String? = null
}

enum class Genero(val descricao: String) {
    MASCULINO("Masculino"), FEMININO("Feminino")
}


class UserCodec (val documentCodec :Codec<Document> ): CollectibleCodec<User> {
    var document = Document()

    override fun getEncoderClass(): Class<User> {
        return User::class.java
    }

    override fun generateIdIfAbsentFromDocument(user: User?): User {
        if (!documentHasId(user)) {
            user?._id = (UUID.randomUUID().toString());
        }
        return user!!;
    }

    fun encodeExternal(user: User?) : Document {

        val document1 = Document()
        val nome = user?.nome
        val dataNasc = user?.dataNasc
        val genero = user?.genero
        val altura = user?.altura
        val email = user?.email
        val password = user?.password

        if (nome != null)
            document1["nome"] = nome
        if (dataNasc != null)
            document1["dataNasc"] = dataNasc
        if (genero != null) {
            if (genero == Genero.FEMININO) {
                document1["genero"] = "feminino"
            } else {
                document1["genero"] = "masculino"
            }
        }
        if (altura != null)
            document1["altura"] = altura
        if (email != null)
            document1["email"] = email
        if (password != null)
            document1["password"] = password
        return document1
    }

    override fun encode(writer: BsonWriter?, user: User?, encoderContext: EncoderContext?) {
        val nome = user?.nome
        val dataNasc = user?.dataNasc
        val genero = user?.genero
        val altura = user?.altura
        val email = user?.email
        val password = user?.password

        if (nome != null)
            document["nome"] = nome
        if (dataNasc != null)
            document["dataNasc"] = dataNasc
        if (genero != null) {
            if (genero == Genero.FEMININO) {
                document["genero"] = "feminino"
            } else {
                document["genero"] = "masculino"
            }
        }
        if (altura != null)
            document["altura"] = altura
        if (email != null)
            document["email"] = email
        if (password != null)
            document["password"] = password

    }

    override fun documentHasId(user: User?): Boolean {
        return user?._id != null;
    }

    override fun decode(reader: BsonReader?, decoderContext: DecoderContext?): User {
        val document = documentCodec?.decode(reader, decoderContext)
        val user = getUserFromDocument(document)
        return user
    }

    fun getUserFromDocument(document: Document?): User {
        val user = User()
        user.nome = document?.getString("nome")
        user.dataNasc = document?.getDate("dataNasc")
        user.genero = if (document?.getString("genero") == "feminino") Genero.FEMININO else Genero.MASCULINO
        user.altura = document?.getDouble("altura")
        user.email = document?.getString("email")
        user.password = document?.getString("password")
        return user
    }

    override fun getDocumentId(user: User?): BsonValue {
        if (!documentHasId(user)) {
            throw IllegalStateException("The document does not contain an _id");
        }

        return BsonString(user?._id);
    }

}