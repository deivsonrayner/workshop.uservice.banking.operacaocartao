package application.rest.v1;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import application.model.v1.Cartao;
import application.util.GeradorCartaoUtil;

@Path("v1/cartoes")
public class CartoesService {
	
	protected static MongoClient mongoClient = null;
	
	public MongoClient getMongoClient() {
		if (this.mongoClient == null) {
			this.mongoClient = MongoClients.create(this.getMongoHost());
		}
		
		return this.mongoClient;
	}
	
	public String getMongoHost() {
		return "mongodb:// 169.57.160.83:27017";
	}
	
	public MongoCollection<Document> getCollection(String name) {
		MongoDatabase dataservice = this.getMongoClient().getDatabase("operacoescartaodb");
		return  (MongoCollection<Document>) dataservice.getCollection(name);
	}
	
	
	 @GET
	 @Path("listar")
	 @Produces(MediaType.APPLICATION_JSON)
	public Response listarcartoes( @QueryParam("cpf") String cpf) {
		MongoCollection<Document> collection = this.getCollection("cartoes");
		FindIterable<Document> result =  collection.find(new Document("cpfTitular",cpf));
		Collection<Cartao> cartaoCol = new ArrayList<Cartao>();
		
		for (Document document : result) {
			Cartao cartao = new Cartao();
			cartao.bloqueado = document.getBoolean("bloqueado");
			cartao.cpfTitular = document.getString("cpfTitular");
			cartao.dataEmissao = document.getDate("dataEmissao");
			cartao.id = document.getString("id");
			cartao.nomeTitular = document.getString("nomeTitular");
			cartao.numero = document.getString("numero");
			cartao.saldo = new BigDecimal(document.getDouble("saldo"));
			cartao.validade = document.getDate("validade");
			cartaoCol.add(cartao);
		}
		
		return Response.ok(cartaoCol).build();
		
	}
	
	public void dataserviceOperation() {
		
	}
	
	public void dataserviceOperationFallBack() {
		
	}
	
	
	 @GET
	 @Path("carregardados")
	 @Produces(MediaType.APPLICATION_JSON)
	public Response cargaDados(@QueryParam("isLocal") Boolean local) {
		 
		MongoCollection<Document> collection = this.getCollection("cartoes");
		
		Collection<String> listCPF = new ArrayList<String>();
		Collection<Cartao> cartoes = GeradorCartaoUtil.gerarCartao();
		
		for (Cartao cartao : cartoes) {
			
			Document document = new Document()
					.append("bloqueado", cartao.bloqueado)
					.append("cpfTitular", cartao.cpfTitular)
					.append("dataEmissao", cartao.dataEmissao)
					.append("_id", cartao.id)
					.append("id", cartao.id)
					.append("nomeTitular", cartao.nomeTitular)
					.append("numero", cartao.numero)
					.append("saldo", cartao.saldo)
					.append("validade", cartao.validade);
			
			collection.insertOne(document);
			listCPF.add(cartao.cpfTitular);
		}
		
		
		return Response.ok(listCPF).build();
	}
	
}
