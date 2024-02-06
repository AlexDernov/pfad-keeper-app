import styled from "styled-components";
import {Route} from "../types/Route.tsx";
import placeholderMap from "../../public/images/placeholderMap.jpg"

type DataRoute={
    route: Route
}
export default function RouteItem(props:DataRoute){
    return(
        <StyledLi>
            <h2>{props.route.name}</h2>
            <img src={placeholderMap} alt={"Placeholder Map"}/>
            <p>{props.route.dataTime}</p>
        </StyledLi>
    )
}
const StyledLi = styled.li`
    display: flex;
    position: relative;
    flex-direction: column;
    justify-content: space-around;
    border: solid rgb(221 221 221) 1px;
    box-shadow: 0 2px 4px 0 rgba(38, 59, 56, 0.10), 0 0 0 1.5px rgba(38, 50, 56, 0.10);
    border-radius: 0.375rem;
    padding: 1rem 2.25rem 2.25rem 2.25rem;
    margin: 2vw 2vw 1vw 0;
    height: auto;
    width: 70vw;
`;

