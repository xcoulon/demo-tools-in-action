package org.jboss.examples.conferences.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import org.jboss.examples.conferences.rest.dto.SessionDTO;
import org.jboss.examples.conferences.domain.Session;

/**
 * 
 */
@Stateless
@Path("/sessions")
public class SessionEndpoint
{
   @PersistenceContext(unitName = "primary")
   private EntityManager em;

   @POST
   @Consumes("application/json")
   public Response create(SessionDTO dto)
   {
      Session entity = dto.fromDTO(null, em);
      em.persist(entity);
      return Response.created(UriBuilder.fromResource(SessionEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   public Response deleteById(@PathParam("id") long id)
   {
      Session entity = em.find(Session.class, id);
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      em.remove(entity);
      return Response.noContent().build();
   }

   @GET
   @Path("/{id:[0-9][0-9]*}")
   @Produces("application/json")
   public Response findById(@PathParam("id") long id)
   {
      TypedQuery<Session> findByIdQuery = em.createQuery("SELECT DISTINCT s FROM Session s LEFT JOIN FETCH s.conference LEFT JOIN FETCH s.speakers WHERE s.id = :entityId ORDER BY s.id", Session.class);
      findByIdQuery.setParameter("entityId", id);
      Session entity;
      try
      {
         entity = findByIdQuery.getSingleResult();
      }
      catch (NoResultException nre)
      {
         entity = null;
      }
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      SessionDTO dto = new SessionDTO(entity);
      return Response.ok(dto).build();
   }

   @GET
   @Produces("application/json")
   public List<SessionDTO> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult)
   {
      TypedQuery<Session> findAllQuery = em.createQuery("SELECT DISTINCT s FROM Session s LEFT JOIN FETCH s.conference LEFT JOIN FETCH s.speakers ORDER BY s.id", Session.class);
      if (startPosition != null)
      {
         findAllQuery.setFirstResult(startPosition);
      }
      if (maxResult != null)
      {
         findAllQuery.setMaxResults(maxResult);
      }
      final List<Session> searchResults = findAllQuery.getResultList();
      final List<SessionDTO> results = new ArrayList<SessionDTO>();
      for (Session searchResult : searchResults)
      {
         SessionDTO dto = new SessionDTO(searchResult);
         results.add(dto);
      }
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes("application/json")
   public Response update(@PathParam("id") long id, SessionDTO dto)
   {
      TypedQuery<Session> findByIdQuery = em.createQuery("SELECT DISTINCT s FROM Session s LEFT JOIN FETCH s.conference LEFT JOIN FETCH s.speakers WHERE s.id = :entityId ORDER BY s.id", Session.class);
      findByIdQuery.setParameter("entityId", id);
      Session entity;
      try
      {
         entity = findByIdQuery.getSingleResult();
      }
      catch (NoResultException nre)
      {
         entity = null;
      }
      entity = dto.fromDTO(entity, em);
      try
      {
         entity = em.merge(entity);
      }
      catch (OptimisticLockException e)
      {
         return Response.status(Response.Status.CONFLICT).entity(e.getEntity()).build();
      }
      return Response.noContent().build();
   }
}
