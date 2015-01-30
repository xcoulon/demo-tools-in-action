package org.jboss.examples.conferenceschedule.rest.dto;

import java.io.Serializable;

import org.jboss.examples.conferenceschedule.domain.Conference;

import javax.persistence.EntityManager;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;

import org.jboss.examples.conferenceschedule.domain.Session;
import org.jboss.examples.conferenceschedule.rest.dto.NestedSessionDTO;

import java.util.Iterator;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ConferenceDTO implements Serializable
{

   private long id;
   private Integer version;
   private String location;
   private String name;
   private Date startDate;
   private Date endDate;
   private Set<NestedSessionDTO> sessions = new HashSet<NestedSessionDTO>();

   public ConferenceDTO()
   {
   }

   public ConferenceDTO(final Conference entity)
   {
      if (entity != null)
      {
         this.id = entity.getId();
         this.version = entity.getVersion();
         this.location = entity.getLocation();
         this.name = entity.getName();
         this.startDate = entity.getStartDate();
         this.endDate = entity.getEndDate();
         Iterator<Session> iterSessions = entity.getSessions().iterator();
         while (iterSessions.hasNext())
         {
            Session element = iterSessions.next();
            this.sessions.add(new NestedSessionDTO(element));
         }
      }
   }

   public Conference fromDTO(Conference entity, EntityManager em)
   {
      if (entity == null)
      {
         entity = new Conference();
      }
      entity.setVersion(this.version);
      entity.setLocation(this.location);
      entity.setName(this.name);
      entity.setStartDate(this.startDate);
      entity.setEndDate(this.endDate);
      Iterator<Session> iterSessions = entity.getSessions().iterator();
      while (iterSessions.hasNext())
      {
         boolean found = false;
         Session session = iterSessions.next();
         Iterator<NestedSessionDTO> iterDtoSessions = this.getSessions()
               .iterator();
         while (iterDtoSessions.hasNext())
         {
            NestedSessionDTO dtoSession = iterDtoSessions.next();
            if (((Long) dtoSession.getId()).equals((Long) session.getId()))
            {
               found = true;
               break;
            }
         }
         if (found == false)
         {
            iterSessions.remove();
         }
      }
      Iterator<NestedSessionDTO> iterDtoSessions = this.getSessions()
            .iterator();
      while (iterDtoSessions.hasNext())
      {
         boolean found = false;
         NestedSessionDTO dtoSession = iterDtoSessions.next();
         iterSessions = entity.getSessions().iterator();
         while (iterSessions.hasNext())
         {
            Session session = iterSessions.next();
            if (((Long) dtoSession.getId()).equals((Long) session.getId()))
            {
               found = true;
               break;
            }
         }
         if (found == false)
         {
            Iterator<Session> resultIter = em
                  .createQuery("SELECT DISTINCT s FROM Session s",
                        Session.class).getResultList().iterator();
            while (resultIter.hasNext())
            {
               Session result = resultIter.next();
               if (((Long) result.getId()).equals((Long) dtoSession
                     .getId()))
               {
                  entity.getSessions().add(result);
                  break;
               }
            }
         }
      }
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

   public Date getStartDate()
   {
      return this.startDate;
   }

   public void setStartDate(final Date startDate)
   {
      this.startDate = startDate;
   }

   public Date getEndDate()
   {
      return this.endDate;
   }

   public void setEndDate(final Date endDate)
   {
      this.endDate = endDate;
   }

   public Set<NestedSessionDTO> getSessions()
   {
      return this.sessions;
   }

   public void setSessions(final Set<NestedSessionDTO> sessions)
   {
      this.sessions = sessions;
   }
}