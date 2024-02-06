import styled from "styled-components";
import RouteItem from "./route-item.tsx";
import {Route} from "../types/Route.tsx";
type DataProps ={
    routesData: Route[]
}
export default function RoutesList(props:DataProps){

    return(
        <StyledDiv>
            <h2>Meine Routen:</h2>
            <ul>
                {props.routesData.map(item => (
                    <RouteItem key={item.id} route={item}/>
                ))}
            </ul>
        </StyledDiv>
    )
}
const StyledDiv = styled.div`
`;