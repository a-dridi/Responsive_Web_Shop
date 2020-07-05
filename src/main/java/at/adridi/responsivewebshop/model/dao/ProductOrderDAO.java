/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.model.dao;

import at.adridi.responsivewebshop.model.ProductOrder;
import java.io.Serializable;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author A.Dridi
 */
public class ProductOrderDAO implements AutoCloseable, Serializable{

    public List<ProductOrder> getAllProductOrder() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("FROM ProductOrder");
            return query.list();
        } catch (HibernateException e) {
            System.out.println("Error in getAllProductOrder(): " + e);
            return null;
        } finally {
            session.close();
        }
    }

    public List<ProductOrder> getAllProductOrderByOrderId(int order_id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("FROM ProductOrder WHERE productor_order_id=:orderIdValue");
            query.setParameter("orderIdValue", order_id);
            return query.list();

        } catch (HibernateException e) {
            System.out.println("Error in getAllProductOrderByOrderId(): " + e);
            return null;
        } finally {
            session.close();
        }
    }

    public boolean addProductOrder(ProductOrder productorder) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        boolean returnValue = false;

        try {
            transaction = session.beginTransaction();
            session.save(productorder);
            transaction.commit();
            returnValue = true;

        } catch (HibernateException e) {
            System.out.println("Error in addProductOrder(): " + e);
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
            return returnValue;
        }
    }

    public boolean deleteProductOrder(ProductOrder productorder) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        boolean returnValue = false;

        try {
            transaction = session.beginTransaction();
            session.delete(productorder);
            transaction.commit();
        } catch (HibernateException e) {
            System.out.println("Error in deleteProductOrder(): " + e);
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
            return returnValue;
        }
    }

    public boolean updateProductOrder(ProductOrder productorder) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        boolean returnValue = false;

        try {
            transaction = session.beginTransaction();
            session.update(productorder);
            transaction.commit();
        } catch (HibernateException e) {
            System.out.println("Error in updateProductOrder(): " + e);
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
            return returnValue;
        }
    }

    @Override
    public void close() throws Exception {
        HibernateUtil.close();
    }

}
