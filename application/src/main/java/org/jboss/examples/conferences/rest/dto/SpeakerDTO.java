package org.jboss.examples.conferences.rest.dto;

import java.io.Serializable;
import org.jboss.examples.conferences.domain.Speaker;
import javax.persistence.EntityManager;
import java.util.Set;
import java.util.HashSet;
import org.jboss.examples.conferences.rest.dto.NestedSessionDTO;
import org.jboss.examples.conferences.domain.Session;
import java.util.Iterator;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SpeakerDTO implements Serializable
{

   private long id;
   private Integer version;
   private String bio;
   private String name;
   private String twitterhandle;
   private Set<NestedSessionDTO> sessions = new HashSet<NestedSessionDTO>();

   public SpeakerDTO()
   {
   }

   public SpeakerDTO(final Speaker entity)
   {
      if (entity != null)
      {
         this.id = entity.getId();
         this.version = entity.getVersion();
         this.bio = entity.getBio();
         this.name = entity.getName();
         this.twitterhandle = entity.getTwitterhandle();
         Iterator<Session> iterSessions = entity.getSessions().iterator();
         while (iterSessions.hasNext())
         {
            Session element = iterSessions.next();
            this.sessions.add(new NestedSessionDTO(element));
         }
      }
   }

   public Speaker fromDTO(Speaker entity, EntityManager em)
   {
      if (entity == null)
      {
         entity = new Speaker();
      }
      entity.setVersion(this.version);
      entity.setBio(this.bio);
      entity.setName(this.name);
      entity.setTwitterhandle(this.twitterhandle);
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

   public String getBio()
   {
      return this.bio;
   }

   public void setBio(final String bio)
   {
      this.bio = bio;
   }

   public String getName()
   {
      return this.name;
   }

   public void setName(final String name)
   {
      this.name = name;
   }

   public String getTwitterhandle()
   {
      return this.twitterhandle;
   }

   public void setTwitterhandle(final String twitterhandle)
   {
      this.twitterhandle = twitterhandle;
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