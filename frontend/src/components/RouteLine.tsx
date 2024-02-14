import {useMap} from "react-leaflet";
import {useEffect} from "react";
import L from "leaflet";
import {Coords} from "../types/Coords.tsx";

type Props={
    coords: Coords[]
}
export default function RouteLine(props:Props){

    const map = useMap();
    useEffect(() => {

        const control = L.Routing.control({
            waypoints: props.coords.map((coord)=>
                L.latLng(parseFloat(coord.latitude), parseFloat(coord.longitude)))
            ,
            showAlternatives: false,
            lineOptions: {
                styles: [
                    {color: "black", opacity: 0.15, weight: 9},
                    {color: "white", opacity: 0.8, weight: 6},
                    {color: "blue", opacity: 0.5, weight: 2},
                ],
                extendToWaypoints: false,
                missingRouteTolerance: 1,
            },
            addWaypoints: false,
            routeWhileDragging: false,
        }).addTo(map);

        return () => {
            map.removeControl(control);
        }
    }, [map]);


    return null;
}