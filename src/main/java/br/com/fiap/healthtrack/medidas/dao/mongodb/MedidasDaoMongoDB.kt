package br.com.fiap.healthtrack.medidas.dao.mongodb
import br.com.fiap.healthtrack.br.com.fiap.mongodb.BasicMongoDBDao
import br.com.fiap.healthtrack.br.com.fiap.mongodb.NoSQLClientManagerMongoDB
import br.com.fiap.healthtrack.medidas.*
import br.com.fiap.healthtrack.medidas.dao.MedidasDao
import br.com.fiap.healthtrack.user.dao.mongodbao.UserMongoDBDao
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
            getMongoCollection().updateOne(Filters.eq(medida),Filters.eq(medida));
        } finally {
            NoSQLClientManagerMongoDB.closeClient()
        }
    }

    override fun deleteMedida(medida: T) {
        try {
            getMongoCollection().deleteOne(Filters.eq(medida))
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

    override fun getListaMedidas(): List<T> {
        val list = ArrayList<T>()
        try {
            val find = getMongoCollection().find()
            for (t in find) {
                list.add(t)
            }
        } finally {
            NoSQLClientManagerMongoDB.closeClient()
        }
        return list
    }
}

fun main(args: Array<String>) {
    val user = UserMongoDBDao().getUserByEmail("sidharta.rezende@gmail.com")
    val dao = AlimentacaoMongoDBDao()
    val a1 = Alimentacao(user, Date(), 100, TipoAlimentacao.ALMOCO, "")
    dao.insertMedida(a1)
    println(dao.getListaMedidas().get(0).tipo.descricao)

}