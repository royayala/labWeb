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
import javax.persistence.EntityTransaction;
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
import com.sinapsistech.laboratorio.model.RegistroResultado;
import com.sinapsistech.laboratorio.model.TomaMuestra;
import com.sinapsistech.laboratorio.model.SolicitudDetalle;
import com.sinapsistech.laboratorio.model.Trabajador;

/**
 * Backing bean for TomaMuestra entities.
 * <p/>
 * This class provides CRUD functionality for all TomaMuestra entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class TomaMuestraBean implements Serializable
{

   private static final long serialVersionUID = 1L;
   FacesContext context = FacesContext.getCurrentInstance();
   ExternalContext externalContext = context.getExternalContext();
   HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();

   /*
    * Support creating and retrieving TomaMuestra entities
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

   private TomaMuestra tomaMuestra;

   public TomaMuestra getTomaMuestra()
   {
      return this.tomaMuestra;
   }

   public void setTomaMuestra(TomaMuestra tomaMuestra)
   {
      this.tomaMuestra = tomaMuestra;
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
         this.tomaMuestra = this.example;
      }
      else
      {
         this.tomaMuestra = findById(getId());
      }
   }

   public TomaMuestra findById(Integer id)
   {

      return this.entityManager.find(TomaMuestra.class, id);
   }

   /*
    * Support updating and deleting TomaMuestra entities
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
          	System.out.println("Entro por toma de Muestra nuevo");
          	
          	// ponemos valores de registro
          	this.tomaMuestra.setFechaReg(new Date());
          	this.tomaMuestra.setUsuarioReg(nombre);
          	this.tomaMuestra.setFlagEstado("AC");
            this.entityManager.persist(this.tomaMuestra);
            
            // actualizamos el examen que esta en toma de muestra.
            
            this.tomaMuestra.getSolicitudDetalle().setFlagEstado("TM");
            
            SolicitudDetalle actualizarSolicitudDetalle = this.tomaMuestra.getSolicitudDetalle() ;
            actualizarSolicitudDetalle.setFlagEstado("TM");
            actualizarSolicitudDetalle.setFechaMod(new Date());		
            actualizarSolicitudDetalle.setUsuarioMod(nombre);
            System.out.println("Actualizando solicitud detalle "+this.tomaMuestra.getSolicitudDetalle().getIdSolicitudDetalle());
            this.entityManager.merge(actualizarSolicitudDetalle);
            
            
            // metemos en la tabla registro resultado 
            
            // 1. primero sacamos los componentes del examen solicitado.
            List<ExamenComponente> componentesExamen = entityManager.createQuery("select o from ExamenComponente o where o.flagEstado='AC' and o.examen.idExamen = "+this.tomaMuestra.getSolicitudDetalle().getExamen().getIdExamen()).getResultList();
            
            // 2. lo metemos en la tabla registro resultado
            
         
           
            
            for(ExamenComponente obComponenteExamen : componentesExamen){
            	System.out.println("examen "+obComponenteExamen.getExamen().getNombre());
            	System.out.println("Componente "+obComponenteExamen.getComponente().getIdComponente());
            	System.out.println("Nombre Componente "+obComponenteExamen.getComponente().getNombre());
            	
            	String consulta = "insert into registro_resultado(id_solicitud_detalle, id_trabajador, id_examen, id_componente, fecha_reg, usuario_reg, flag_estado) values (?,?,?,?,?,?,?)";
            	
            	entityManager.createNativeQuery(consulta, RegistroResultado.class)
            		.setParameter(1,this.tomaMuestra.getSolicitudDetalle().getIdSolicitudDetalle() )
            		.setParameter(2, this.tomaMuestra.getTrabajador().getIdTrabajador())
            		.setParameter(3, this.tomaMuestra.getSolicitudDetalle().getExamen().getIdExamen())
            		.setParameter(4, obComponenteExamen.getComponente().getIdComponente())
            		.setParameter(5, new Date())
            		.setParameter(6, nombre)
            		.setParameter(7, "NU").executeUpdate();
            	
            	
            	
            }

      
            
            System.out.println("Insertado a Registro resultado con exito");
            
            return "search?faces-redirect=true";
         }
         else
         {
           	System.out.println("Entro por toma de Muestra modificado");
           	
           	this.tomaMuestra.setFechaMod(new Date());
           	this.tomaMuestra.setUsuarioMod(nombre);
            this.entityManager.merge(this.tomaMuestra);
            return "view?faces-redirect=true&id=" + this.tomaMuestra.getIdTomaMuestra();
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
         TomaMuestra deletableEntity = findById(getId());
         SolicitudDetalle solicitudDetalle = deletableEntity.getSolicitudDetalle();
         solicitudDetalle.getTomaMuestras().remove(deletableEntity);
         deletableEntity.setSolicitudDetalle(null);
         this.entityManager.merge(solicitudDetalle);
         Trabajador trabajador = deletableEntity.getTrabajador();
         trabajador.getTomaMuestras().remove(deletableEntity);
         deletableEntity.setTrabajador(null);
         this.entityManager.merge(trabajador);
         this.entityManager.remove(deletableEntity);
         */
    	  String nombre = request.getUserPrincipal().getName();
          TomaMuestra deletableEntity = findById(getId());
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
    * Support searching TomaMuestra entities with pagination
    */

   private int page;
   private long count;
   private List<TomaMuestra> pageItems;

   private TomaMuestra example = new TomaMuestra();

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

   public TomaMuestra getExample()
   {
      return this.example;
   }

   public void setExample(TomaMuestra example)
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
      Root<TomaMuestra> root = countCriteria.from(TomaMuestra.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<TomaMuestra> criteria = builder.createQuery(TomaMuestra.class);
      root = criteria.from(TomaMuestra.class);
      TypedQuery<TomaMuestra> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<TomaMuestra> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idTomaMuestra = this.example.getIdTomaMuestra();
      if (idTomaMuestra != 0)
      {
         predicatesList.add(builder.equal(root.get("idTomaMuestra"), idTomaMuestra));
      }
      SolicitudDetalle solicitudDetalle = this.example.getSolicitudDetalle();
      if (solicitudDetalle != null)
      {
         predicatesList.add(builder.equal(root.get("solicitudDetalle"), solicitudDetalle));
      }
      Trabajador trabajador = this.example.getTrabajador();
      if (trabajador != null)
      {
         predicatesList.add(builder.equal(root.get("trabajador"), trabajador));
      }
      String observaciones = this.example.getObservaciones();
      if (observaciones != null && !"".equals(observaciones))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("observaciones")), '%' + observaciones.toLowerCase() + '%'));
      }
      String flagEstado = this.example.getFlagEstado();
      if (flagEstado != null && !"".equals(flagEstado))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("flagEstado")), '%' + flagEstado.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<TomaMuestra> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back TomaMuestra entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<TomaMuestra> getAll()
   {

      CriteriaQuery<TomaMuestra> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(TomaMuestra.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(TomaMuestra.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final TomaMuestraBean ejbProxy = this.sessionContext.getBusinessObject(TomaMuestraBean.class);

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

            return String.valueOf(((TomaMuestra) value).getIdTomaMuestra());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private TomaMuestra add = new TomaMuestra();

   public TomaMuestra getAdd()
   {
      return this.add;
   }

   public TomaMuestra getAdded()
   {
      TomaMuestra added = this.add;
      this.add = new TomaMuestra();
      return added;
   }
}
