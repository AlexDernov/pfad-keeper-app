import '../index.css'
import map from "../images/map.png"
import styled from "styled-components";
import {ChangeEvent, useState} from "react";
import {useNavigate} from "react-router-dom";
import {MyRouteDto} from "../types/MyRouteDto.tsx";


type PropsForm ={
    name: string;
    date: string;
    isEdit:boolean
    onSubmit: (x: MyRouteDto)=> void;
}
export default function RouteForm(props:PropsForm){
    const [name, setName]= useState<string>("");
    const [dateTime, setDateTime]= useState<Date>(new Date());
    const navigate = useNavigate()
    function handleSubmit(event:ChangeEvent<HTMLFormElement>) {
        event.preventDefault();
        console.log(JSON.stringify({name, dateTime}))
        props.onSubmit({name, dateTime});
        navigate("/routes")

    }
    return(
        <StyledForm onSubmit={handleSubmit}>
            <legend>{props.isEdit? "Edit die Route" : "Neue Route"}</legend>
            <StyledImg src={map} alt={"map"}/>
            <StyledSection>
                <label htmlFor={"name"}>Wo:
                    <input type={"text"} id={"name"} onChange={(e)=>setName(e.target.value)} defaultValue={props.name}
                           pattern="[0-9A-Za-zß-üА-Яа-яЁё?\s]+"/></label>
                <label htmlFor={"dateTime"}>Wann:
                    <input type={"datetime-local"} name="date" id={"dateTime"}
                           onChange={(e)=>setDateTime(new Date(e.target.value))} defaultValue={props.date} required pattern="\d{4}-\d{2}-\d{2}" min="2023-01-01"
                           max="2080-01-01"/>
                    <span className="validity"/></label>
            </StyledSection>
            <button type={"submit"}>Speichern</button>
        </StyledForm>
    )
}
const StyledForm = styled.form`
display: flex;
flex-direction: column;
    align-items: center;
justify-content: space-around`;

const StyledSection = styled.section`
display: flex;
flex-direction: column;
    align-items: start;
 `;

const StyledImg = styled.img`
width: 70vw`;