import L from "leaflet";
import 'leaflet-routing-machine';

export default function Map() {
    const baseLayer = L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png?",{
        attribution:'&copy; <a href="https://www.openstreetmap.org/copyright"> OpenStreetMap</a> contributors'
    });
    <div id="map"></div>
    const map = L.map(`map`,{
        center:[52.509663, 13.376481],
        zoom: 13,
        zoomControl: false,
        layers:[baseLayer],
        contextmenu: true,
        contextmenuItems: [{
            text:`Start from here`,
            callback: startHere
        }, {
            text:`Go to here`,
            callback: goHere
        }]
    });


        const control = L.Routing.control({
            waypoints: [
                L.latLng(52.509663, 13.376481),
                L.latLng(52.521992, 13.413244)
            ],
            /*createMarker: function (i:number, waypoints: L.Routing.Waypoint[] | L.LatLng[] | undefined, n:number){
                const startEndIcon = L.icon({
                    iconUrl: `./images/startIcon.svg`,
                    className: "",
                    iconSize: [25, 25],
                    iconAnchor: [0, 25],
                });
                const betweenIcon = L.divIcon({
                    iconUrl: `./images/betweenIcon.svg`,
                    className: "",
                    iconSize: [25, 25],
                    iconAnchor: [0, 25],
                });
                if(i == 0){
                   let marker_icon = startEndIcon
                } else if (i > 0 && i< n-1){
                   let marker_icon = startEndIcon
                } else if (i == n-1){
                   let marker_icon = betweenIcon
                }
                return L.marker(waypoints?.latLng, {
                    draggable: true,
                    icon: marker_icon
                });
            },*/
            showAlternatives: true,
            lineOptions: {
                styles: [
                    {color: "black", opacity: 0.15, weight: 9},
                    {color: "white", opacity: 0.8, weight: 6},
                    {color: "blue", opacity: 0.5, weight: 2},
                ], extendToWaypoints: true,
                missingRouteTolerance: 0,
            },

            //geocoder: L.Control.Geocoder.nominatim(),
            routeWhileDragging: true,
        }).addTo(map);
    function startHere (e){
        control.spliceWaypoints(0,1,e.latlng)
    }
    function goHere(e){
        control.spliceWaypoints(control.getWaypoints().length -1, 1, e.latlng);
    }
        return ()=> {
            map.removeControl(control);
        }
}