import L, {Control} from "leaflet";
import 'leaflet-routing-machine';
import {useMap} from "react-leaflet";
import "leaflet/dist/leaflet.css";
import "leaflet-control-geocoder/dist/Control.Geocoder.css";
import "leaflet-control-geocoder/dist/Control.Geocoder.js";
import "leaflet-routing-machine/dist/leaflet-routing-machine.css";
import {Dispatch, SetStateAction, useEffect} from "react";

type Props = {
    setter: Dispatch<SetStateAction<Control | undefined>>
}
export default function Routing(props: Props) {

    const map = useMap();
    useEffect(() => {

        const control = L.Routing.control({
            waypoints: [
                L.latLng(0, 0),
                L.latLng(0, 0)
            ],
            showAlternatives: false,
            lineOptions: {
                styles: [
                    {color: "black", opacity: 0.15, weight: 9},
                    {color: "white", opacity: 0.8, weight: 6},
                    {color: "blue", opacity: 0.5, weight: 2},
                ],
                extendToWaypoints: true,
                missingRouteTolerance: 1,
            },
            addWaypoints: true,
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