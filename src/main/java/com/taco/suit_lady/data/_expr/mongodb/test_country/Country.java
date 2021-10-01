package com.taco.suit_lady.data._expr.mongodb.test_country;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document("countries")
public class Country
{
    @Id private String id;
    
    private String name;
    private int population;
    
    public Country(String name, int population)
    {
        this.name = name;
        this.population = population;
    }
    
    //
    
    public String getId()
    {
        return this.id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public int getPopulation()
    {
        return this.population;
    }
    
    public void setPopulation(int population)
    {
        this.population = population;
    }
    
    //
    
    //<editor-fold desc="— Generic Object Methods —">
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        final Country c = (Country) obj;
        return getPopulation() ==
                c.getPopulation()
                && Objects.equals(getId(), c.getId())
                && Objects.equals(getName(), c.getName());
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(id, name, population);
    }
    
    @Override
    public String toString()
    {
        return "Country{" + "id='" + id + '\'' +
                ", name=" + name + '\'' +
                ", population=" + population +
                '}';
    }
    
    //</editor-fold>
}
