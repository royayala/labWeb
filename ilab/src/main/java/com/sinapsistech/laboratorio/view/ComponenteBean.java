package com.sinapsistech.laboratorio.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
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
import javax.servlet.http.HttpServletRequest;

import com.sinapsistech.laboratorio.model.Componente;
import com.sinapsistech.laboratorio.model.ExamenComponente;
import com.sinapsistech.laboratorio.model.TipoTarifa;

import java.util.Iterator;

/**
 * Backing bean for Componente entities.
 * <p/>
 * This class provides CRUD functionality for all Componente entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ComponenteBean implements Serializable
{

   private static final long serialVersionUID = 1L;
   FacesContext context = FacesContext.getCurrentInstance();
   ExternalContext externalContext = context.getExternalContext();
   HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();

   /*
    * Support creating and retrieving Componente entities
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

   private Componente componente;

   public Componente getComponente()
   {
      return this.componente;
   }

   public void setComponente(Componente componente)
   {
      this.componente = componente;
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
         this.componente = this.example;
      }
      else
      {
         this.componente = findById(getId());
      }
   }

   public Componente findById(Integer id)
   {

      return this.entityManager.find(Componente.class, id);
   }

   /*
    * Support updating and deleting Componente entities
    */

   public String update()
   {
	  System.out.println("Agarrando el contexto.");
	  String nombre = request.getUserPrincipal().getName();
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
         	System.out.println("Entro por componente nuevo");

         	this.componente.setFechaReg(new Date());
         	this.componente.setUsuarioReg(nombre);
         	this.componente.setFlagEstado("AC");
            this.entityManager.persist(this.componente);
            return "search?faces-redirect=true";
         }
         else
         {
          	System.out.println("Entro por componente modificado");

          	this.componente.setFechaMod(new Date());
          	this.componente.setUsuarioMod(nombre);
            this.entityManager.merge(this.componente);
            return "view?faces-redirect=true&id=" + this.componente.getIdComponente();
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
    	  /*
         Componente deletableEntity = findById(getId());
         Iterator<ExamenComponente> iterExamenComponentes = deletableEntity.getExamenComponentes().iterator();
         for (; iterExamenComponentes.hasNext();)
         {
            ExamenComponente nextInExamenComponentes = iterExamenComponentes.next();
            nextInExamenComponentes.setComponente(null);
            iterExamenComponentes.remove();
            this.entityManager.merge(nextInExamenComponentes);
         }
         this.entityManager.remove(deletableEntity);
         */
    	  String nombre = request.getUserPrincipal().getName();
          Componente deletableEntity = findById(getId());
    	  deletableEntity.setUsuarioBorrado(nombre);
    	  deletableEntity.setFechaBorrado(new Date());
    	  deletableEntity.setFlagEstado("IN");
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
    * Support searching Componente entities with pagination
    */

   private int page;
   private long count;
   private List<Componente> pageItems;

   private Componente example = new Componente();

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

   public Componente getExample()
   {
      return this.example;
   }

   public void setExample(Componente example)
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
      Root<Componente> root = countCriteria.from(Componente.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Componente> criteria = builder.createQuery(Componente.class);
      root = criteria.from(Componente.class);
      TypedQuery<Componente> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Componente> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idComponente = this.example.getIdComponente();
      if (idComponente != 0)
      {
         predicatesList.add(builder.equal(root.get("idComponente"), idComponente));
      }
      String nombre = this.example.getNombre();
      if (nombre != null && !"".equals(nombre))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("nombre")), '%' + nombre.toLowerCase() + '%'));
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
      String usuarioMod = this.example.getUsuarioMod();
      if (usuarioMod != null && !"".equals(usuarioMod))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("usuarioMod")), '%' + usuarioMod.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Componente> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Componente entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Componente> getAll()
   {

      CriteriaQuery<Componente> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Componente.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Componente.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final ComponenteBean ejbProxy = this.sessionContext.getBusinessObject(ComponenteBean.class);

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

            return String.valueOf(((Componente) value).getIdComponente());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Componente add = new Componente();

   public Componente getAdd()
   {
      return this.add;
   }

   public Componente getAdded()
   {
      Componente added = this.add;
      this.add = new Componente();
      return added;
   }
}
