/*
 * Copyright (C) 2016 hcadavid
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
import edu.eci.pdsw.samples.entities.Consulta;
import edu.eci.pdsw.samples.entities.Paciente;
import edu.eci.pdsw.samples.persistence.DaoFactory;
import edu.eci.pdsw.samples.persistence.DaoPaciente;
import edu.eci.pdsw.samples.persistence.PersistenceException;
import edu.eci.pdsw.samples.persistence.jdbcimpl.JDBCDaoPaciente;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hcadavid
 */
public class PacientePersistenceTest {
  
    @Test
    public void databaseConnectionTest1() throws IOException, PersistenceException{
        InputStream input = null;
        input = ClassLoader.getSystemResourceAsStream("applicationconfig_test.properties");
        Properties properties=new Properties();
        properties.load(input);
        
        DaoFactory daof=DaoFactory.getInstance(properties);
        
        daof.beginSession();
        
        
        //IMPLEMENTACION DE LAS PRUEBAS
        DaoPaciente bd=daof.getDaoPaciente();
        Paciente p=new Paciente(12,"cc","carlos",java.sql.Date.valueOf("2000-01-01"));
        Set<Consulta> consultas=new LinkedHashSet<>();
        consultas.add(new Consulta(java.sql.Date.valueOf("2000-01-01"), "se esta agregando una nueva consulta"));
        p.setConsultas(consultas);
        bd.save(p);
        Paciente aMirar=bd.load(12,"cc");
        daof.commitTransaction();
        daof.endSession(); 
        Assert.assertEquals(aMirar,p);
    }
    
    @Test
    public void databaseConnectionTest2() throws IOException, PersistenceException{
        InputStream input = null;
        input = ClassLoader.getSystemResourceAsStream("applicationconfig_test.properties");
        Properties properties=new Properties();
        properties.load(input);
        
        DaoFactory daof=DaoFactory.getInstance(properties);
        
        daof.beginSession();
        
        
        //IMPLEMENTACION DE LAS PRUEBAS
        DaoPaciente bd=daof.getDaoPaciente();
        Paciente p=new Paciente(12,"cc","carlos",java.sql.Date.valueOf("2000-01-01"));
        bd.save(p);
        Paciente aMirar=bd.load(12,"cc");
        daof.commitTransaction();
        daof.endSession(); 
        Assert.assertEquals(aMirar,p);
        
    }
    
    
}
