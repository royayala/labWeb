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

import com.sinapsistech.laboratorio.model.Examen;
import com.sinapsistech.laboratorio.model.FormaPago;
import com.sinapsistech.laboratorio.model.Solicitud;

import java.util.Iterator;

/**
 * Backing bean for FormaPago entities.
 * <p/>
 * This class provides CRUD functionality for all FormaPago entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class FormaPagoBean implements Serializable
{

   private static final long serialVersionUID = 1L;
   FacesContext context = FacesContext.getCurrentInstance();
   ExternalContext externalContext = context.getExternalContext();
   HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();

   /*
    * Support creating and retrieving FormaPago entities
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

   private FormaPago formaPago;

   public FormaPago getFormaPago()
   {
      return this.formaPago;
   }

   public void setFormaPago(FormaPago formaPago)
   {
      this.formaPago = formaPago;
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
         this.formaPago = this.example;
      }
      else
      {
         this.formaPago = findById(getId());
      }
   }

   public FormaPago findById(Integer id)
   {

      return this.entityManager.find(FormaPago.class, id);
   }

   /*
    * Support updating and deleting FormaPago entities
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
            System.out.println("Entro por forma de pago nueva");

            this.formaPago.setFechaReg(new Date());
            this.formaPago.setUsuarioReg(nombre);
            this.entityManager.persist(this.formaPago);
            return "search?faces-redirect=true";
         }
         else
         {
            System.out.println("Entro por forma de pago modificada");
             
            this.formaPago.setFechaMod(new Date());
            this.formaPago.setUsuarioMod(nombre);
            this.entityManager.merge(this.formaPago);
            return "view?faces-redirect=true&id=" + this.formaPago.getIdFormaPago();
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
         FormaPago deletableEntity = findById(getId());
         Iterator<Solicitud> iterSolicituds = deletableEntity.getSolicituds().iterator();
         for (; iterSolicituds.hasNext();)
         {
            Solicitud nextInSolicituds = iterSolicituds.next();
            nextInSolicituds.setFormaPago(null);
            iterSolicituds.remove();
            this.entityManager.merge(nextInSolicituds);
         }
         this.entityManager.remove(deletableEntity);
         */
    	  String nombre = request.getUserPrincipal().getName();
          FormaPago deletableEntity = findById(getId());
    	  deletableEntity.setUsuarioBorrado(nombre);
    	  deletableEntity.setFechaBorrado(new Date());    	  
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
    * Support searching FormaPago entities with pagination
    */

   private int page;
   private long count;
   private List<FormaPago> pageItems;

   private FormaPago example = new FormaPago();

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

   public FormaPago getExample()
   {
      return this.example;
   }

   public void setExample(FormaPago example)
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
      Root<FormaPago> root = countCriteria.from(FormaPago.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<FormaPago> criteria = builder.createQuery(FormaPago.class);
      root = criteria.from(FormaPago.class);
      TypedQuery<FormaPago> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<FormaPago> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idFormaPago = this.example.getIdFormaPago();
      if (idFormaPago != 0)
      {
         predicatesList.add(builder.equal(root.get("idFormaPago"), idFormaPago));
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

   public List<FormaPago> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back FormaPago entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<FormaPago> getAll()
   {

      CriteriaQuery<FormaPago> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(FormaPago.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(FormaPago.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final FormaPagoBean ejbProxy = this.sessionContext.getBusinessObject(FormaPagoBean.class);

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

            return String.valueOf(((FormaPago) value).getIdFormaPago());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private FormaPago add = new FormaPago();

   public FormaPago getAdd()
   {
      return this.add;
   }

   public FormaPago getAdded()
   {
      FormaPago added = this.add;
      this.add = new FormaPago();
      return added;
   }
}
