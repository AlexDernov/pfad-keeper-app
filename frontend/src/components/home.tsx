import styled from "styled-components";
import "leaflet/dist/leaflet.css";
import {MyRoute} from "../types/MyRoute.tsx";
import Map from "./map.tsx";


type Props = {
    routesData: MyRoute[],
}
export default function Home(props: Props) {

    return (
        <StyledDiv>
            <h3>Willkommen auf unserer Webseite für die Planung, Speicherung und Bearbeitung von Reisen!</h3>
            <p><i>
                Hier kannst du deine Abenteuer noch besser organisieren und deine Lieblingsrouten mit
                Leichtigkeit erkunden. Hier kannst du nicht nur Routen planen und speichern, sondern auch
                zusätzliche Informationen und Bilder zu jeder Route hinzufügen. Egal, ob du alleine unterwegs bist oder
                deine Abenteuer mit Freunden teilst, wir helfen dir dabei, deine Erlebnisse zu dokumentieren.
            </i></p>
            <Map routesData={props.routesData} oneRouteData={undefined} setter={undefined} planOn={false} isHome={true}/>
        </StyledDiv>
    )
}
const StyledDiv = styled.div`
    padding: 2vw
`;
