
import br.com.fiap.healthtrack.user.Genero
import br.com.fiap.healthtrack.user.User
import org.junit.jupiter.api.Test
import java.util.*
import br.com.fiap.healthtrack.medidas.Peso
import br.com.fiap.healthtrack.medidas.IMC
import br.com.fiap.healthtrack.medidas.OperacoesMedidaHelper
import junit.framework.Assert.assertTrue
import br.com.fiap.healthtrack.medidas.SituacaoPressao
import br.com.fiap.healthtrack.medidas.Pressao
import br.com.fiap.healthtrack.medidas.TipoAlimentacao
import br.com.fiap.healthtrack.medidas.Alimentacao
import br.com.fiap.healthtrack.medidas.dao.mongodb.PesoMongoDBDao


class HealthTrackTest{

    var user =  User("Pedro Fonseca", Date(), Genero.MASCULINO, 1.7, "sidharta.rezende@gmail.com", "")

    @Test
    fun testIMC(){
        val peso1 = Peso(user , Date(),40.0)
        val peso2 = Peso(user , Date(),46.0)
        val peso3 = Peso(user , Date(), 46.5)
        val peso4 = Peso(user , Date(),49.4)
        val peso5 = Peso(user , Date(),52.0)
        val peso6 = Peso(user , Date(),55.0)
        val peso7 = Peso(user , Date(),69.0)
        val peso8 = Peso(user , Date(), 75.5)
        val peso9 = Peso(user , Date(), 83.0)
        val peso10 = Peso(user , Date(),89.5)
        val peso11 = Peso(user , Date(), 98.0)
        val peso12 = Peso(user , Date(), 104.05)
        val peso13 = Peso(user , Date(),112.0)

        val IMC1 = OperacoesMedidaHelper.getIMC(user?.altura!!, peso1.peso)
        val IMC2 = OperacoesMedidaHelper.getIMC(user?.altura!!, peso2.peso)
        val IMC3 = OperacoesMedidaHelper.getIMC(user?.altura!!, peso3.peso)
        val IMC4 = OperacoesMedidaHelper.getIMC(user?.altura!!, peso4.peso)
        val IMC5 = OperacoesMedidaHelper.getIMC(user?.altura!!, peso5.peso)
        val IMC6 = OperacoesMedidaHelper.getIMC(user?.altura!!, peso6.peso)
        val IMC7 = OperacoesMedidaHelper.getIMC(user?.altura!!, peso7.peso)
        val IMC8 = OperacoesMedidaHelper.getIMC(user?.altura!!, peso8.peso)
        val IMC9 = OperacoesMedidaHelper.getIMC(user?.altura!!, peso9.peso)
        val IMC10 = OperacoesMedidaHelper.getIMC(user?.altura!!, peso10.peso)
        val IMC11 = OperacoesMedidaHelper.getIMC(user?.altura!!, peso11.peso)
        val IMC12 = OperacoesMedidaHelper.getIMC(user?.altura!!, peso12.peso)
        val IMC13 = OperacoesMedidaHelper.getIMC(user?.altura!!, peso13.peso)

        assertTrue(IMC1.equals(IMC.MAGREZA_GRAVE));
        assertTrue(IMC2.equals(IMC.MAGREZA_GRAVE));
        assertTrue(IMC3.equals(IMC.MAGREZA_MODERADA));
        assertTrue(IMC4.equals(IMC.MAGREZA_LEVE));
        assertTrue(IMC5.equals(IMC.MAGREZA_LEVE));
        assertTrue(IMC6.equals(IMC.SAUDAVEL));
        assertTrue(IMC7.equals(IMC.SAUDAVEL));
        assertTrue(IMC8.equals(IMC.SOBREPESO));
        assertTrue(IMC9.equals(IMC.SOBREPESO));
        assertTrue(IMC10.equals(IMC.OBESIDADE_1));
        assertTrue(IMC11.equals(IMC.OBESIDADE_1));
        assertTrue(IMC12.equals(IMC.OBESIDADE_2));
        assertTrue(IMC13.equals(IMC.OBESIDADE_2));
    }
    @Test
    fun testSituacaoPressao() {
        val p1 = Pressao(user , Date(),141, 91)// alta
        val p2 = Pressao(user , Date(),130, 91)// alta
        val p3 = Pressao(user , Date(),141, 85)// alta

        val p4 = Pressao(user , Date(),130, 81)// normal

        val p5 = Pressao(user , Date(),119, 75)// baixa
        val p6 = Pressao(user , Date(),130, 75)// baixa
        val p7 = Pressao(user , Date(),119, 81)// baixa

        assertTrue(SituacaoPressao.ELEVADA == OperacoesMedidaHelper.getSituacaoPressao(p1.sistolica, p1.diastolica))
        assertTrue(SituacaoPressao.ELEVADA == OperacoesMedidaHelper.getSituacaoPressao(p2.sistolica, p2.diastolica))
        assertTrue(SituacaoPressao.ELEVADA == OperacoesMedidaHelper.getSituacaoPressao(p3.sistolica, p3.diastolica))

        assertTrue(SituacaoPressao.NORMAL == OperacoesMedidaHelper.getSituacaoPressao(p4.sistolica, p4.diastolica))

        assertTrue(SituacaoPressao.ABAIXO_DO_NORMAL == OperacoesMedidaHelper.getSituacaoPressao(p5.sistolica, p5.diastolica))
        assertTrue(SituacaoPressao.ABAIXO_DO_NORMAL == OperacoesMedidaHelper.getSituacaoPressao(p6.sistolica, p6.diastolica))
        assertTrue(SituacaoPressao.ABAIXO_DO_NORMAL == OperacoesMedidaHelper.getSituacaoPressao(p7.sistolica, p7.diastolica))
    }

    @Test
    fun testAlimentacao() {
        val a = Alimentacao(user, Date(), 100,  TipoAlimentacao.ALMOCO,"")
        assertTrue("Almoço" == a.tipo.descricao)
    }

    @Test
    fun testPesoDaoNosql() {

        val peso1 = Peso(user, Date(), 40.0)
        val peso2 = Peso(user, Date(),46.0)
        val peso3 = Peso(user, Date(),46.5)
        val peso4 = Peso(user, Date(),49.4)
        val peso5 = Peso(user, Date(),52.0)
        val peso6 = Peso(user, Date(),55.0)
        val peso7 = Peso(user, Date(),69.0)
        val peso8 = Peso(user, Date(),75.5)
        val peso9 = Peso(user, Date(),83.0)
        val peso10 = Peso(user, Date(),89.5)

        val dao = PesoMongoDBDao()

        dao.purgeAll()

        dao.insertMedida(peso1)
        dao.insertMedida(peso2)
        dao.insertMedida(peso3)
        dao.insertMedida(peso4)
        dao.insertMedida(peso5)
        dao.insertMedida(peso6)
        dao.insertMedida(peso7)
        dao.insertMedida(peso8)
        dao.insertMedida(peso9)
        dao.insertMedida(peso10)

        var listaMedidas = dao.getListaMedidas("sidharta.rezende@gmail.com")

        assertTrue(listaMedidas.size == 10)
        listaMedidas.get(0).peso = 50.0
        dao.updateMedida(listaMedidas.get(0))
        listaMedidas = dao.getListaMedidas("sidharta.rezende@gmail.com")
        assertTrue(listaMedidas.get(0).peso === 50.0)
        dao.deleteMedida(listaMedidas.get(0))
        listaMedidas = dao.getListaMedidas("sidharta.rezende@gmail.com")
        assertTrue(listaMedidas.size == 9)
    }
}

