import styled from "styled-components";
import RouteItem from "./route-item.tsx";
import {MyRoute} from "../types/MyRoute.tsx";
import {MyUsersDto} from "../types/MyUsersDto.tsx";
import {useState} from "react";

type DataProps = {
    routesData: MyRoute[],
    logInUser: MyUsersDto
}
export default function RoutesList(props: Readonly<DataProps>) {
    const [allRoutesMode, setAllRoutesMode] = useState<boolean>(true)

    const myEmail = props.logInUser?.email
    const myRouten = props.routesData.filter(routes => routes.members?.some((member=>member?.email === myEmail)));

    return (
        <StyledDiv>
            <StyledH2>Meine Routen:</StyledH2>
            <div>
                <button type="button" onClick={() => setAllRoutesMode(true)}>All Routes</button>
                <button type="button" onClick={() => setAllRoutesMode(false)}>My Routes</button>
            </div>
            <StyledDivList>

                {allRoutesMode ?
                    props.routesData.map(filteredRoute => (
                        <RouteItem key={filteredRoute.id} route={filteredRoute}/>))
                    : props.routesData
                        .filter(item => !myRouten.some(route => route.id === item.id))
                        .map(filteredRoute => (
                            <RouteItem key={filteredRoute.id} route={filteredRoute}/>
                        ))}
            </StyledDivList>
        </StyledDiv>
    )
}
const StyledH2 = styled.h2`
    font-size: 3vw;
`;
const StyledDiv = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;

`;
const StyledDivList = styled.div`
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    margin: 0;
    gap: 2rem`;
