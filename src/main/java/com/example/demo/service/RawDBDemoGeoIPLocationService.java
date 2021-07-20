package com.example.demo.service;

import com.example.demo.entity.GeoIp;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Logger;

@Component
public class RawDBDemoGeoIPLocationService {

    private Logger logger = Logger.getLogger(RawDBDemoGeoIPLocationService.class.getName());

    public RawDBDemoGeoIPLocationService()  {

    }

    public GeoIp getLocation(String ip, DatabaseReader databaseReader)
            throws IOException, GeoIp2Exception {
        InetAddress ipAddress = InetAddress.getByName(ip);
        CityResponse response = databaseReader.city(ipAddress);

        String cityName = response.getCity().getName();
        String nameCountry = response.getCountry().getName();
        String latitude =
                response.getLocation().getLatitude().toString();
        String longitude =
                response.getLocation().getLongitude().toString();



        logger.info(response.getCity().getName());
        logger.info(response.getCountry().getName());



        return new GeoIp(ip, cityName, latitude, longitude, nameCountry);
    }
}
