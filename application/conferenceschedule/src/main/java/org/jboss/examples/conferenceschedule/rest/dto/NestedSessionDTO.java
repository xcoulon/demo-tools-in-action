package org.jboss.examples.conferenceschedule.rest.dto;

import java.io.Serializable;
import org.jboss.examples.conferenceschedule.domain.Session;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;

public class NestedSessionDTO implements Serializable
{

   private long id;
   private Integer version;
   private String description;
   private Date endTime;
   private String location;
   private String name;
   private Date startTime;

   public NestedSessionDTO()
   {
   }

   public NestedSessionDTO(final Session entity)
   {
      if (entity != null)
      {
         this.id = entity.getId();
         this.version = entity.getVersion();
         this.description = entity.getDescription();
         this.endTime = entity.getEndTime();
         this.location = entity.getLocation();
         this.name = entity.getName();
         this.startTime = entity.getStartTime();
      }
   }

   public Session fromDTO(Session entity, EntityManager em)
   {
      if (entity == null)
      {
         entity = new Session();
      }
      if (((Long) this.id) != null)
      {
         TypedQuery<Session> findByIdQuery = em.createQuery(
               "SELECT DISTINCT s FROM Session s WHERE s.id = :entityId",
               Session.class);
         findByIdQuery.setParameter("entityId", this.id);
         try
         {
            entity = findByIdQuery.getSingleResult();
         }
         catch (javax.persistence.NoResultException nre)
         {
            entity = null;
         }
         return entity;
      }
      entity.setVersion(this.version);
      entity.setDescription(this.description);
      entity.setEndTime(this.endTime);
      entity.setLocation(this.location);
      entity.setName(this.name);
      entity.setStartTime(this.startTime);
      entity = em.merge(entity);
      return entity;
   }

   public long getId()
   {
      return this.id;
   }

   public void setId(final long id)
   {
      this.id = id;
   }

   public Integer getVersion()
   {
      return this.version;
   }

   public void setVersion(final Integer version)
   {
      this.version = version;
   }

   public String getDescription()
   {
      return this.description;
   }

   public void setDescription(final String description)
   {
      this.description = description;
   }

   public Date getEndTime()
   {
      return this.endTime;
   }

   public void setEndTime(final Date endTime)
   {
      this.endTime = endTime;
   }

   public String getLocation()
   {
      return this.location;
   }

   public void setLocation(final String location)
   {
      this.location = location;
   }

   public String getName()
   {
      return this.name;
   }

   public void setName(final String name)
   {
      this.name = name;
   }

   public Date getStartTime()
   {
      return this.startTime;
   }

   public void setStartTime(final Date startTime)
   {
      this.startTime = startTime;
   }
}