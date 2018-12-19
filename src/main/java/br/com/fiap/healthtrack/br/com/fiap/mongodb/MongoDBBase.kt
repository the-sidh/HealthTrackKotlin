package br.com.fiap.healthtrack.br.com.fiap.mongodb

import com.mongodb.client.MongoClients
import com.mongodb.MongoClientSettings
import org.bson.codecs.configuration.CodecRegistries

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection

import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.pojo.Conventions.ANNOTATION_CONVENTION
import org.bson.codecs.pojo.PojoCodecProvider
import java.util.Arrays.asList

abstract class BasicMongoDBDao<T>{
    fun getDatabase() = NoSQLClientManagerMongoDB.retrieveMongoClient().getDatabase("HealthTrack")
    abstract fun getMongoCollection(): MongoCollection<T>
}

object NoSQLClientManagerMongoDB {

    var mongoClient: MongoClient? = null

    fun retrieveMongoClient(): MongoClient {

        val codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), fromProviders(PojoCodecProvider.builder().conventions(asList(ANNOTATION_CONVENTION)).automatic(true).build()));

        val settings = MongoClientSettings.builder().codecRegistry(codecRegistry).build()
        mongoClient = MongoClients.create(settings)
        return mongoClient as MongoClient
    }

    fun closeClient() {
        mongoClient?.close()

    }
}