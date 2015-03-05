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

import com.sinapsistech.laboratorio.model.SolicitudDetalle;
import com.sinapsistech.laboratorio.model.Entrega;
import com.sinapsistech.laboratorio.model.Examen;
import com.sinapsistech.laboratorio.model.ListaPrecio;
import com.sinapsistech.laboratorio.model.RegistroResultado;
import com.sinapsistech.laboratorio.model.Solicitud;
import com.sinapsistech.laboratorio.model.TomaMuestra;

import java.util.Iterator;

/**
 * Backing bean for SolicitudDetalle entities.
 * <p/>
 * This class provides CRUD functionality for all SolicitudDetalle entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class SolicitudDetalleBean implements Serializable
{

   private static final long serialVersionUID = 1L;
   FacesContext context = FacesContext.getCurrentInstance();
   ExternalContext externalContext = context.getExternalContext();
   HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();

   /*
    * Support creating and retrieving SolicitudDetalle entities
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

   private SolicitudDetalle solicitudDetalle;

   public SolicitudDetalle getSolicitudDetalle()
   {
      return this.solicitudDetalle;
   }

   public void setSolicitudDetalle(SolicitudDetalle solicitudDetalle)
   {
      this.solicitudDetalle = solicitudDetalle;
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
         this.solicitudDetalle = this.example;
      }
      else
      {
         this.solicitudDetalle = findById(getId());
      }
   }

   public SolicitudDetalle findById(Integer id)
   {

      return this.entityManager.find(SolicitudDetalle.class, id);
   }

   /*
    * Support updating and deleting SolicitudDetalle entities
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
        	System.out.println("Entro por solicitud Detalle nueva");
        	
        	this.solicitudDetalle.setFechaReg(new Date());
        	this.solicitudDetalle.setUsuarioReg(nombre);
        	this.solicitudDetalle.setFlagEstado("AC");
            this.entityManager.persist(this.solicitudDetalle);
            return "search?faces-redirect=true";
         }
         else
         {
        	System.out.println("Entro por solicitud Detalle modificada");
        	
        	this.solicitudDetalle.setFechaMod(new Date());
        	this.solicitudDetalle.setUsuarioMod(nombre);
            this.entityManager.merge(this.solicitudDetalle);
            return "view?faces-redirect=true&id=" + this.solicitudDetalle.getIdSolicitudDetalle();
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
         SolicitudDetalle deletableEntity = findById(getId());
         Examen examen = deletableEntity.getExamen();
         examen.getSolicitudDetalles().remove(deletableEntity);
         deletableEntity.setExamen(null);
         this.entityManager.merge(examen);
         ListaPrecio listaPrecio = deletableEntity.getListaPrecio();
         listaPrecio.getSolicitudDetalles().remove(deletableEntity);
         deletableEntity.setListaPrecio(null);
         this.entityManager.merge(listaPrecio);
         Solicitud solicitud = deletableEntity.getSolicitud();
         solicitud.getSolicitudDetalles().remove(deletableEntity);
         deletableEntity.setSolicitud(null);
         this.entityManager.merge(solicitud);
         Iterator<RegistroResultado> iterRegistroResultados = deletableEntity.getRegistroResultados().iterator();
         for (; iterRegistroResultados.hasNext();)
         {
            RegistroResultado nextInRegistroResultados = iterRegistroResultados.next();
            nextInRegistroResultados.setSolicitudDetalle(null);
            iterRegistroResultados.remove();
            this.entityManager.merge(nextInRegistroResultados);
         }
         Iterator<Entrega> iterEntregas = deletableEntity.getEntregas().iterator();
         for (; iterEntregas.hasNext();)
         {
            Entrega nextInEntregas = iterEntregas.next();
            nextInEntregas.setSolicitudDetalle(null);
            iterEntregas.remove();
            this.entityManager.merge(nextInEntregas);
         }
         Iterator<TomaMuestra> iterTomaMuestras = deletableEntity.getTomaMuestras().iterator();
         for (; iterTomaMuestras.hasNext();)
         {
            TomaMuestra nextInTomaMuestras = iterTomaMuestras.next();
            nextInTomaMuestras.setSolicitudDetalle(null);
            iterTomaMuestras.remove();
            this.entityManager.merge(nextInTomaMuestras);
         }
         this.entityManager.remove(deletableEntity);
         */
    	  
    	  String nombre = request.getUserPrincipal().getName();
          SolicitudDetalle deletableEntity = findById(getId());
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
    * Support searching SolicitudDetalle entities with pagination
    */

   private int page;
   private long count;
   private List<SolicitudDetalle> pageItems;

   private SolicitudDetalle example = new SolicitudDetalle();

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

   public SolicitudDetalle getExample()
   {
      return this.example;
   }

   public void setExample(SolicitudDetalle example)
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
      Root<SolicitudDetalle> root = countCriteria.from(SolicitudDetalle.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<SolicitudDetalle> criteria = builder.createQuery(SolicitudDetalle.class);
      root = criteria.from(SolicitudDetalle.class);
      TypedQuery<SolicitudDetalle> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<SolicitudDetalle> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idSolicitudDetalle = this.example.getIdSolicitudDetalle();
      if (idSolicitudDetalle != 0)
      {
         predicatesList.add(builder.equal(root.get("idSolicitudDetalle"), idSolicitudDetalle));
      }
      Examen examen = this.example.getExamen();
      if (examen != null)
      {
         predicatesList.add(builder.equal(root.get("examen"), examen));
      }
      ListaPrecio listaPrecio = this.example.getListaPrecio();
      if (listaPrecio != null)
      {
         predicatesList.add(builder.equal(root.get("listaPrecio"), listaPrecio));
      }
      Solicitud solicitud = this.example.getSolicitud();
      if (solicitud != null)
      {
         predicatesList.add(builder.equal(root.get("solicitud"), solicitud));
      }
      String descripcion = this.example.getDescripcion();
      if (descripcion != null && !"".equals(descripcion))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("descripcion")), '%' + descripcion.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<SolicitudDetalle> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   
   /*
    * Support listing and POSTing back SolicitudDetalle entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<SolicitudDetalle> getAllSolicitudesNueva()
   {

      CriteriaQuery<SolicitudDetalle> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(SolicitudDetalle.class);
      String consulta = "select sd from SolicitudDetalle sd where sd.flagEstado='AC'";
      return this.entityManager.createQuery(consulta).getResultList();
   }
   
   /*
    * Support listing and POSTing back SolicitudDetalle entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<SolicitudDetalle> getAll()
   {

      CriteriaQuery<SolicitudDetalle> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(SolicitudDetalle.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(SolicitudDetalle.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final SolicitudDetalleBean ejbProxy = this.sessionContext.getBusinessObject(SolicitudDetalleBean.class);

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

            return String.valueOf(((SolicitudDetalle) value).getIdSolicitudDetalle());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private SolicitudDetalle add = new SolicitudDetalle();

   public SolicitudDetalle getAdd()
   {
      return this.add;
   }

   public SolicitudDetalle getAdded()
   {
      SolicitudDetalle added = this.add;
      this.add = new SolicitudDetalle();
      return added;
   }
}
