import styled from "styled-components";
import React, {useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";

type Props = {
    userName: string | undefined,
    userEmail: string | undefined;

}
export default function ProfilPage(props: Readonly<Props>) {

    const [name, setName] = useState<string | undefined>(props.userName);
    const navigate = useNavigate()

    function handleSubmit(e: React.FormEvent<HTMLFormElement>) {
        e.preventDefault();

        axios.post(`/api/users/me`, name, {headers: {"Content-Type": "text/plain"}})
            .then((response) => {
                setName(response.data.name)
                navigate("/routes")
            })
            .catch(error => {
                console.error('Error updating user:', error);
            });
    }

    return (
        <StyledDiv> <StyledForm onSubmit={handleSubmit}>
            <StyledLegend>Profil von {name} bearbeiten:</StyledLegend>
            <p>Bitte bearbeiten Sie Ihren Namen. Der Name soll klar und eindeutig sein, damit Sie einer Route zugefügt
                werden können. Falls kein Name vergeben wurde steht in der Liste von Teilnehmern Ihres
                E-mail {props.userEmail}. </p>
            <StyledLabel htmlFor={"name"}>Name:
                <StyledInput type={"text"} id={"name"} defaultValue={props.userName}
                             onChange={(e) => setName(e.target.value)}

                /></StyledLabel>
            <StyledButton type="submit">Speichern</StyledButton>
        </StyledForm>
            <StyledButton>Delete Profil</StyledButton>
        </StyledDiv>
    )
}
const StyledDiv = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center`;

const StyledButton = styled.button`
    color: #ffffff;
    background-color: #1c859c;
    border-radius: 0.975rem;
    padding: 1vw 1.5vw;
    font-size: 1.7vw;
    font-weight: 500;
    margin: 1vw 1vw;
    cursor: pointer;

    &:hover {
        padding: 0.5vw 1vw;
        font-size: 2vw;
        margin: 0.55vw 0.5vw;
    }
`;
const StyledLabel = styled.label`
    width: 100%;
    margin: 0.5vw;
    font-size: 2.5vw`;
const StyledForm = styled.form`
    display: flex;
    width: 35%;
    flex-direction: column;
    align-items: center;
    justify-content: space-around`;

const StyledLegend = styled.legend`
    font-size: 3vw;
    margin: 2vw;
`;
const StyledInput = styled.input`
    width: 100%;
    font-size: 2vw;
    padding: 1vw;
    border-radius: 0.8vw;
    border: rgba(162, 160, 160, 0.92) solid;
`;