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

import com.sinapsistech.laboratorio.model.Entrega;
import com.sinapsistech.laboratorio.model.SolicitudDetalle;
import com.sinapsistech.laboratorio.model.Trabajador;

/**
 * Backing bean for Entrega entities.
 * <p/>
 * This class provides CRUD functionality for all Entrega entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class EntregaBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Entrega entities
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

   private Entrega entrega;

   public Entrega getEntrega()
   {
      return this.entrega;
   }

   public void setEntrega(Entrega entrega)
   {
      this.entrega = entrega;
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
         this.entrega = this.example;
      }
      else
      {
         this.entrega = findById(getId());
      }
   }

   public Entrega findById(Integer id)
   {

      return this.entityManager.find(Entrega.class, id);
   }

   /*
    * Support updating and deleting Entrega entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.entrega);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.entrega);
            return "view?faces-redirect=true&id=" + this.entrega.getIdEntrega();
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
         Entrega deletableEntity = findById(getId());
         SolicitudDetalle solicitudDetalle = deletableEntity.getSolicitudDetalle();
         solicitudDetalle.getEntregas().remove(deletableEntity);
         deletableEntity.setSolicitudDetalle(null);
         this.entityManager.merge(solicitudDetalle);
         Trabajador trabajador = deletableEntity.getTrabajador();
         trabajador.getEntregas().remove(deletableEntity);
         deletableEntity.setTrabajador(null);
         this.entityManager.merge(trabajador);
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
    * Support searching Entrega entities with pagination
    */

   private int page;
   private long count;
   private List<Entrega> pageItems;

   private Entrega example = new Entrega();

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

   public Entrega getExample()
   {
      return this.example;
   }

   public void setExample(Entrega example)
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
      Root<Entrega> root = countCriteria.from(Entrega.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Entrega> criteria = builder.createQuery(Entrega.class);
      root = criteria.from(Entrega.class);
      TypedQuery<Entrega> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Entrega> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idEntrega = this.example.getIdEntrega();
      if (idEntrega != 0)
      {
         predicatesList.add(builder.equal(root.get("idEntrega"), idEntrega));
      }
      SolicitudDetalle solicitudDetalle = this.example.getSolicitudDetalle();
      if (solicitudDetalle != null)
      {
         predicatesList.add(builder.equal(root.get("solicitudDetalle"), solicitudDetalle));
      }
      Trabajador trabajador = this.example.getTrabajador();
      if (trabajador != null)
      {
         predicatesList.add(builder.equal(root.get("trabajador"), trabajador));
      }
      String observaciones = this.example.getObservaciones();
      if (observaciones != null && !"".equals(observaciones))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("observaciones")), '%' + observaciones.toLowerCase() + '%'));
      }
      String nombreCompleto = this.example.getNombreCompleto();
      if (nombreCompleto != null && !"".equals(nombreCompleto))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("nombreCompleto")), '%' + nombreCompleto.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Entrega> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Entrega entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Entrega> getAll()
   {

      CriteriaQuery<Entrega> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Entrega.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Entrega.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final EntregaBean ejbProxy = this.sessionContext.getBusinessObject(EntregaBean.class);

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

            return String.valueOf(((Entrega) value).getIdEntrega());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Entrega add = new Entrega();

   public Entrega getAdd()
   {
      return this.add;
   }

   public Entrega getAdded()
   {
      Entrega added = this.add;
      this.add = new Entrega();
      return added;
   }
}
