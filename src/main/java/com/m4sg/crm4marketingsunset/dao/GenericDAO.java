package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4sg.crm4marketingsunset.core.dto.PaginationDTO;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.QueryException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;

import java.util.List;

/**
 * Created by sistemas on 06/01/2015.
 */
public abstract class GenericDAO<E> extends AbstractDAO<E>{

    public abstract List<E> findAll();

    public abstract Optional<E> find(long id);

    public abstract E saveOrUpdate(E entity);

    public GenericDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<E> findSubset(Criteria criteria, int start, int pageLength) {
        criteria.setProjection(null);
        criteria.setFirstResult(start);
        criteria.setMaxResults(pageLength);
        return list(criteria);
    }
    public List<E> findSubset(Query query, int start, int pageLength) {
        query.setFirstResult(start);
        query.setMaxResults(pageLength);
        return list(query);
    }

    public long count(Criteria criteria) {
        return (Long)criteria.setProjection(Projections.rowCount()).uniqueResult();
    }

    public long count(Query query) {

        return (long)list(query).size();
    }


    public PaginationDTO<E> pagination(Criteria criteria, int pageLength, int page) throws QueryException {

        long totalElements = count(criteria);

        int totalPages = (int) Math.ceil(new Double(totalElements) / new Double(pageLength));

        // redirect too-large page numbers to the last page
        if (page > totalPages) {
            page = totalPages;
        }
        // redirect too-small page numbers (or strings converted to 0) to the first page
        if (page < 1) {
            page = 1;
        }

        // determine the start and end element for the current page; for example, on
        // page 3 with 50 elements per page, $start and $end would be 101 and 150
        int start = ((page - 1) * pageLength);

        List<E> elements = findSubset(criteria, start, pageLength);


        PaginationDTO<E> paginationDTO = new PaginationDTO<E>(page, totalElements, totalPages,elements);

        return paginationDTO;
    }

    public PaginationDTO<E> pagination(Query query, int pageLength, int page) throws QueryException {

        long totalElements = count(query);

        int totalPages = (int) Math.ceil(new Double(totalElements) / new Double(pageLength));

        // redirect too-large page numbers to the last page
        if (page > totalPages) {
            page = totalPages;
        }
        // redirect too-small page numbers (or strings converted to 0) to the first page
        if (page < 1) {
            page = 1;
        }

        // determine the start and end element for the current page; for example, on
        // page 3 with 50 elements per page, $start and $end would be 101 and 150
        int start = ((page - 1) * pageLength);
        int end = page * pageLength;

        List<E> elements = findSubset(query, start, end);

        PaginationDTO<E> paginationDTO = new PaginationDTO<E>(page, totalElements, totalPages, elements);

        return paginationDTO;
    }

}
