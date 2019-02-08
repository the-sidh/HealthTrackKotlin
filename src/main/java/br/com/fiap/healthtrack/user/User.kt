package br.com.fiap.healthtrack.user

import org.bson.*
import org.bson.codecs.Codec
import org.bson.codecs.CollectibleCodec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.pojo.annotations.BsonId
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import java.time.temporal.TemporalQueries.localDate


data class Teste(var nome : String){
    @BsonId
    var _id: String? = null
        set(value) {
            field = value}
        get() {
            return field
        }
}

data class User(var nome: String?, var dataNasc: LocalDate?, var genero: Genero?, var altura: Double?, var email: String?, var password: String?) {
    constructor() : this(null, null, null, null, null, null)

    @BsonId
    var _id: String? = null
        set(value) {
            field = value}
        get() {
            return field
        }
}

enum class Genero(val descricao: String) {
    MASCULINO("Masculino"), FEMININO("Feminino")
}

class TesteCodec (val documentCodec :Codec<Document> ): CollectibleCodec<Teste> {
    var document = Document()
    override fun generateIdIfAbsentFromDocument(document: Teste?): Teste {
        if (!documentHasId(document)) {
            document?._id = (UUID.randomUUID().toString());
        }
        return document!!;
    }


    override fun encode(writer: BsonWriter?, value: Teste?, encoderContext: EncoderContext?) {
        document["_id"] =  value?._id
            document["nome"] = value?.nome
    }

    override fun documentHasId(document: Teste?): Boolean {
        return document?._id != null;
    }

    override fun decode(reader: BsonReader?, decoderContext: DecoderContext?): Teste {
        val document = documentCodec?.decode(reader, decoderContext)
        val nome = document.getString("nome")
        val teste: Teste = Teste(nome)
        teste._id = document.getString("_id")

        return teste
    }

    override fun getDocumentId(document: Teste?): BsonValue {
        if (!documentHasId(document)) {
            throw IllegalStateException("The document does not contain an _id");
        }

        return BsonString(document?._id);
    }

    override fun getEncoderClass(): Class<Teste> {
        return Teste::class.java
    }


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
        if (dataNasc != null) {
            val date = Date.from(dataNasc.atStartOfDay(ZoneId.systemDefault()).toInstant())
            document1["dataNasc"] = date
        }
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
        document["_id"] =  user?._id
        if (nome != null)
            document["nome"] = nome
        if (dataNasc != null) {
            val date = Date.from(dataNasc.atStartOfDay(ZoneId.systemDefault()).toInstant())
            document["dataNasc"] = date
        }
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
        documentCodec?.encode(writer, document, encoderContext)

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
        user.dataNasc = document?.getDate("dataNasc")?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
        user.genero = if (document?.getString("genero") == "feminino") Genero.FEMININO else Genero.MASCULINO
        user.altura = document?.getDouble("altura")
        user.email = document?.getString("email")
        user.password = document?.getString("password")
        user._id = document?.getString("_id")
        return user
    }

    override fun getDocumentId(user: User?): BsonValue {
        if (!documentHasId(user)) {
            throw IllegalStateException("The document does not contain an _id");
        }

        return BsonString(user?._id);
    }

}