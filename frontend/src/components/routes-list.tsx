import styled from "styled-components";
import RouteItem from "./route-item.tsx";
import {MyRoute} from "../types/MyRoute.tsx";
import {MyUsersDto} from "../types/MyUsersDto.tsx";
import {useState} from "react";
import {MyImages} from "../types/MyImages.tsx";

type DataProps = {
    routesData: MyRoute[],
    logInUser: MyUsersDto,
    routImages: MyImages[],
}
export default function RoutesList(props: Readonly<DataProps>) {
    const [allRoutesMode, setAllRoutesMode] = useState<boolean>(true)

    const myEmail = props.logInUser?.email
    const myRouten = props.routesData.filter(routes => routes.members?.some((member => member?.email === myEmail)));

    return (
        <StyledDiv>
            <ButtonDiv>
                <StyledFilterButton type="button" onClick={() => setAllRoutesMode(true)}>All Routes</StyledFilterButton>
                <StyledFilterButton type="button" onClick={() => setAllRoutesMode(false)}>My Routes</StyledFilterButton>
            </ButtonDiv>
            <StyledDivList>

                {allRoutesMode ?
                    props.routesData.map(filteredRoute => (
                        <RouteItem routImages={props.routImages} key={filteredRoute.id} route={filteredRoute}/>))
                    : props.routesData
                        .filter(item => !myRouten.some(route => route.id === item.id))
                        .map(filteredRoute => (
                            <RouteItem key={filteredRoute.id} routImages={props.routImages} route={filteredRoute}/>
                        ))}
            </StyledDivList>
        </StyledDiv>
    )
}

const StyledFilterButton = styled.button`
    color: #1c859c;
    font: small-caps bold 24px/1 sans-serif;
    padding: 10px;
    background-color: transparent;
    border: transparent none;

    &:hover {
        font: small-caps bold 26px/1 sans-serif;
        padding: 9px 6px;
    }
`;
const ButtonDiv = styled.div`
    display: flex;
    gap: 10vw;
    margin: 2vw 0 2vw 0;
`;

const StyledDiv = styled.div`
    margin: 8vw;
    display: flex;
    flex-direction: column;
    align-items: center;

`;
const StyledDivList = styled.div`
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    margin: 0;
    width: 100%;
    gap: 2rem`;
