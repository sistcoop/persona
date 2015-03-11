package org.sistcoop.models;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJBException;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sistcoop.models.AccionistaModel;
import org.sistcoop.models.AccionistaProvider;
import org.sistcoop.models.PersonaJuridicaModel;
import org.sistcoop.models.PersonaJuridicaProvider;
import org.sistcoop.models.PersonaNaturalModel;
import org.sistcoop.models.PersonaNaturalProvider;
import org.sistcoop.models.TipoDocumentoModel;
import org.sistcoop.models.TipoDocumentoProvider;
import org.sistcoop.models.enums.Sexo;
import org.sistcoop.models.enums.TipoEmpresa;
import org.sistcoop.models.enums.TipoPersona;
import org.sistcoop.models.jpa.AccionistaAdapter;
import org.sistcoop.models.jpa.JpaAccionistaProvider;
import org.sistcoop.models.jpa.JpaPersonaJuridicaProvider;
import org.sistcoop.models.jpa.JpaPersonaNaturalProvider;
import org.sistcoop.models.jpa.JpaTipoDocumentoProvider;
import org.sistcoop.models.jpa.PersonaJuridicaAdapter;
import org.sistcoop.models.jpa.PersonaNaturalAdapter;
import org.sistcoop.models.jpa.TipoDocumentoAdapter;
import org.sistcoop.models.jpa.entities.PersonaEntity;
import org.sistcoop.provider.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Arquillian.class)
public class AccionistaProviderTest {

	Logger log = LoggerFactory.getLogger(AccionistaProviderTest.class);	
	
	@Inject
	private TipoDocumentoProvider tipoDocumentoProvider;
	
	@Inject
	private PersonaNaturalProvider personaNaturalProvider;
	
	@Inject
	private PersonaJuridicaProvider personaJuridicaProvider;		
	
	@Inject
	private AccionistaProvider accionistaProvider;
	
	private PersonaJuridicaModel personaJuridicaModel;	
	private PersonaNaturalModel representanteLegalModel;
	
	@Deployment
	public static WebArchive createDeployment() {
		File[] dependencies = Maven.resolver()
				.resolve("org.slf4j:slf4j-simple:1.7.10")
				.withoutTransitivity().asFile();

		WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
				/**persona-model-api**/
				.addClass(Provider.class)										
				.addClass(TipoDocumentoProvider.class)
				.addClass(PersonaNaturalProvider.class)
				.addClass(PersonaJuridicaProvider.class)				
				.addClass(AccionistaProvider.class)
				
				.addPackage(TipoDocumentoModel.class.getPackage())
				.addPackage(TipoPersona.class.getPackage())								
				
				/**persona-model-jpa**/		
				.addClass(JpaTipoDocumentoProvider.class)
				.addClass(TipoDocumentoAdapter.class)		
						
				.addClass(JpaPersonaNaturalProvider.class)
				.addClass(PersonaNaturalAdapter.class)	
				
				.addClass(JpaPersonaJuridicaProvider.class)
				.addClass(PersonaJuridicaAdapter.class)						
				
				.addClass(JpaAccionistaProvider.class)
				.addClass(AccionistaAdapter.class)	
				
				.addPackage(PersonaEntity.class.getPackage())
				
				.addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsWebInfResource("test-ds.xml");

		war.addAsLibraries(dependencies);

		return war;
	}		

	@Before
    public void executedBeforeEach()  {    
		TipoDocumentoModel tipoDocumentoModel1 = tipoDocumentoProvider.addTipoDocumento("DNI", "Documento nacional de identidad", 8, TipoPersona.NATURAL);
		TipoDocumentoModel tipoDocumentoModel2 = tipoDocumentoProvider.addTipoDocumento("RUC", "Registro unico de contribuyente", 11, TipoPersona.JURIDICA);
				
		representanteLegalModel = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel1, "12345678", "Flores", "Huertas", "Jhon wilber", 
				Calendar.getInstance().getTime(), Sexo.MASCULINO);
		
		personaJuridicaModel = personaJuridicaProvider.addPersonaJuridica(
				representanteLegalModel, "PER", tipoDocumentoModel2, "10467793549", 
				"Softgreen S.A.C.", Calendar.getInstance().getTime(), TipoEmpresa.PRIVADA, true);								
    }
	
	@After
    public void executedAfterEach() {      
		
		//remove all AccionistaModel
		List<AccionistaModel> accionistasModels = personaJuridicaModel.getAccionistas();
		for (AccionistaModel accionistaModel : accionistasModels) {
			accionistaProvider.removeAccionista(accionistaModel);
		}
		
		//remove all PersonaNaturalModel
		List<PersonaNaturalModel> personaNaturalModels = personaNaturalProvider.getPersonasNaturales();
		for (PersonaNaturalModel personaNaturalModel : personaNaturalModels) {
			personaNaturalProvider.removePersonaNatural(personaNaturalModel);
		}

		//remove all PersonaJuridicaModel
		List<PersonaJuridicaModel> personaJuridicaModels = personaJuridicaProvider.getPersonasJuridicas();
		for (PersonaJuridicaModel personaJuridicaModel : personaJuridicaModels) {
			personaJuridicaProvider.removePersonaJuridica(personaJuridicaModel);
		}								
				
		//remove all TipoDocumentoModels				
		List<TipoDocumentoModel> tipoDocumentoModels = tipoDocumentoProvider.getTiposDocumento();
		for (TipoDocumentoModel tipoDocumentoModel : tipoDocumentoModels) {
			tipoDocumentoProvider.removeTipoDocumento(tipoDocumentoModel);
		}

    }
	   
	@Test
	public void addAccionista() {
		AccionistaModel model = accionistaProvider.addAccionista(personaJuridicaModel, representanteLegalModel, BigDecimal.TEN);
		
		assertThat(model, is(notNullValue()));
	}
	
	@Test
	public void addAccionistaUniqueTest()  {		
		AccionistaModel model1 = accionistaProvider.addAccionista(personaJuridicaModel, representanteLegalModel, BigDecimal.TEN);
		
		AccionistaModel model2 = null;
		try {
			model2 = accionistaProvider.addAccionista(personaJuridicaModel, representanteLegalModel, BigDecimal.TEN);
		} catch (Exception e) {		
			assertThat(e, instanceOf(EJBException.class));						
		}	
		
		assertThat(model1, is(notNullValue()));
		assertThat(model2, is(nullValue()));
	}
	
	@Test
	public void getAccionistaById()  {		
		AccionistaModel model1 =  accionistaProvider.addAccionista(personaJuridicaModel, representanteLegalModel, BigDecimal.TEN);
		
		Long id = model1.getId();		
		AccionistaModel model2 = accionistaProvider.getAccionistaById(id);
		
		assertThat(model1, is(equalTo(model2)));
	}
	
	@Test
	public void removeAccionista()  {	
		AccionistaModel model1 =  accionistaProvider.addAccionista(personaJuridicaModel, representanteLegalModel, BigDecimal.TEN);		
		
		Long id = model1.getId();		
		boolean result = accionistaProvider.removeAccionista(model1);
		
		AccionistaModel model2 = accionistaProvider.getAccionistaById(id);
		
		assertThat(result, is(true));
		assertThat(model2, is(nullValue()));				
	}
	
}