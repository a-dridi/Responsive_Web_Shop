/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.model.dao;

import at.adridi.responsivewebshop.model.Order;
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
public class OrderDAO implements AutoCloseable, Serializable {

    /**
     * Get All Orders
     *
     * @return
     */
    public List<Order> getAllOrder() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("FROM Order ORDER BY order_id DESC");
            return query.list();
        } catch (HibernateException e) {
            System.out.println("Error in getAllOrder(): " + e);
            return null;
        } finally {
            session.close();
        }
    }

    /**
     * Get All created Orders. Paid and confirmed.
     *
     * @return
     */
    public List<Order> getAllOrderCreated() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("FROM Order WHERE created=false ORDER BY order_id DESC");
            return query.list();
        } catch (HibernateException e) {
            System.out.println("Error in getAllOrder(): " + e);
            return null;
        } finally {
            session.close();
        }
    }

    public Order getOrderById(Integer orderId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("FROM Order WHERE order_id=:orderIdValue");
            query.setParameter("orderIdValue", orderId);
            List<Order> ordersWithProductId = query.list();
            if (ordersWithProductId != null) {
                return ordersWithProductId.get(0);
            } else {
                return null;
            }
        } catch (HibernateException e) {
            System.out.println("Error in getOrderById(): " + e);
            return null;
        } finally {
            session.close();
        }
    }

    public Order getOrderByPaypalId(Integer paypalorderId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("FROM Order WHERE paypalOrderId=:paypalorderIdValue");
            query.setParameter("paypalorderIdValue", paypalorderId);
            List<Order> ordersWithProductId = query.list();
            if (ordersWithProductId != null) {
                return ordersWithProductId.get(0);
            } else {
                return null;
            }
        } catch (HibernateException e) {
            System.out.println("Error in getOrderByPaypalId(): " + e);
            return null;
        } finally {
            session.close();
        }
    }

    public boolean addOrder(Order order) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.save(order);
            transaction.commit();
            return true;
        } catch (HibernateException e) {
            System.out.println("Error in addOrder(): " + e);
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        } finally {
            session.close();
        }
    }

    public boolean deleteOrder(Order order) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.delete(order);
            transaction.commit();
            return true;
        } catch (HibernateException e) {
            System.out.println("Error in deleteOrder(): " + e);
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        } finally {
            session.close();
        }
    }

    public boolean updateOrder(Order order) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.update(order);
            transaction.commit();
            return true;
        } catch (HibernateException e) {
            System.out.println("Error in updateOrder(): " + e);
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        } finally {
            session.close();
        }
    }

    public boolean updateOrderStatus(Integer order_id, Integer orderstatusId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("UPDATE Order o set o.orderStatus=:statusId");
            query.setParameter("statusId", orderstatusId);
            query.executeUpdate();
            return true;
        } catch (HibernateException e) {
            System.out.println("Error in getAllProduct(): " + e);
            return false;
        } finally {
            session.close();
        }

    }

    @Override
    public void close() throws Exception {
        HibernateUtil.close();
    }
}
