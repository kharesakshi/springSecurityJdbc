/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcs.hms;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;

/**
 *
 * @author Shubham
 */

//Step-2

@Configuration
@EnableWebSecurity
public class DemoSecurityConfig extends WebSecurityConfigurerAdapter{

    
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      
        
        auth.jdbcAuthentication().dataSource(securityDataSource());
        
    }
    

    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http.csrf().disable().authorizeRequests()            //Restrict access based on HttpServletRequest     
                .antMatchers("/**").hasRole("RECEPTION")
                .and()
                .formLogin()
                .loginPage("/loginPage")
                .loginProcessingUrl("/authenticateTheUser")
                .permitAll()
                .and().logout().permitAll()
                    .and()
                            .exceptionHandling()
                            .accessDeniedPage("/access-denied");    //If authorization fails spring security will show this page as an error page
  
    } 
    
    @Bean
    public static DataSource securityDataSource(){
        
        //Create connection pool
        ComboPooledDataSource securityDataSource=new ComboPooledDataSource();
        
        //set the jdbc driver class 
        try{
       // securityDataSource.setDriverClass(env.getProperty("jdbc.driver"));
         securityDataSource.setDriverClass("com.mysql.jdbc.Driver");                                   
        }catch(Exception e)
        {
            System.out.println("********************Exeption "+e);
            throw new RuntimeException(e);
        }
        
       
        
        //set database connection props
       /* securityDataSource.setJdbcUrl(env.getProperty("jdbc.url"));
        securityDataSource.setUser(env.getProperty("jdbc.username"));
        securityDataSource.setPassword(env.getProperty("jdbc.password"));
         */       
       securityDataSource.setJdbcUrl("jdbc:mysql://localhost:3308//*<schema_name>*/");
        securityDataSource.setUser("/*<username>*/");
        securityDataSource.setPassword("/*<password>*/");
                
        //set connection pool props
        /*
        securityDataSource.setInitialPoolSize(getIntProperty("connection.pool.initialPoolSize"));
        securityDataSource.setMaxPoolSize(getIntProperty("connection.pool.maxPoolSize"));
        securityDataSource.setMinPoolSize(getIntProperty("connection.pool.minPoolSize"));
        securityDataSource.setMaxIdleTime(getIntProperty("connection.pool.maxIdleTime"));        
        */
        securityDataSource.setInitialPoolSize(5);
        securityDataSource.setMaxPoolSize(5);
        securityDataSource.setMinPoolSize(20);
        securityDataSource.setMaxIdleTime(3000);        
        
        
        return securityDataSource;
    }

    
    
    
 
    
}
