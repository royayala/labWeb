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

import com.sinapsistech.laboratorio.model.TipoExamen;
import com.sinapsistech.laboratorio.model.SubTipoExamen;
import java.util.Iterator;

/**
 * Backing bean for TipoExamen entities.
 * <p/>
 * This class provides CRUD functionality for all TipoExamen entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class TipoExamenBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving TipoExamen entities
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

   private TipoExamen tipoExamen;

   public TipoExamen getTipoExamen()
   {
      return this.tipoExamen;
   }

   public void setTipoExamen(TipoExamen tipoExamen)
   {
      this.tipoExamen = tipoExamen;
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
         this.tipoExamen = this.example;
      }
      else
      {
         this.tipoExamen = findById(getId());
      }
   }

   public TipoExamen findById(Integer id)
   {

      return this.entityManager.find(TipoExamen.class, id);
   }

   /*
    * Support updating and deleting TipoExamen entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.tipoExamen);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.tipoExamen);
            return "view?faces-redirect=true&id=" + this.tipoExamen.getIdTipoExamen();
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
         TipoExamen deletableEntity = findById(getId());
         Iterator<SubTipoExamen> iterSubTipoExamens = deletableEntity.getSubTipoExamens().iterator();
         for (; iterSubTipoExamens.hasNext();)
         {
            SubTipoExamen nextInSubTipoExamens = iterSubTipoExamens.next();
            nextInSubTipoExamens.setTipoExamen(null);
            iterSubTipoExamens.remove();
            this.entityManager.merge(nextInSubTipoExamens);
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
    * Support searching TipoExamen entities with pagination
    */

   private int page;
   private long count;
   private List<TipoExamen> pageItems;

   private TipoExamen example = new TipoExamen();

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

   public TipoExamen getExample()
   {
      return this.example;
   }

   public void setExample(TipoExamen example)
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
      Root<TipoExamen> root = countCriteria.from(TipoExamen.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<TipoExamen> criteria = builder.createQuery(TipoExamen.class);
      root = criteria.from(TipoExamen.class);
      TypedQuery<TipoExamen> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<TipoExamen> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idTipoExamen = this.example.getIdTipoExamen();
      if (idTipoExamen != 0)
      {
         predicatesList.add(builder.equal(root.get("idTipoExamen"), idTipoExamen));
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
      String flagEstado = this.example.getFlagEstado();
      if (flagEstado != null && !"".equals(flagEstado))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("flagEstado")), '%' + flagEstado.toLowerCase() + '%'));
      }
      String usuarioReg = this.example.getUsuarioReg();
      if (usuarioReg != null && !"".equals(usuarioReg))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("usuarioReg")), '%' + usuarioReg.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<TipoExamen> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back TipoExamen entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<TipoExamen> getAll()
   {

      CriteriaQuery<TipoExamen> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(TipoExamen.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(TipoExamen.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final TipoExamenBean ejbProxy = this.sessionContext.getBusinessObject(TipoExamenBean.class);

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

            return String.valueOf(((TipoExamen) value).getIdTipoExamen());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private TipoExamen add = new TipoExamen();

   public TipoExamen getAdd()
   {
      return this.add;
   }

   public TipoExamen getAdded()
   {
      TipoExamen added = this.add;
      this.add = new TipoExamen();
      return added;
   }
}
