/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectocine;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author sportak
 */
public class Mensaje implements Serializable {

    public ArrayList<Object> data;
    public String error;

    public Mensaje() {
    }

    public Mensaje(ArrayList<Object> data) {
        this.data = data;
    }

    public ArrayList<Object> getData() {
        return data;
    }

    public void setData(ArrayList<Object> data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "Mensaje{" + "data=" + data + ", error=" + error + '}';
    }

}
