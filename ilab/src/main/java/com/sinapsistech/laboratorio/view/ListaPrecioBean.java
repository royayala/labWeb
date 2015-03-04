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

import com.sinapsistech.laboratorio.model.ListaPrecio;
import com.sinapsistech.laboratorio.model.Examen;
import com.sinapsistech.laboratorio.model.SolicitudDetalle;
import com.sinapsistech.laboratorio.model.TipoTarifa;
import com.sinapsistech.laboratorio.model.TomaMuestra;

import java.util.Iterator;

/**
 * Backing bean for ListaPrecio entities.
 * <p/>
 * This class provides CRUD functionality for all ListaPrecio entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ListaPrecioBean implements Serializable
{

   private static final long serialVersionUID = 1L;
   FacesContext context = FacesContext.getCurrentInstance();
   ExternalContext externalContext = context.getExternalContext();
   HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();

   /*
    * Support creating and retrieving ListaPrecio entities
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

   private ListaPrecio listaPrecio;

   public ListaPrecio getListaPrecio()
   {
      return this.listaPrecio;
   }

   public void setListaPrecio(ListaPrecio listaPrecio)
   {
      this.listaPrecio = listaPrecio;
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
         this.listaPrecio = this.example;
      }
      else
      {
         this.listaPrecio = findById(getId());
      }
   }

   public ListaPrecio findById(Integer id)
   {

      return this.entityManager.find(ListaPrecio.class, id);
   }

   /*
    * Support updating and deleting ListaPrecio entities
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
           	System.out.println("Entro por lista de precio nuevo");

           	this.listaPrecio.setFechaReg(new Date());
           	this.listaPrecio.setUsuarioReg(nombre);
           	this.listaPrecio.setFlagEstado("AC");
            this.entityManager.persist(this.listaPrecio);
            return "search?faces-redirect=true";
         }
         else
         {
        	System.out.println("Entro por lista de precio modificado");
        	
        	this.listaPrecio.setFechaMod(new Date());
        	this.listaPrecio.setUsuarioMod(nombre);
            this.entityManager.merge(this.listaPrecio);
            return "view?faces-redirect=true&id=" + this.listaPrecio.getIdListaPrecio();
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
         ListaPrecio deletableEntity = findById(getId());
         Examen examen = deletableEntity.getExamen();
         examen.getListaPrecios().remove(deletableEntity);
         deletableEntity.setExamen(null);
         this.entityManager.merge(examen);
         TipoTarifa tipoTarifa = deletableEntity.getTipoTarifa();
         tipoTarifa.getListaPrecios().remove(deletableEntity);
         deletableEntity.setTipoTarifa(null);
         this.entityManager.merge(tipoTarifa);
         Iterator<SolicitudDetalle> iterSolicitudDetalles = deletableEntity.getSolicitudDetalles().iterator();
         for (; iterSolicitudDetalles.hasNext();)
         {
            SolicitudDetalle nextInSolicitudDetalles = iterSolicitudDetalles.next();
            nextInSolicitudDetalles.setListaPrecio(null);
            iterSolicitudDetalles.remove();
            this.entityManager.merge(nextInSolicitudDetalles);
         }
         this.entityManager.remove(deletableEntity);
         */
    	  String nombre = request.getUserPrincipal().getName();
          ListaPrecio deletableEntity = findById(getId());
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
    * Support searching ListaPrecio entities with pagination
    */

   private int page;
   private long count;
   private List<ListaPrecio> pageItems;

   private ListaPrecio example = new ListaPrecio();

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

   public ListaPrecio getExample()
   {
      return this.example;
   }

   public void setExample(ListaPrecio example)
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
      Root<ListaPrecio> root = countCriteria.from(ListaPrecio.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<ListaPrecio> criteria = builder.createQuery(ListaPrecio.class);
      root = criteria.from(ListaPrecio.class);
      TypedQuery<ListaPrecio> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<ListaPrecio> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idListaPrecio = this.example.getIdListaPrecio();
      if (idListaPrecio != 0)
      {
         predicatesList.add(builder.equal(root.get("idListaPrecio"), idListaPrecio));
      }
      Examen examen = this.example.getExamen();
      if (examen != null)
      {
         predicatesList.add(builder.equal(root.get("examen"), examen));
      }
      TipoTarifa tipoTarifa = this.example.getTipoTarifa();
      if (tipoTarifa != null)
      {
         predicatesList.add(builder.equal(root.get("tipoTarifa"), tipoTarifa));
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

   public List<ListaPrecio> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back ListaPrecio entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<ListaPrecio> getAll()
   {

      CriteriaQuery<ListaPrecio> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(ListaPrecio.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(ListaPrecio.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final ListaPrecioBean ejbProxy = this.sessionContext.getBusinessObject(ListaPrecioBean.class);

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

            return String.valueOf(((ListaPrecio) value).getIdListaPrecio());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private ListaPrecio add = new ListaPrecio();

   public ListaPrecio getAdd()
   {
      return this.add;
   }

   public ListaPrecio getAdded()
   {
      ListaPrecio added = this.add;
      this.add = new ListaPrecio();
      return added;
   }
}
