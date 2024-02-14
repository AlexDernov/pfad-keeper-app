import styled from "styled-components";
import {MapContainer, TileLayer} from "react-leaflet";
import "leaflet/dist/leaflet.css";
//import LeafletControlGeocoder from "../LeafletControlGeocoder";
import {Control, Coords, LatLngExpression} from "leaflet";
import Routing from "../Routing.tsx";
import {useState} from "react";



export default function Home() {
    const [control, setControl] = useState<Control>()
    const [coords, setCoords] = useState<Coords[]>([])
    const position: LatLngExpression | undefined = [51.09, 10.27];

    function getCoords() {
        const waypoints = control?.getWaypoints();
        if (waypoints) {
            const extractedCoords = waypoints.map(coord => ({
                latitude: coord.latLng.lat,
                longitude: coord.latLng.lng
            }));
            setCoords(extractedCoords);
            return extractedCoords
        }
        return []
    }

    return (
        <StyledDiv>
            <h2>Willkommen auf unserer Webseite für die Planung, Speicherung und Bearbeitung von Wanderrouten!</h2>
            <p>
                Hier kannst du deine Abenteuer im Freien noch besser organisieren und deine Lieblingsrouten mit
                Leichtigkeit erkunden. Egal, ob du bereits geplante Routen auf einer Karte mit Markern festlegen
                möchtest oder deine Wanderungen spontan aufzeichnen und speichern möchtest – wir haben alles, was du
                brauchst.
                Mit unserer benutzerfreundlichen Plattform kannst du nicht nur Routen planen und speichern, sondern auch
                zusätzliche Informationen und Bilder zu jeder Route hinzufügen. Egal, ob du alleine unterwegs bist oder
                deine Abenteuer mit Freunden teilst, wir helfen dir dabei, deine Wandererlebnisse zu dokumentieren und
                unvergessliche Erinnerungen zu schaffen.

                Begib dich auf die Reise und entdecke die Natur auf eine ganz neue Art und Weise – mit unserer Webseite
                für Wanderroutenplanung!</p>

            <MapContainer center={position} zoom={5} style={{height: "50vh", color: "black"}} contextmenu={true}
            contextmenuItems={ [{
            text:"Start from here",
            //callback: startHere
        }, {
            text:`Go to here`,
           // callback: goHere
        }]}>
                <Routing setter={setControl}/>
                <TileLayer
                    attribution='&copy; <a href="https://www.openstreetmap.org/copyright"> OpenStreetMap
          </a> contributors'
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />
            </MapContainer>
            <button type="button" onClick={()=>{console.log(getCoords())}}>Coords speichern</button>

        </StyledDiv>
    )
}
const StyledDiv = styled.div`
`;
