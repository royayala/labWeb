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

import com.sinapsistech.laboratorio.model.Cargo;
import com.sinapsistech.laboratorio.model.Trabajador;
import java.util.Iterator;

/**
 * Backing bean for Cargo entities.
 * <p/>
 * This class provides CRUD functionality for all Cargo entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class CargoBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Cargo entities
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

   private Cargo cargo;

   public Cargo getCargo()
   {
      return this.cargo;
   }

   public void setCargo(Cargo cargo)
   {
      this.cargo = cargo;
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
         this.cargo = this.example;
      }
      else
      {
         this.cargo = findById(getId());
      }
   }

   public Cargo findById(Integer id)
   {

      return this.entityManager.find(Cargo.class, id);
   }

   /*
    * Support updating and deleting Cargo entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.cargo);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.cargo);
            return "view?faces-redirect=true&id=" + this.cargo.getIdCargo();
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
         Cargo deletableEntity = findById(getId());
         Iterator<Trabajador> iterTrabajadors = deletableEntity.getTrabajadors().iterator();
         for (; iterTrabajadors.hasNext();)
         {
            Trabajador nextInTrabajadors = iterTrabajadors.next();
            nextInTrabajadors.setCargo(null);
            iterTrabajadors.remove();
            this.entityManager.merge(nextInTrabajadors);
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
    * Support searching Cargo entities with pagination
    */

   private int page;
   private long count;
   private List<Cargo> pageItems;

   private Cargo example = new Cargo();

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

   public Cargo getExample()
   {
      return this.example;
   }

   public void setExample(Cargo example)
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
      Root<Cargo> root = countCriteria.from(Cargo.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Cargo> criteria = builder.createQuery(Cargo.class);
      root = criteria.from(Cargo.class);
      TypedQuery<Cargo> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Cargo> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idCargo = this.example.getIdCargo();
      if (idCargo != 0)
      {
         predicatesList.add(builder.equal(root.get("idCargo"), idCargo));
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

   public List<Cargo> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Cargo entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Cargo> getAll()
   {

      CriteriaQuery<Cargo> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Cargo.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Cargo.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final CargoBean ejbProxy = this.sessionContext.getBusinessObject(CargoBean.class);

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

            return String.valueOf(((Cargo) value).getIdCargo());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Cargo add = new Cargo();

   public Cargo getAdd()
   {
      return this.add;
   }

   public Cargo getAdded()
   {
      Cargo added = this.add;
      this.add = new Cargo();
      return added;
   }
}
