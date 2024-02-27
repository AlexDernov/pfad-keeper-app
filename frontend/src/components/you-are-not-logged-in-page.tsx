import styled from "styled-components";
import pleaseLogin from "../assets/images/pleaseLogin.gif"

export default function PleaseLogIn() {
    return (
        <StyledDiv>
            <StyledP>Um weitere Optionen von dem App nutzen zu können, sollten Sie eingeloggt sein ↗️ </StyledP>
            <StyledImg alt={"please log in"} src={pleaseLogin}/>
        </StyledDiv>
    )
}
const StyledP = styled.p`
    width: 68vw;
    text-align: center;
    font-size: 1.5rem;
    color: RGB(129, 113, 87);
    margin-bottom: -5.3rem;
    z-index: 700`;
const StyledImg = styled.img`
    width: 70vw`;
const StyledDiv = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 9vh;
`;