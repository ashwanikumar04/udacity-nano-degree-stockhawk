package in.ashwanik.udacitystockhawk.entities;

/**
 * Created by AshwaniK on 7/30/2016.
 */
public class Query<T> {


    public int count;
    public String created;
    public String lang;
    public Results<T> results;
}