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

import com.sinapsistech.laboratorio.model.UsuarioRol;
import com.sinapsistech.laboratorio.model.Usuario;

/**
 * Backing bean for UsuarioRol entities.
 * <p/>
 * This class provides CRUD functionality for all UsuarioRol entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class UsuarioRolBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving UsuarioRol entities
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

   private UsuarioRol usuarioRol;

   public UsuarioRol getUsuarioRol()
   {
      return this.usuarioRol;
   }

   public void setUsuarioRol(UsuarioRol usuarioRol)
   {
      this.usuarioRol = usuarioRol;
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
         this.usuarioRol = this.example;
      }
      else
      {
         this.usuarioRol = findById(getId());
      }
   }

   public UsuarioRol findById(Integer id)
   {

      return this.entityManager.find(UsuarioRol.class, id);
   }

   /*
    * Support updating and deleting UsuarioRol entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.usuarioRol);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.usuarioRol);
            return "view?faces-redirect=true&id=" + this.usuarioRol.getCorrelativo();
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
         UsuarioRol deletableEntity = findById(getId());
         Usuario usuario = deletableEntity.getUsuario();
         usuario.getUsuarioRols().remove(deletableEntity);
         deletableEntity.setUsuario(null);
         this.entityManager.merge(usuario);
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
    * Support searching UsuarioRol entities with pagination
    */

   private int page;
   private long count;
   private List<UsuarioRol> pageItems;

   private UsuarioRol example = new UsuarioRol();

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

   public UsuarioRol getExample()
   {
      return this.example;
   }

   public void setExample(UsuarioRol example)
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
      Root<UsuarioRol> root = countCriteria.from(UsuarioRol.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<UsuarioRol> criteria = builder.createQuery(UsuarioRol.class);
      root = criteria.from(UsuarioRol.class);
      TypedQuery<UsuarioRol> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<UsuarioRol> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int correlativo = this.example.getCorrelativo();
      if (correlativo != 0)
      {
         predicatesList.add(builder.equal(root.get("correlativo"), correlativo));
      }
      Usuario usuario = this.example.getUsuario();
      if (usuario != null)
      {
         predicatesList.add(builder.equal(root.get("usuario"), usuario));
      }
      String rolAsignado = this.example.getRolAsignado();
      if (rolAsignado != null && !"".equals(rolAsignado))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("rolAsignado")), '%' + rolAsignado.toLowerCase() + '%'));
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

   public List<UsuarioRol> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back UsuarioRol entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<UsuarioRol> getAll()
   {

      CriteriaQuery<UsuarioRol> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(UsuarioRol.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(UsuarioRol.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final UsuarioRolBean ejbProxy = this.sessionContext.getBusinessObject(UsuarioRolBean.class);

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

            return String.valueOf(((UsuarioRol) value).getCorrelativo());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private UsuarioRol add = new UsuarioRol();

   public UsuarioRol getAdd()
   {
      return this.add;
   }

   public UsuarioRol getAdded()
   {
      UsuarioRol added = this.add;
      this.add = new UsuarioRol();
      return added;
   }
}
