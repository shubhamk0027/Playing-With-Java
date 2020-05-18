package com.springfactory.springfactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class APIFactory {
    private static HashMap<String,APIInterface> nameToAPI=new HashMap<>();

    private APIFactory(List<APIInterface> APIS){
        for(APIInterface api: APIS ){
            System.out.println("Added the "+api.name()+" class");
            nameToAPI.put(api.name(),api);
        }
        System.out.println(nameToAPI.size()+" :P They are passed on its own!!!");
    }

    public static APIInterface getAPI(String name) throws Exception {
        if(name==null || !nameToAPI.containsKey(name)){
            throw new Exception("No Such API! Register it first!");
        }
        return nameToAPI.get(name);
    }

    // double check registration for adding a new api class
    public static void registerAPI(APIInterface newAPI){
        boolean res = nameToAPI.containsKey(newAPI.name());
        if(!res){
            synchronized (nameToAPI){
                res= nameToAPI.containsKey(newAPI.name());
                if(!res){ nameToAPI.put(newAPI.name(),newAPI);}
            }
        }
    }

    // double check registration for removing api
    public static void deRegisterAPI(APIInterface newAPI) {
        boolean res = nameToAPI.containsKey(newAPI.name());
        if(res){
            synchronized (nameToAPI){
                res= nameToAPI.containsKey(newAPI.name());
                if(res){ nameToAPI.remove(newAPI.name());}
            }
        }
    }

}

// This constructor does not requires autowiring! and will be called by spring automatically
// The list of components are automatically passed to the APIFactory constructor!
// A service methods and objs are static by default
// In case a new api is to be added, just pass to registerAPI
// https://stackoverflow.com/questions/6390810/implement-a-simple-factory-pattern-with-spring-3-annotations/39361500#39361500