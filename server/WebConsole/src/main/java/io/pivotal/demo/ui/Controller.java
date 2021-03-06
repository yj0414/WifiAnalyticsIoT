package io.pivotal.demo.ui;


import io.pivotal.demo.trilateration.CalculateLocationDistanceListener;
import io.pivotal.demo.trilateration.Scale;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
@RequestMapping(value = "/controller")
public class Controller {
    
	
	//private static final int distanceBetweenPIs = 3;
	//private static final int scaleX = 300; // 900 pixels / 3m
	//private static final int scaleY = 300; // 900 pixels / 3m
	
	
    public Controller() {
        
    }
    
 
    @RequestMapping(value="/getDeviceMap")
    public @ResponseBody DeviceMap getDeviceMap(){
    	
    	Collection<DeviceLocation> deviceLocations = GeodeClient.getInstance().getDeviceLocations();
    	DeviceMap map = new DeviceMap();
        
    	Iterator<DeviceLocation> it = deviceLocations.iterator();
    	while (it.hasNext()){
    		DeviceLocation device = it.next();
    		map.addDevice(device.getDeviceId(), device.getX(), device.getY());
    	}
    	
        return map;

    }    
    
    @RequestMapping(value="/getPIs")
    public @ResponseBody DeviceMap getPIs(){
    	
    	Collection<DeviceLocation> piLocations = GeodeClient.getInstance().getPIsLocation();
    	DeviceMap map = new DeviceMap();
        
    	Iterator<DeviceLocation> it = piLocations.iterator();
    	while (it.hasNext()){
    		DeviceLocation pi = it.next();
    		map.addDevice(pi.getDeviceId(), pi.getX(), pi.getY());
    	}
    	
        return map;
    }        
    
    @RequestMapping(value="/updatePILocation")
    public void updatePILocation(@RequestParam("deviceId") String deviceId, @RequestParam("x") double x, @RequestParam("y") double y){
    	
    	DeviceLocation piLocation = new DeviceLocation(deviceId, x, y);
    	GeodeClient.getInstance().setPILocation(piLocation);
    	recalculateAllPositions();
    }   
    
    @RequestMapping(value="/updateDimensions")
    public void updateDimensions(@RequestParam("xPx") double xPixels, @RequestParam("yPx") double yPixels, 
    							@RequestParam("xMeters") double xMeters, @RequestParam("yMeters") double yMeters){
    	
    	Scale.getInstance().setDimensions(xPixels, yPixels, xMeters, yMeters);
    	recalculateAllPositions();
    	
    }      
    
    
    
    
    
    private void recalculateAllPositions(){
    	
    	Iterator<DeviceLocation> devices = GeodeClient.getInstance().getDeviceLocations().iterator();
    	while (devices.hasNext()){
    		DeviceLocation device = devices.next();
    		CalculateLocationDistanceListener.updateDeviceLocation(device.getDeviceId());
    	}
    	
    }
    
    
    
    
    
}