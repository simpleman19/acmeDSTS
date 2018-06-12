package acme.ui;

import acme.data.HibernateAdapter;
import acme.data.PersistableEntity;
import acme.pd.Company;
import acme.pd.Courier;

import acme.pd.Direction;
import acme.pd.MapIntersection;
import acme.pd.Road;

public class Main {
    public static void main(String[] args) {
    	HibernateAdapter.startUp();
        Company company = new Company();
        company.setName("The coolest delivery company");
        Courier courier = new Courier();
        courier.setName("Bill");
        company.save();
        Company company2 = PersistableEntity.get(Company.class, company.getId());
        System.out.println(company2.getId() == company.getId());
        courier.save();
        HibernateAdapter.shutDown();
        
  
        MapIntersection tempMap[][] = company.getMap().getMap();
        
       for(int x = 0; x < tempMap.length; x++)
       {
         System.out.println("");
         for(int y = 0; y < tempMap.length; y++)
         {
           System.out.print(tempMap[x][y].getIntersectionName() + "    ");
           System.out.print(tempMap[x][y].getEWroad().getDirection() + "    ");
           System.out.print(tempMap[x][y].getNSroad().getDirection() + "    ");
           
         }
       }
       
       MapIntersection tempPickUp = new MapIntersection();
       Road tempPickUpRoad = new Road();
       tempPickUpRoad.setName("3");
       tempPickUpRoad.setBidirectional(false);
       tempPickUpRoad.setDirection(Direction.NORTH);
       tempPickUp.setNSroad(tempPickUpRoad);
       tempPickUpRoad = new Road();
       tempPickUpRoad.setName("E");
       tempPickUpRoad.setBidirectional(false);
       tempPickUpRoad.setDirection(Direction.WEST);
       tempPickUp.setEWroad(tempPickUpRoad);
       
       System.out.println("");
       System.out.println("");
       System.out.println("");
       System.out.println("");
       System.out.println(tempPickUp.getIntersectionName());
       System.out.println(tempPickUp.getEWroad().getDirection());
       System.out.println(tempPickUp.getNSroad().getDirection());
       
       MapIntersection tempDropOff = new MapIntersection();
       Road tempDropOffRoad = new Road();
       tempDropOffRoad.setName("5");
       tempDropOffRoad.setBidirectional(false);
       tempDropOffRoad.setDirection(Direction.NORTH);
       tempDropOff.setNSroad(tempDropOffRoad);
       tempDropOffRoad = new Road();
       tempDropOffRoad.setName("C");
       tempDropOffRoad.setBidirectional(false);
       tempDropOffRoad.setDirection(Direction.WEST);
       tempDropOff.setEWroad(tempDropOffRoad);
       
       System.out.println(tempDropOff.getIntersectionName());
       System.out.println(tempDropOff.getEWroad().getDirection());
       System.out.println(tempDropOff.getNSroad().getDirection());

       
       company.getMap().getPath(tempPickUp,tempDropOff );
     
    	 new UserUI();
    	 new CompanyUI();
    }
    
}
