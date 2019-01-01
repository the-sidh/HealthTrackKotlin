package br.com.fiap.healthtrack.medidas.dao.mongodb
import br.com.fiap.healthtrack.mongodb.BasicMongoDBDao
import br.com.fiap.healthtrack.mongodb.NoSQLClientManagerMongoDB
import br.com.fiap.healthtrack.medidas.*
import br.com.fiap.healthtrack.medidas.dao.MedidasDao
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import java.util.*

class AlimentacaoMongoDBDao : MedidasMongoDBDao<Alimentacao>() {
    override fun getMongoCollection(): MongoCollection<Alimentacao> = getDatabase().getCollection("alimentacao", Alimentacao::class.java)
}
class PressaoMongoDBDao : MedidasMongoDBDao<Pressao>() {
    override fun getMongoCollection(): MongoCollection<Pressao> = getDatabase().getCollection("pressao", Pressao::class.java)
}
class AtividadeFisicaMongoDBDao : MedidasMongoDBDao<AtividadeFisica>() {
    override fun getMongoCollection(): MongoCollection<AtividadeFisica> = getDatabase().getCollection("atividade-fisica", AtividadeFisica::class.java)
}
class PesoMongoDBDao : MedidasMongoDBDao<Peso>() {
    override fun getMongoCollection(): MongoCollection<Peso> = getDatabase().getCollection("peso", Peso::class.java)
}

abstract class MedidasMongoDBDao<T> : BasicMongoDBDao<T>(), MedidasDao<T> {

    override fun insertMedida(medida: T) {
        try {
            getMongoCollection().insertOne(medida)
        } finally {
            NoSQLClientManagerMongoDB.closeClient()
        }
    }

    override fun updateMedida(medida: T) {
        try {
            getMongoCollection().replaceOne(Filters.eq("_id", (medida as Medida)._id), medida);
        } finally {
            NoSQLClientManagerMongoDB.closeClient()
        }
    }

    override fun deleteMedida(medida: T) {
        try {
            getMongoCollection().deleteOne(Filters.eq("_id", (medida as Medida)._id))
        } finally {
            NoSQLClientManagerMongoDB.closeClient()
        }
    }

    override fun deleteMedida(id: String) {
        try {
            getMongoCollection().deleteOne(Filters.eq("_id", id))
        } finally {
            NoSQLClientManagerMongoDB.closeClient()
        }
    }

    override fun getMedida(id: String): T {
        var result: T
        try {
            result = getMongoCollection().find(Filters.eq("_id", id)).first()
        } finally {
            NoSQLClientManagerMongoDB.closeClient()
        }
        return result;
    }

    override fun getListaMedidas(email: String?): List<T> {
        val list = ArrayList<T>()
        try {
            val find = getMongoCollection().find(Filters.eq("user.email", email))
            for (t in find) {
                list.add(t)
            }
        } finally {
            NoSQLClientManagerMongoDB.closeClient()
        }
        return list
    }

    override fun purgeAll() {
        try {
            getMongoCollection().drop()
        } finally {
            NoSQLClientManagerMongoDB.closeClient()
        }

    }
}
