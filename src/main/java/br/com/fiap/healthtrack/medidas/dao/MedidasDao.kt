package br.com.fiap.healthtrack.medidas.dao

interface MedidasDao<Medidas> {

    fun getListaMedidas(email: String?): List<Medidas>
    fun getMedida(id: String): Medidas
    fun insertMedida(medida: Medidas)
    fun updateMedida(medida: Medidas)
    fun deleteMedida(medida: Medidas)
    fun deleteMedida(id: String)
    fun purgeAll()

}