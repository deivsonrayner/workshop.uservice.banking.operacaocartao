package application.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Random;

import application.model.v1.Cartao;


public class GeradorCartaoUtil {
	
	protected static Collection<Cartao> cartoes = new ArrayList<Cartao>();
	
	protected static String[] nomes = new String[] {
			"Guilherme Garrau:123.123.123-11",
		    "Liana Fragoso:123.123.223-11",
		    "Olívia Linhares:123.123.423-11",
		    "Tomás Carneiro:123.123.523-11",
		    "Gastão Ourique:123.123.623-11",
		    "Donato Reis:123.123.723-11",
		    "Geraldina Delgado:123.123.723-11",
		    "Hugo Trigueiro:123.123.823-11",
		    "Gueda Vides:123.123.923-11",
		    "Gisela Santana:123.223.123-11",
		    "Adão Paiva:123.323.123-11",
		    "Aarão Ferraz:123.423.123-11",
		    "Adolfo Goulart:123.523.123-11",
		    "Silvério Andrade:123.523.123-11",
		    "Fernão Regueira:123.623.123-11",
		    "Josias Cesário:123.723.123-11",
		    "Moisés Valcanaia:123.823.123-11",
		    "Domingos Escobar:123.923.123-11",
		    "Angelino Cedraz:223.123.123-11",
		    "Marlene Fitas:323.123.123-11",
		    "Piedade Loureiro:423.123.123-11",
		    "Acacio Garrido:523.123.123-11",
		    "Lucília Cortesão:623.123.123-11",
		    "Ilídio Sobreira:723.123.123-11",
		    "Mateus Festas:823.123.123-11",
		    "Selma Liberato:923.123.123-11",
		    "Piedade Montenegro:143.123.123-11",
		    "Joana Rego:123.153.153-11",
		    "Diana Milheirão:124.124.124-11",
		    "Jadir Morgado:126.125.124-11",
		    "Milu Pinhal:122.122.122-11",
		    "Dinis Tabosa:126.126.126-11",
		    "Débora Silva:121.121.121-11",
		    "Renan Café:129.129.129-11",
		    "Florêncio Borba:120.120.120-11",
		    "Miriam Morais:123.111.111-11",
		    "Iuri Ruas:122.122.122-11",
		    "Luciana Vergueiro:122.122.223-11",
		    "Morgana Valcanaia:133.133.133-11",
		    "Nádia Dutra:173.173.727-11"
	};
	
	public static String gerarNome() {
		int idx = GeradorCartaoUtil.getRandomNumberInRange(1, nomes.length);
		return nomes[idx];
	}
	
	
	public static Collection<Cartao> gerarCartao() {
		
		if (cartoes.isEmpty()) {
			for (int idx = 0; idx < 50; idx++) {
				Cartao cartao = new Cartao();
				String ident = gerarNome();
				String nome = ident.split(":")[0];
				String cpf = ident.split(":")[1];
				
				cartao.bloqueado = new Boolean(getRandomNumberInRange(0, 1) == 0);
				cartao.cpfTitular = cpf;
				cartao.dataEmissao = new Date(getRandomNumberInRange(2013, 2017), getRandomNumberInRange(01, 12), getRandomNumberInRange(1, 28));
				cartao.nomeTitular = nome;
				cartao.numero = new String(getRandomNumberInRange(1000, 9999)+" ") + new String(getRandomNumberInRange(1000, 9999)+" ") + new String(getRandomNumberInRange(1000, 9999)+" ") + new String(getRandomNumberInRange(1000, 9999)+"");
				cartao.id = cartao.numero.trim();
				cartao.saldo = new Double(getRandomNumberInRange(1000, 20000));
				cartao.validade = new Date(getRandomNumberInRange(2019, 2026), getRandomNumberInRange(01, 12), getRandomNumberInRange(1, 28));
				cartoes.add(cartao);
			}	
		}
		
		return cartoes;
	}
	
	protected static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	

}
