package com.sinapsistech.laboratorio.aciones;

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

import com.sinapsistech.laboratorio.model.Componente;
import com.sinapsistech.laboratorio.model.Examen;
import com.sinapsistech.laboratorio.model.ExamenComponente;
import com.sinapsistech.laboratorio.model.ListaPrecio;
import com.sinapsistech.laboratorio.model.SolicitudDetalle;
import com.sinapsistech.laboratorio.model.SubTipoExamen;

import java.util.Iterator;

import javax.faces.context.FacesContext;

import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

/**
 * Backing bean for Examen entities.
 * <p/>
 * This class provides CRUD functionality for all Examen entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ComponentePasoBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Examen entities
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

   private Examen examen;

   public Examen getExamen()
   {
      return this.examen;
   }

   public void setExamen(Examen examen)
   {
      this.examen = examen;
   }

   @Inject
   private Conversation conversation;

   @PersistenceContext(unitName = "ilab-persistence-unit", type = PersistenceContextType.EXTENDED)
   private EntityManager entityManager;

   private ExamenComponente selectedExaComponente;
   private ExamenComponente nuevoExaComponente;
   
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
         this.examen = this.example;
      }
      else
      {
         this.examen = findById(getId());
      }
   }

   public Examen findById(Integer id)
   {

      return this.entityManager.find(Examen.class, id);
   }

   /*
    * Support updating and deleting Examen entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.examen);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.examen);
            return "view?faces-redirect=true&id=" + this.examen.getIdExamen();
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
         Examen deletableEntity = findById(getId());
         SubTipoExamen subTipoExamen = deletableEntity.getSubTipoExamen();
         subTipoExamen.getExamens().remove(deletableEntity);
         deletableEntity.setSubTipoExamen(null);
         this.entityManager.merge(subTipoExamen);
         Iterator<ListaPrecio> iterListaPrecios = deletableEntity.getListaPrecios().iterator();
         for (; iterListaPrecios.hasNext();)
         {
            ListaPrecio nextInListaPrecios = iterListaPrecios.next();
            nextInListaPrecios.setExamen(null);
            iterListaPrecios.remove();
            this.entityManager.merge(nextInListaPrecios);
         }
         Iterator<ExamenComponente> iterExamenComponentes = deletableEntity.getExamenComponentes().iterator();
         for (; iterExamenComponentes.hasNext();)
         {
            ExamenComponente nextInExamenComponentes = iterExamenComponentes.next();
            nextInExamenComponentes.setExamen(null);
            iterExamenComponentes.remove();
            this.entityManager.merge(nextInExamenComponentes);
         }
         Iterator<SolicitudDetalle> iterSolicitudDetalles = deletableEntity.getSolicitudDetalles().iterator();
         for (; iterSolicitudDetalles.hasNext();)
         {
            SolicitudDetalle nextInSolicitudDetalles = iterSolicitudDetalles.next();
            nextInSolicitudDetalles.setExamen(null);
            iterSolicitudDetalles.remove();
            this.entityManager.merge(nextInSolicitudDetalles);
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
    * Support searching Examen entities with pagination
    */

   private int page;
   private long count;
   private List<Examen> pageItems;

   private Examen example = new Examen();

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

   public Examen getExample()
   {
      return this.example;
   }

   public void setExample(Examen example)
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
      Root<Examen> root = countCriteria.from(Examen.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Examen> criteria = builder.createQuery(Examen.class);
      root = criteria.from(Examen.class);
      TypedQuery<Examen> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Examen> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idExamen = this.example.getIdExamen();
      if (idExamen != 0)
      {
         predicatesList.add(builder.equal(root.get("idExamen"), idExamen));
      }
      SubTipoExamen subTipoExamen = this.example.getSubTipoExamen();
      if (subTipoExamen != null)
      {
         predicatesList.add(builder.equal(root.get("subTipoExamen"), subTipoExamen));
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

   public List<Examen> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Examen entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Examen> getAll()
   {

      CriteriaQuery<Examen> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Examen.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Examen.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final ComponentePasoBean ejbProxy = this.sessionContext.getBusinessObject(ComponentePasoBean.class);

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

            return String.valueOf(((Examen) value).getIdExamen());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Examen add = new Examen();

   public Examen getAdd()
   {
      return this.add;
   }

   public Examen getAdded()
   {
      Examen added = this.add;
      this.add = new Examen();
      return added;
   }
   
   public void onRowEdit(RowEditEvent event) {
       FacesMessage msg = new FacesMessage("Datos Editados", ((ExamenComponente) event.getObject()).getIdExamenComponente()+"");
       FacesContext.getCurrentInstance().addMessage(null, msg);
   }
    
   public void onRowCancel(RowEditEvent event) {
       FacesMessage msg = new FacesMessage("Cancelado", ((ExamenComponente) event.getObject()).getIdExamenComponente()+"");
       FacesContext.getCurrentInstance().addMessage(null, msg);
   }
    
   public void onCellEdit(CellEditEvent event) {
       Object oldValue = event.getOldValue();
       Object newValue = event.getNewValue();
        
       if(newValue != null && !newValue.equals(oldValue)) {
           FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
           FacesContext.getCurrentInstance().addMessage(null, msg);
       }
   }

public ExamenComponente getSelectedExaComponente() {
	return selectedExaComponente;
}

public void setSelectedExaComponente(
		ExamenComponente selectedExaComponente) {
	this.selectedExaComponente = selectedExaComponente;
}

public ExamenComponente getNuevoExaComponente() {
	return nuevoExaComponente;
}

public void setNuevoExaComponente(ExamenComponente nuevoExaComponente) {
	this.nuevoExaComponente = nuevoExaComponente;
}
}
