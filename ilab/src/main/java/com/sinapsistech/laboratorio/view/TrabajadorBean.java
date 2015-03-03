package com.sinapsistech.laboratorio.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
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

import com.sinapsistech.laboratorio.model.Trabajador;
import com.sinapsistech.laboratorio.model.Cargo;
import com.sinapsistech.laboratorio.model.Entrega;
import com.sinapsistech.laboratorio.model.Especialidad;
import com.sinapsistech.laboratorio.model.Persona;
import com.sinapsistech.laboratorio.model.RegistroResultado;
import com.sinapsistech.laboratorio.model.Solicitud;
import com.sinapsistech.laboratorio.model.TomaMuestra;
import java.util.Iterator;

/**
 * Backing bean for Trabajador entities.
 * <p/>
 * This class provides CRUD functionality for all Trabajador entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class TrabajadorBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Trabajador entities
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

   private Trabajador trabajador;

   public Trabajador getTrabajador()
   {
      return this.trabajador;
   }

   public void setTrabajador(Trabajador trabajador)
   {
      this.trabajador = trabajador;
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
         this.trabajador = this.example;
      }
      else
      {
         this.trabajador = findById(getId());
      }
   }

   public Trabajador findById(Integer id)
   {

      return this.entityManager.find(Trabajador.class, id);
   }

   /*
    * Support updating and deleting Trabajador entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.trabajador);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.trabajador);
            return "view?faces-redirect=true&id=" + this.trabajador.getIdTrabajador();
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
         Trabajador deletableEntity = findById(getId());
         Cargo cargo = deletableEntity.getCargo();
         cargo.getTrabajadors().remove(deletableEntity);
         deletableEntity.setCargo(null);
         this.entityManager.merge(cargo);
         Especialidad especialidad = deletableEntity.getEspecialidad();
         especialidad.getTrabajadors().remove(deletableEntity);
         deletableEntity.setEspecialidad(null);
         this.entityManager.merge(especialidad);
         Persona persona = deletableEntity.getPersona();
         persona.getTrabajadors().remove(deletableEntity);
         deletableEntity.setPersona(null);
         this.entityManager.merge(persona);
         Iterator<Solicitud> iterSolicituds = deletableEntity.getSolicituds().iterator();
         for (; iterSolicituds.hasNext();)
         {
            Solicitud nextInSolicituds = iterSolicituds.next();
            nextInSolicituds.setTrabajador(null);
            iterSolicituds.remove();
            this.entityManager.merge(nextInSolicituds);
         }
         Iterator<TomaMuestra> iterTomaMuestras = deletableEntity.getTomaMuestras().iterator();
         for (; iterTomaMuestras.hasNext();)
         {
            TomaMuestra nextInTomaMuestras = iterTomaMuestras.next();
            nextInTomaMuestras.setTrabajador(null);
            iterTomaMuestras.remove();
            this.entityManager.merge(nextInTomaMuestras);
         }
         Iterator<Entrega> iterEntregas = deletableEntity.getEntregas().iterator();
         for (; iterEntregas.hasNext();)
         {
            Entrega nextInEntregas = iterEntregas.next();
            nextInEntregas.setTrabajador(null);
            iterEntregas.remove();
            this.entityManager.merge(nextInEntregas);
         }
         Iterator<RegistroResultado> iterRegistroResultados = deletableEntity.getRegistroResultados().iterator();
         for (; iterRegistroResultados.hasNext();)
         {
            RegistroResultado nextInRegistroResultados = iterRegistroResultados.next();
            nextInRegistroResultados.setTrabajador(null);
            iterRegistroResultados.remove();
            this.entityManager.merge(nextInRegistroResultados);
         }
         this.entityManager.remove(deletableEntity);
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
    * Support searching Trabajador entities with pagination
    */

   private int page;
   private long count;
   private List<Trabajador> pageItems;

   private Trabajador example = new Trabajador();

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

   public Trabajador getExample()
   {
      return this.example;
   }

   public void setExample(Trabajador example)
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
      Root<Trabajador> root = countCriteria.from(Trabajador.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Trabajador> criteria = builder.createQuery(Trabajador.class);
      root = criteria.from(Trabajador.class);
      TypedQuery<Trabajador> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Trabajador> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idTrabajador = this.example.getIdTrabajador();
      if (idTrabajador != 0)
      {
         predicatesList.add(builder.equal(root.get("idTrabajador"), idTrabajador));
      }
      Cargo cargo = this.example.getCargo();
      if (cargo != null)
      {
         predicatesList.add(builder.equal(root.get("cargo"), cargo));
      }
      Especialidad especialidad = this.example.getEspecialidad();
      if (especialidad != null)
      {
         predicatesList.add(builder.equal(root.get("especialidad"), especialidad));
      }
      Persona persona = this.example.getPersona();
      if (persona != null)
      {
         predicatesList.add(builder.equal(root.get("persona"), persona));
      }
      String observaciones = this.example.getObservaciones();
      if (observaciones != null && !"".equals(observaciones))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("observaciones")), '%' + observaciones.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Trabajador> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Trabajador entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Trabajador> getAll()
   {

      CriteriaQuery<Trabajador> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Trabajador.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Trabajador.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final TrabajadorBean ejbProxy = this.sessionContext.getBusinessObject(TrabajadorBean.class);

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

            return String.valueOf(((Trabajador) value).getIdTrabajador());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Trabajador add = new Trabajador();

   public Trabajador getAdd()
   {
      return this.add;
   }

   public Trabajador getAdded()
   {
      Trabajador added = this.add;
      this.add = new Trabajador();
      return added;
   }
}
