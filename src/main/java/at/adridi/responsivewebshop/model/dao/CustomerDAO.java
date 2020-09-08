/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.model.dao;

import at.adridi.responsivewebshop.model.Customer;
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
public class CustomerDAO implements AutoCloseable, Serializable {

    public List<Customer> getAllCustomer() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("FROM Customer");
            return query.list();
        } catch (HibernateException e) {
            System.out.println("Error in getAllCustomer(): " + e);
            return null;
        } finally {
            session.close();
        }
    }

    public Customer getCustomerByEmail(String email) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("FROM Customer WHERE email=:emailValue");
            query.setParameter("emailValue", email);
            List<Customer> customer = query.list();
            if (customer != null) {
                return customer.get(0);
            } else {
                return null;
            }
        } catch (HibernateException e) {
            System.out.println("Error in getCustomerByEmail(): " + e);
            return null;
        } finally {
            session.close();
        }

    }

    public boolean addCustomer(Customer customer) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.save(customer);
            transaction.commit();
            return true;
        } catch (HibernateException e) {
            System.out.println("Error in addCustomer(): " + e);
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        } finally {
            session.close();
        }
    }

    public boolean deleteCustomer(Customer customer) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.delete(customer);
            transaction.commit();
            return true;
        } catch (HibernateException e) {
            System.out.println("Error in deleteCustomer(): " + e);
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        } finally {
            session.close();
        }
    }

    public boolean updateCustomer(Customer customer) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.update(customer);
            transaction.commit();
            return true;
        } catch (HibernateException e) {
            System.out.println("Error in updateCustomer(): " + e);
            if (transaction != null) {
                transaction.rollback();
            }
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
