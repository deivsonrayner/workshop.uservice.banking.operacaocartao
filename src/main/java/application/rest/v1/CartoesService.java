package application.rest.v1;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.BasicBSONEncoder;
import org.bson.BasicBSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;

import application.model.v1.Cartao;
import application.util.GeradorCartaoUtil;

@Path("v1/cartoes")
public class CartoesService {
	
	@Resource(name = "dataservice/remote/cartoes")
	protected DB dataservice;
	
	
	 @GET
	 @Produces(MediaType.APPLICATION_JSON)
	public Response listarcartoes( @PathParam("cpf") String cpf) {
		
		return null;
		
	}
	
	public void dataserviceOperation() {
		
	}
	
	public void dataserviceOperationFallBack() {
		
	}
	
	
	 @GET
	 @Path("carregardados")
	 @Produces(MediaType.APPLICATION_JSON)
	public Response cargaDados(@QueryParam("isLocal") Boolean local) {
		
		
		MongoCollection<DBObject> collection = (MongoCollection<DBObject>) dataservice.getCollection("cartoes");
		Collection<String> listCPF = new ArrayList<String>();
		Collection<Cartao> cartoes = GeradorCartaoUtil.gerarCartao();
		
		for (Cartao cartao : cartoes) {
			
			DBObject document = BasicDBObjectBuilder.start()
					.add("bloqueado", cartao.bloqueado)
					.add("cpfTitular", cartao.cpfTitular)
					.add("dataEmissao", cartao.dataEmissao)
					.add("_id", cartao.id)
					.add("id", cartao.id)
					.add("nomeTitular", cartao.nomeTitular)
					.add("numero", cartao.numero)
					.add("saldo", cartao.saldo)
					.add("validade", cartao.validade)
					.get();
			
			collection.insertOne(document);
			listCPF.add(cartao.cpfTitular);
		}
		
		
		return Response.ok(listCPF).build();
	}
	
}
