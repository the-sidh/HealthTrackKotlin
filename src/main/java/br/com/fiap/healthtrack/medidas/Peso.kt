package br.com.fiap.healthtrack.medidas

import br.com.fiap.healthtrack.medidas.Medida
import br.com.fiap.healthtrack.user.User
import br.com.fiap.healthtrack.user.UserCodec
import com.mongodb.MongoClientSettings
import org.bson.*
import org.bson.codecs.Codec
import org.bson.codecs.CollectibleCodec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import java.util.*

class Peso( user : User?, data : Date, var peso: Double) : Medida( user, data){
    constructor() : this(User(), Date(), 0.0)
    constructor(data : Date,  peso: Double) : this(null,  data, peso)
}

enum class IMC(id : String, descricao : String){
    MAGREZA_GRAVE("1", "Magreza grave"),
    MAGREZA_MODERADA("2", "Magreza moderada"),
    MAGREZA_LEVE("3", "Magreza leve"),
    SAUDAVEL("4", "Saudável"),
    SOBREPESO("5", "Sobrepeso"),
    OBESIDADE_1("6","Obesidade grau 1"),
    OBESIDADE_2("7","Obesidade grau 2");
}

class PesoCodec(val documentCodec : Codec<Document>) : CollectibleCodec<Peso> {
    var document = Document()

    override fun getEncoderClass(): Class<Peso> {
        return Peso::class.java
    }

    override fun generateIdIfAbsentFromDocument(document: Peso?): Peso {
        if (!documentHasId(document)) {
            document?._id = UUID.randomUUID().toString()
        }
        return document!!
    }

    override fun encode(writer: BsonWriter?, value: Peso?, encoderContext: EncoderContext?) {
        val user = value?.user
        val defaultDocumentCodec = MongoClientSettings.getDefaultCodecRegistry().get(Document::class.java)
        val userCodec = UserCodec(defaultDocumentCodec)
        document["user"] = userCodec.encodeExternal(user)
        document["_id"] =  value?._id
        document["peso"] =  value?.peso
        document["date"] = value?.data

        documentCodec?.encode(writer, document, encoderContext)
    }

    override fun documentHasId(document: Peso?): Boolean {
        return document?._id != null;
    }

    override fun decode(reader: BsonReader?, decoderContext: DecoderContext?): Peso {
        val document = documentCodec?.decode(reader, decoderContext)
        val medida = Peso()
        val defaultDocumentCodec = MongoClientSettings.getDefaultCodecRegistry().get(Document::class.java)
        val userCodec = UserCodec(defaultDocumentCodec)
        medida.user = userCodec.getUserFromDocument(document?.get("user")!! as Document)
        medida.data = document?.getDate("date")!!
        medida._id =  document?.getString("_id")!!
        medida.peso = document?.getDouble("peso")!!

        return medida
    }

    override fun getDocumentId(document: Peso?): BsonValue {
        if (!documentHasId(document)) {
            throw IllegalStateException("The document does not contain an _id");
        }
        return  BsonString(document?._id);
    }

}