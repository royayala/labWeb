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

import com.sinapsistech.laboratorio.model.TipoTarifa;
import com.sinapsistech.laboratorio.model.ListaPrecio;
import com.sinapsistech.laboratorio.model.Solicitud;
import java.util.Iterator;

/**
 * Backing bean for TipoTarifa entities.
 * <p/>
 * This class provides CRUD functionality for all TipoTarifa entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class TipoTarifaBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving TipoTarifa entities
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

   private TipoTarifa tipoTarifa;

   public TipoTarifa getTipoTarifa()
   {
      return this.tipoTarifa;
   }

   public void setTipoTarifa(TipoTarifa tipoTarifa)
   {
      this.tipoTarifa = tipoTarifa;
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
         this.tipoTarifa = this.example;
      }
      else
      {
         this.tipoTarifa = findById(getId());
      }
   }

   public TipoTarifa findById(Integer id)
   {

      return this.entityManager.find(TipoTarifa.class, id);
   }

   /*
    * Support updating and deleting TipoTarifa entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.tipoTarifa);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.tipoTarifa);
            return "view?faces-redirect=true&id=" + this.tipoTarifa.getIdTipoTarifa();
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
         TipoTarifa deletableEntity = findById(getId());
         Iterator<Solicitud> iterSolicituds = deletableEntity.getSolicituds().iterator();
         for (; iterSolicituds.hasNext();)
         {
            Solicitud nextInSolicituds = iterSolicituds.next();
            nextInSolicituds.setTipoTarifa(null);
            iterSolicituds.remove();
            this.entityManager.merge(nextInSolicituds);
         }
         Iterator<ListaPrecio> iterListaPrecios = deletableEntity.getListaPrecios().iterator();
         for (; iterListaPrecios.hasNext();)
         {
            ListaPrecio nextInListaPrecios = iterListaPrecios.next();
            nextInListaPrecios.setTipoTarifa(null);
            iterListaPrecios.remove();
            this.entityManager.merge(nextInListaPrecios);
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
    * Support searching TipoTarifa entities with pagination
    */

   private int page;
   private long count;
   private List<TipoTarifa> pageItems;

   private TipoTarifa example = new TipoTarifa();

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

   public TipoTarifa getExample()
   {
      return this.example;
   }

   public void setExample(TipoTarifa example)
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
      Root<TipoTarifa> root = countCriteria.from(TipoTarifa.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<TipoTarifa> criteria = builder.createQuery(TipoTarifa.class);
      root = criteria.from(TipoTarifa.class);
      TypedQuery<TipoTarifa> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<TipoTarifa> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idTipoTarifa = this.example.getIdTipoTarifa();
      if (idTipoTarifa != 0)
      {
         predicatesList.add(builder.equal(root.get("idTipoTarifa"), idTipoTarifa));
      }
      String nombre = this.example.getNombre();
      if (nombre != null && !"".equals(nombre))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("nombre")), '%' + nombre.toLowerCase() + '%'));
      }
      String decripcion = this.example.getDecripcion();
      if (decripcion != null && !"".equals(decripcion))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("decripcion")), '%' + decripcion.toLowerCase() + '%'));
      }
      Integer cuota = this.example.getCuota();
      if (cuota != null && cuota.intValue() != 0)
      {
         predicatesList.add(builder.equal(root.get("cuota"), cuota));
      }
      String flagEstado = this.example.getFlagEstado();
      if (flagEstado != null && !"".equals(flagEstado))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("flagEstado")), '%' + flagEstado.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<TipoTarifa> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back TipoTarifa entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<TipoTarifa> getAll()
   {

      CriteriaQuery<TipoTarifa> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(TipoTarifa.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(TipoTarifa.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final TipoTarifaBean ejbProxy = this.sessionContext.getBusinessObject(TipoTarifaBean.class);

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

            return String.valueOf(((TipoTarifa) value).getIdTipoTarifa());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private TipoTarifa add = new TipoTarifa();

   public TipoTarifa getAdd()
   {
      return this.add;
   }

   public TipoTarifa getAdded()
   {
      TipoTarifa added = this.add;
      this.add = new TipoTarifa();
      return added;
   }
}
