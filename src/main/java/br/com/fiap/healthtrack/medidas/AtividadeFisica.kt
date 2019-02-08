package br.com.fiap.healthtrack.medidas

import java.util.*
import br.com.fiap.healthtrack.user.User
import br.com.fiap.healthtrack.user.UserCodec
import com.mongodb.MongoClientSettings
import org.bson.*
import org.bson.codecs.Codec
import org.bson.codecs.CollectibleCodec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext

class AtividadeFisica(user: User, data: Date, calorias: Int, tipo: TipoAtividadeFisica, descricao: String)
    : MedidaCalorico<TipoAtividadeFisica>(user, data, calorias, tipo, descricao) {
    constructor() : this(User(), Date(), 0, TipoAtividadeFisica.CAMINHADA, "")
}

enum class TipoAtividadeFisica(val descricao: String) {
    CORRIDA("Corrida"),
    CAMINHADA("Caminhada"),
    MUSCULACAO("Musculação"),
    PEDALADA("Pedalada")
}

class AtividadeFisicaCodec(val documentCodec : Codec<Document>) : CollectibleCodec<AtividadeFisica> {
    var document = Document()

    override fun getEncoderClass(): Class<AtividadeFisica> {
        return AtividadeFisica::class.java
    }

    override fun generateIdIfAbsentFromDocument(document: AtividadeFisica?): AtividadeFisica {
        if (!documentHasId(document)) {
            document?._id = UUID.randomUUID().toString()
        }
        return document!!
    }

    override fun encode(writer: BsonWriter?, value: AtividadeFisica?, encoderContext: EncoderContext?) {
        val user = value?.user
        val qualificadorTipo = value?.tipo
        val defaultDocumentCodec = MongoClientSettings.getDefaultCodecRegistry().get(Document::class.java)
        val userCodec = UserCodec(defaultDocumentCodec)
        document.put("user", userCodec.encodeExternal(user))
        document.put("_id", value?._id)
        document.put("calorias",  value?.calorias)
        val descricaoTipoAlimentacao = qualificadorTipo?.descricao
        document.put("tipoAtividadeFisica", descricaoTipoAlimentacao)
        document.put("date", value?.data)
        document.put("descricao",  value?.descricao)

        documentCodec?.encode(writer, document, encoderContext)
    }

    override fun documentHasId(document: AtividadeFisica?): Boolean {
        return document?._id != null;
    }

    override fun decode(reader: BsonReader?, decoderContext: DecoderContext?): AtividadeFisica {
        val document = documentCodec?.decode(reader, decoderContext)
        val medida = AtividadeFisica()
        val descTipoAtividadeFisica= document?.getString("tipoAtividadeFisica")
        val defaultDocumentCodec = MongoClientSettings.getDefaultCodecRegistry().get(Document::class.java)
        val userCodec = UserCodec(defaultDocumentCodec)
        medida.user = userCodec.getUserFromDocument(document?.get("user")!! as Document)

        val map = HashMap<String, TipoAtividadeFisica>()
        map.put("Caminhada", TipoAtividadeFisica.CAMINHADA);
        map.put("Corrida", TipoAtividadeFisica.CORRIDA);
        map.put("Musculação", TipoAtividadeFisica.MUSCULACAO);
        map.put("Pedalada", TipoAtividadeFisica.PEDALADA);

        val qualificadorMedidas = map[descTipoAtividadeFisica]
        medida.data = document?.getDate("date")!!
        medida._id =  document?.getString("_id")!!
        medida.calorias = document?.getInteger("calorias")!!
        medida.descricao = document?.getString("descricao")!!
        medida.tipo = qualificadorMedidas!!

        return medida
    }

    override fun getDocumentId(document: AtividadeFisica?): BsonValue {
        if (!documentHasId(document)) {
            throw IllegalStateException("The document does not contain an _id");
        }
        return  BsonString(document?._id);
    }

}
