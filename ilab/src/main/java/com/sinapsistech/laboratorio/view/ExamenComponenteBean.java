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

import com.sinapsistech.laboratorio.model.ExamenComponente;
import com.sinapsistech.laboratorio.model.Componente;
import com.sinapsistech.laboratorio.model.Examen;

/**
 * Backing bean for ExamenComponente entities.
 * <p/>
 * This class provides CRUD functionality for all ExamenComponente entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ExamenComponenteBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving ExamenComponente entities
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

   private ExamenComponente examenComponente;

   public ExamenComponente getExamenComponente()
   {
      return this.examenComponente;
   }

   public void setExamenComponente(ExamenComponente examenComponente)
   {
      this.examenComponente = examenComponente;
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
         this.examenComponente = this.example;
      }
      else
      {
         this.examenComponente = findById(getId());
      }
   }

   public ExamenComponente findById(Integer id)
   {

      return this.entityManager.find(ExamenComponente.class, id);
   }

   /*
    * Support updating and deleting ExamenComponente entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.examenComponente);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.examenComponente);
            return "view?faces-redirect=true&id=" + this.examenComponente.getIdExamenComponente();
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
         ExamenComponente deletableEntity = findById(getId());
         Componente componente = deletableEntity.getComponente();
         componente.getExamenComponentes().remove(deletableEntity);
         deletableEntity.setComponente(null);
         this.entityManager.merge(componente);
         Examen examen = deletableEntity.getExamen();
         examen.getExamenComponentes().remove(deletableEntity);
         deletableEntity.setExamen(null);
         this.entityManager.merge(examen);
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
    * Support searching ExamenComponente entities with pagination
    */

   private int page;
   private long count;
   private List<ExamenComponente> pageItems;

   private ExamenComponente example = new ExamenComponente();

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

   public ExamenComponente getExample()
   {
      return this.example;
   }

   public void setExample(ExamenComponente example)
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
      Root<ExamenComponente> root = countCriteria.from(ExamenComponente.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<ExamenComponente> criteria = builder.createQuery(ExamenComponente.class);
      root = criteria.from(ExamenComponente.class);
      TypedQuery<ExamenComponente> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<ExamenComponente> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idExamenComponente = this.example.getIdExamenComponente();
      if (idExamenComponente != 0)
      {
         predicatesList.add(builder.equal(root.get("idExamenComponente"), idExamenComponente));
      }
      Componente componente = this.example.getComponente();
      if (componente != null)
      {
         predicatesList.add(builder.equal(root.get("componente"), componente));
      }
      Examen examen = this.example.getExamen();
      if (examen != null)
      {
         predicatesList.add(builder.equal(root.get("examen"), examen));
      }
      String valorRefVaronA = this.example.getValorRefVaronA();
      if (valorRefVaronA != null && !"".equals(valorRefVaronA))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("valorRefVaronA")), '%' + valorRefVaronA.toLowerCase() + '%'));
      }
      String valorRefVaronB = this.example.getValorRefVaronB();
      if (valorRefVaronB != null && !"".equals(valorRefVaronB))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("valorRefVaronB")), '%' + valorRefVaronB.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<ExamenComponente> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back ExamenComponente entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<ExamenComponente> getAll()
   {

      CriteriaQuery<ExamenComponente> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(ExamenComponente.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(ExamenComponente.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final ExamenComponenteBean ejbProxy = this.sessionContext.getBusinessObject(ExamenComponenteBean.class);

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

            return String.valueOf(((ExamenComponente) value).getIdExamenComponente());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private ExamenComponente add = new ExamenComponente();

   public ExamenComponente getAdd()
   {
      return this.add;
   }

   public ExamenComponente getAdded()
   {
      ExamenComponente added = this.add;
      this.add = new ExamenComponente();
      return added;
   }
}
