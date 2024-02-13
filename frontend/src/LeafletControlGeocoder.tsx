

import { useMap } from "react-leaflet";
import "leaflet-control-geocoder/dist/Control.Geocoder.css";
import "leaflet-control-geocoder/dist/Control.Geocoder.js";
import  L from "leaflet";
import {useEffect} from "react";




export default function LeafletControlGeocoder() {
    const map = useMap();

    useEffect(() => {
//@ts-expect-error typeScriptLibrary
       const geocoder = L.Control.Geocoder.nominatim();
        //@ts-expect-error typeScriptLibrary
        const control = L.Control.geocoder({
            query: 'Moon',
            placeholder: 'Search here...',
            geocoder: geocoder
        }).addTo(map);

        let marker: L.Marker<any>;


        setTimeout(function () {
            control.setQuery('Earth');
        }, 12000);


        map.on('click', function (e) {

            geocoder.reverse(e.latlng, map.options.crs?.scale(map.getZoom()), function (results: any) {
                const r = results[0];
                if (r) {
                    if (marker) {
                        marker
                            .setLatLng(r.center)
                            .setPopupContent(r.html || r.name)
                            .openPopup();
                    } else {
                        marker = L.marker(r.center)
                            .bindPopup(r.name)
                            .addTo(map)
                            .openPopup();
                    }
                }
            });
        });
        return ()=> {
            map.removeControl(control);
        }
    },[]);
        return null
}
/*
        const geocoder = L.Control.geocoder({
            query: "",
            placeholder: "Search here...",
            defaultMarkGeocode: false,
            geocoder:geocoder
        })
            .on("markgeocode", function (e) {
                let latlng = e.geocode.center;
                L.marker(latlng, { icon })
                    .addTo(map)
                    .bindPopup(e.geocode.name)
                    .openPopup();
                map.fitBounds(e.geocode.bbox);
            })
            .addTo(map);
    }, []);

    return null;*/

