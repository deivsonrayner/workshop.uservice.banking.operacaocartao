package application.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

import application.model.v1.Cartao;


public class GeradorCartaoUtil {
	
	protected static String[] nomes = new String[] {
			"Guilherme Garrau",
		    "Liana Fragoso",
		    "Olívia Linhares",
		    "Tomás Carneiro",
		    "Gastão Ourique",
		    "Donato Reis",
		    "Geraldina Delgado",
		    "Hugo Trigueiro",
		    "Gueda Vides",
		    "Gisela Santana",
		    "Adão Paiva",
		    "Aarão Ferraz",
		    "Adolfo Goulart",
		    "Silvério Andrade",
		    "Fernão Regueira",
		    "Josias Cesário",
		    "Moisés Valcanaia",
		    "Domingos Escobar",
		    "Angelino Cedraz",
		    "Marlene Fitas",
		    "Piedade Loureiro",
		    "Acacio Garrido",
		    "Lucília Cortesão",
		    "Ilídio Sobreira",
		    "Mateus Festas",
		    "Selma Liberato",
		    "Piedade Montenegro",
		    "Joana Rego",
		    "Diana Milheirão",
		    "Jadir Morgado",
		    "Milu Pinhal",
		    "Dinis Tabosa",
		    "Débora Silva",
		    "Renan Café",
		    "Florêncio Borba",
		    "Miriam Morais",
		    "Iuri Ruas",
		    "Luciana Vergueiro",
		    "Morgana Valcanaia",
		    "Nádia Dutra"
	};
	
	public static String gerarNome() {
		int idx = GeradorCartaoUtil.getRandomNumberInRange(1, nomes.length);
		return nomes[idx];
	}
	
	public static Cartao gerarCartao() {
		Cartao cartao = new Cartao();
		cartao.bloqueado = new Boolean(getRandomNumberInRange(0, 1) == 0);
		cartao.cpfTitular = new String(getRandomNumberInRange(100, 999)+".") + new String(getRandomNumberInRange(100, 999)+".") + new String(getRandomNumberInRange(100, 999)+"-") + new String(getRandomNumberInRange(10, 99)+"");
		cartao.dataEmissao = new Date(getRandomNumberInRange(2013, 2017), getRandomNumberInRange(01, 12), getRandomNumberInRange(1, 28));
		cartao.nomeTitular = gerarNome();
		cartao.numero = new String(getRandomNumberInRange(1000, 9999)+" ") + new String(getRandomNumberInRange(1000, 9999)+" ") + new String(getRandomNumberInRange(1000, 9999)+" ") + new String(getRandomNumberInRange(1000, 9999)+"");
		cartao.id = cartao.numero.trim();
		cartao.saldo = new BigDecimal(getRandomNumberInRange(1000, 20000));
		cartao.validade = new Date(getRandomNumberInRange(2019, 2026), getRandomNumberInRange(01, 12), getRandomNumberInRange(1, 28));
		
		return cartao;
	}
	
	protected static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	

}
