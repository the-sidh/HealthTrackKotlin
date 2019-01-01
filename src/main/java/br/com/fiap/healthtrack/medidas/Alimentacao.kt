package br.com.fiap.healthtrack.medidas

import br.com.fiap.healthtrack.user.User
import br.com.fiap.healthtrack.user.UserCodec
import com.mongodb.MongoClientSettings
import org.bson.*
import org.bson.codecs.Codec
import org.bson.codecs.CollectibleCodec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import java.util.*
import java.util.HashMap

class Alimentacao(user: User, data: Date, calorias: Int, tipo: TipoAlimentacao, descricao: String) : MedidaCalorico<TipoAlimentacao>(user, data, calorias, tipo, descricao) {
    constructor() : this(User(), Date(), 0, TipoAlimentacao.ALMOCO, "");
}

enum class TipoAlimentacao(val descricao: String, var id: String) {
    CAFE_DA_MANHA("Café da manhã", "1"),
    ALMOCO("Almoço", "2"),
    JANTAR("Jantar", "3")
}

class AlimentacaoCodec(val documentCodec :Codec<Document> ) : CollectibleCodec<Alimentacao> {
    var document = Document()

    override fun getEncoderClass(): Class<Alimentacao> {
        return Alimentacao::class.java
    }

    override fun generateIdIfAbsentFromDocument(document: Alimentacao?): Alimentacao {
        if (!documentHasId(document)) {
            document?._id = UUID.randomUUID().toString()
        }
        return document!!
    }

    override fun encode(writer: BsonWriter?, value: Alimentacao?, encoderContext: EncoderContext?) {
        val user = value?.user
        val tipoAlimentacao = value?.tipo
        val defaultDocumentCodec = MongoClientSettings.getDefaultCodecRegistry().get(Document::class.java)
        val userCodec = UserCodec(defaultDocumentCodec)
        document.put("user", userCodec.encodeExternal(user))
        document.put("_id", value?._id)
        document.put("calorias",  value?.calorias)
        val descricaoTipoAlimentacao = tipoAlimentacao?.descricao
        document.put("tipoAlimentacao", descricaoTipoAlimentacao)
        document.put("date", value?.data)
        document.put("descricao",  value?.descricao)

        documentCodec?.encode(writer, document, encoderContext)
    }

    override fun documentHasId(document: Alimentacao?): Boolean {
        return document?._id != null;
    }

    override fun decode(reader: BsonReader?, decoderContext: DecoderContext?): Alimentacao {
        val document = documentCodec?.decode(reader, decoderContext)
        val alimentacao = Alimentacao()
        val descTipoAlimentacao = document?.getString("tipoAlimentacao")

        val defaultDocumentCodec = MongoClientSettings.getDefaultCodecRegistry().get(Document::class.java)
        val userCodec = UserCodec(defaultDocumentCodec)
        alimentacao.user = userCodec.getUserFromDocument(document?.get("user")!! as Document)

        val map = HashMap<String, TipoAlimentacao>()
        map["Café da manhã"] = TipoAlimentacao.CAFE_DA_MANHA
        map["Almoço"] = TipoAlimentacao.ALMOCO
        map["Jantar"] = TipoAlimentacao.JANTAR

        val tipoAlimentacao = map[descTipoAlimentacao]
        alimentacao.data = document?.getDate("date")!!
        alimentacao._id =  document?.getString("_id")!!
        alimentacao.calorias = document?.getInteger("calorias")!!
        alimentacao.descricao = document?.getString("descricao")!!
        alimentacao.tipo = tipoAlimentacao!!

        return alimentacao
    }

    override fun getDocumentId(document: Alimentacao?): BsonValue {
        if (!documentHasId(document)) {
            throw IllegalStateException("The document does not contain an _id");
        }
        return  BsonString(document?._id);
    }

}



