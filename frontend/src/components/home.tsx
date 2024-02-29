import styled from "styled-components";
import "leaflet/dist/leaflet.css";
import {MyRoute} from "../types/MyRoute.tsx";
import InteractiveMap from "./interactiveMap.tsx";


type Props = {
    routesData: MyRoute[],
}
export default function Home(props: Readonly<Props>) {

    return (
        <StyledDiv>
            <StyledH3>Deine Reise, von der Planung bis zur Erinnerung: Alles in einer App!</StyledH3>
            <StyledP><i>
                Hier kannst du deine Abenteuer noch besser organisieren und deine Lieblingsrouten mit
                Leichtigkeit erkunden. Hier kannst du nicht nur Routen planen und speichern, sondern auch
                zusätzliche Informationen und Bilder zu jeder Route hinzufügen. Egal, ob du alleine unterwegs bist oder
                deine Abenteuer mit Freunden teilst, wir helfen dir dabei, deine Erlebnisse zu dokumentieren.
            </i></StyledP>
            <InteractiveMap routesData={props.routesData} planOn={false} isHome={true}/>
        </StyledDiv>
    )
}

const StyledP = styled.p`
    margin: 0 10px 20px 10px;
    text-align: center`;
const StyledH3 = styled.h3`
    color: #1c859c;
    text-align: center;
    font: small-caps bold 24px/1 sans-serif`;
const StyledDiv = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 2vw
`;
