import styled from "styled-components";
import {MyRoute} from "../types/MyRoute.tsx";
import map from "../assets/images/map.png"
import {Link} from "react-router-dom";
import {MyImages} from "../types/MyImages.tsx";

type DataRoute = {
    route: MyRoute,
    routImages:MyImages[]
}
export default function RouteItem(props: Readonly<DataRoute>) {
    const listOfImagesOfTheRoute = props.routImages.filter(image=> image.routeId === props.route.id)
    const randomImage = listOfImagesOfTheRoute.length > 0 ? listOfImagesOfTheRoute[Math.floor(Math.random() * listOfImagesOfTheRoute.length)].url : null;
    return (
        <StyledLink to={`/routes/${props.route.id}`}>
            <StyledDiv>
                <StyledH>{props.route.name}</StyledH>
                <StylesDate>Datum: <i>{new Date(props.route.dateTime).toLocaleDateString()}</i></StylesDate>
                <StyledImg src={randomImage ?? map} alt={"Placeholder Map"}/>
            </StyledDiv>
        </StyledLink>
    )
}

const StylesDate = styled.p`
    margin: 0.3vw 0 0 0;
    max-height: 8vh;
    max-font-size: 2.5vw;
    color: rgba(14, 16, 14, 0.84);
    font-style: normal`;

const StyledImg = styled.img`
    margin: 1vw 0 1vw 0;
    width: auto;
    max-width: 99%;
    height: 100%;
    overflow: hidden;
    object-fit: cover`;

const StyledH = styled.h2`
    margin: 1vw 0 0 0;
    max-height: 10vh;
    max-font-size: 2vw;
    color: rgba(14, 16, 14, 0.84);
    font-style: normal`;

const StyledLink = styled(Link)`
    text-decoration: none;
    padding: 0;
    cursor: pointer;
    min-width: 30vw;
    max-width: 90vw;
    margin: 0`;

const StyledDiv = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    align-items: center;
    box-shadow: 0 2px 4px 0 rgba(38, 59, 56, 0.10), 0 0 0 1.5px rgba(38, 50, 56, 0.10);
    margin: 0.25vw 0 0.25vw 0;
    min-width: 30vw;
    max-width: 500px;
    border-radius: 0.375rem;
    border-color: rgb(221 221 221);
    background-color: white;
    padding: 0.25vw 0 0.25vw 0;
    height: 100%;
    overflow: hidden;
`;


