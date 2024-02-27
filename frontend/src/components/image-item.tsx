import {MyImages} from "../types/MyImages.tsx";
import styled from "styled-components";

type DataImage = {
    imgData: MyImages,
    onDelete: (id: string) => void
}
export default function ImageItem(props: Readonly<DataImage>) {
    function handleImageDelete() {
        props.onDelete(props.imgData.id)
    }

    return (
        <StyledContaner>
            <StyledDiv>
                <StyledImg src={props.imgData.url} alt="image"/>
                <StyledDeleteButton title="DELETE" onClick={handleImageDelete}>X</StyledDeleteButton>
            </StyledDiv>
        </StyledContaner>
    )
}
const StyledDeleteButton = styled.button`
    color: #1c859c;
    border: transparent none;
    cursor: pointer;
    font-size: 2.5vw;
    background-color: transparent`;

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
