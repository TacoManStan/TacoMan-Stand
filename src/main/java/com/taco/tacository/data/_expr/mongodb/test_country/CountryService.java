package com.taco.tacository.data._expr.mongodb.test_country;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class CountryService
{
    private final CountryRepository repo;
    
    public CountryService(CountryRepository repo)
    {
        this.repo = repo;
    }
    
    public boolean save(Country c)
    {
        if (c == null)
            return false;
        repo.save(c);
        return true;
    }
    
    public List<Country> listAll()
    {
        return repo.findAll();
    }
    
    public void print()
    {
        List<Country> cs = listAll();
        if (cs.isEmpty())
            System.out.println("No countries have been added to MongoDB database.");
        else
            cs.forEach(System.out::println);
    }
    
    public static Country generateCountry()
    {
        final String[] possibleNames = {
                "USA", "Denmark", "China", "Mexico", "Great Britain", "Ireland", "Iceland", "Greenland", "Canada", "Chile",
                "France", "Italy", "Germany", "Japan", "North Korea", "South Korea", "Vietnam", "Iraq", "Iran", "Turkey"
        };
        
        final String genName = Arrays.asList(possibleNames).get(new Random().nextInt(possibleNames.length));
        final int genPopulation = new Random().nextInt(1000000000);
        
        return new Country(genName, genPopulation);
    }
}
