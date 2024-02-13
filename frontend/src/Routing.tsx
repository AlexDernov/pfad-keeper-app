import L, {Control} from "leaflet";
import 'leaflet-routing-machine';
import {useMap} from "react-leaflet";
import "leaflet-control-geocoder/dist/Control.Geocoder.css";
import "leaflet-control-geocoder/dist/Control.Geocoder.js";
import {Dispatch, SetStateAction, useEffect} from "react";
type Props={
    setter: Dispatch<SetStateAction<Control | undefined>>
}
export default function Routing(props:Props) {

    const map = useMap();
    useEffect(() => {

        const control = L.Routing.control({
            waypoints: [
                L.latLng(52.509663, 13.376481),
                L.latLng(52.521992, 13.413244)
            ],
            showAlternatives: true,
            lineOptions: {
                styles: [
                    {color: "black", opacity: 0.15, weight: 9},
                    {color: "white", opacity: 0.8, weight: 6},
                    {color: "blue", opacity: 0.5, weight: 2},
                ], extendToWaypoints: true,
                missingRouteTolerance: 0,
            },
            geocoder: L.Control.Geocoder.nominatim(),
            routeWhileDragging: true,
        }).addTo(map);
        props.setter(control);
        return () => {
            map.removeControl(control);
        }
    }, [map]);

    return null;
}