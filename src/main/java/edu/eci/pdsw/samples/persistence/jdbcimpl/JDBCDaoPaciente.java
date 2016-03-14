/*
 * Copyright (C) 2015 hcadavid
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
package edu.eci.pdsw.samples.persistence.jdbcimpl;

import edu.eci.pdsw.samples.entities.Consulta;
import edu.eci.pdsw.samples.entities.Paciente;
import edu.eci.pdsw.samples.persistence.DaoPaciente;
import edu.eci.pdsw.samples.persistence.PersistenceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author hcadavid
 */
public class JDBCDaoPaciente implements DaoPaciente {

    Connection con;

    public JDBCDaoPaciente(Connection con) {
        this.con = con;
    }
        

    @Override
    public Paciente load(int idpaciente, String tipoid) throws PersistenceException {
        PreparedStatement ps;
        Paciente p;
        try {
            
            String consulta="SELECT * FROM PACIENTES WHERE id=? and tipo_id=?";
            ps=con.prepareStatement(consulta);
            //Asignar parámetros
            ps.setInt(1,idpaciente);
            ps.setString(2,tipoid);
            
            
            //usar executeQuery
            ResultSet rs=ps.executeQuery();
            rs.next();
            p=new Paciente(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getDate(4));

            //////Agregando consultas
            PreparedStatement prep;
            Set<Consulta> consultas=new LinkedHashSet<Consulta>();
            
            String consult="SELECT fecha_y_hora,resumen,idCONSULTAS FROM CONSULTAS WHERE PACIENTES_id=? and PACIENTES_tipo_id=?";
            prep=con.prepareStatement(consult);
            //Asignar parámetros
            prep.setInt(1,idpaciente);
            prep.setString(2,tipoid);
            //usar executeQuery
            
            ResultSet r=prep.executeQuery();
            while(r.next()){
                Consulta cons=new Consulta(r.getDate(1),r.getString(2));
                cons.setId(r.getInt(3));
                consultas.add(cons);
            }
            p.setConsultas(consultas);
        } catch (SQLException ex) {
            throw new PersistenceException("An error ocurred while loading "+idpaciente+" "+ex.getMessage(),ex);
        }
        return p;
    }

    @Override
    public void save(Paciente p) throws PersistenceException {
        PreparedStatement ps;
        try {
            
            //Crear preparedStatement
        PreparedStatement statement;
        String consulta="INSERT INTO PACIENTES (id,tipo_id,nombre,fecha_nacimiento) values (?,?,?,?)" ;
        statement=con.prepareStatement(consulta);
        //Asignar parámetros
        statement.setInt(1,p.getId());
        statement.setString(2,p.getTipo_id());
        statement.setString(3,p.getNombre());
        statement.setDate(4, p.getFechaNacimiento());
        //usar 'execute'
        
        statement.execute();
       
        for (Consulta c:p.getConsultas()){
            PreparedStatement agregarConsulta;
            String cons="INSERT INTO CONSULTAS (idCONSULTAS,fecha_y_hora,resumen,PACIENTES_id,PACIENTES_tipo_id) "
                    + "VALUES(?,?,?,?,?)";
            agregarConsulta=con.prepareStatement(cons);
            agregarConsulta.setInt(1, c.getId());
            agregarConsulta.setDate(2, c.getFechayHora());
            agregarConsulta.setString(3, c.getResumen());
            agregarConsulta.setInt(4, p.getId());
            agregarConsulta.setString(5, p.getTipo_id());
            agregarConsulta.execute();
        }
            
        } catch (SQLException ex) {
            throw new PersistenceException("An error ocurred while saving a product."+ex.getMessage(),ex);
        }
        
       

    }

    @Override
    public void update(Paciente p) throws PersistenceException {
        PreparedStatement ps;
        /*try {
            
        } catch (SQLException ex) {
            throw new PersistenceException("An error ocurred while loading a product.",ex);
        } */
        throw new RuntimeException("No se ha implementado el metodo 'load' del DAOPAcienteJDBC");
    }
    
}
