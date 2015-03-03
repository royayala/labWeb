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

import com.sinapsistech.laboratorio.model.Solicitud;
import com.sinapsistech.laboratorio.model.FormaPago;
import com.sinapsistech.laboratorio.model.Persona;
import com.sinapsistech.laboratorio.model.SolicitudDetalle;
import com.sinapsistech.laboratorio.model.TipoTarifa;
import com.sinapsistech.laboratorio.model.Trabajador;
import java.util.Iterator;

/**
 * Backing bean for Solicitud entities.
 * <p/>
 * This class provides CRUD functionality for all Solicitud entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class SolicitudBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Solicitud entities
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

   private Solicitud solicitud;

   public Solicitud getSolicitud()
   {
      return this.solicitud;
   }

   public void setSolicitud(Solicitud solicitud)
   {
      this.solicitud = solicitud;
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
         this.solicitud = this.example;
      }
      else
      {
         this.solicitud = findById(getId());
      }
   }

   public Solicitud findById(Integer id)
   {

      return this.entityManager.find(Solicitud.class, id);
   }

   /*
    * Support updating and deleting Solicitud entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.solicitud);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.solicitud);
            return "view?faces-redirect=true&id=" + this.solicitud.getIdSolicitud();
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
         Solicitud deletableEntity = findById(getId());
         FormaPago formaPago = deletableEntity.getFormaPago();
         formaPago.getSolicituds().remove(deletableEntity);
         deletableEntity.setFormaPago(null);
         this.entityManager.merge(formaPago);
         Persona persona = deletableEntity.getPersona();
         persona.getSolicituds().remove(deletableEntity);
         deletableEntity.setPersona(null);
         this.entityManager.merge(persona);
         TipoTarifa tipoTarifa = deletableEntity.getTipoTarifa();
         tipoTarifa.getSolicituds().remove(deletableEntity);
         deletableEntity.setTipoTarifa(null);
         this.entityManager.merge(tipoTarifa);
         Trabajador trabajador = deletableEntity.getTrabajador();
         trabajador.getSolicituds().remove(deletableEntity);
         deletableEntity.setTrabajador(null);
         this.entityManager.merge(trabajador);
         Iterator<SolicitudDetalle> iterSolicitudDetalles = deletableEntity.getSolicitudDetalles().iterator();
         for (; iterSolicitudDetalles.hasNext();)
         {
            SolicitudDetalle nextInSolicitudDetalles = iterSolicitudDetalles.next();
            nextInSolicitudDetalles.setSolicitud(null);
            iterSolicitudDetalles.remove();
            this.entityManager.merge(nextInSolicitudDetalles);
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
    * Support searching Solicitud entities with pagination
    */

   private int page;
   private long count;
   private List<Solicitud> pageItems;

   private Solicitud example = new Solicitud();

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

   public Solicitud getExample()
   {
      return this.example;
   }

   public void setExample(Solicitud example)
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
      Root<Solicitud> root = countCriteria.from(Solicitud.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Solicitud> criteria = builder.createQuery(Solicitud.class);
      root = criteria.from(Solicitud.class);
      TypedQuery<Solicitud> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Solicitud> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idSolicitud = this.example.getIdSolicitud();
      if (idSolicitud != 0)
      {
         predicatesList.add(builder.equal(root.get("idSolicitud"), idSolicitud));
      }
      FormaPago formaPago = this.example.getFormaPago();
      if (formaPago != null)
      {
         predicatesList.add(builder.equal(root.get("formaPago"), formaPago));
      }
      Persona persona = this.example.getPersona();
      if (persona != null)
      {
         predicatesList.add(builder.equal(root.get("persona"), persona));
      }
      TipoTarifa tipoTarifa = this.example.getTipoTarifa();
      if (tipoTarifa != null)
      {
         predicatesList.add(builder.equal(root.get("tipoTarifa"), tipoTarifa));
      }
      Trabajador trabajador = this.example.getTrabajador();
      if (trabajador != null)
      {
         predicatesList.add(builder.equal(root.get("trabajador"), trabajador));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Solicitud> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Solicitud entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Solicitud> getAll()
   {

      CriteriaQuery<Solicitud> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Solicitud.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Solicitud.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final SolicitudBean ejbProxy = this.sessionContext.getBusinessObject(SolicitudBean.class);

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

            return String.valueOf(((Solicitud) value).getIdSolicitud());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Solicitud add = new Solicitud();

   public Solicitud getAdd()
   {
      return this.add;
   }

   public Solicitud getAdded()
   {
      Solicitud added = this.add;
      this.add = new Solicitud();
      return added;
   }
}
