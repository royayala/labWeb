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

import com.sinapsistech.laboratorio.model.ClasePaciente;
import com.sinapsistech.laboratorio.model.Persona;
import java.util.Iterator;

/**
 * Backing bean for ClasePaciente entities.
 * <p/>
 * This class provides CRUD functionality for all ClasePaciente entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ClasePacienteBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving ClasePaciente entities
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

   private ClasePaciente clasePaciente;

   public ClasePaciente getClasePaciente()
   {
      return this.clasePaciente;
   }

   public void setClasePaciente(ClasePaciente clasePaciente)
   {
      this.clasePaciente = clasePaciente;
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
         this.clasePaciente = this.example;
      }
      else
      {
         this.clasePaciente = findById(getId());
      }
   }

   public ClasePaciente findById(Integer id)
   {

      return this.entityManager.find(ClasePaciente.class, id);
   }

   /*
    * Support updating and deleting ClasePaciente entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.clasePaciente);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.clasePaciente);
            return "view?faces-redirect=true&id=" + this.clasePaciente.getIdClasePaciente();
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
         ClasePaciente deletableEntity = findById(getId());
         Iterator<Persona> iterPersonas = deletableEntity.getPersonas().iterator();
         for (; iterPersonas.hasNext();)
         {
            Persona nextInPersonas = iterPersonas.next();
            nextInPersonas.setClasePaciente(null);
            iterPersonas.remove();
            this.entityManager.merge(nextInPersonas);
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
    * Support searching ClasePaciente entities with pagination
    */

   private int page;
   private long count;
   private List<ClasePaciente> pageItems;

   private ClasePaciente example = new ClasePaciente();

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

   public ClasePaciente getExample()
   {
      return this.example;
   }

   public void setExample(ClasePaciente example)
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
      Root<ClasePaciente> root = countCriteria.from(ClasePaciente.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<ClasePaciente> criteria = builder.createQuery(ClasePaciente.class);
      root = criteria.from(ClasePaciente.class);
      TypedQuery<ClasePaciente> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<ClasePaciente> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idClasePaciente = this.example.getIdClasePaciente();
      if (idClasePaciente != 0)
      {
         predicatesList.add(builder.equal(root.get("idClasePaciente"), idClasePaciente));
      }
      String nombre = this.example.getNombre();
      if (nombre != null && !"".equals(nombre))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("nombre")), '%' + nombre.toLowerCase() + '%'));
      }
      String usuarioReg = this.example.getUsuarioReg();
      if (usuarioReg != null && !"".equals(usuarioReg))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("usuarioReg")), '%' + usuarioReg.toLowerCase() + '%'));
      }
      String usuarioMod = this.example.getUsuarioMod();
      if (usuarioMod != null && !"".equals(usuarioMod))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("usuarioMod")), '%' + usuarioMod.toLowerCase() + '%'));
      }
      String usuarioBorrado = this.example.getUsuarioBorrado();
      if (usuarioBorrado != null && !"".equals(usuarioBorrado))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("usuarioBorrado")), '%' + usuarioBorrado.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<ClasePaciente> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back ClasePaciente entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<ClasePaciente> getAll()
   {

      CriteriaQuery<ClasePaciente> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(ClasePaciente.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(ClasePaciente.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final ClasePacienteBean ejbProxy = this.sessionContext.getBusinessObject(ClasePacienteBean.class);

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

            return String.valueOf(((ClasePaciente) value).getIdClasePaciente());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private ClasePaciente add = new ClasePaciente();

   public ClasePaciente getAdd()
   {
      return this.add;
   }

   public ClasePaciente getAdded()
   {
      ClasePaciente added = this.add;
      this.add = new ClasePaciente();
      return added;
   }
}
