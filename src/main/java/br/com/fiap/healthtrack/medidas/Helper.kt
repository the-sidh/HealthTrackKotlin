package br.com.fiap.healthtrack.medidas

import java.math.BigDecimal
import java.math.RoundingMode

object OperacoesMedidaHelper{

    fun getSituacaoPressao(sistolica : Int, diastolica : Int) : SituacaoPressao {
        val situacao : SituacaoPressao;

        if(sistolica>=140 ||diastolica>=90) {
            situacao = SituacaoPressao.ELEVADA;
        }else if(sistolica <=120 || diastolica <=80) {
            situacao = SituacaoPressao.ABAIXO_DO_NORMAL;
        }else {
            situacao = SituacaoPressao.NORMAL;
        }
        return situacao;
    }

    fun getIMC(altura : Double, peso : Double) : IMC {
        var theImc : IMC = IMC.SAUDAVEL;

        val squareHeigth : BigDecimal =  BigDecimal(altura).multiply(BigDecimal(altura))
        val imcCalc : BigDecimal  = BigDecimal(peso).divide(squareHeigth , RoundingMode.HALF_UP)
        if(imcCalc.compareTo(BigDecimal(16))<=0) {
            theImc = IMC.MAGREZA_GRAVE;
        }else if(imcCalc.compareTo( BigDecimal(16))>0 && imcCalc.compareTo( BigDecimal(17))<=0) {
            theImc = IMC.MAGREZA_MODERADA;
        }else if(imcCalc.compareTo( BigDecimal(17))>0 && imcCalc.compareTo( BigDecimal(18.5))<=0) {
            theImc = IMC.MAGREZA_LEVE;
        }else if(imcCalc.compareTo( BigDecimal(18.5))>0 && imcCalc.compareTo( BigDecimal(25))<=0) {
            theImc = IMC.SAUDAVEL;
        }else if(imcCalc.compareTo( BigDecimal(25))>0 && imcCalc.compareTo( BigDecimal(30))<=0) {
            theImc = IMC.SOBREPESO;
        }else if(imcCalc.compareTo( BigDecimal(30))>0 && imcCalc.compareTo( BigDecimal(35))<=0) {
            theImc = IMC.OBESIDADE_1;
        }else if(imcCalc.compareTo( BigDecimal(35))>0) {
            theImc = IMC.OBESIDADE_2;
        }
        return theImc;

    }

}
