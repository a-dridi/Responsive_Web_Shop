/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.model.dao;

import at.adridi.responsivewebshop.model.Settings;
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
public class SettingsDAO implements AutoCloseable, Serializable{

    public List<Settings> getAllSettings() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("FROM Settings");
            return query.list();
        } catch (HibernateException e) {
            System.out.println("Error in getAllSettings(): " + e);
            return null;
        } finally {
            session.close();
        }
    }

    public Settings getSettingBySettingkey(String settingkey) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("FROM Settings WHERE settingkey=:settingkeyvalue");
            query.setParameter("settingkeyvalue", settingkey);
            if (query.list() != null && query.list().size() > 0) {
                return (Settings) query.list().get(0);
            } else {
                return null;
            }
        } catch (HibernateException e) {
            System.out.println("Error in getAllSettings(): " + e);
            return null;
        } finally {
            session.close();
        }

    }

    public boolean addSettings(Settings settings) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        boolean returnValue = false;

        try {
            transaction = session.beginTransaction();
            session.save(settings);
            transaction.commit();
            returnValue = true;

        } catch (HibernateException e) {
            System.out.println("Error in addSettings(): " + e);
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
            return returnValue;
        }
    }

    public boolean deleteSettings(Settings settings) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        boolean returnValue = false;

        try {
            transaction = session.beginTransaction();
            session.delete(settings);
            transaction.commit();
        } catch (HibernateException e) {
            System.out.println("Error in deleteSettings(): " + e);
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
            return returnValue;
        }
    }

    public boolean updateSettings(Settings settings) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        boolean returnValue = false;

        try {
            transaction = session.beginTransaction();
            session.update(settings);
            transaction.commit();
        } catch (HibernateException e) {
            System.out.println("Error in updateSettings(): " + e);
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
