package org.sistcoop.persona.services.resources.admin;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.sistcoop.persona.Jsend;
import org.sistcoop.persona.admin.client.resource.PersonaNaturalResource;
import org.sistcoop.persona.admin.client.resource.PersonasNaturalesResource;
import org.sistcoop.persona.models.PersonaNaturalModel;
import org.sistcoop.persona.models.PersonaNaturalProvider;
import org.sistcoop.persona.models.TipoDocumentoModel;
import org.sistcoop.persona.models.TipoDocumentoProvider;
import org.sistcoop.persona.models.search.PagingModel;
import org.sistcoop.persona.models.search.SearchCriteriaModel;
import org.sistcoop.persona.models.search.SearchResultsModel;
import org.sistcoop.persona.models.utils.ModelToRepresentation;
import org.sistcoop.persona.models.utils.RepresentationToModel;
import org.sistcoop.persona.representations.idm.PersonaNaturalRepresentation;
import org.sistcoop.persona.representations.idm.search.SearchResultsRepresentation;

@Stateless
public class PersonasNaturalesResourceImpl implements PersonasNaturalesResource {

    @Inject
    private PersonaNaturalProvider personaNaturalProvider;

    @Inject
    private TipoDocumentoProvider tipoDocumentoProvider;

    @Inject
    private RepresentationToModel representationToModel;

    @Inject
    private PersonaNaturalResource personaNaturalResource;

    @Context
    private UriInfo uriInfo;

    @Override
    public PersonaNaturalResource personaNatural(String personaNatural) {
        return personaNaturalResource;
    }

    @Override
    public Response create(PersonaNaturalRepresentation representation) {
        TipoDocumentoModel tipoDocumentoModel = tipoDocumentoProvider.findByAbreviatura(representation
                .getTipoDocumento());
        PersonaNaturalModel model = representationToModel.createPersonaNatural(representation,
                tipoDocumentoModel, personaNaturalProvider);

        return Response.created(uriInfo.getAbsolutePathBuilder().path(model.getId()).build())
                .header("Access-Control-Expose-Headers", "Location")
                .entity(Jsend.getSuccessJSend(model.getId())).build();
    }

    @Override
    public SearchResultsRepresentation<PersonaNaturalRepresentation> search(String tipoDocumento,
            String numeroDocumento, String filterText, int page, int pageSize) {

        SearchResultsModel<PersonaNaturalModel> results = null;
        if (tipoDocumento != null && numeroDocumento != null) {
            TipoDocumentoModel tipoDocumentoModel = tipoDocumentoProvider.findByAbreviatura(tipoDocumento);
            PersonaNaturalModel personaNaturalModel = personaNaturalProvider.findByTipoNumeroDocumento(
                    tipoDocumentoModel, numeroDocumento);

            List<PersonaNaturalModel> items = new ArrayList<>();
            if (personaNaturalModel != null) {
                items.add(personaNaturalModel);
            }

            results = new SearchResultsModel<>();
            results.setModels(items);
            results.setTotalSize(items.size());
        } else {
            PagingModel paging = new PagingModel();
            paging.setPage(page);
            paging.setPageSize(pageSize);

            SearchCriteriaModel searchCriteriaBean = new SearchCriteriaModel();
            searchCriteriaBean.setPaging(paging);

            results = personaNaturalProvider.search(searchCriteriaBean, filterText);
        }

        SearchResultsRepresentation<PersonaNaturalRepresentation> rep = new SearchResultsRepresentation<>();
        List<PersonaNaturalRepresentation> representations = new ArrayList<>();
        for (PersonaNaturalModel model : results.getModels()) {
            representations.add(ModelToRepresentation.toRepresentation(model));
        }
        rep.setTotalSize(results.getTotalSize());
        rep.setItems(representations);
        return rep;
    }

}
