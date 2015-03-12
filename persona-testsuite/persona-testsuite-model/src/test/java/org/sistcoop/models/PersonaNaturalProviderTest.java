package org.sistcoop.models;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sistcoop.models.enums.Sexo;
import org.sistcoop.models.enums.TipoPersona;
import org.sistcoop.models.jpa.JpaPersonaNaturalProvider;
import org.sistcoop.models.jpa.JpaTipoDocumentoProvider;
import org.sistcoop.models.jpa.PersonaNaturalAdapter;
import org.sistcoop.models.jpa.TipoDocumentoAdapter;
import org.sistcoop.models.jpa.entities.PersonaEntity;
import org.sistcoop.provider.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Arquillian.class)
@UsingDataSet("empty.xml")
public class PersonaNaturalProviderTest {

	Logger log = LoggerFactory.getLogger(PersonaNaturalProviderTest.class);

	private Date date;	
	
	@Inject
	private PersonaNaturalProvider personaNaturalProvider;		
	
	@Inject
	private TipoDocumentoProvider tipoDocumentoProvider;	
	
	private TipoDocumentoModel tipoDocumentoModel;
	
	@Deployment
	public static WebArchive createDeployment() {
		File[] dependencies = Maven.resolver()
				.resolve("org.slf4j:slf4j-simple:1.7.10")
				.withoutTransitivity().asFile();

		WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
				/**persona-model-api**/
				.addClass(Provider.class)										
				.addClass(PersonaNaturalProvider.class)
				.addClass(TipoDocumentoProvider.class)
				
				.addPackage(PersonaNaturalModel.class.getPackage())
				.addPackage(TipoPersona.class.getPackage())
												
				/**persona-model-jpa**/				
				.addClass(JpaPersonaNaturalProvider.class)
				.addClass(PersonaNaturalAdapter.class)											
				
				.addClass(JpaTipoDocumentoProvider.class)
				.addClass(TipoDocumentoAdapter.class)					
				
				.addPackage(PersonaEntity.class.getPackage())
				
				.addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsWebInfResource("test-ds.xml");

		war.addAsLibraries(dependencies);

		return war;
	}			
	
	@Before
    public void executedBeforeEach() throws ParseException {   
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		date = formatter.parse("01/01/1991");
		
		tipoDocumentoModel = tipoDocumentoProvider.addTipoDocumento("DNI", "Documento nacional de identidad", 8, TipoPersona.NATURAL);
    }
	
	@After
    public void executedAfterEach() {      
		// remove all PersonaNaturalModel
		List<PersonaNaturalModel> personaNaturalModels = personaNaturalProvider.getPersonasNaturales();
		for (PersonaNaturalModel personaNaturalModel : personaNaturalModels) {
			personaNaturalProvider.removePersonaNatural(personaNaturalModel);
		}

		// remove all TipoDocumentoModels
		List<TipoDocumentoModel> tipoDocumentoModels = tipoDocumentoProvider.getTiposDocumento();
		for (TipoDocumentoModel tipoDocumentoModel : tipoDocumentoModels) {
			tipoDocumentoProvider.removeTipoDocumento(tipoDocumentoModel);
		}
    }
	   
	@Test
	public void addPersonaNatural() {
		PersonaNaturalModel model = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel, "12345678", "Flores", "Huertas", "Jhon wilber", 
				date, Sexo.MASCULINO);
		
		assertThat(model, is(notNullValue()));
	}
	
	@Test
	public void addPersonaNaturalUniqueTest()  {		
		PersonaNaturalModel model1 = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel, "12345678", "Flores", "Huertas", "Jhon wilber", 
				date, Sexo.MASCULINO);
		
		PersonaNaturalModel model2 = null;
		try {
			model2 = personaNaturalProvider.addPersonaNatural(
					"PER", tipoDocumentoModel, "12345678", "Flores", "Huertas", "Jhon wilber", 
					date, Sexo.MASCULINO);
		} catch (Exception e) {		
			assertThat(e, instanceOf(EJBException.class));						
		}	
		
		assertThat(model1, is(notNullValue()));
		assertThat(model2, is(nullValue()));
	}	

	@Test
	public void getPersonaNaturalById()  {
		PersonaNaturalModel model1 = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel, "12345678", "Flores", "Huertas", "Jhon wilber", 
				date, Sexo.MASCULINO);		
		
		Long id = model1.getId();		
		PersonaNaturalModel model2 = personaNaturalProvider.getPersonaNaturalById(id);
		
		assertThat(model1, is(equalTo(model2)));
	}
	
	@Test
	public void getPersonaNaturalByTipoNumeroDoc()  {							
		PersonaNaturalModel model1 = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel, "12345678", "Flores", "Huertas", "Jhon wilber", 
				date, Sexo.MASCULINO);	
		
		TipoDocumentoModel tipoDocumento = model1.getTipoDocumento();
		String numeroDocumento = model1.getNumeroDocumento();		
		PersonaNaturalModel model2 = personaNaturalProvider.getPersonaNaturalByTipoNumeroDoc(tipoDocumento, numeroDocumento);
		
		assertThat(model1, is(equalTo(model2)));
	}
	
	@Test
	public void getPersonasNaturales()  {										
		List<PersonaNaturalModel> models = personaNaturalProvider.getPersonasNaturales();
		for (PersonaNaturalModel personaNaturalModel : models) {
			assertThat(personaNaturalModel, is(notNullValue()));	
		}		
	}
	
	@Test
	public void getPersonasNaturalesFirstResulAndLimit()  {										
		List<PersonaNaturalModel> models = personaNaturalProvider.getPersonasNaturales(0, 10);
		for (PersonaNaturalModel personaNaturalModel : models) {
			assertThat(personaNaturalModel, is(notNullValue()));
		}
	}
	
	@Test
	public void searchForFilterText()  {										
		List<PersonaNaturalModel> models = personaNaturalProvider.searchForFilterText("f");
		for (PersonaNaturalModel personaNaturalModel : models) {
			assertThat(personaNaturalModel, is(notNullValue()));
		}		
	}
	
	@Test
	public void searchForFilterTextFirstResulAndLimit()  {										
		List<PersonaNaturalModel> models = personaNaturalProvider.searchForFilterText("f", 0, 10);
		for (PersonaNaturalModel personaNaturalModel : models) {
			assertThat(personaNaturalModel, is(notNullValue()));
		}		
	}	
	
	@Test
	public void removePersonaNatural()  {	
		PersonaNaturalModel model1 = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel, "12345678", "Flores", "Huertas", "Jhon wilber", 
				date, Sexo.MASCULINO);				
		
		Long id = model1.getId();		
		boolean result = personaNaturalProvider.removePersonaNatural(model1);
		
		PersonaNaturalModel model2 = personaNaturalProvider.getPersonaNaturalById(id);
		
		assertThat(result, is(true));
		assertThat(model2, is(nullValue()));				
	}
	
}
