package br.com.fiap.healthtrack.mongodb

import com.mongodb.client.MongoClients
import com.mongodb.MongoClientSettings
import org.bson.codecs.configuration.CodecRegistries

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection

import br.com.fiap.healthtrack.medidas.AlimentacaoCodec
import br.com.fiap.healthtrack.medidas.AtividadeFisicaCodec
import br.com.fiap.healthtrack.medidas.PesoCodec
import br.com.fiap.healthtrack.medidas.PressaoCodec
import br.com.fiap.healthtrack.user.TesteCodec
import br.com.fiap.healthtrack.user.UserCodec
import org.bson.Document
import com.mongodb.ServerAddress
import com.mongodb.connection.ClusterSettings
import java.util.Arrays.asList


abstract class BasicMongoDBDao<T> {
    fun getDatabase() = NoSQLClientManagerMongoDB.retrieveMongoClient().getDatabase("healthtrack")
    abstract fun getMongoCollection(): MongoCollection<T>
}

object NoSQLClientManagerMongoDB {

    var mongoClient: MongoClient? = null

    fun retrieveMongoClient(): MongoClient {
        val defaultDocumentCodec = MongoClientSettings.getDefaultCodecRegistry().get(Document::class.java)

        val alimentacaoCodec = AlimentacaoCodec(defaultDocumentCodec)
        val atividadeFisicaCodec = AtividadeFisicaCodec(defaultDocumentCodec)
        val pesoCodec = PesoCodec(defaultDocumentCodec)
        val pressaoCodec = PressaoCodec(defaultDocumentCodec)
        val userCodec = UserCodec(defaultDocumentCodec)
        val testeCodec = TesteCodec(defaultDocumentCodec)

        val codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), CodecRegistries.fromCodecs(userCodec, alimentacaoCodec, atividadeFisicaCodec, pesoCodec, pressaoCodec, testeCodec))
        val clusterSettings = ClusterSettings.builder()
                .hosts(asList(
                        ServerAddress("localhost", 27017)))
                .build()
        val settings = MongoClientSettings.builder().codecRegistry(codecRegistry).build()
        mongoClient = MongoClients.create(settings)
        return mongoClient as MongoClient
    }

    fun closeClient() {
        mongoClient?.close()

    }
}