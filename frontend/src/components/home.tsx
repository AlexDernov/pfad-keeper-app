import styled from "styled-components";
import placeholderMap from "../../public/images/placeholderMap.jpg"

export default function Home() {
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
            <StyledImg src={placeholderMap} alt={"Placeholder Map"}/>
        </StyledDiv>
    )
}
const StyledDiv = styled.div`
`;
const StyledImg = styled.img`
    margin: 1vw 0 1vw 0;
    height: auto;
    width: auto;
    max-width: 99%;
    object-fit: contain`;