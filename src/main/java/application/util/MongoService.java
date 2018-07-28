package application.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.Document;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.faulttolerance.Asynchronous;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import application.model.v1.Cartao;

@ApplicationScoped
public class MongoService {
	
	protected static MongoClient mongoRemoteClient = null;
	protected static MongoClient mongoLocalClient = null;
	
	public MongoClient getMongoClient(boolean isLocal) {
		if (isLocal) {
			if (this.mongoLocalClient == null) {
				this.mongoLocalClient = MongoClients.create(this.getMongoLocalHost());
			}
			return this.mongoLocalClient;
		} else {
			if (this.mongoRemoteClient == null) {
				this.mongoRemoteClient = MongoClients.create(this.getMongoRemoteHost());
			}
			return this.mongoRemoteClient;
		}
	}
	
	public String getMongoRemoteHost() {
		Config config = ConfigProvider.getConfig();
		return config.getValue("MONGO_REMOTE_URL", String.class);
	}
	
	public String getMongoLocalHost() {
		Config config = ConfigProvider.getConfig();
		return config.getValue("MONGO_LOCAL_URL", String.class);
	}

	public MongoCollection<Document> getCollection(String name, boolean isLocal) {
		MongoDatabase dataservice = this.getMongoClient(isLocal).getDatabase("operacoescartaodbv10");
		return (MongoCollection<Document>) dataservice.getCollection(name);
	}
	
	private Collection<Cartao> transformaRetorno(FindIterable<Document> result) {
		Collection<Cartao> cartaoCol = new ArrayList<Cartao>();
		
		for (Document document : result) {
			Cartao cartao = new Cartao();
			cartao.bloqueado = document.getBoolean("bloqueado");
			cartao.cpfTitular = document.getString("cpfTitular");
			cartao.dataEmissao = document.getDate("dataEmissao");
			cartao.id = document.getString("id").trim();
			cartao.nomeTitular = document.getString("nomeTitular");
			cartao.numero = document.getString("numero");
			cartao.saldo = document.getDouble("saldo");
			cartao.validade = document.getDate("validade");
			cartaoCol.add(cartao);
		}
		return cartaoCol;
	}
	
	//@CircuitBreaker(requestVolumeThreshold=2, failureRatio=0.50, delay=5000, successThreshold=2)
	@Fallback(fallbackMethod="pesquisarFallBack")
	@Retry(maxRetries=2, maxDuration=5000, delay=1000)
	@Timeout(5000)
	@Asynchronous
	public Future<Collection<Cartao>> pesquisar(Document document, String collectionName) throws Exception {
		MongoCollection<Document> collection = this.getCollection("cartoes",false);
		FindIterable<Document> result = collection.find(document);
		Collection<Cartao> cartaoCol = transformaRetorno(result);
		return CompletableFuture.completedFuture(cartaoCol);
	}
	
	public Future<Collection<Cartao>> pesquisarFallBack(Document document, String collectionName) throws Exception {
		MongoCollection<Document> collection = this.getCollection("cartoes",true);
		FindIterable<Document> result = collection.find(document);
		Collection<Cartao> cartaoCol = transformaRetorno(result);
		return CompletableFuture.completedFuture(cartaoCol);
	}

}