import {MyImages} from "../types/MyImages.tsx";
import styled from "styled-components";

type DataImage = {
    imgData: MyImages,
    onDelete:(id:string)=>void
}
export default function ImageItem(props: DataImage) {
function handleImageDelete(){
    props.onDelete(props.imgData.id)
}

    return (
        <StyledContaner>
        <StyledDiv>
            <StyledImg src={props.imgData.url} alt="image"/>
            <button onClick={handleImageDelete}>X</button>
        </StyledDiv>
        </StyledContaner>
    )
}
const StyledImg = styled.img`
    margin: 1vw 0 1vw 0;
    height: auto;
    width: auto;
    max-width: 99%;
    object-fit: contain`;

const StyledContaner = styled.div`
    padding: 0;
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
`;
/*
const StylesDate = styled.p`
    margin: 0.3vw 0 0 0;
    max-height: 8vh;
    max-font-size: 2.5vw;
    color: rgba(14, 16, 14, 0.84);
    font-style: normal`;

const StyledImg = styled.img`
    margin: 1vw 0 1vw 0;
    height: auto;
    width: auto;
    max-width: 99%;
    object-fit: contain`;

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
`;*/
