package org.sistcoop.models.jpa;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

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
import org.sistcoop.models.PersonaJuridicaModel;
import org.sistcoop.models.PersonaJuridicaProvider;
import org.sistcoop.models.PersonaNaturalModel;
import org.sistcoop.models.PersonaNaturalProvider;
import org.sistcoop.models.TipoDocumentoModel;
import org.sistcoop.models.TipoDocumentoProvider;
import org.sistcoop.models.enums.Sexo;
import org.sistcoop.models.enums.TipoEmpresa;
import org.sistcoop.models.enums.TipoPersona;
import org.sistcoop.models.jpa.entities.PersonaEntity;
import org.sistcoop.models.jpa.entities.PersonaJuridicaEntity;
import org.sistcoop.models.jpa.entities.PersonaNaturalEntity;
import org.sistcoop.models.jpa.entities.TipoDocumentoEntity;
import org.sistcoop.provider.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Arquillian.class)
public class JpaPersonaJuridicaProviderTest {

	Logger log = LoggerFactory.getLogger(JpaPersonaJuridicaProviderTest.class);

	@PersistenceContext
	private EntityManager em;

	@Resource           
	private UserTransaction utx; 
	
	@Inject
	private TipoDocumentoProvider tipoDocumentoProvider;
	
	@Inject
	private PersonaNaturalProvider personaNaturalProvider;
	
	@Inject
	private PersonaJuridicaProvider personaJuridicaProvider;		
	
	private TipoDocumentoModel tipoDocumentoModel;
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
				.addClass(PersonaJuridicaProvider.class)
				.addClass(PersonaNaturalProvider.class)
				
				.addPackage(TipoDocumentoModel.class.getPackage())
				.addPackage(TipoPersona.class.getPackage())								
				
				/**persona-model-jpa**/
				.addClass(JpaTipoDocumentoProvider.class)
				.addClass(TipoDocumentoAdapter.class)		
						
				.addClass(JpaPersonaNaturalProvider.class)
				.addClass(PersonaNaturalAdapter.class)	
				
				.addClass(JpaPersonaJuridicaProvider.class)
				.addClass(PersonaJuridicaAdapter.class)						
				
				.addPackage(PersonaEntity.class.getPackage())
				
				.addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsWebInfResource("test-ds.xml");

		war.addAsLibraries(dependencies);

		return war;
	}		

	@Before
    public void executedBeforeEach()  {    
		tipoDocumentoModel = tipoDocumentoProvider.addTipoDocumento("RUC", "Registro unico de contribuyente", 8, TipoPersona.JURIDICA);
		
		representanteLegalModel = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel, "12345678", "Flores", "Huertas", "Jhon wilber", 
				Calendar.getInstance().getTime(), Sexo.MASCULINO);					
    }
	
	@After
    public void executedAfterEach() throws NotSupportedException, 
    SystemException, SecurityException, IllegalStateException, 
    RollbackException, HeuristicMixedException, HeuristicRollbackException {      
		
		utx.begin();
	       
		//remove all PersonaJuridicaEntity
		List<Object> listPersonaJuridica = null;
		CriteriaQuery<Object> cqPersonaJuridica = this.em.getCriteriaBuilder().createQuery();
		cqPersonaJuridica.select(cqPersonaJuridica.from(PersonaJuridicaEntity.class));
		listPersonaJuridica = this.em.createQuery(cqPersonaJuridica).getResultList();
			
		for (Object object : listPersonaJuridica) {
			this.em.remove(object);
		}
				
		//remove all PersonaNaturalEntity
		List<Object> listPersonaNatural = null;
		CriteriaQuery<Object> cqPersonaNatural = this.em.getCriteriaBuilder().createQuery();
		cqPersonaNatural.select(cqPersonaNatural.from(PersonaNaturalEntity.class));
		listPersonaNatural = this.em.createQuery(cqPersonaNatural).getResultList();
			
		for (Object object : listPersonaNatural) {
			this.em.remove(object);
		}					
				
		//remove all TipoDocumentoEntity				
		List<Object> listTipoDocumento = null;
		CriteriaQuery<Object> cqTipoDocumento = this.em.getCriteriaBuilder().createQuery();
		cqTipoDocumento.select(cqTipoDocumento.from(TipoDocumentoEntity.class));
		listTipoDocumento = this.em.createQuery(cqTipoDocumento).getResultList();		
		for (Object object : listTipoDocumento) {
			this.em.remove(object);
		}
						
		utx.commit();
    }
	   
	@Test
	public void addPersonaJuridica()  {				
		PersonaJuridicaModel model = personaJuridicaProvider.addPersonaJuridica(
				representanteLegalModel, "PER", tipoDocumentoModel, "10467793549", 
				"Softgreen S.A.C.", Calendar.getInstance().getTime(), TipoEmpresa.PRIVADA, true);
		
		assertThat(model, is(notNullValue()));	
	}
	
	@Test
	public void addPersonaJuridicaUniqueTest()  {		
		PersonaJuridicaModel model1 = personaJuridicaProvider.addPersonaJuridica(
				representanteLegalModel, "PER", tipoDocumentoModel, "10467793549", 
				"Softgreen S.A.C.", Calendar.getInstance().getTime(), TipoEmpresa.PRIVADA, true);
				
		PersonaJuridicaModel model2 = null;
		try {
			model2 = personaJuridicaProvider.addPersonaJuridica(
					representanteLegalModel, "PER", tipoDocumentoModel, "10467793549", 
					"Softgreen S.A.C.", Calendar.getInstance().getTime(), TipoEmpresa.PRIVADA, true);
		} catch (Exception e) {		
			assertThat(e, instanceOf(EJBException.class));						
		}		
		
		assertThat(model1, is(notNullValue()));
		assertThat(model2, is(nullValue()));
	}
	
	/*@SuppressWarnings("unchecked")
	@Test
	public void addPersonaJuridicaNotnullAttibutesTest()  {		
		Object[] fields = new Object[8];
		fields[0] = representanteLegalModel;
		fields[1] = "PER";
		fields[2] = tipoDocumentoModel;
		fields[3] = "10467793549";
		fields[4] = "Softgreen S.A.C.";
		fields[5] = Calendar.getInstance().getTime();
		fields[6] = TipoEmpresa.PRIVADA;
		fields[7] = true;		
		
		Object aux = null;
		
		for (int i = 0; i < fields.length; i++) {			
			if(i > 0)
				fields[i - 1] = aux;
			aux = fields[i];
			fields[i] = null;			
								
			PersonaJuridicaModel model = null;
			try {				
				model = personaJuridicaProvider.addPersonaJuridica(
						(PersonaNaturalModel) fields[0], (String) fields[1], (TipoDocumentoModel) fields[2], (String) fields[3], 
						(String) fields[4], (Date) fields[5], (TipoEmpresa) fields[6], (Boolean) fields[7]);
			} catch (Exception e) {						
				assertThat(e, anyOf(instanceOf(NullPointerException.class), instanceOf(EJBException.class)));						
			}	
			
			assertThat(model, is(nullValue()));
		}	
	}*/
	
	@Test
	public void getPersonaJuridicaById()  {
		PersonaJuridicaModel model1 = personaJuridicaProvider.addPersonaJuridica(
				representanteLegalModel, "PER", tipoDocumentoModel, "10467793549", 
				"Softgreen S.A.C.", Calendar.getInstance().getTime(), TipoEmpresa.PRIVADA, true);
			
		Long id = model1.getId();		
		PersonaJuridicaModel model2 = personaJuridicaProvider.getPersonaJuridicaById(id);
		
		assertThat(model1, is(equalTo(model2)));
	}
	
	@Test
	public void getPersonaJuridicaByTipoNumeroDoc()  {							
		PersonaJuridicaModel model1 = personaJuridicaProvider.addPersonaJuridica(
				representanteLegalModel, "PER", tipoDocumentoModel, "10467793549", 
				"Softgreen S.A.C.", Calendar.getInstance().getTime(), TipoEmpresa.PRIVADA, true);			
		
		TipoDocumentoModel tipoDocumento = model1.getTipoDocumento();
		String numeroDocumento = model1.getNumeroDocumento();		
		PersonaJuridicaModel model2 = personaJuridicaProvider.getPersonaJuridicaByTipoNumeroDoc(tipoDocumento, numeroDocumento);
		
		assertThat(model1, is(equalTo(model2)));
	}
	
	@Test
	public void getPersonasJuridicasCount()  {	
		Long count1 = personaJuridicaProvider.getPersonasJuridicasCount();
		
		CriteriaQuery<Object> cq = this.em.getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<PersonaJuridicaEntity> rt = cq.from(PersonaJuridicaEntity.class);
        cq.select(this.em.getCriteriaBuilder().count(rt));
        javax.persistence.Query q = this.em.createQuery(cq);
        Long count2 = (Long) q.getSingleResult();
        
        assertThat(count1, is(equalTo(count2))); 
	}
	
	@Test
	public void removePersonaJuridica()  {	
		PersonaJuridicaModel model1 = personaJuridicaProvider.addPersonaJuridica(
				representanteLegalModel, "PER", tipoDocumentoModel, "10467793549", 
				"Softgreen S.A.C.", Calendar.getInstance().getTime(), TipoEmpresa.PRIVADA, true);								
		
		Long id = model1.getId();		
		boolean result = personaJuridicaProvider.removePersonaJuridica(model1);		
				
		PersonaJuridicaModel model2 = personaJuridicaProvider.getPersonaJuridicaById(id);
		
		assertThat(result, is(true));
		assertThat(model2, is(nullValue()));
	}
	
}