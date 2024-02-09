import styled from "styled-components";
import RouteItem from "./route-item.tsx";
import {MyRoute} from "../types/MyRoute.tsx";

type DataProps = {
    routesData: MyRoute[]
}
export default function RoutesList(props: Readonly<DataProps>) {

    return (
        <StyledDiv>
            <StyledH2>Meine Routen:</StyledH2>
            <StyledDivList>
                {props.routesData.map(item => (
                    <RouteItem key={item.id} route={item}/>
                ))}
            </StyledDivList>
        </StyledDiv>
    )
}
const StyledH2 = styled.h2`
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
    gap: 4rem`;
