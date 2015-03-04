package com.sinapsistech.laboratorio.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import com.sinapsistech.laboratorio.model.Persona;
import com.sinapsistech.laboratorio.model.Ciudad;
import com.sinapsistech.laboratorio.model.ClasePaciente;
import com.sinapsistech.laboratorio.model.RegistroResultado;
import com.sinapsistech.laboratorio.model.Solicitud;
import com.sinapsistech.laboratorio.model.TipoPersona;
import com.sinapsistech.laboratorio.model.Trabajador;

import java.util.Iterator;

/**
 * Backing bean for Persona entities.
 * <p/>
 * This class provides CRUD functionality for all Persona entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class PersonaBean implements Serializable
{

   private static final long serialVersionUID = 1L;
   FacesContext context = FacesContext.getCurrentInstance();
   ExternalContext externalContext = context.getExternalContext();
   HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();

   /*
    * Support creating and retrieving Persona entities
    */

   private Integer id;

   public Integer getId()
   {
      return this.id;
   }

   public void setId(Integer id)
   {
      this.id = id;
   }

   private Persona persona;

   public Persona getPersona()
   {
      return this.persona;
   }

   public void setPersona(Persona persona)
   {
      this.persona = persona;
   }

   @Inject
   private Conversation conversation;

   @PersistenceContext(unitName = "ilab-persistence-unit", type = PersistenceContextType.EXTENDED)
   private EntityManager entityManager;

   public String create()
   {

      this.conversation.begin();
      this.conversation.setTimeout(1800000L);
      return "create?faces-redirect=true";
   }

   public void retrieve()
   {

      if (FacesContext.getCurrentInstance().isPostback())
      {
         return;
      }

      if (this.conversation.isTransient())
      {
         this.conversation.begin();
         this.conversation.setTimeout(1800000L);
      }

      if (this.id == null)
      {
         this.persona = this.example;
      }
      else
      {
         this.persona = findById(getId());
      }
   }

   public Persona findById(Integer id)
   {

      return this.entityManager.find(Persona.class, id);
   }

   /*
    * Support updating and deleting Persona entities
    */

   public String update()
   {
	  System.out.println("Agarrando el contexto.");
	  String nombre = request.getUserPrincipal().getName();
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            System.out.println("Entro por Persona nueva");
            
            this.persona.setFechaReg(new Date());
            this.persona.setUsuarioReg(nombre);
            this.entityManager.persist(this.persona);
            return "search?faces-redirect=true";
         }
         else
         {
            System.out.println("Entro por Persona modificada");
            
            this.persona.setFechaMod(new Date());
            this.persona.setUsuarioMod(nombre);
            this.entityManager.merge(this.persona);
            return "view?faces-redirect=true&id=" + this.persona.getIdPersona();
         }
      }
      catch (Exception e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
         return null;
      }
   }

   public String delete()
   {
      this.conversation.end();

      try
      {
    	 /*
         Persona deletableEntity = findById(getId());
         Ciudad ciudad = deletableEntity.getCiudad();
         ciudad.getPersonas().remove(deletableEntity);
         deletableEntity.setCiudad(null);
         this.entityManager.merge(ciudad);
         ClasePaciente clasePaciente = deletableEntity.getClasePaciente();
         clasePaciente.getPersonas().remove(deletableEntity);
         deletableEntity.setClasePaciente(null);
         this.entityManager.merge(clasePaciente);
         TipoPersona tipoPersona = deletableEntity.getTipoPersona();
         tipoPersona.getPersonas().remove(deletableEntity);
         deletableEntity.setTipoPersona(null);
         this.entityManager.merge(tipoPersona);
         Iterator<Trabajador> iterTrabajadors = deletableEntity.getTrabajadors().iterator();
         for (; iterTrabajadors.hasNext();)
         {
            Trabajador nextInTrabajadors = iterTrabajadors.next();
            nextInTrabajadors.setPersona(null);
            iterTrabajadors.remove();
            this.entityManager.merge(nextInTrabajadors);
         }
         Iterator<Solicitud> iterSolicituds = deletableEntity.getSolicituds().iterator();
         for (; iterSolicituds.hasNext();)
         {
            Solicitud nextInSolicituds = iterSolicituds.next();
            nextInSolicituds.setPersona(null);
            iterSolicituds.remove();
            this.entityManager.merge(nextInSolicituds);
         }
         this.entityManager.remove(deletableEntity);
         */
    	  String nombre = request.getUserPrincipal().getName();
          Persona deletableEntity = findById(getId());
    	  deletableEntity.setUsuarioBorrado(nombre);
    	  deletableEntity.setFechaBorrado(new Date());   	  
         this.entityManager.flush();
         return "search?faces-redirect=true";
      }
      catch (Exception e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
         return null;
      }
   }

   /*
    * Support searching Persona entities with pagination
    */

   private int page;
   private long count;
   private List<Persona> pageItems;

   private Persona example = new Persona();

   public int getPage()
   {
      return this.page;
   }

   public void setPage(int page)
   {
      this.page = page;
   }

   public int getPageSize()
   {
      return 10;
   }

   public Persona getExample()
   {
      return this.example;
   }

   public void setExample(Persona example)
   {
      this.example = example;
   }

   public String search()
   {
      this.page = 0;
      return null;
   }

   public void paginate()
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

      // Populate this.count

      CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
      Root<Persona> root = countCriteria.from(Persona.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Persona> criteria = builder.createQuery(Persona.class);
      root = criteria.from(Persona.class);
      TypedQuery<Persona> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Persona> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idPersona = this.example.getIdPersona();
      if (idPersona != 0)
      {
         predicatesList.add(builder.equal(root.get("idPersona"), idPersona));
      }
      Ciudad ciudad = this.example.getCiudad();
      if (ciudad != null)
      {
         predicatesList.add(builder.equal(root.get("ciudad"), ciudad));
      }
      ClasePaciente clasePaciente = this.example.getClasePaciente();
      if (clasePaciente != null)
      {
         predicatesList.add(builder.equal(root.get("clasePaciente"), clasePaciente));
      }
      TipoPersona tipoPersona = this.example.getTipoPersona();
      if (tipoPersona != null)
      {
         predicatesList.add(builder.equal(root.get("tipoPersona"), tipoPersona));
      }
      String nombre = this.example.getNombre();
      if (nombre != null && !"".equals(nombre))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("nombre")), '%' + nombre.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Persona> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Persona entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Persona> getAll()
   {

      CriteriaQuery<Persona> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Persona.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Persona.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final PersonaBean ejbProxy = this.sessionContext.getBusinessObject(PersonaBean.class);

      return new Converter()
      {

         @Override
         public Object getAsObject(FacesContext context,
               UIComponent component, String value)
         {

            return ejbProxy.findById(Integer.valueOf(value));
         }

         @Override
         public String getAsString(FacesContext context,
               UIComponent component, Object value)
         {

            if (value == null)
            {
               return "";
            }

            return String.valueOf(((Persona) value).getIdPersona());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Persona add = new Persona();

   public Persona getAdd()
   {
      return this.add;
   }

   public Persona getAdded()
   {
      Persona added = this.add;
      this.add = new Persona();
      return added;
   }
}
