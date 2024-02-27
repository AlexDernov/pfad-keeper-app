import {MyImages} from "../types/MyImages.tsx";
import ImageItem from "./image-item.tsx";
import styled from "styled-components";

type Props={
    imgData:MyImages[],
    onDelete:(id:string)=>void,
    routeID:string | undefined,
}
export default function ImagesList(props:Props){
return(<StyledDiv>
    <StyledDivList>
    {props.imgData.filter((imgRouteData)=>(imgRouteData.routeId === props.routeID)).map((img)=><ImageItem key={img.id} imgData={img} onDelete={props.onDelete}/>)}
        </StyledDivList>
    </StyledDiv>)
}
const StyledDiv = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
`;
const StyledDivList = styled.ul`
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    margin: 0;
    gap: 2rem`;