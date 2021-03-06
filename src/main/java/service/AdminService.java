package service;

import model.Admin;
import model.Business;
import model.Customer;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


/**
 * Created by Quang Linh on 04 Aug 2020
 * AdminService: CRUD functions
 */

@Transactional
@Service
public class AdminService {

    @Autowired
    private SessionFactory sessionFactory;

    public void  setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // save admin
    public void saveAdmin(Admin admin) {
        System.out.println(admin.getBusiness());
        if (admin.getRole() == null){
            admin.setRole("ADMIN");                               //if admin get no role
                                                                    //automated set role
        }

        // Admin and Business have one-to-one relationship
        //in case admin set none of business_id
        //set bu_id in admin details
        if(admin.getBusiness()!=null ){
            int business_id = admin.getBusiness().getId();
            Query query = sessionFactory.getCurrentSession().createQuery("from Business where id =:id");
            query.setInteger("id",business_id);
            Business business = (Business) query.uniqueResult();
            admin.setBusiness(business);

        }
        sessionFactory.getCurrentSession().saveOrUpdate(admin);
    }

    //querying admin by id
    public Admin getAdminById(int id) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Admin where id =:id");
        query.setInteger("id", id);
        Admin admin = (Admin) query.uniqueResult();
        return admin;
    }


    //get list admin
    public List<Admin> getAllAdmin(){
        Query query = sessionFactory.getCurrentSession().createQuery("from Admin");
        return query.list();
    }

    //find admin by name
    public Admin findAdminByName(String name){
        Query query = sessionFactory.getCurrentSession().createQuery("from Admin where name =:name");
        query.setString("name", name);
        Admin admin = (Admin) query.uniqueResult();
        return admin;
    }

    //update admin
    public void updateAdmin(Admin admin) {
        sessionFactory.getCurrentSession().update(admin);
    }

    //delete admin by id
    //following /admin{id}
    public void deleteAdmin(int id) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Admin where id =:id");
        query.setInteger("id",id);
        Admin admin = (Admin) query.uniqueResult();
        sessionFactory.getCurrentSession().delete(admin);
    }


    //check admin username
    public boolean checkUsername(Admin admin){
        String username = admin.getUsername();
        Query query = sessionFactory.getCurrentSession().createQuery("from Admin where username =:username");
        query.setString("username",username);
        Admin checkAdminUsername = (Admin) query.uniqueResult();
        if(checkAdminUsername != null){                                 //if admin's username exist
            return true;
        }
        return false;
    }

    public boolean checkLogin(Admin admin){
        String username = admin.getUsername();
        String password = admin.getPassword();
        //querying username and pwd
        Query query = sessionFactory.getCurrentSession().createQuery("from Admin where username =:username and password =:password");
        query.setString("username",username).setString("password", password);
        Admin checkAdminExist = (Admin) query.uniqueResult();
        //if username and pwd match
        return checkAdminExist != null;

    }
}






















