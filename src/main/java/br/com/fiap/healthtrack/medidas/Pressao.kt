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

class Pressao ( user : User, data : Date, var sistolica:Int, var diastolica : Int) : Medida( user, data){
    constructor() : this(User(), Date(), 0, 0)
}

enum class SituacaoPressao(id: String, descricao : String){
    NORMAL("1", "Normal"),
    ELEVADA("2","Elevada"),
    ABAIXO_DO_NORMAL("3","Abaixo do normal");
}

class PressaoCodec(val documentCodec : Codec<Document>) : CollectibleCodec<Pressao> {
    var document = Document()

    override fun getEncoderClass(): Class<Pressao> {
        return Pressao::class.java
    }

    override fun generateIdIfAbsentFromDocument(document: Pressao?): Pressao {
        if (!documentHasId(document)) {
            document?._id = UUID.randomUUID().toString()
        }
        return document!!
    }

    override fun encode(writer: BsonWriter?, value: Pressao?, encoderContext: EncoderContext?) {
        val user = value?.user
        val defaultDocumentCodec = MongoClientSettings.getDefaultCodecRegistry().get(Document::class.java)
        val userCodec = UserCodec(defaultDocumentCodec)
        document["user"] = userCodec.encodeExternal(user)
        document["_id"] =  value?._id
        document["sistolica"] =  value?.sistolica
        document["diastolica"] = value?.diastolica
        document["date"] = value?.data

        documentCodec?.encode(writer, document, encoderContext)
    }

    override fun documentHasId(document: Pressao?): Boolean {
        return document?._id != null;
    }

    override fun decode(reader: BsonReader?, decoderContext: DecoderContext?): Pressao {
        val document = documentCodec?.decode(reader, decoderContext)
        val medida = Pressao()
        val defaultDocumentCodec = MongoClientSettings.getDefaultCodecRegistry().get(Document::class.java)
        val userCodec = UserCodec(defaultDocumentCodec)
        medida.user = userCodec.getUserFromDocument(document?.get("user")!! as Document)
        medida.data = document?.getDate("date")!!
        medida._id =  document?.getString("_id")!!
        medida.sistolica = document?.getInteger("sistolica")!!
        medida.diastolica = document?.getInteger("diastolica")!!

        return medida
    }

    override fun getDocumentId(document: Pressao?): BsonValue {
        if (!documentHasId(document)) {
            throw IllegalStateException("The document does not contain an _id");
        }
        return  BsonString(document?._id);
    }

}