package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.BatchCertificateFolios;
import com.m4c.model.entity.BatchCertificates;
import com.m4sg.crm4marketingsunset.core.dto.AllocateBatchDTO;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Usuario on 16/06/2015.
 */
public class BatchCertificateFoliosDAO extends GenericDAO<BatchCertificateFolios> {
    public BatchCertificateFoliosDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    @Override
    public List<BatchCertificateFolios> findAll() {
        return null;
    }

    @Override
    public Optional<BatchCertificateFolios> find(long id) {
        return Optional.fromNullable(get(id));
    }

    public List<BatchCertificateFolios> findbyrange(AllocateBatchDTO allocateBatchDTO, BatchCertificates batchCertificates) {
        return criteria().add(
                Restrictions.and(
                        Restrictions.or(
                                Restrictions.and(Restrictions.le("begin", (int) (long) allocateBatchDTO.getStart()),
                                        Restrictions.ge("end", (int) (long) allocateBatchDTO.getStart())
                                ),
                                Restrictions.and(Restrictions.le("begin", (int) (long) allocateBatchDTO.getEnd()),
                                        Restrictions.ge("end", (int) (long) allocateBatchDTO.getEnd())
                                )
                        ),
                        Restrictions.eq("batchCertificates", batchCertificates)
                )
        ).list();
    }

    @Override
    public BatchCertificateFolios saveOrUpdate(BatchCertificateFolios entity) {
        this.currentSession().saveOrUpdate(entity);
        return entity;
    }



}
