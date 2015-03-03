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

import com.sinapsistech.laboratorio.model.SubTipoExamen;
import com.sinapsistech.laboratorio.model.Examen;
import com.sinapsistech.laboratorio.model.TipoExamen;
import java.util.Iterator;

/**
 * Backing bean for SubTipoExamen entities.
 * <p/>
 * This class provides CRUD functionality for all SubTipoExamen entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class SubTipoExamenBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving SubTipoExamen entities
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

   private SubTipoExamen subTipoExamen;

   public SubTipoExamen getSubTipoExamen()
   {
      return this.subTipoExamen;
   }

   public void setSubTipoExamen(SubTipoExamen subTipoExamen)
   {
      this.subTipoExamen = subTipoExamen;
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
         this.subTipoExamen = this.example;
      }
      else
      {
         this.subTipoExamen = findById(getId());
      }
   }

   public SubTipoExamen findById(Integer id)
   {

      return this.entityManager.find(SubTipoExamen.class, id);
   }

   /*
    * Support updating and deleting SubTipoExamen entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.subTipoExamen);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.subTipoExamen);
            return "view?faces-redirect=true&id=" + this.subTipoExamen.getIdSubTipoExamen();
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
         SubTipoExamen deletableEntity = findById(getId());
         TipoExamen tipoExamen = deletableEntity.getTipoExamen();
         tipoExamen.getSubTipoExamens().remove(deletableEntity);
         deletableEntity.setTipoExamen(null);
         this.entityManager.merge(tipoExamen);
         Iterator<Examen> iterExamens = deletableEntity.getExamens().iterator();
         for (; iterExamens.hasNext();)
         {
            Examen nextInExamens = iterExamens.next();
            nextInExamens.setSubTipoExamen(null);
            iterExamens.remove();
            this.entityManager.merge(nextInExamens);
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
    * Support searching SubTipoExamen entities with pagination
    */

   private int page;
   private long count;
   private List<SubTipoExamen> pageItems;

   private SubTipoExamen example = new SubTipoExamen();

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

   public SubTipoExamen getExample()
   {
      return this.example;
   }

   public void setExample(SubTipoExamen example)
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
      Root<SubTipoExamen> root = countCriteria.from(SubTipoExamen.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<SubTipoExamen> criteria = builder.createQuery(SubTipoExamen.class);
      root = criteria.from(SubTipoExamen.class);
      TypedQuery<SubTipoExamen> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<SubTipoExamen> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idSubTipoExamen = this.example.getIdSubTipoExamen();
      if (idSubTipoExamen != 0)
      {
         predicatesList.add(builder.equal(root.get("idSubTipoExamen"), idSubTipoExamen));
      }
      TipoExamen tipoExamen = this.example.getTipoExamen();
      if (tipoExamen != null)
      {
         predicatesList.add(builder.equal(root.get("tipoExamen"), tipoExamen));
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

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<SubTipoExamen> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back SubTipoExamen entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<SubTipoExamen> getAll()
   {

      CriteriaQuery<SubTipoExamen> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(SubTipoExamen.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(SubTipoExamen.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final SubTipoExamenBean ejbProxy = this.sessionContext.getBusinessObject(SubTipoExamenBean.class);

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

            return String.valueOf(((SubTipoExamen) value).getIdSubTipoExamen());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private SubTipoExamen add = new SubTipoExamen();

   public SubTipoExamen getAdd()
   {
      return this.add;
   }

   public SubTipoExamen getAdded()
   {
      SubTipoExamen added = this.add;
      this.add = new SubTipoExamen();
      return added;
   }
}
