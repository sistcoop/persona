package org.sistcoop.admin.client.resource;

import java.util.List;

import javax.validation.constraints.Size;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.sistcoop.representations.idm.TipoDocumentoRepresentation;

@Produces(MediaType.APPLICATION_JSON)
public interface TiposDocumentoResource {

	@GET
	public List<TipoDocumentoRepresentation> findAll(
			@QueryParam("tipoPersona") 
			@Size(min = 1, max = 20) String tipoPersona,
			
			@QueryParam("estado") Boolean estado);

}