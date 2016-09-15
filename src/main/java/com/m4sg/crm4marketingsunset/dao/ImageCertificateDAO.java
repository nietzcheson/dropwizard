package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.ImageCertificate;
import com.m4sg.crm4marketingsunset.core.dto.PaginationDTO;
import com.m4sg.crm4marketingsunset.util.Constants;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by jhon on 07/05/15.
 */
public class ImageCertificateDAO extends GenericDAO<ImageCertificate>  {
    @Override
    public List<ImageCertificate> findAll() {
        return null;
    }

    @Override
    public Optional<ImageCertificate> find(long id) {
        return null;
    }

    public Optional<ImageCertificate> find(String name) {
        return Optional.fromNullable((ImageCertificate) this.currentSession().
                createCriteria(ImageCertificate.class).
                add(Restrictions.eq("image", name)).setMaxResults(1).uniqueResult());
    }

    @Override
    public ImageCertificate saveOrUpdate(ImageCertificate entity) {
        currentSession().saveOrUpdate(entity);
        return entity;
    }
    public PaginationDTO<ImageCertificate> list(int sizePage,int page){
        Criteria criteria;
        criteria=criteria().addOrder(Order.asc("id"));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        return pagination(criteria,sizePage,page);
    }

    public ImageCertificateDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    public PaginationDTO<ImageCertificate> findAll(String orderBy,String dir,int sizePage,int page) {

        Criteria criteria=this.currentSession().createCriteria(ImageCertificate.class);

        if(dir.equalsIgnoreCase(Constants.ORDER_ASC) && !orderBy.isEmpty()) {
            criteria.addOrder(Order.asc(orderBy));
        }else if(dir.equalsIgnoreCase(Constants.ORDER_DESC) && !orderBy.isEmpty()){
            criteria.addOrder(Order.desc(orderBy));
        }
        criteria.add(Restrictions.eq("type", new Long(1)));
        criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        return pagination(criteria,sizePage,page);

    }
    public PaginationDTO<ImageCertificate> globalSearch(String search,String orderBy,String direction,int sizePage,int page){

        StringBuilder hql=new StringBuilder();

        hql.append("select i from ImageCertificate i where  LOWER(i.image) like :name  and type = 2");


        Query query=currentSession().createQuery(hql.toString());

        query.setParameter("name","%"+search.toLowerCase()+"%");


        if(direction.equalsIgnoreCase(Constants.ORDER_ASC) && !orderBy.isEmpty()){

            hql.append(" order by u."+orderBy +" "+Constants.ORDER_ASC);

        }else if(direction.equalsIgnoreCase(Constants.ORDER_DESC) && !orderBy.isEmpty()){
            hql.append(" order by u." + orderBy + " " + Constants.ORDER_DESC);
        }

        return pagination(query,sizePage,page);
    }

}
