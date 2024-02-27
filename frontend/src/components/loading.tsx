import styled from "styled-components";

export default function Loading(){
    return(
        <StyledDiv>
            <img src="../assets/images/routenplanung-botendienst.gif" width="150px" height="150px" alt="loading"/>
        </StyledDiv>
    )
}
const StyledDiv = styled.div`
  display: flex;
  flex-direction: column;
  margin: 190px 20px 10px 70px;
  padding: 20px;
  align-item: center;
`;