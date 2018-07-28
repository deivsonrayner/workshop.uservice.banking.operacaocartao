package application.rest.v1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import application.model.v1.Cartao;
import application.util.GeradorCartaoUtil;
import application.util.MongoService;

@Path("v1/cartoes")
public class CartoesService {



	@Inject
	MongoService mongoService;

	
	@GET
	@Path("listar")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listarcartoes(@QueryParam("cpf") String cpf) {
		Document pesquisa = new Document("cpfTitular", cpf);
		Collection<Cartao> cartaoCol = null;
		try {
			cartaoCol = mongoService.pesquisar(pesquisa, "cartoes").get();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return Response.ok(cartaoCol).build();
	}
	
	@GET
	@Path("carregardados")
	@Produces(MediaType.APPLICATION_JSON)
	public Response cargaDados(@QueryParam("isLocal") Boolean local) {

		MongoCollection<Document> collection = mongoService.getCollection("cartoes", local);

		Collection<String> listCPF = new ArrayList<String>();
		Collection<Cartao> cartoes = GeradorCartaoUtil.gerarCartao();

		for (Cartao cartao : cartoes) {

			Document document = new Document().append("bloqueado", cartao.bloqueado)
					.append("cpfTitular", cartao.cpfTitular).append("dataEmissao", cartao.dataEmissao)
					.append("_id", cartao.id).append("id", cartao.id).append("nomeTitular", cartao.nomeTitular)
					.append("numero", cartao.numero).append("saldo", cartao.saldo).append("validade", cartao.validade);

			collection.insertOne(document);
			listCPF.add(cartao.cpfTitular);
		}

		return Response.ok(listCPF).build();
	}



}
